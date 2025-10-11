import http from "k6/http";
import { sleep, check } from "k6";
import { Rate } from "k6/metrics";

export let failureRate = new Rate("failed_requests");

export let options = {
  stages: [
    { duration: "30s", target: 10 }, // ramp-up to 10 users
    { duration: "1m", target: 50 }, // sustain 50 users
    { duration: "30s", target: 100 }, // ramp-up to 100 users (stress)
    { duration: "1m", target: 0 }, // ramp-down
  ],
  thresholds: {
    failed_requests: ["rate<0.05"], // <5% failure rate
    http_req_duration: ["p(95)<500"], // 95% of requests < 500ms
  },
};

const BASE_URL = "http://localhost:8080";

function randomUrl() {
  const randomString = Math.random().toString(36).substring(2, 10);
  return `https://example.com/${randomString}`;
}

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

  sleep(0.1);
}
