package dev.notioniq.quickstarts.mcp.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProductService {

	@Inject
	private ProductRepository productRepository;

	public List<Product> findAll() {
		return productRepository.findAll().items().stream().toList();
	}

}