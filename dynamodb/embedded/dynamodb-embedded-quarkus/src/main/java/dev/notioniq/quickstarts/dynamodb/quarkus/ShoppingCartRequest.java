package dev.notioniq.quickstarts.dynamodb.quarkus;

import java.math.BigDecimal;

public record ShoppingCartRequest(Integer numberOfItems, BigDecimal totalPrice) {
}
