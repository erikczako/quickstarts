package quickstarts.dynamodb.spring;

import java.math.BigDecimal;

public record ShoppingCartRequest(Integer numberOfItems, BigDecimal totalPrice) {
}
