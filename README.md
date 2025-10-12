# URL Shortener

A production-style URL shortener built with Spring Boot (Java), React + TypeScript, and Postgres.

Features:
- Shorten long URLs into short codes.
- Redirect short URLs to their original destination.
- Track click analytics (timestamp, IP, user agent).

---

## Tech Stack
- **Backend**: Spring Boot, Spring Data JPA, PostgreSQL, Swagger/OpenAPI
- **Frontend**: React, TypeScript, React Router, Tailwind
- **Database**: PostgreSQL
- **Build/Deploy**: Docker, Docker Compose

---

## Project Structure


## Performance Testing
To validate the efficiency and concurrency safety of the URL Shortener service, a series of load tests were conducted using k6.

**Test Setup**
- **Tool:** k6
- **Environment:** Local development machine, with the application, PostgreSQL database (running in a Docker container), and load generator all on the same host
- **Database:**  PostgreSQL
- **DUration:** 20 seconds sustained load (also verified with longer runs exceeding several minutes)
- **Target rate:** 3000 requests/second
- **Max Virtual Users:** 300
- **Endpoints tested:**
  - POST /api/url - Create short URL
  - GET /{shortCode} - Redirect

**Results Summary**
| Metric                   | Result                                              |
| ------------------------ | --------------------------------------------------- |
| Successful requests      | **100% (0 failures)**                               |
| Average request duration | **~122 ms**                                         |
| 95th percentile latency  | **~262 ms**                                         |
| Peak throughput          | **~2,400 requests per second**                      |
| Database                 | All writes batched asynchronously with no data loss |

**Interpretation**
- The service consistently handled 2.4K requests per second with zero failed requests.
- The 95th percentile latency maintained under 300ms, demonstraiting excellent responsivess under load.
- Results reflect full-stack performance as all requests trigerred real database reads and writes.
- No concurrency issues were observed thanks to the asynchronus queueing and batch inserts.
- Longer-duration tests (minutes rather than seconds) showed no degradation in performance, confirming stability and reliability of the asynchronous batch writes.

**Limitations**
- These tests were executed ona single local machine, with the application server and load generator competing with CPU resources.
- Throuput is limited by hardware, not by application design.
- Running the same test on a dedicated server environment would likely yield significantly higher throughput.

**Conculsion**
Despite running on a constrained local setup, the system demonstrated strong scalability and concurrency safety, efficiently handling thousnds of requests per second with consistent performance and no failures.



