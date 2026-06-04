package dev.notioniq.quickstarts.mcp.shoppingcart;

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
public final class ShoppingCartItem {

	public static final String PRIMARY_KEY_PREFIX = "SHOPPING_CART#";

	@Getter(onMethod_ = @DynamoDbPartitionKey)
	private String pk;

	@Getter(onMethod_ = @DynamoDbSortKey)
	private String sk;

	private BigDecimal unitPrice;

	public ShoppingCartItem(Integer userId, Integer productId, BigDecimal unitPrice) {
		this.pk = PRIMARY_KEY_PREFIX + userId;
		this.sk = productId.toString();
		this.unitPrice = unitPrice;
	}

}
