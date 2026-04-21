package dev.notioniq.quickstarts;

import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class DynamoDbContainerLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private static final int DEFAULT_PORT = 8000;

    private GenericContainer container;

    @Override
    public Map<String, String> start() {
        var dockerImage = DockerImageName.parse("amazon/dynamodb-local:latest");
        container = new GenericContainer(dockerImage).withExposedPorts(DEFAULT_PORT);
        container.start();

        var endpoint = "http://localhost:" + container.getMappedPort(DEFAULT_PORT);
        createShoppingCartTable(endpoint);
        return Collections.singletonMap("quarkus.dynamodb.endpoint-override", endpoint);
    }

    @Override
    public void stop() {
        container.stop();
    }

    private void createShoppingCartTable(String endpoint) {
        var provider = StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test"));
        try (var dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(provider)
                .build()) {

            var enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();

            try {
                enhancedClient.table(ShoppingCart.TABLE_NAME, TableSchema.fromBean(ShoppingCart.class)).createTable();
            } catch (ResourceInUseException ignored) {
                Log.info("Table already created");
            }
        }
    }
}
