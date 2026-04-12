package dev.notioniq.quickstarts.dynamodb.spring;

import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.dynamodb.services.local.embedded.DynamoDBEmbedded;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbTableNameResolver tableNameResolver) {
        var dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDBEmbedded.create(true).dynamoDbClient())
                .build();

        var tableName = tableNameResolver.resolve(ShoppingCart.class);
        var table = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(ShoppingCart.class));
        table.createTable();
        return dynamoDbEnhancedClient;
    }
}
