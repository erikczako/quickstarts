package dev.notioniq.quickstarts.dynamodb.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
     * This is a placeholder. In a real application, this would be retrieved from the current spring security context.
     * For example: var authentication = SecurityContextHolder.getContext().getAuthentication();
     * @return current users id
     */
    private Long currentUserId() {
        return 222L;
    }
}
