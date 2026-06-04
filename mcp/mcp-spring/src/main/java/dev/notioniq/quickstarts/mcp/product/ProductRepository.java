package dev.notioniq.quickstarts.mcp.product;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.Optional;

import static dev.notioniq.quickstarts.mcp.product.Product.PRIMARY_KEY;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

	private final DynamoDbTemplate dynamoDbTemplate;

	public Optional<Product> findOne(Integer id) {
		var key = Key.builder().partitionValue(PRIMARY_KEY).sortValue(id.toString()).build();
		return Optional.ofNullable(dynamoDbTemplate.load(key, Product.class));
	}

	public PageIterable<Product> findAll() {
		var condition = QueryConditional.keyEqualTo(builder -> builder.partitionValue(PRIMARY_KEY));
		var query = QueryEnhancedRequest.builder().queryConditional(condition).build();
		return dynamoDbTemplate.query(query, Product.class);
	}

}