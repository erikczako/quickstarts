package dev.notioniq.quickstarts.dynamodb.spring;

import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@SpringBootApplication
public class ShoppingCartExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartExampleApplication.class, args);
    }

    /**
     * Configures and provides a {@link DynamoDbEnhancedClient} bean for interacting with an embedded DynamoDB instance.
     * This method initializes an in-memory DynamoDB Local instance, creates the "shopping_cart" table, and returns a client configured to use it.
     *
     * @return The configured {@link DynamoDbEnhancedClient}.
     */
    @Bean
    DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        var dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDBEmbedded.create(true).dynamoDbClient())
                .build();

        var table = dynamoDbEnhancedClient.table("shopping_cart", TableSchema.fromBean(ShoppingCart.class));
        table.createTable();
        return dynamoDbEnhancedClient;
    }
}
