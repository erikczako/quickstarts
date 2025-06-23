package dev.notioniq.quickstarts.dynamodb.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class ShoppingCartResourceTest {

    @Test
    void testCrudOnShoppingCart() {
        var expectedPrice = "3000";
        var expectedNumberOfItems = 50;

        // create a new shopping cart for current user
        var shoppingCartPrice = new ShoppingCartRequest(expectedNumberOfItems, new BigDecimal(expectedPrice));
        var location = given()
                .body(shoppingCartPrice)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post("/v1/shopping-carts")
                .then()
                .statusCode(201)
                .extract().header(HttpHeaders.LOCATION);

        assertNotNull(location);

        // get the shopping cart for current user
        given().baseUri(location)
                .when().get()
                .then()
                .statusCode(200)
                .body("totalPrice", equalTo(Integer.valueOf(expectedPrice)))
                .body("numberOfItems", equalTo(expectedNumberOfItems));

        // delete the shopping cart for current user
        given().baseUri(location)
                .when().delete()
                .then()
                .statusCode(204);

        // try to get the shopping cart for current user again
        given().baseUri(location)
                .when().get()
                .then()
                .statusCode(404);
    }
}