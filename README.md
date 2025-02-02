# E-commerce Retail Service

# Overview

This project is a simulation of an e-commerce service for a retail business. It provides RESTful APIs for managing customers, products, and orders. The system is designed to handle order processing, apply discount validations, and ensure data integrity through structured business logic.

# Features

Customer Management: Create, retrieve, update, and delete customer records.

Product Management: Register new products, update details, and retrieve product information.

Order Processing: Place new orders, validate discounts, and manage order transactions.

Messaging System: Uses JMS (Java Message Service) to handle asynchronous order processing.

# Technologies Used

Spring Boot (for building the REST APIs)

Spring Web (for handling HTTP requests)

Spring Data JPA (for database interactions)

H2 / PostgreSQL (for database management)

Spring JMS (for messaging and order processing)

Mockito & JUnit (for unit and integration testing)

# Installation & Setup

Prerequisites

Java 17+

Maven

Docker (optional, for database containerization)

# Running the Project

Clone the repository:

git clone https://github.com/Winchester890/Retail-e-commerce.git


# Build the project:

mvn clean install

# Run the application:

mvn spring-boot:run

# API Endpoints

Customers

POST /customer - Create a new customer

GET /customer/{id} - Retrieve customer details by ID

GET /customer - Retrieve all customers details

PUT /customer - Update customer details

DELETE /customer/{id} - Delete customer by ID

Products

POST /product - Add a new product

GET /product/{id} - Get product details

GET /product/byDescription/{description} - Get product details by description

GET /product - Retrieve all products details

PUT /product - Update product details

DELETE /product/{id} - Delete product by ID

Orders

POST /order - Create a new order

GET /order - Retrieve all orders details

GET /order/{id} - Retrieve order details by ID

GET /order/byProduct/{productId} - Retrieve order details by product ID

PUT /order - Update order details

DELETE /order/{id} - Delete order by ID

# Testing

To execute unit and integration tests, run:

mvn test

# Contributing

Feel free to open issues and submit pull requests for improvements!

# License

This project is licensed under the MIT License.
