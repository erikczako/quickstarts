package dev.notioniq.quickstarts.dynamodb.localstack.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {

    @Autowired
    RestTestClient restTestClient;

    @Container
    public static LocalStackContainer localstack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:community-archive"))
            .withServices("dynamodb");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.credentials.access-key", localstack::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localstack::getSecretKey);
        registry.add("spring.cloud.aws.region.static", localstack::getRegion);
        registry.add("spring.cloud.aws.dynamodb.endpoint", () -> localstack.getEndpoint().toString());
    }

    @Test
    void testCrudShoppingCart() {
        var expectedTotalPrice = new BigDecimal("5000");
        var expectedNumberOfItems = 7;

        // create a new shopping cart for current user
        var uri = restTestClient.post()
                .uri("/v1/shopping-carts")
                .body(new ShoppingCartRequest(expectedNumberOfItems, expectedTotalPrice))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult()
                .getResponseHeaders()
                .getLocation();

        assertNotNull(uri);

        // get the shopping cart for current user
        var response = restTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(ShoppingCart.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertEquals(expectedTotalPrice, response.getTotalPrice());
        assertEquals(expectedNumberOfItems, response.getNumberOfItems());

        // delete the shopping cart for current user
        restTestClient.delete()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // try to get the shopping cart for current user again
        restTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}