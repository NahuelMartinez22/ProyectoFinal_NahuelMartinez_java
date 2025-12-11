package com.talento.crud.service;

import com.talento.crud.model.Cart;

import java.util.List;

public interface CartService {

    Cart createCart();

    Cart findById(Long id);

    List<Cart> findAll();

    Cart addItem(Long cartId, Long productId, Integer quantity);

    Cart updateItem(Long cartId, Long itemId, Integer quantity);

    Cart removeItem(Long cartId, Long itemId);

    void clearCart(Long cartId);

    void deleteCart(Long id);
}
