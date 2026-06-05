package dev.notioniq.quickstarts.mcp.shoppingcart;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.Optional;

import static dev.notioniq.quickstarts.mcp.shoppingcart.ShoppingCartItem.PRIMARY_KEY_PREFIX;

@ApplicationScoped
public class ShoppingCartRepository {

	@Inject
	private DynamoDbTable<ShoppingCartItem> shoppingCartItemTable;

	public PageIterable<ShoppingCartItem> findAll(Integer userId) {
		var condition = QueryConditional.keyEqualTo(builder -> builder.partitionValue(PRIMARY_KEY_PREFIX + userId));
		var query = QueryEnhancedRequest.builder().queryConditional(condition).build();
		return shoppingCartItemTable.query(query);
	}

	public Optional<ShoppingCartItem> findOne(Integer userId, Integer productId) {
		var key = Key.builder().partitionValue(PRIMARY_KEY_PREFIX + userId).sortValue(productId.toString()).build();
		return Optional.ofNullable(shoppingCartItemTable.getItem(key));
	}

	public ShoppingCartItem save(ShoppingCartItem shoppingCartItem) {
		shoppingCartItemTable.putItem(shoppingCartItem);
		return shoppingCartItem;
	}

	public void deleteAll(Integer userId) {
		findAll(userId).items().forEach(this::delete);
	}

	public void delete(ShoppingCartItem item) {
		shoppingCartItemTable.deleteItem(item);
	}

}
