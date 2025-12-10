package com.talento.crud.controller;

import com.talento.crud.dto.CartItemRequest;
import com.talento.crud.model.Cart;
import com.talento.crud.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart cart = cartService.createCart();
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getById(@PathVariable Long id) {
        return cartService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Cart> addItem(@PathVariable Long cartId,
                                        @RequestBody CartItemRequest request) {
        Cart updated = cartService.addItem(cartId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updated);
    }


    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<Cart> updateItem(@PathVariable Long cartId,
                                           @PathVariable Long itemId,
                                           @RequestBody CartItemRequest request) {
        Cart updated = cartService.updateItem(cartId, itemId, request.getQuantity());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long cartId,
                                           @PathVariable Long itemId) {
        Cart updated = cartService.removeItem(cartId, itemId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
