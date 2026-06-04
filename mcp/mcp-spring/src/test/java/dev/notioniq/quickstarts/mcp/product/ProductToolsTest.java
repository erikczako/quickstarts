package dev.notioniq.quickstarts.mcp.product;

import dev.notioniq.quickstarts.mcp.DynamoDbContainer;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@ImportTestcontainers(DynamoDbContainer.class)
class ProductToolsTest {

	@Autowired
	ProductTools productTools;

	@Autowired
	DynamoDbTemplate dynamoDbTemplate;

	@BeforeEach
	void beforeEach() {
		dynamoDbTemplate.scanAll(Product.class).items().forEach(dynamoDbTemplate::delete);
	}

	@Test
	void retrieveAllProductsReturnsEmptyListTest() {
		assertTrue(productTools.retrieveAllProducts().isEmpty());
	}

	@Test
	void retrieveAllProductsReturnsSavedProduct() {
		var product = new Product("1", "Personal Computer", new BigDecimal("2499"), "Some PC, 24GB RAM, 512GB SSD.");
		dynamoDbTemplate.save(product);

		var result = productTools.retrieveAllProducts();

		assertEquals(1, result.size());
		var response = result.getFirst();
		assertNotNull(response);
		assertEquals(product.getPk(), response.getPk());
		assertEquals(product.getName(), response.getName());
		assertEquals(product.getPrice(), response.getPrice());
	}

}