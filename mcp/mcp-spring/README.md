# Spring AI MCP Server

This project is a small Spring Boot quickstart that exposes an **MCP (Model Context Protocol) server**
backed by DynamoDB Local. It lets an AI client (Claude Code, Codex CLI, etc.) browse a tiny product
catalog and manage a shopping cart through MCP tools.

It uses:

- `spring-ai-starter-mcp-server-webmvc` to expose tools over the MCP streamable HTTP transport
- `spring-cloud-aws-starter-dynamodb` for DynamoDB table mapping and access
- `spring-cloud-aws-docker-compose` to start DynamoDB Local automatically in dev mode
- the official `amazon/dynamodb-local` image

## How It Works

The application starts an MCP server named `shopping-cart` and exposes it over streamable HTTP at:

```
http://localhost:8080/mcp
```

The following MCP tools are registered (see `ProductTools` and `ShoppingCartTools`):

- `retrieveAllProducts` — list the products in the catalog
- `retrieveShoppingCartItems` — get the items currently in the cart
- `addProduct(productId)` — add a product to the cart
- `removeProduct(productId)` — remove a product from the cart
- `removeShoppingCartItems` — clear the cart

Products and cart items are stored in a single DynamoDB table `shopping-cart` using a
[single-table design](https://docs.aws.amazon.com/prescriptive-guidance/latest/dynamodb-data-modeling/data-modeling.html),
so multiple entity types share the same table.

For the purpose of this quickstart, the current user is hard-coded in the service layer.

## DynamoDB Local With Docker

The repository includes a Docker Compose file for DynamoDB Local:

```yaml
services:
  dynamodb-local:
    image: "amazon/dynamodb-local:latest"
    container_name: dynamodb-local
    ports:
      - "8000:8000"
```

In the `dev` profile, Spring Boot's Docker Compose support starts this container automatically and
connects the application to `http://localhost:8000`.

## Dev Mode

Run the application in dev mode with:

```sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

In dev mode, the application creates the `shopping-cart` table on startup and seeds a few sample
products (Coffee Mug, Notebook, Desk Lamp).

Relevant dev configuration:

- `spring.cloud.aws.dynamodb.endpoint=http://localhost:8000`
- region `eu-central-1`

## Add It To Claude Code

With the server running, register it as an MCP server in Claude Code:

```sh
claude mcp add --transport http shopping-cart http://localhost:8080/mcp
```

Then start a Claude Code session and ask things like *"what products are available?"* or
*"add the desk lamp to my cart"*.

## Add It To Codex CLI

Codex CLI configures MCP servers in `~/.codex/config.toml`. Add the following entry:

```sh
codex mcp add shopping-cart --url http://localhost:8080/mcp
```

## Tests

The test suite starts DynamoDB Local with Testcontainers, creates the same `shopping-cart` table,
and exercises the MCP tools end-to-end.

Run tests with:

```sh
./mvnw test
```

## Notes

- This quickstart is centered on local development with Docker and Spring Boot dev mode.
- Table creation and product seeding are automated in dev mode and in tests.