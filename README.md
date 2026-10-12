# PayWithoutBank

A Spring Boot digital wallet backend that handles concurrent money transfers safely.

## Features
- Create users with auto-generated wallets
- Deposit money into a wallet
- Transfer money between wallets
- Full transaction ledger (audit trail)
- Concurrency-safe transfers using pessimistic locking

## Tech Stack
Java 17 · Spring Boot 3 · Spring Data JPA · Hibernate · H2 · Maven · JUnit 5

## Architecture
Controller → Service → Repository → Database

## Key Engineering Challenge: Race Conditions

Naive transfer code reads balance, subtracts, writes back. Under concurrency, two threads read the same balance and both succeed — money is created or lost.

**Reproduced:** Fired 100 parallel transfers → Alice ended at ₹987 instead of ₹900 (₹87 lost).

**Fixed:** Pessimistic locking with @Lock(LockModeType.PESSIMISTIC_WRITE). Test now passes — Alice = ₹900, Bob = ₹100.

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/users | Create user + wallet |
| POST | /api/wallets/{id}/deposit | Deposit money |
| GET | /api/wallets/{id}/balance | Check balance |
| POST | /api/wallets/transfer | Transfer between wallets |

## How to Run
git clone https://github.com/YOUR_USERNAME/paywithoutbank.git
cd paywithoutbank
mvn spring-boot:run

App runs on http://localhost:8080

## Author
YOUR_NAME — UTKARSH SRIVASTAVA