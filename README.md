# listed-api

## Description

This repository contains the backend API code for Listed Inventory Management System. 

## Frameworks and Tools

The system is built using Java 17 and Spring Boot 3.1.2 with MySQL as the database.

## Folder Structure

The project follows a model-based folder structure. Here's an example layout:

```
com
+- citu
      +- listed
          +- ListedApplication.java
          |
          +- user
          |   +- config
          |   +- User.java
          |   +- UserController.java
          |   +- UserService.java
          |   +- UserRepository.java
          |
          +- product
          |   +- dtos
          |   +- enums
          |   +- mappers
          |   +- exceptions
          |   +- Product.java
          |   +- ProductController.java
          |   +- ProductService.java
          |   +- ProductRepository.java
          |
          +- store
          |   +- dtos
          |   +- enums
          |   +- mappers
          |   +- exceptions
          |   +- Store.java
          |   +- StoreController.java
          |   +- StoreService.java
          |   +- StoreRepository.java
          |
          +- shared
          |   +- dtos
          |   +- enums
          |   +- mappers
          |   +- exceptions
```

## Naming Convention

- Class, Interface: PascalCase
- Variable, Method: camelCase
- Constant: UPPER_CASE
- Package: lowercase

## Contributions

We encourage contributors to follow the suggested GitHub workflow when contributing to this repository. Make sure to:

1. Create a new branch for your feat/fix/misc.
3. Commit your changes with descriptive messages.
4. Push your changes to your branch.
5. Submit a pull request to the repository's main branch.

## Getting Started

To get started with the backend API, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in your preferred Java development environment.
3. Configure the necessary application properties (database credentials, etc.).
4. Run the application with *./mvnw spring-boot:run*.
