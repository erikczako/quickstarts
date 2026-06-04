package dev.notioniq.quickstarts.mcp.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpTool.McpAnnotations;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductTools {

	private final ProductService productService;

	@McpTool(description = "Retrieve all products", annotations = @McpAnnotations(readOnlyHint = true,
			destructiveHint = false, idempotentHint = true, openWorldHint = false))
	public List<Product> retrieveAllProducts() {
		log.info("Retrieving all products");
		return productService.findAll();
	}

}
