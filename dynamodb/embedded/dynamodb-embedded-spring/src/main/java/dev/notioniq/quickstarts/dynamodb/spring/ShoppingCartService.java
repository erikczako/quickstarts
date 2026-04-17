package dev.notioniq.quickstarts.dynamodb.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public Optional<ShoppingCart> find() {
        return shoppingCartRepository.find(currentUserId());
    }

    public void delete() {
        var userId = currentUserId();
        log.info("Deleting shopping cart for user {}", userId);
        shoppingCartRepository.delete(userId);
        log.info("Successfully deleted shopping cart for user {}", userId);
    }

    public ShoppingCart save(ShoppingCartRequest shoppingCartRequest) {
        var userId = currentUserId();
        var shoppingCart = new ShoppingCart(userId, shoppingCartRequest.numberOfItems(), shoppingCartRequest.totalPrice());
        log.info("Creating new shopping cart for user {}.", userId);

        shoppingCartRepository.save(shoppingCart);
        log.info("Successfully created shopping cart for user {}.", userId);
        return shoppingCart;
    }

    private Long currentUserId() {
        return 222L;
    }
}
