# Embedded DynamoDB Local with Spring Boot and Maven
This project is a practical demonstration of how to run an embedded instance of DynamoDB Local directly within a Spring Boot application using Maven.

This approach completely automates the local database setup, requiring no external tools like the AWS CLI or Docker. It's ideal for local development, integration testing, and ensuring a consistent environment for all developers on a team.

The example application is a simple Shopping Cart service demonstrating basic CRUD (Create, Read, Delete) operations.

For a detailed, step-by-step explanation, please see the full blog post. [link]

## Features
* Zero Manual Setup: DynamoDB Local starts and stops with the Spring Boot application.
* Automated Table Creation: The required table is created programmatically on startup.
* Fully Integrated: Uses Spring Cloud AWS for seamless integration with DynamoDbEnhancedClient and DynamoDbTemplate.
* Complete CRUD Example: Includes a fully functional REST controller, service, and repository layer.
* Integration Tested: Comes with a @SpringBootTest that validates the entire workflow.

## Prerequisites
To build and run this project, you will need:

* OpenJDK 21
* Maven 3.9+

## Getting Started
### Clone the repository:
```bash
   git clone https://github.com/erikczako/quickstarts.git
   cd dynamodb/embedded/dynamodb-embedded-spring/
```

### Run the application:

```bash
   ./mvnw spring-boot:run
```

The application will be available at http://localhost:8080.

## How It Works
The automation is achieved through two key components:

### DynamoDBLocal Maven Dependency:
The pom.xml includes the com.amazonaws:DynamoDBLocal dependency, which contains the necessary classes to run an embedded, in-process instance of DynamoDB.

```xml
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>DynamoDBLocal</artifactId>
    <version>2.6.0</version>
</dependency>   
```

### Embedded Client Configuration:
A Spring @Bean configuration, programmatically starts the embedded database and creates the shopping_cart table on application startup.

```java
@Bean
DynamoDbEnhancedClient dynamoDbEnhancedClient() {
// Starts an in-memory, embedded DynamoDB instance
var dynamoDbClient = DynamoDBEmbedded.create(true).dynamoDbClient();

    // Configures the Enhanced Client to use the embedded instance
    var dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    // Creates the table on the new, empty database instance
    var table = dynamoDbEnhancedClient.table("shopping_cart", TableSchema.fromBean(ShoppingCart.class));
    table.createTable();

    return dynamoDbEnhancedClient;
}
```

### Running Tests
To run the integration tests, which validate the entire application stack against the embedded database, execute:

```bash
   ./mvnw clean test
```