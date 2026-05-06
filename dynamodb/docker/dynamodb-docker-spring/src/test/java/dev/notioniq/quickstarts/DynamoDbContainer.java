package dev.notioniq.quickstarts;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public interface DynamoDbContainer {

    int DEFAULT_PORT = 8000;

    DockerImageName DOCKER_IMAGE = DockerImageName.parse("amazon/dynamodb-local:latest");

    @Container
    GenericContainer CONTAINER = new GenericContainer(DOCKER_IMAGE).withExposedPorts(DEFAULT_PORT);

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.dynamodb.endpoint", () -> "http://localhost:" + CONTAINER.getMappedPort(DEFAULT_PORT));
    }
}
