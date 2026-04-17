package dev.notioniq.quickstarts.dynamodb.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {

    @Autowired
    RestTestClient restTestClient;

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