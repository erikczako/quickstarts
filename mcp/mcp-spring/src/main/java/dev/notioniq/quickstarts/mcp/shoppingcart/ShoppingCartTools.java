package dev.notioniq.quickstarts.mcp.shoppingcart;

import lombok.RequiredArgsConstructor;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpTool.McpAnnotations;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShoppingCartTools {

	private final ShoppingCartService shoppingCartService;

	@McpTool(description = "Retrieve shopping cart",
			annotations = @McpAnnotations(readOnlyHint = true, destructiveHint = false, openWorldHint = false))
	public List<ShoppingCartItem> retrieveShoppingCartItems() {
		return shoppingCartService.find();
	}

	@McpTool(description = "Add product to shopping cart",
			annotations = @McpAnnotations(destructiveHint = false, openWorldHint = false))
	public ShoppingCartItem addProduct(@McpToolParam(description = "ID of the product") Integer productId) {
		return shoppingCartService.addProduct(productId);
	}

	@McpTool(description = "Remove product from shopping cart",
			annotations = @McpAnnotations(idempotentHint = true, openWorldHint = false))
	public void removeProduct(@McpToolParam(description = "ID of the product") Integer productId) {
		shoppingCartService.removeProduct(productId);
	}

	@McpTool(description = "Remove shopping cart",
			annotations = @McpAnnotations(idempotentHint = true, openWorldHint = false))
	public List<ShoppingCartItem> removeShoppingCartItems() {
		shoppingCartService.delete();
		return Collections.emptyList();
	}

}
