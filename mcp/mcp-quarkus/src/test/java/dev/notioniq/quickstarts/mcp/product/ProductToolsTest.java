package dev.notioniq.quickstarts.mcp.product;

import dev.notioniq.quickstarts.mcp.DynamoDbContainerLifecycleManager;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(DynamoDbContainerLifecycleManager.class)
class ProductToolsTest {

	@Inject
	ProductTools productTools;

	@Inject
	DynamoDbTable<Product> productTable;

	@BeforeEach
	void beforeEach() {
		productTable.createTable();
	}

	@AfterEach
	void afterEach() {
		productTable.deleteTable();
	}

	@Test
	void retrieveAllProductsReturnsEmptyListTest() {
		assertTrue(productTools.retrieveAllProducts().isEmpty());
	}

	@Test
	void retrieveAllProductsReturnsSavedProduct() {
		var product = new Product("1", "Personal Computer", new BigDecimal("2499"), "Some PC, 24GB RAM, 512GB SSD.");
		productTable.putItem(product);

		var result = productTools.retrieveAllProducts();

		assertEquals(1, result.size());
		var response = result.getFirst();
		assertNotNull(response);
		assertEquals(product.getPk(), response.getPk());
		assertEquals(product.getName(), response.getName());
		assertEquals(product.getPrice(), response.getPrice());
	}

}