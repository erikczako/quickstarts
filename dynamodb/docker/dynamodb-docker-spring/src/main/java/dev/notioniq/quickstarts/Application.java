package dev.notioniq.quickstarts;

import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

@Slf4j
@SpringBootApplication
public class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Profile({ "dev", "test" })
	CommandLineRunner commandLineRunner(DynamoDbEnhancedClient client, DynamoDbTableNameResolver tableNameResolver) {
		var tableName = tableNameResolver.resolve(ShoppingCart.class);
		return _ -> {
			try {
				client.table(tableName, TableSchema.fromBean(ShoppingCart.class)).createTable();
			} catch (ResourceInUseException e) {
				log.info("Table already created");
			}
		};
	}
}
