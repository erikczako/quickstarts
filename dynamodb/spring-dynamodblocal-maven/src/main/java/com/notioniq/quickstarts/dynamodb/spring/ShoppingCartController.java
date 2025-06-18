package com.notioniq.quickstarts.dynamodb.spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1/shopping-carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("current")
    public ResponseEntity<ShoppingCart> find() {
        var shoppingCart = shoppingCartService.find()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping cart not found"));
        return ResponseEntity.ok(shoppingCart);
    }

    @DeleteMapping("current")
    public ResponseEntity<Void> delete() {
        shoppingCartService.delete();
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ShoppingCart> create(@RequestBody ShoppingCartRequest shoppingCartRequest) {
        var shoppingCart = shoppingCartService.save(shoppingCartRequest);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/current")
                .build()
                .toUri();

        return ResponseEntity.created(uri).body(shoppingCart);
    }
}
