package com.talento.crud.service;

import com.talento.crud.model.Cart;

import java.util.List;
import java.util.Optional;

public interface CartService {

    Cart createCart();

    Optional<Cart> findById(Long id);

    List<Cart> findAll();

    Cart addItem(Long cartId, Long productId, Integer quantity);

    Cart updateItem(Long cartId, Long itemId, Integer quantity);

    Cart removeItem(Long cartId, Long itemId);

    void clearCart(Long cartId);
}
