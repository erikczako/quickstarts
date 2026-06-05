package dev.notioniq.quickstarts.mcp.product;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.Tool.Annotations;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProductTools {

	@Inject
	private ProductService productService;

	@Tool(description = "Retrieve all products", annotations = @Annotations(readOnlyHint = true,
			destructiveHint = false, idempotentHint = true, openWorldHint = false))
	public List<Product> retrieveAllProducts() {
		Log.info("Retrieving all products");
		return productService.findAll();
	}

}
