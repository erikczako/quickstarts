package dev.notioniq.quickstarts.mcp.shoppingcart;

import dev.notioniq.quickstarts.mcp.product.Product;
import dev.notioniq.quickstarts.mcp.product.ProductRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ShoppingCartService {

	@Inject
	private ProductRepository productRepository;

	@Inject
	private ShoppingCartRepository shoppingCartRepository;

	public List<ShoppingCartItem> find() {
		return shoppingCartRepository.findAll(currentUserId()).items().stream().toList();
	}

	public ShoppingCartItem addProduct(Integer productId) {
		Log.info("Adding product to shopping cart with id " + productId);
		var product = findProduct(productId);
		var userId = currentUserId();

		if (shoppingCartRepository.findOne(userId, productId).isPresent()) {
			throw new IllegalArgumentException("Product " + productId + " already in shopping cart");
		}

		var item = new ShoppingCartItem(userId, productId, product.getPrice());
		return shoppingCartRepository.save(item);
	}

	public void removeProduct(Integer productId) {
		Log.info("Removing product from shopping cart with id " + productId);
		var userId = currentUserId();
		var existing = shoppingCartRepository.findOne(userId, productId)
			.orElseThrow(() -> new IllegalArgumentException("Product " + productId + " is not in the shopping cart"));

		shoppingCartRepository.delete(existing);
	}

	public void delete() {
		var userId = currentUserId();
		Log.info("Deleting shopping cart for user " + userId);
		shoppingCartRepository.deleteAll(userId);
	}

	private Product findProduct(Integer productId) {
		return productRepository.findOne(productId)
			.orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " does not exist"));
	}

	private Integer currentUserId() {
		return 222;
	}

}