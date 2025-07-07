package dev.notioniq.quickstarts.dynamodb.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

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