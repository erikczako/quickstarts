package dev.notioniq.quickstarts.mcp;

import dev.notioniq.quickstarts.mcp.product.Product;
import dev.notioniq.quickstarts.mcp.shoppingcart.ShoppingCartItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@ApplicationScoped
public class DynamoDbConfiguration {

    @Inject
    @ConfigProperty(name = "shopping-cart.table-name")
    private String tableName;

    @Inject
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Produces
    @ApplicationScoped
    public DynamoDbTable<Product> productTable() {
        return dynamoDbEnhancedClient.table(tableName, TableSchema.fromClass(Product.class));
    }

    @Produces
    @ApplicationScoped
    public DynamoDbTable<ShoppingCartItem> shoppingCartItemDynamoDbTable() {
        return dynamoDbEnhancedClient.table(tableName, TableSchema.fromClass(ShoppingCartItem.class));
    }
}
