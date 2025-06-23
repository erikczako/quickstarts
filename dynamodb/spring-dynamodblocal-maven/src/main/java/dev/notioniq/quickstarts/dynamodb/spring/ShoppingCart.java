package dev.notioniq.quickstarts.dynamodb.spring;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;

@DynamoDbBean
public class ShoppingCart {

    private Long userId;

    private int numberOfItems;

    private BigDecimal totalPrice;

    public ShoppingCart() {}

    public ShoppingCart(Long userId, BigDecimal totalPrice, int numberOfItems) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.numberOfItems = numberOfItems;
    }

    @DynamoDbPartitionKey
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
