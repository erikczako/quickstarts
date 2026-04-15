package dev.notioniq.quickstarts.dynamodb.localstack.spring;

import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@SpringBootApplication
public class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(DynamoDbEnhancedClient client, DynamoDbTableNameResolver tableNameResolver) {
		var tableName = tableNameResolver.resolve(ShoppingCart.class);
		return (args) -> client.table(tableName, TableSchema.fromBean(ShoppingCart.class)).createTable();
	}
}
