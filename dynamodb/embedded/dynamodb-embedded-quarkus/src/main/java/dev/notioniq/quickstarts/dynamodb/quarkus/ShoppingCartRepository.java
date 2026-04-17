package dev.notioniq.quickstarts.dynamodb.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@ApplicationScoped
public class ShoppingCartRepository {

    @Inject
    private DynamoDbTable<ShoppingCart> dynamoDbTable;

    public Optional<ShoppingCart> find(Long userId) {
        var key = Key.builder().partitionValue(userId).build();
        return Optional.ofNullable(dynamoDbTable.getItem(key));
    }

    public void save(ShoppingCart shoppingCart) {
        dynamoDbTable.putItem(shoppingCart);
    }

    public void delete(Long userId) {
        var key = Key.builder().partitionValue(userId).build();
        dynamoDbTable.deleteItem(key);
    }
}
