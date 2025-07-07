package dev.notioniq.quickstarts.dynamodb.quarkus;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/v1/shopping-carts")
public class ShoppingCartResource {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartResource(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GET
    @Path("current")
    public Response find() {
        var shoppingCart = shoppingCartService.find()
                .orElseThrow(() -> new WebApplicationException("Shopping cart not found", Response.status(Response.Status.NOT_FOUND).build()));
        return Response.ok(shoppingCart).build();
    }

    @DELETE
    @Path("current")
    public Response delete() {
        shoppingCartService.delete();
        return Response.noContent().build();
    }

    @POST
    public Response create(ShoppingCartRequest shoppingCartRequest) {
        var shoppingCart = shoppingCartService.save(shoppingCartRequest);
        var uri = UriBuilder.fromResource(ShoppingCartResource.class)
                .path("current")
                .build();
        return Response.created(uri).entity(shoppingCart).build();
    }
}
