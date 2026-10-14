# PayWithoutBank 💸

### A Spring Boot Digital Wallet Backend

PayWithoutBank is a digital wallet backend built using Java and Spring Boot. It demonstrates REST API development, database persistence, wallet deposits, money transfers, and concurrency control.

The project focuses on maintaining consistent wallet balances when multiple operations access wallet data concurrently.

## 🚀 Live Demo

**Live Application:** https://paywithoutbank.onrender.com

**API Documentation:** https://paywithoutbank.onrender.com/swagger-ui/index.html

Use Swagger UI to explore and test the available REST API endpoints.

## ✨ Features

- Create users and automatically associate wallets with them.
- Deposit money into a wallet.
- Transfer money between two wallets.
- Retrieve wallet balances.
- Store wallet and transaction data in a relational database.
- Handle wallet operations using transactional processing.
- Use pessimistic locking to help prevent conflicting concurrent updates.
- Test concurrent money transfers using JUnit and Spring Boot testing.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 3 | Backend framework |
| Spring Data JPA | Database access |
| Hibernate | Object-relational mapping |
| PostgreSQL | Relational database |
| Neon | Hosted PostgreSQL database |
| Docker | Containerization |
| Render | Deployment |
| JUnit 5 | Automated testing |
| Swagger / OpenAPI | API documentation |
| Maven | Build and dependency management |

## 🏗️ Application Architecture

The application follows a layered architecture.

```text
Client (Swagger UI)
        |
        v
REST Controllers
        |
        v
WalletService
        |
        v
JPA Repositories
        |
        v
PostgreSQL Database
```

### Main Components

- **Controller Layer:** Receives HTTP requests and returns API responses.
- **Service Layer:** Implements wallet creation, deposits, transfers, and balance retrieval.
- **Repository Layer:** Performs database operations through Spring Data JPA.
- **Entity Layer:** Represents users, wallets, and transactions.
- **Database Layer:** Stores persistent application data.

## 🔌 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users` | Create a user and associated wallet |
| POST | `/api/wallets/{id}/deposit` | Deposit money into a wallet |
| POST | `/api/wallets/transfer` | Transfer money between wallets |
| GET | `/api/wallets/{id}/balance` | Retrieve a wallet balance |

Additional endpoints may be available in Swagger UI.

## 🔐 Concurrency Control

### The Problem: Race Conditions

When multiple requests update the same wallet simultaneously, conflicting updates can lead to incorrect balances if concurrency is not handled properly.

### Pessimistic Locking

The wallet repository uses a pessimistic write lock when fetching a wallet for a protected update.

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT w FROM Wallet w WHERE w.id = :id")
Optional<Wallet> findByIdWithLock(@Param("id") Long id);
```

This requests a database-level write lock on the selected wallet row, helping prevent conflicting concurrent updates.

### Transaction Management

The service uses `@Transactional` for deposit and transfer operations so that related database changes can be committed or rolled back together.

## 🧪 Concurrency Testing

The application includes a concurrency test using JUnit, Spring Boot, `ExecutorService`, and `CountDownLatch`.

The test creates two users, deposits 1000.00 into Alice's wallet, and executes 100 concurrent transfers of 1.00 each from Alice to Bob.

### Test Results

| Metric | Result |
|---|---:|
| Initial Alice Balance | 1000.00 |
| Number of Transfers | 100 |
| Successful Transfers | 100 |
| Amount Per Transfer | 1.00 |
| Alice Final Balance | 900.00 |
| Bob Final Balance | 100.00 |
| Test Status | PASSED |

### Key Observations

- All 100 transfer requests completed successfully in the test run.
- Alice's balance decreased by 100.00.
- Bob's balance increased by 100.00.
- The actual final balances matched the expected balances.

These results demonstrate correct balance updates in this test scenario under concurrent transfer requests. They do not, by themselves, guarantee correctness under every possible workload or production condition.

## 🗄️ Database Design

The application uses entities to represent the following data:

- **User:** Stores user information such as name and email.
- **Wallet:** Stores wallet balances and the associated user.
- **Transaction:** Records wallet operations such as deposits and transfers.

A wallet is associated with a user, and transaction records help track money movements.

## ▶️ Running Locally

### Prerequisites

- Java 17 or a compatible JDK.
- Maven.
- A configured database, or the project's local H2 configuration.

### Steps

1. Clone the repository.

   ```bash
   git clone https://github.com/utkarshswastikshree-source/paywithoutbank.git
   ```

2. Open the project in IntelliJ IDEA or another Java IDE.

3. Configure the required database settings in `application.properties`.

4. Run the Spring Boot application using the IDE or Maven.

   ```bash
   mvn spring-boot:run
   ```

5. Open Swagger UI to explore the API endpoints.

   ```text
   http://localhost:8080/swagger-ui/index.html
   ```

## 📚 What I Learned

- Designing REST APIs with Spring Boot.
- Implementing database persistence using Spring Data JPA.
- Managing transactional money transfers.
- Using pessimistic locking for concurrent wallet updates.
- Writing automated tests with JUnit and Spring Boot.
- Deploying a backend application and connecting it to a hosted database.

## 👨‍💻 Author

**Utkarsh Srivastava**

GitHub: [@utkarshswastikshree-source](https://github.com/utkarshswastikshree-source)

---

If you find this project useful, feel free to explore the repository and try the live API documentation.
