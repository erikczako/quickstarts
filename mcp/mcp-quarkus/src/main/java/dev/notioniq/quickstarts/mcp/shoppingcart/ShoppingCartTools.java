package dev.notioniq.quickstarts.mcp.shoppingcart;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.Tool.Annotations;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ShoppingCartTools {

	@Inject
	private ShoppingCartService shoppingCartService;

	@Tool(description = "Retrieve shopping cart",
			annotations = @Annotations(readOnlyHint = true, destructiveHint = false, openWorldHint = false))
	public List<ShoppingCartItem> retrieveShoppingCartItems() {
		return shoppingCartService.find();
	}

	@Tool(description = "Add product to shopping cart",
			annotations = @Annotations(destructiveHint = false, openWorldHint = false))
	public ShoppingCartItem addProduct(@ToolArg(description = "ID of the product") Integer productId) {
		return shoppingCartService.addProduct(productId);
	}

	@Tool(description = "Remove product from shopping cart",
			annotations = @Annotations(idempotentHint = true, openWorldHint = false))
	public String removeProduct(@ToolArg(description = "ID of the product") Integer productId) {
		shoppingCartService.removeProduct(productId);
		return "Product " + productId + " removed from shopping cart";
	}

	@Tool(description = "Remove shopping cart",
			annotations = @Annotations(idempotentHint = true, openWorldHint = false))
	public List<ShoppingCartItem> removeShoppingCartItems() {
		shoppingCartService.delete();
		return Collections.emptyList();
	}

}
