# Embedded DynamoDB Local with Quarkus and Maven
This project is a practical demonstration of how to run an embedded instance of DynamoDB Local directly within a Quarkus application using Maven.

This approach completely automates the local database setup, requiring no external tools like the AWS CLI or Docker. It's ideal for local development, integration testing, and ensuring a consistent environment for all developers on a team.

The example application is a simple Shopping Cart service demonstrating basic CRUD (Create, Read, Delete) operations.

For a detailed, step-by-step explanation, please see the full blog post. [link]

## Features
* Zero Manual Setup: DynamoDB Local starts and stops with the Quarkus application.
* Automated Table Creation: The required table is created programmatically on startup.
* Fully Integrated: Uses Quarkus AWS for seamless integration with DynamoDbEnhancedClient and DynamoDbTemplate.
* Complete CRUD Example: Includes a fully functional REST controller, service, and repository layer.
* Integration Tested: Comes with a @QuarkusTest that validates the entire workflow.

## Prerequisites
To build and run this project, you will need:

* OpenJDK 24
* Maven 3.9+

## Getting Started
### Clone the repository:
```bash
   git clone [link]
   cd quarkus-dynamodblocal-maven
```

### Run the application:
```bash
   ./mvnw quarkus:dev
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
A @Produces configuration, which programmatically starts the embedded database and creates the shopping_cart table on application startup.

```java
public class DynamoDbLocalConfiguration {

    @Produces
    DynamoDbTable<ShoppingCart> shoppingCartDynamoDbTable() {
        var dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDBEmbedded.create(true).dynamoDbClient())
                .build();

        var table = dynamoDbEnhancedClient.table("shopping_cart", TableSchema.fromClass(ShoppingCart.class));
        table.createTable();
        return table;
    }
}
```

### Running Tests
To run the integration tests, which validate the entire application stack against the embedded database, execute:

Bash
```bash
   ./mvnw clean test
```