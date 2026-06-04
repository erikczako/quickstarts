package dev.notioniq.quickstarts.mcp;

import dev.notioniq.quickstarts.mcp.product.Product;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.math.BigDecimal;

@Slf4j
@SpringBootApplication
public class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	DynamoDbTableNameResolver dynamoDbTableNameResolver(@Value("${shopping-cart.table-name}") String tableName) {
		return new DynamoDbTableNameResolver() {

			@Override
			public <T> @NonNull String resolve(@NonNull Class<T> clazz) {
				return tableName;
			}
		};
	}

	@Bean
	@Profile({ "dev", "test" })
	CommandLineRunner runner(DynamoDbEnhancedClient client, @Value("${shopping-cart.table-name}") String tableName) {
		return _ -> {
			try {
				var productTable = client.table(tableName, TableSchema.fromBean(Product.class));
				productTable.createTable();
				productTable.putItem(new Product("1", "Coffee Mug", new BigDecimal("12.99"), "Ceramic mug"));
				productTable.putItem(new Product("2", "Notebook", new BigDecimal("8.50"), "A5 notebook, 200 pages"));
				productTable.putItem(new Product("3", "Desk Lamp", new BigDecimal("34.00"), "LED desk lamp"));
			}
			catch (ResourceInUseException e) {
				log.info("Table {} already created", tableName);
			}
		};
	}

}
