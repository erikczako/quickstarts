package com.notioniq.quickstarts.dynamodb.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ApplicationScoped
public class ShoppingCartService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public Optional<ShoppingCart> find() {
        return shoppingCartRepository.find(currentUserId());
    }

    public void delete() {
        var userId = currentUserId();
        logger.info("Deleting shopping cart for user {}", userId);
        shoppingCartRepository.delete(userId);
        logger.info("Successfully deleted shopping cart for user {}", userId);
    }

    public ShoppingCart save(ShoppingCartRequest shoppingCartRequest) {
        var userId = currentUserId();
        var shoppingCart = new ShoppingCart(userId, shoppingCartRequest.getTotalPrice(), shoppingCartRequest.getNumberOfItems());
        logger.info("Creating new shopping cart for user {}.", userId);

        shoppingCartRepository.save(shoppingCart);
        logger.info("Successfully created shopping cart for user {}.", userId);
        return shoppingCart;
    }

    /**
     * This is a placeholder. In a real application, this would be retrieved from the current security context.
     * For example: var principal = securityContext.getUserPrincipal();
     * @return current users id
     */
    private Long currentUserId() {
        return 222L;
    }
}
