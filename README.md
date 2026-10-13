# PayWithoutBank

A Spring Boot digital wallet backend that handles concurrent money transfers safely.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue)
![Docker](https://img.shields.io/badge/Docker-enabled-blue)

---

## 🚀 Live Demo

**Swagger UI:** https://paywithoutbank.onrender.com/swagger-ui/index.html

**API Base URL:** https://paywithoutbank.onrender.com

> *Hosted on Render free tier — first request may take 30–50 seconds due to cold start. Subsequent requests are fast.*

**Test it with curl:**

```bash
curl -X POST https://paywithoutbank.onrender.com/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@test.com"}'