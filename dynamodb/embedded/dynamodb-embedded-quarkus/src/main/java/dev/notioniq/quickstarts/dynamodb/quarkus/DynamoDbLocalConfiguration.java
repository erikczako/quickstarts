package dev.notioniq.quickstarts.dynamodb.quarkus;

import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import jakarta.enterprise.inject.Produces;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

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
