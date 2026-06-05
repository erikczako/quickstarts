package dev.notioniq.quickstarts.mcp.shoppingcart;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@DynamoDbBean
public final class ShoppingCartItem {

	public static final String PRIMARY_KEY_PREFIX = "SHOPPING_CART#";

	private String pk;

	private String sk;

	private BigDecimal unitPrice;

	public ShoppingCartItem() {}

	public ShoppingCartItem(Integer userId, Integer productId, BigDecimal unitPrice) {
		this.pk = PRIMARY_KEY_PREFIX + userId;
		this.sk = productId.toString();
		this.unitPrice = unitPrice;
	}

	@DynamoDbPartitionKey
	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	@DynamoDbSortKey
	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
}
