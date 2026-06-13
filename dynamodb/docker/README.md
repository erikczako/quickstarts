# DynamoDB Docker Quickstarts

This directory contains two small Java quickstarts that demonstrate how to run an application against DynamoDB Local in Docker and persist a simple shopping cart.

## Projects

### `dynamodb-docker-spring`

A Spring Boot 4 example built with Spring Web MVC and Spring Cloud AWS DynamoDB support.

- Uses `spring-cloud-aws-docker-compose` to integrate with the local Docker setup
- Stores shopping cart data in a `shopping_cart` table in DynamoDB Local
- Exposes simple REST endpoints for creating, reading, and deleting the current cart

Project README: [dynamodb-docker-spring/README.md](https://github.com/erikczako/quickstarts/blob/main/dynamodb/docker/dynamodb-docker-spring/README.md)

### `dynamodb-docker-quarkus`

A Quarkus 3 example built with Quarkus REST and the DynamoDB Enhanced extension.

- Uses DynamoDB Local in Docker through the included `compose-devservices.yml`
- Stores shopping cart data in the same `shopping_cart` table structure
- Exposes the same REST API so the framework-specific implementation is easy to compare

Project README: [dynamodb-docker-quarkus/README.md](https://github.com/erikczako/quickstarts/blob/main/dynamodb/docker/dynamodb-docker-quarkus/README.md)

## Goal

The two projects implement the same basic use case with different frameworks, making this folder useful for side-by-side comparison of local DynamoDB integration, application structure, and testing approach.
