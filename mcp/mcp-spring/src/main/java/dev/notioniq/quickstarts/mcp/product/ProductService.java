package dev.notioniq.quickstarts.mcp.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public List<Product> findAll() {
		return productRepository.findAll().items().stream().toList();
	}

}