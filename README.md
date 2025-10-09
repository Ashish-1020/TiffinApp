# 🍱 Tiffin – Food Ordering & Delivery App  

![Kotlin](https://img.shields.io/badge/Kotlin-1DA1F2?style=for-the-badge&logo=kotlin&logoColor=white)    ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)  ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)  ![Cloudinary](https://img.shields.io/badge/Cloudinary-3448C5?style=for-the-badge&logo=cloudinary&logoColor=white)


![Splash and Login Screens (1)](https://github.com/user-attachments/assets/c44ca24c-bcb6-482d-b966-9cb5df1eb177)



# Demo Video

https://github.com/user-attachments/assets/181a7740-e032-4e7b-a06a-609d5244a328



---

## Overview  
Tiffin is a **full-stack food ordering and delivery application** designed to provide a seamless experience for users, delivery partners, and restaurant admins.  
It offers:  
- **JWT-secured REST APIs** for secure login.  
- **WebSocket-powered live tracking** for orders & delivery.  
- A **wallet system** with Razorpay integration.  
- Scalable architecture with **MVVM + Clean Architecture + Hilt**.  

---

##  Key Features  

###  Security  
- JWT-secured authentication & role-based authorization.  

###  Real-Time Tracking  
- WebSocket-powered live delivery updates (80% latency reduction).  

###  Wallet System  
- Razorpay payment gateway integration.  
- PostgreSQL-backed wallet with **credit, debit, cashback & refund support**.  

###  Clean Architecture  
- MVVM pattern with **Hilt DI**.  
- Reduced boilerplate → 30% smaller codebase.  

---

##  Transaction Concurrency Handling  

Ensuring **atomic and consistent wallet transactions** was a key challenge.  

**Transactional Endpoints** → every operation wrapped with `@Transactional` for ACID compliance.  
**Idempotency** → each request has a unique `transactionId` to avoid duplicates.  
**Wallet Ledger** → full audit trail of all credits, debits, and refunds.  
 







