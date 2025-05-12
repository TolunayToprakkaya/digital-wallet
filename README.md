# Spring Boot Digital Wallet Project

Spring Boot Web application to provide REST API in JSON

<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/TolunayToprakkaya/digital-wallet.git
   ```
2. Install NPM packages
   ```sh
   npm install
   ```
3. Build application
   ```sh
   java -jar .\target\digitalwallet-0.0.1-SNAPSHOT.jar
   ```
4. Run the test suite
   ```sh
   mvn clean test
   ```   
## Tech Stacks

This project template uses:

* Java 21
* Spring Boot
* [H2 Database](https://www.h2database.com/html/main.html) for database migrations
* [Spring Data JPA](https://spring-io.translate.goog/projects/spring-data-jpa?_x_tr_sl=en&_x_tr_tl=tr&_x_tr_hl=tr&_x_tr_pto=tc&_x_tr_hist=true) for database access
* [Spring Web](https://spring.io/guides/gs/serving-web-content/) to serve HTTP requests
* [Spring Security](https://spring.io/projects/spring-security/) for authentication
* [Maven](https://maven.apache.org/) to build and, in dev-mode, run the application with hot reload
* [Junit](https://junit.org/junit5/) for testing

<!-- API DESCRIPTIONS -->
## API Descriptions

### Sign Up (Both Type Can Reach)
```
POST /auth/signUp
Accept: application/json
Content-Type: application/json
{
    "identityNumber": 12345678987,
    "name": "john",
    "surname": "lastname",
    "email": "john@gmail",
    "password": "john",
    "role": "USER"
}

Response: HTTP 200
{
    "statusCode": 200,
    "message": "User Saved Successfully",
    "customer": {
        "id": 1,
        "customerId": "4d8ac112-4605-450b-935c-d1f2ef659b2e",
        "identityNumber": 12345678987,
        "name": "john",
        "surname": "lastname",
        "email": "john@gmail",
        "password": "$2a$10$XT2qrmudbMV1RIIuImq/uOcunMFjr5gyxrRYudPzUIqgg4go6ERVu",
        "role": "USER",
        "enabled": true,
        "accountNonLocked": true,
        "username": "john@gmail",
        "authorities": [
            {
                "authority": "USER"
            }
        ],
        "accountNonExpired": true,
        "credentialsNonExpired": true
    }
}
```
### Sign In (Both Type Can Reach)
```
POST /auth/signIn
Accept: application/json
Content-Type: application/json
{
    "email": "john@gmail",
    "password": "john"
}

Response: HTTP 200
{
    "statusCode": 200,
    "message": "Successfully Signed In",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGdtYWlsIiwiaWF0IjoxNzQ3MDgwOTE4LCJleHAiOjE3NDcxNjczMTh9.Qew1x98Xz6gIa4cGvk1sLBrwEVffZAkreXkc6Tg0WaU",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGdtYWlsIiwiaWF0IjoxNzQ3MDgwOTE4LCJleHAiOjE3NDcxNjczMTh9.Qew1x98Xz6gIa4cGvk1sLBrwEVffZAkreXkc6Tg0WaU",
    "expressionTime": "24Hr"
}
```
### Refresh Token (Both Type Can Reach)
```
Get /api/user/refreshToken
Accept: application/json
Content-Type: application/json

Response: HTTP 200
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiaWF0Ijo....",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiaWF0Ijox....",
    "error": null
}
```
### Create Wallet (Both Type Can Reach)
```
POST /wallet/create
Accept: application/json
Content-Type: application/json
{
    "name": "john wallet",
    "currency": "TL",
    "activeForShopping": true,
    "activeForWithdraw": true
}

Response: HTTP 200
{
    "name": "john wallet",
    "currency": "TL",
    "balance": 0,
    "usableBalance": 0,
    "activeForWithdraw": true,
    "activeForShopping": true
}
```
### Create Wallet By Customer Id (Both Type Can Reach)
```
GET /wallet/{customerId}?currency=TL&minBalance=0&maxBalance=0
Accept: application/json
Content-Type: application/json

Response: HTTP 200
[
    {
        "name": "john wallet",
        "currency": "TL",
        "balance": 0.0000,
        "usableBalance": 0.0000,
        "activeForWithdraw": true,
        "activeForShopping": true
    }
]
```
### Deposit (Only User Type Can Reach)
```
POST /deposit
Accept: application/json
Content-Type: application/json
{
    "amount": 1000,
    "walletId": "4715d131-7b49-40af-aaa4-f8a34c5b72d4",
    "source": "TR76 0009 9012 3456 7800 1000 01"
}

Response: HTTP 200
{
    "name": "john wallet",
    "currency": "TL",
    "balance": 1000.0000,
    "usableBalance": 1000.0000
}
```
### Get Transaction List By Wallet Id (Only User Type Can Reach)
```
GET /transaction/customer/{walletId}
Accept: application/json
Content-Type: application/json

Response: HTTP 200
[
    {
        "id": null, // This value just display admin's request
        "walletId": null, // This value just display admin's request
        "amount": 1000.00,
        "type": "DEPOSIT",
        "oppositePartyType": "PAYMENT",
        "oppositeParty": "TR76 0009 9012 3456 7800 1000 01",
        "status": "APPROVED"
    }
]
```
### Get Transaction List By Wallet Id (Only Admin Type Can Reach)
```
GET /transaction/{walletId}
Accept: application/json
Content-Type: application/json

Response: HTTP 200
[
    {
        "id": 123, // This value just display admin's request
        "walletId": 4715d131-7b49-40af-aaa4-f8a34c5b72d4, // This value just display admin's request
        "amount": 1000.00,
        "type": "DEPOSIT",
        "oppositePartyType": "PAYMENT",
        "oppositeParty": "TR76 0009 9012 3456 7800 1000 01",
        "status": "APPROVED"
    }
]
```
### Withdraw (Only User Type Can Reach)
```
POST /withdraw
Accept: application/json
Content-Type: application/json
{
    "amount": 900,
    "walletId": "4715d131-7b49-40af-aaa4-f8a34c5b72d4",
    "destination": "TR76 0009 9012 3456 7800 1000 01"
}

Response: HTTP 200
{
    "name": "john wallet",
    "currency": "TL",
    "balance": 100.0000,
    "usableBalance": 100.0000
}
```
### Approve Deposit (Only Admin Type Can Reach)
```
POST /approve/deposit
Accept: application/json
Content-Type: application/json
{
    "transactionId": 3,
    "status": "APPROVED"
}

Response: HTTP 200
{
    "walletId": "4715d131-7b49-40af-aaa4-f8a34c5b72d4",
    "balance": 1101.0000,
    "usableBalance": 1101.0000,
    "status": "APPROVED"
}
```
### Approve Withdraw (Only Admin Type Can Reach)
```
POST /approve/withdraw
Accept: application/json
Content-Type: application/json
{
    "transactionId": 3,
    "status": "APPROVED"
}

Response: HTTP 200
{
    "walletId": "4715d131-7b49-40af-aaa4-f8a34c5b72d4",
    "balance": 1101.0000,
    "usableBalance": 1101.0000,
    "status": "APPROVED"
}
```
