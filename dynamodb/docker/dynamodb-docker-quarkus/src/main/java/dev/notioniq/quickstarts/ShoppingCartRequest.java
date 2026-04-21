package dev.notioniq.quickstarts;

import java.math.BigDecimal;

public record ShoppingCartRequest(Integer numberOfItems, BigDecimal totalPrice) {
}
