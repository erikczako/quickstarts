package dev.notioniq.quickstarts.mcp.shoppingcart;

import dev.notioniq.quickstarts.mcp.DynamoDbContainer;
import dev.notioniq.quickstarts.mcp.product.Product;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@ImportTestcontainers(DynamoDbContainer.class)
class ShoppingCartToolsTest {

	@Autowired
	ShoppingCartTools shoppingCartTools;

	@Autowired
	DynamoDbTemplate dynamoDbTemplate;

	@BeforeEach
	void beforeEach() {
		dynamoDbTemplate.scanAll(Product.class).items().forEach(dynamoDbTemplate::delete);
		dynamoDbTemplate.scanAll(ShoppingCartItem.class).items().forEach(dynamoDbTemplate::delete);
	}

	@Test
	void retrieveShoppingCartItemsReturnsEmptyListTest() {
		assertTrue(shoppingCartTools.retrieveShoppingCartItems().isEmpty());
	}

	@Test
	void retrieveShoppingCartItemsReturnsSavedItem() {
		var item = mockItem();
		var result = shoppingCartTools.retrieveShoppingCartItems();

		assertEquals(1, result.size());
		var response = result.getFirst();

		assertNotNull(response);
		assertEquals(item.getPk(), response.getPk());
		assertEquals(item.getSk(), response.getSk());
		assertEquals(item.getUnitPrice(), response.getUnitPrice());
	}

	@Test
	void addProductThrowsExceptionWhenProductDoesntExistTest() {
		assertThrows(IllegalArgumentException.class, () -> shoppingCartTools.addProduct(1));
	}

	@Test
	void addProductThrowsExceptionWhenProductInShoppingCartTest() {
		mockProduct();
		mockItem();
		assertThrows(IllegalArgumentException.class, () -> shoppingCartTools.addProduct(1));
	}

	@Test
	void addProductTest() {
		var product = mockProduct();
		var result = shoppingCartTools.addProduct(1);

		assertNotNull(result);
		assertEquals(product.getSk(), result.getSk());
		assertEquals(product.getPrice(), result.getUnitPrice());
	}

	@Test
	void removeProductThrowsExceptionWhenProductNotInShoppingCartTest() {
		assertThrows(IllegalArgumentException.class, () -> shoppingCartTools.removeProduct(1));
	}

	@Test
	void removeProductTest() {
		mockItem();
		shoppingCartTools.removeProduct(1);

		assertTrue(shoppingCartTools.retrieveShoppingCartItems().isEmpty());
	}

	@Test
	void removeShoppingCartItemsTest() {
		mockItem();
		assertTrue(shoppingCartTools.removeShoppingCartItems().isEmpty());
	}

	private ShoppingCartItem mockItem() {
		var item = new ShoppingCartItem(222, 1, new BigDecimal("2499"));
		return dynamoDbTemplate.save(item);
	}

	private Product mockProduct() {
		var product = new Product("1", "Test", new BigDecimal("1000"), "Test description");
		return dynamoDbTemplate.save(product);
	}

}