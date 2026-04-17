package dev.notioniq.quickstarts.dynamodb.quarkus;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.logging.Log;
import jakarta.enterprise.inject.Produces;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.dynamodb.services.local.embedded.DynamoDBEmbedded;

public class DynamoDbLocalConfiguration {

    @Produces
    @IfBuildProfile(anyOf = {"dev", "test"})
    DynamoDbTable<ShoppingCart> shoppingCartDynamoDbTable() {
        var dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDBEmbedded.create(true).dynamoDbClient())
                .build();

        var table = dynamoDbEnhancedClient.table("shopping_cart", TableSchema.fromClass(ShoppingCart.class));
        createTable(table);
        return table;
    }

    private void createTable(DynamoDbTable<ShoppingCart> table) {
        try {
            table.createTable();
        } catch (ResourceInUseException exception) {
            Log.info("Table already created");
        }
    }
}
