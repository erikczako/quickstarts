package dev.notioniq.quickstarts.mcp;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.Map;

public class DynamoDbContainerLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private static final int DEFAULT_PORT = 8000;

    private GenericContainer container;

    @Override
    public Map<String, String> start() {
        var dockerImage = DockerImageName.parse("amazon/dynamodb-local:latest");
        container = new GenericContainer<>(dockerImage).withExposedPorts(DEFAULT_PORT);
        container.start();

        var endpoint = "http://localhost:" + container.getMappedPort(DEFAULT_PORT);
        return Collections.singletonMap("quarkus.dynamodb.endpoint-override", endpoint);
    }

    @Override
    public void stop() {
        container.stop();
    }
}
