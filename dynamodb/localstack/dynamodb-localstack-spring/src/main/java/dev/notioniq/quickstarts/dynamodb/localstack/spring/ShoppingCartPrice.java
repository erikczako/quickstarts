package dev.notioniq.quickstarts.dynamodb.localstack.spring;

import java.math.BigDecimal;

public class ShoppingCartPrice {

    private BigDecimal totalPrice;

    public ShoppingCartPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
