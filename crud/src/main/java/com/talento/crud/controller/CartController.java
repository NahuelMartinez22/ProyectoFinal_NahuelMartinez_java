package com.talento.crud.controller;

import com.talento.crud.dto.CartItemRequest;
import com.talento.crud.model.Cart;
import com.talento.crud.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Cart cart = cartService.findById(id);
        return ResponseEntity.ok(cart);
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

    @GetMapping
    public ResponseEntity<List<Cart>> getAll() {
        return ResponseEntity.ok(cartService.findAll());
    }

    @DeleteMapping("/{cartId}/delete")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
