package dev.notioniq.quickstarts.dynamodb.localstack.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for the {@link ShoppingCartController}.

 * These tests leverage {@link Testcontainers} to manage a LocalStack instance,
 * providing a mock DynamoDB environment. This setup enables testing the controller's interaction
 * with the DynamoDB-backed service layer in an isolated manner, without requiring a connection to a live AWS environment.

 * The Spring Boot application context is configured with a "test" profile and
 * starts the embedded web server on a random port for these tests.
 *
 * @author Erik Czako
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    /**
     * A static {@link LocalStackContainer} instance managed by Testcontainers is configured to run the DynamoDB service.
     * The {@code @Container} annotation in conjunction with {@code @Testcontainers} handles the
     * lifecycle of this Docker container, starting it before any tests in this class run and stopping it after all tests have completed.
     */
    @Container
    public static LocalStackContainer localstack = new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
            .withServices(LocalStackContainer.Service.DYNAMODB);

    /**
     * Dynamically configures environment properties to integrate with the running LocalStack container.
     * This method is invoked by Spring Test after the {@link #localstack} container has started but before the application context is fully refreshed.

     * Because of the nature of {@link LocalStackContainer} we must set all AWS SDK variables dynamically.
     *
     * @param registry The {@link DynamicPropertyRegistry} used to add environment properties.
     */
    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.credentials.access-key", localstack::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localstack::getSecretKey);
        registry.add("spring.cloud.aws.region.static", localstack::getRegion);
        registry.add("spring.cloud.aws.dynamodb.endpoint", () -> localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString());
    }

    @Test
    void testCrudShoppingCart() {
        var expectedTotalPrice = new BigDecimal("5000");
        var expectedNumberOfItems = 7;

        // create a new shopping cart for current user
        var uri = restTemplate.postForLocation("/v1/shopping-carts", new ShoppingCartRequest(expectedNumberOfItems, expectedTotalPrice));
        assertNotNull(uri);

        // get the shopping cart for current user
        var response = restTemplate.getForObject(uri, ShoppingCart.class);
        assertEquals(expectedTotalPrice, response.getTotalPrice());
        assertEquals(expectedNumberOfItems, response.getNumberOfItems());

        // delete the shopping cart for current user
        var deleteResponse = restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertTrue(deleteResponse.getStatusCode().is2xxSuccessful());

        // try to get the shopping cart for current user again
        var getResponse = restTemplate.getForEntity(uri, ShoppingCart.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testShoppingCartNotFound() {
        var response = restTemplate.getForEntity("/v1/shopping-carts/current", ShoppingCart.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}