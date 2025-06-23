package dev.notioniq.quickstarts.dynamodb.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@ApplicationScoped
public class ShoppingCartRepository {

    private final DynamoDbTable<ShoppingCart> dynamoDbTable;

    public ShoppingCartRepository(DynamoDbTable<ShoppingCart> dynamoDbTable) {
        this.dynamoDbTable = dynamoDbTable;
    }

    /**
     * Finds a shopping cart by its composite key.
     *
     * @param userId The partition key (user's ID).
     * @return An Optional containing the ShoppingCart if found.
     */
    public Optional<ShoppingCart> find(Long userId) {
        var key = Key.builder().partitionValue(userId).build();
        return Optional.ofNullable(dynamoDbTable.getItem(key));
    }

    /**
     * Saves (creates or updates) a shopping cart.
     *
     * @param shoppingCart The shopping cart to save.
     */
    public void save(ShoppingCart shoppingCart) {
        dynamoDbTable.putItem(shoppingCart);
    }

    /**
     * Deletes a shopping cart by its composite key.
     *
     * @param userId The partition key (user's ID).
     */
    public void delete(Long userId) {
        var key = Key.builder().partitionValue(userId).build();
        dynamoDbTable.deleteItem(key);
    }
}
