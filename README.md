
# 🐐 Crypto Payment Gateway with FX Hedging

A **Java Spring Boot Fintech Project** that demonstrates a **Crypto Payment Gateway** using:
- ✅ **Transactional Outbox Pattern** to solve the dual-write problem
- ✅ **Kafka** for event streaming (payments, settlements, notifications)
- ✅ **Redis** for caching, idempotency, and session tracking
- ✅ **Postgres** as the system of record
- ✅ **Docker Compose** for easy setup of dependencies

---

## 🚀 Features
- Accepts **crypto payments** (simulated API input)
- Persists payments + outbox event in a **single transaction**
- Outbox relay guarantees **event delivery to Kafka**
- Kafka consumers handle:
  - **FX Hedging** (mock conversion to fiat)
  - **Settlement** (credit merchant wallets)
  - **Notifications** (alerts/receipts)
- **Redis** ensures:
  - Idempotency (no double credit)
  - Payment session caching
  - Rate limiting (prevent abuse)

---

## 🛠 Tech Stack
- **Java 17**, **Spring Boot 3**
- **Postgres** (system of record)
- **Redis** (cache + idempotency)
- **Kafka** (event streaming)
- **Docker Compose** (infra setup)
- **Maven** (build tool)

---

## 📂 Project Structure
```
crypto-payment-gateway/
 ├── src/main/java/com/example/crypto/
 │   ├── controller/PaymentController.java
 │   ├── entity/Payment.java
 │   ├── entity/OutboxEvent.java
 │   ├── service/PaymentService.java
 │   ├── service/OutboxRelay.java
 │   ├── consumer/FXHedgingConsumer.java
 │   ├── consumer/SettlementConsumer.java
 │   ├── consumer/NotificationConsumer.java
 │   └── CryptoPaymentGatewayApplication.java
 ├── src/main/resources/
 │   └── application.yml
 ├── docker-compose.yml
 ├── pom.xml
 └── README.md
```

---

## ⚡ Getting Started

### 1️⃣ Start Infra with Docker Compose
```bash
docker-compose up -d
```

This starts:
- Postgres (port 5432)
- Redis (port 6379)
- Kafka + Zookeeper (ports 9092, 2181)

### 2️⃣ Run Spring Boot App
```bash
mvn spring-boot:run
```

### 3️⃣ Test API

#### Create a Crypto Payment
```bash
POST http://localhost:8080/payments
Content-Type: application/json

{
  "userId": "U123",
  "amount": 100,
  "currency": "XRP"
}
```

#### Response
```json
{
  "paymentId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "PENDING",
  "currency": "XRP",
  "amount": 100
}
```

---

## 📊 Event Flow

1. Payment received → stored in Postgres + outbox.
2. Outbox relay publishes `PAYMENT_RECEIVED` event to Kafka.
3. Kafka Consumers:
   - `FXHedgingConsumer` → simulates hedge conversion.
   - `SettlementConsumer` → credits merchant wallet.
   - `NotificationConsumer` → sends receipt.
4. Redis ensures no double-processing (idempotency).

---

## 👨‍💻 Author
Designed for **Fintech Java Projects** with focus on:
- Event-driven systems
- Outbox pattern (avoiding dual-write)
- High-performance caching with Redis
- Real-world fintech problem-solving

---

## 📌 Next Steps / Extensions
- Add **Dead Letter Queue (DLQ)** for failed events.
- Integrate **real crypto APIs** (Binance, Coinbase).
- Build a **frontend dashboard** for merchants.
- Add **unit + integration tests**.

---
