package dev.notioniq.quickstarts.dynamodb.quarkus;

import java.math.BigDecimal;

public class ShoppingCartRequest {

    private int numberOfItems;
    private BigDecimal totalPrice;

    public ShoppingCartRequest(int numberOfItems, BigDecimal totalPrice) {
        this.numberOfItems = numberOfItems;
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
