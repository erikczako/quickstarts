package dev.notioniq.quickstarts.mcp;

import dev.notioniq.quickstarts.mcp.product.Product;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.math.BigDecimal;

@QuarkusMain
@IfBuildProfile("dev")
public class Application implements QuarkusApplication {

	@Inject
	private DynamoDbTable<Product> productTable;

	@Override
	public int run(String... args) {
		try {
			productTable.createTable();
			productTable.putItem(new Product("1", "Coffee Mug", new BigDecimal("12.99"), "Ceramic mug"));
			productTable.putItem(new Product("2", "Notebook", new BigDecimal("8.50"), "A5 notebook, 200 pages"));
			productTable.putItem(new Product("3", "Desk Lamp", new BigDecimal("34.00"), "LED desk lamp"));
		} catch (ResourceInUseException exception) {
			Log.info("Table already created");
		}

		Quarkus.waitForExit();
		return 0;
	}
}
