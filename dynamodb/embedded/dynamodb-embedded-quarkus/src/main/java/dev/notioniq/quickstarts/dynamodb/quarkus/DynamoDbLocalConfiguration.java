package dev.notioniq.quickstarts.dynamodb.quarkus;

import jakarta.enterprise.inject.Produces;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.dynamodb.services.local.embedded.DynamoDBEmbedded;

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
