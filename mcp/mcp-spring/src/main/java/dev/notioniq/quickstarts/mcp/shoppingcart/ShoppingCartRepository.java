package dev.notioniq.quickstarts.mcp.shoppingcart;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.Optional;

import static dev.notioniq.quickstarts.mcp.shoppingcart.ShoppingCartItem.PRIMARY_KEY_PREFIX;

@Repository
@RequiredArgsConstructor
public class ShoppingCartRepository {

	private final DynamoDbTemplate dynamoDbTemplate;

	public PageIterable<ShoppingCartItem> findAll(Integer userId) {
		var condition = QueryConditional.keyEqualTo(builder -> builder.partitionValue(PRIMARY_KEY_PREFIX + userId));
		var query = QueryEnhancedRequest.builder().queryConditional(condition).build();
		return dynamoDbTemplate.query(query, ShoppingCartItem.class);
	}

	public Optional<ShoppingCartItem> findOne(Integer userId, Integer productId) {
		var key = Key.builder().partitionValue(PRIMARY_KEY_PREFIX + userId).sortValue(productId.toString()).build();
		return Optional.ofNullable(dynamoDbTemplate.load(key, ShoppingCartItem.class));
	}

	public ShoppingCartItem save(ShoppingCartItem shoppingCartItem) {
		return dynamoDbTemplate.save(shoppingCartItem);
	}

	public void deleteAll(Integer userId) {
		findAll(userId).items().forEach(this::delete);
	}

	public void delete(ShoppingCartItem item) {
		dynamoDbTemplate.delete(item);
	}

}
