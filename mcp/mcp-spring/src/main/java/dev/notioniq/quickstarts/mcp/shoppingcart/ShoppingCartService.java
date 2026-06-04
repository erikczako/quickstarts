package dev.notioniq.quickstarts.mcp.shoppingcart;

import dev.notioniq.quickstarts.mcp.product.Product;
import dev.notioniq.quickstarts.mcp.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartService {

	private final ProductRepository productRepository;

	private final ShoppingCartRepository shoppingCartRepository;

	public List<ShoppingCartItem> find() {
		return shoppingCartRepository.findAll(currentUserId()).items().stream().toList();
	}

	public ShoppingCartItem addProduct(Integer productId) {
		log.info("Adding product to shopping cart with id {}", productId);
		var product = findProduct(productId);
		var userId = currentUserId();

		if (shoppingCartRepository.findOne(userId, productId).isPresent()) {
			throw new IllegalArgumentException("Product " + productId + " already in shopping cart");
		}

		var item = new ShoppingCartItem(userId, productId, product.getPrice());
		return shoppingCartRepository.save(item);
	}

	public void removeProduct(Integer productId) {
		log.info("Removing product from shopping cart with id {}", productId);
		var userId = currentUserId();
		var existing = shoppingCartRepository.findOne(userId, productId)
			.orElseThrow(() -> new IllegalArgumentException("Product " + productId + " is not in the shopping cart"));

		shoppingCartRepository.delete(existing);
	}

	public void delete() {
		var userId = currentUserId();
		log.info("Deleting shopping cart for user {}", userId);
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