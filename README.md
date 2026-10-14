# PayWithoutBank 💸

### A Spring Boot Digital Wallet Backend

PayWithoutBank is a digital wallet backend built using Java and Spring Boot. It demonstrates REST API development, database persistence, wallet deposits, money transfers, and concurrency control.

The project focuses on maintaining consistent wallet balances when multiple operations access wallet data concurrently.

## 🚀 Live Demo

**Swagger UI:**  
https://paywithoutbank.onrender.com/swagger-ui/index.html

**GitHub Repository:**  
https://github.com/utkarshswastikshree-source/paywithoutbank

> The application is deployed on Render. If the service has been inactive, the first request may take additional time to respond.

## ✨ Features

- Create users through REST APIs.
- Automatically create a wallet when creating a user.
- Deposit money into a wallet.
- Transfer money between wallets.
- Retrieve wallet balances.
- Store users, wallets, and transaction records in PostgreSQL.
- Use Spring Data JPA and Hibernate for database operations.
- Apply pessimistic locking to wallet retrieval operations.
- Use transactional processing for wallet updates.
- Explore and test APIs through Swagger UI.
- Include automated tests using JUnit 5.

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

## 🏗️ Application Architecture

The application follows a layered architecture.

```text
             Client
          (Swagger UI)
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
           PostgreSQL
```

### Main Components

- **Controller Layer:** Handles incoming HTTP requests and API responses.
- **Service Layer:** Implements wallet operations and business logic.
- **Repository Layer:** Performs database operations using Spring Data JPA.
- **Entity Layer:** Represents users, wallets, and transactions.
- **Database:** PostgreSQL stores persistent application data.

## 🔌 API Endpoints

The following endpoints are available in the Swagger documentation.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users` | Create a user and associated wallet |
| POST | `/api/wallets/{id}/deposit` | Deposit money into a wallet |
| POST | `/api/wallets/transfer` | Transfer money between wallets |
| GET | `/api/wallets/{id}/balance` | Retrieve a wallet balance |

Additional endpoints may be available in Swagger UI.

## 📝 Example API Requests

### 1. Create a User

**Endpoint**

```http
POST /api/users
Content-Type: application/json
```

**Request Body**

```json
{
  "name": "DemoUser",
  "email": "demo@example.com"
}
```

The API returns the created user's details. The service also creates an associated wallet.

### 2. Deposit Money

**Endpoint**

```http
POST /api/wallets/{id}/deposit
Content-Type: application/json
```

**Example Request Body**

```json
{
  "amount": 100
}
```

Replace `{id}` with the actual wallet ID.

### 3. Transfer Money

**Endpoint**

```http
POST /api/wallets/transfer
Content-Type: application/json
```

**Request Body**

```json
{
  "fromWalletId": 2,
  "toWalletId": 1,
  "amount": 30
}
```

This example transfers ₹30 from wallet 2 to wallet 1. The wallet IDs must exist, and the sender must have sufficient funds.

### 4. Check Wallet Balance

**Endpoint**

```http
GET /api/wallets/{id}/balance
```

Replace `{id}` with the wallet ID whose balance you want to retrieve.

## 🧪 Manual API Testing

The deployed APIs were tested through Swagger UI using test data.

One verified scenario was:

| Operation | Result |
|---|---|
| Create a user | Successful |
| Deposit ₹100 into wallet 2 | Successful |
| Transfer ₹30 from wallet 2 to wallet 1 | Successful |
| Check wallet 2 balance | ₹70 |
| Check wallet 1 balance | ₹30 |
| Compare combined balances | ₹100 |

This scenario demonstrates that the tested transfer deducted ₹30 from the sender and credited ₹30 to the receiver.

## 🔒 Concurrency Control

### The Problem: Race Conditions

A race condition can occur when multiple requests read and update the same wallet concurrently. Without suitable concurrency control, updates may conflict and result in inconsistent balances.

### Pessimistic Locking

The wallet repository uses a pessimistic write lock:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT w FROM Wallet w WHERE w.id = :id")
Optional<Wallet> findByIdWithLock(@Param("id") Long id);
```

This requests a database write lock when retrieving a wallet, helping prevent conflicting concurrent updates to the selected row.

### Transaction Management

The service uses `@Transactional` for deposit and transfer operations.

Transactional processing groups related database changes so that qualifying failures can trigger a rollback instead of leaving partial updates.

The effectiveness of concurrency control depends on the transaction boundaries, locking strategy, and database behaviour for all rows involved in a transfer.

## 🧪 Automated Testing

The project includes JUnit 5 testing for concurrent transfer behaviour.

The concurrency test should verify:

- Expected final balances.
- Consistency of the combined wallet balance.
- Correct handling of concurrent requests.
- Correct behaviour when a transfer cannot be completed.

To run the tests locally, use the Maven test command described below. Confirm the test results in your IDE or build output before making claims about the number of successful concurrent transfers.

## 🗄️ Database Design

The project uses PostgreSQL with Spring Data JPA and Hibernate.

The main entities include:

- **User:** Stores user details.
- **Wallet:** Stores the wallet balance and its associated user.
- **Transaction:** Records wallet operations such as deposits and transfers.

Wallet balances use Java's `BigDecimal`, which is appropriate for representing decimal monetary values without the usual binary floating-point rounding issues.

## 💻 Running the Project Locally

### Prerequisites

- Java 17
- PostgreSQL
- Maven, or the Maven Wrapper included in the repository

### Step 1: Clone the Repository

```bash
git clone https://github.com/utkarshswastikshree-source/paywithoutbank.git
cd paywithoutbank
```

### Step 2: Configure the Database

Configure the PostgreSQL connection in:

`src/main/resources/application.properties`

Use your own database URL, username, and password. Keep credentials and other secrets out of GitHub.

### Step 3: Run the Application

On Windows, if the Maven Wrapper is present:

```bash
mvnw.cmd spring-boot:run
```

Alternatively, if Maven is installed:

```bash
mvn spring-boot:run
```

### Step 4: Open Swagger UI

Visit:

http://localhost:8080/swagger-ui/index.html

You can then explore and test the available endpoints.

## ✅ Running Automated Tests

On Windows, if the Maven Wrapper is available:

```bash
mvnw.cmd test
```

Alternatively:

```bash
mvn test
```

Review the test output and confirm that all expected assertions pass.

## 🔐 Security and Limitations

PayWithoutBank is an educational backend project, not a production banking system.

Before any production use, additional work would be required for:

- Authentication and authorization.
- Strong input validation and error handling.
- More comprehensive concurrency and integration testing.
- Secure secret management.
- Monitoring, logging, and operational safeguards.
- Appropriate financial compliance and security reviews.

Use test data only. Do not use this demonstration application to store real financial information or handle real money.

## 🔮 Future Improvements

- Implement authentication and role-based authorization.
- Add transaction history and pagination APIs.
- Expand integration and concurrency test coverage.
- Improve error responses and request validation.
- Add structured logging and monitoring.
- Improve deployment automation and production security.

These items are possible future enhancements and are not claims that they have already been implemented.

## 👨‍💻 Author

**Utkarsh Srivastava**  
B.Tech Computer Science and Engineering  
Cochin University of Science and Technology (CUSAT)

---

Built as a hands-on project to explore Java backend development, REST APIs, database transactions, and concurrency control.
