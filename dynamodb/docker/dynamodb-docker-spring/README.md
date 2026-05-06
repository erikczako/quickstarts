# DynamoDB Docker Spring Boot

This project is a small Spring Boot quickstart that stores a shopping cart in DynamoDB Local running in Docker.

It uses:

- `spring-cloud-aws-docker-compose` for Docker Compose
- `spring-cloud-aws-starter-dynamodb` for DynamoDB table mapping and access
- the official Amazon DynamoDB Local image: `amazon/dynamodb-local`

## How It Works

The application exposes a simple shopping cart API:

- `POST /v1/shopping-carts`
- `GET /v1/shopping-carts/current`
- `DELETE /v1/shopping-carts/current`

The persistence layer uses the Quarkus DynamoDB enhanced extension with a named table:

- table name: `shopping_cart`
- entity class: `ShoppingCart`
- repository access through `DynamoDbTable<ShoppingCart>`

For the purpose of this quickstart, the current user is hard-coded in the service layer.

## DynamoDB Local With Docker

The repository includes a Docker Compose file for DynamoDB Local:

```yaml
services:
  dynamodb-local:
    command: "-jar DynamoDBLocal.jar -disableTelemetry"
    image: "amazon/dynamodb-local:latest"
    container_name: dynamodb-local
    ports:
      - "8000:8000"
```

Docker Compose runs automatically

The application is configured in the `dev` profile to connect to that local instance on `http://localhost:8000`.

## Dev Mode

Run the application in dev mode with:

```sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

In dev mode, the application creates the `shopping_cart` table on startup. This is implemented in the custom Quarkus main class and is only intended for local development.

Relevant dev configuration:


- `spring.cloud.aws.dynamodb.endpoint="http://localhost:8000"`
- region `eu-central-1`

## Example Requests

Create a shopping cart:

```sh
curl -X POST http://localhost:8080/v1/shopping-carts \
  -H "Content-Type: application/json" \
  -d '{
    "numberOfItems": 3,
    "totalPrice": 149.99
  }'
```

Get the current shopping cart:

```sh
curl http://localhost:8080/v1/shopping-carts/current
```

Delete the current shopping cart:

```sh
curl -X DELETE http://localhost:8080/v1/shopping-carts/current
```

## Tests

The test suite starts DynamoDB Local with Testcontainers, creates the same `shopping_cart` table, and verifies the create, read, and delete flow of the REST resource.

Run tests with:

```sh
./mvnw test
```

## Notes

- This quickstart is centered on local development with Docker and Spring Boot dev mode.
- Table creation is automated in dev mode and in tests.