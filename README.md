## Auction Platform – Backend (Spring Boot)

**Frontend (React) → REST API → Spring Boot → MySQL Database**  <br>

This is the backend service for the Auction Platform. <br>
It is built using Spring Boot and follows a layered architecture (Controller → Service → Repository).  <br>

The backend handles authentication, bidding logic, auction management, role-based access control, and payment verification. <br>

### 🚀 Features
🔐 Authentication & Security

* JWT-based authentication
* Google OAuth 2.0 Login integration
* Role-based authorization (User / Seller / Admin)
* Password encryption using BCrypt
* Secure REST APIs using Spring Security

🛍 Auction Management

* Create, update, delete auctions
* View all auction listings
* View auction details
* Automatic auction status update (Active / Closed / Expired)

💰 Bidding System

* Place bids on active auctions
* Validates bid amount (cannot bid lower than highest bid)
* Tracks highest bidder
* Maintains bidding history

🛒 Seller Management
* Sellers can manage their own auctions
* View bids on their products
* Track auction performance

🛠 Admin Controls
* Manage users
* Manage auctions
* Monitor platform activity

💳 Payment Integration
* Razorpay payment verification
* Secure transaction handling

### 🧠 Backend Responsibilities
* Handles business logic
* Validates bidding rules
* Manages users & roles
* Secures APIs using JWT
* Connects to MySQL database
* Schedules automatic auction updates
* Global exception handling

### 🧑🏽‍💻Tech Stack
* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* OAuth2 (Google Login)
* MySQL / TiDB(Cloud Database Deployment)
* Maven
* Render (Deployment)

  [Frontend Repository](https://github.com/Jarin0026/auction-frontend)
