package dev.notioniq.quickstarts.dynamodb.quarkus;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public Optional<ShoppingCart> find() {
        return shoppingCartRepository.find(currentUserId());
    }

    public void delete() {
        var userId = currentUserId();
        Log.info("Deleting shopping cart for user " + userId);
        shoppingCartRepository.delete(userId);
        Log.info("Successfully deleted shopping cart for user " + userId);
    }

    public ShoppingCart save(ShoppingCartRequest shoppingCartRequest) {
        var userId = currentUserId();
        var shoppingCart = new ShoppingCart(userId, shoppingCartRequest.totalPrice(), shoppingCartRequest.numberOfItems());
        Log.info("Creating new shopping cart for user " + userId);

        shoppingCartRepository.save(shoppingCart);
        Log.info("Successfully created shopping cart for user " + userId);
        return shoppingCart;
    }

    private Long currentUserId() {
        return 222L;
    }
}
