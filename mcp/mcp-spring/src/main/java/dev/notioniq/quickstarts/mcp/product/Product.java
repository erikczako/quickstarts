package dev.notioniq.quickstarts.mcp.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@Getter
@Setter
@DynamoDbBean
@NoArgsConstructor
public final class Product {

	public static final String PRIMARY_KEY = "PRODUCT";

	@Getter(onMethod_ = @DynamoDbPartitionKey)
	private String pk;

	@Getter(onMethod_ = @DynamoDbSortKey)
	private String sk;

	private String name;

	private BigDecimal price;

	private String description;

	public Product(String productId, String name, BigDecimal price, String description) {
		this.pk = PRIMARY_KEY;
		this.sk = productId;
		this.name = name;
		this.price = price;
		this.description = description;
	}

}