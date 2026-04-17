package dev.notioniq.quickstarts.dynamodb.spring;

import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.dynamodb.services.local.embedded.DynamoDBEmbedded;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Profile({"local", "test"})
    DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbTableNameResolver tableNameResolver) {
        var dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDBEmbedded.create(true).dynamoDbClient())
                .build();

        var tableName = tableNameResolver.resolve(ShoppingCart.class);
        var table = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(ShoppingCart.class));
        createTable(table);
        return dynamoDbEnhancedClient;
    }

    private void createTable(DynamoDbTable<ShoppingCart> table) {
        try {
            table.createTable();
        } catch (ResourceInUseException exception) {
            log.info("Table already created");
        }
    }
}
