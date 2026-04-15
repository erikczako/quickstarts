package dev.notioniq.quickstarts.dynamodb.localstack.spring;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShoppingCartRepository {

    private final DynamoDbTemplate dynamoDbTemplate;

    public Optional<ShoppingCart> find(Long userId) {
        var key = Key.builder().partitionValue(userId).build();
        return Optional.ofNullable(dynamoDbTemplate.load(key, ShoppingCart.class));
    }

    public void save(ShoppingCart shoppingCart) {
        dynamoDbTemplate.save(shoppingCart);
    }

    public void delete(Long userId) {
        var key = Key.builder().partitionValue(userId).build();
        dynamoDbTemplate.delete(key, ShoppingCart.class);
    }
}
