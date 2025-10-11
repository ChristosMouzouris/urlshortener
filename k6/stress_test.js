import http from "k6/http";
import { check } from "k6";
import { Rate } from "k6/metrics";

export let failureRate = new Rate("failed_requests");

const BASE_URL = "http://localhost:8080";

function randomUrl() {
  // small and fast random string
  return `https://example.com/${Math.random().toString(36).substring(2, 10)}`;
}

export let options = {
  scenarios: {
    high_load: {
      executor: "constant-arrival-rate",
      rate: 3000, // target 5000 requests/sec
      duration: "20s", // total test duration
      preAllocatedVUs: 200, // minimum number of VUs
      maxVUs: 400, // maximum VUs k6 can spin up
      timeUnit: "1s", // rate is per second
    },
  },
  thresholds: {
    failed_requests: ["rate<0.05"], // <5% failures allowed
    http_req_duration: ["p(95)<500"], // 95% of requests under 500ms
  },
};

export default function () {
  const payload = JSON.stringify({ longUrl: randomUrl() });
  const params = { headers: { "Content-Type": "application/json" } };
  const shortenResult = http.post(`${BASE_URL}/api/url`, payload, params);

  check(shortenResult, {
    "shorten returned 200": (r) => r.status === 200,
  }) || failureRate.add(1);

  const shortCode = shortenResult.json("shortUrl") || "invalid";
  const redirectResult = http.get(`${BASE_URL}/${shortCode}`, { redirects: 0 });

  check(redirectResult, {
    "redirect returned 302": (r) => r.status === 302,
  }) || failureRate.add(1);
}
