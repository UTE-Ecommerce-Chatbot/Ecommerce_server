# Ecommerce_server

## Overview

Ecommerce_server is a Spring Boot application designed to manage an e-commerce platform. It provides RESTful APIs for managing products, orders, customers, and more.

## Features

- User authentication and authorization
- Product management
- Order processing
- Customer management
- Reporting and analytics

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- SLF4J with Logback
- RESTful APIs

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- MySQL

## Getting Started

### Clone the repository

```bash
git clone https://github.com/nguyenphuctien4865/Ecommerce_server.git
cd Ecommerce_server# Ecommerce_server

## Configure the database
## Update the application.properties file with your MySQL database configuration

spring.datasource.url=jdbc:mysql://localhost:3306/yourdatabase
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

## Build the project

mvn clean install

## Run the application
mvn spring-boot:run

## Access the application
## The application will be accessible at http://localhost:8080.

## API Documentation
## API documentation is available at http://localhost:8080/swagger-ui.html (if Swagger is configured).

## Logging
## SLF4J with Logback is used for logging. You can configure the logging settings in the src/main/resources/logback.xml file.
```
