package dev.notioniq.quickstarts.dynamodb.localstack.spring;

import java.math.BigDecimal;

public record ShoppingCartRequest(Integer numberOfItems, BigDecimal totalPrice) {
}
