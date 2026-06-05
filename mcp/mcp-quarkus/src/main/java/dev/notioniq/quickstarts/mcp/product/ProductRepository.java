package dev.notioniq.quickstarts.mcp.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.Optional;

import static dev.notioniq.quickstarts.mcp.product.Product.PRIMARY_KEY;

@ApplicationScoped
public class ProductRepository {

	@Inject
	private DynamoDbTable<Product> productTable;

	public Optional<Product> findOne(Integer id) {
		var key = Key.builder().partitionValue(PRIMARY_KEY).sortValue(id.toString()).build();
		return Optional.ofNullable(productTable.getItem(key));
	}

	public PageIterable<Product> findAll() {
		var condition = QueryConditional.keyEqualTo(builder -> builder.partitionValue(PRIMARY_KEY));
		var query = QueryEnhancedRequest.builder().queryConditional(condition).build();
		return productTable.query(query);
	}

}