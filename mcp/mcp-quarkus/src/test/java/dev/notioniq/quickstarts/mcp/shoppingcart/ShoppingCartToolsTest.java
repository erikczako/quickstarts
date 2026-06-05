package dev.notioniq.quickstarts.mcp.shoppingcart;

import dev.notioniq.quickstarts.mcp.DynamoDbContainerLifecycleManager;
import dev.notioniq.quickstarts.mcp.product.Product;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(DynamoDbContainerLifecycleManager.class)
class ShoppingCartToolsTest {

	@Inject
	ShoppingCartTools shoppingCartTools;

    @Inject
    DynamoDbTable<Product> productTable;

    @Inject
    DynamoDbTable<ShoppingCartItem> shoppingCartItemTable;

    @BeforeEach
    void beforeEach() {
        productTable.createTable();
    }

    @AfterEach
    void afterEach() {
        productTable.deleteTable();
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
		assertTrue(shoppingCartTools.retrieveShoppingCartItems().isEmpty());
	}

	private ShoppingCartItem mockItem() {
		var item = new ShoppingCartItem(222, 1, new BigDecimal("2499"));
		shoppingCartItemTable.putItem(item);
        return item;
	}

	private Product mockProduct() {
		var product = new Product("1", "Test", new BigDecimal("1000"), "Test description");
		productTable.putItem(product);
        return product;
	}

}