package com.talento.crud.service;

import com.talento.crud.model.Cart;
import com.talento.crud.model.CartItem;
import com.talento.crud.model.Product;
import com.talento.crud.repository.CartItemRepository;
import com.talento.crud.repository.CartRepository;
import com.talento.crud.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.talento.crud.exception.CartExceptions.*;
import static com.talento.crud.exception.ProductExceptions.*;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Cart createCart() {
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }

    @Override
    public Cart findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del carrito no puede ser nulo");
        }

        return cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException(id));
    }

    @Override
    public Cart addItem(Long cartId, Long productId, Integer quantity) {
        if (cartId == null) {
            throw new IllegalArgumentException("El id del carrito no puede ser nulo");
        }
        if (productId == null) {
            throw new IllegalArgumentException("El id del producto no puede ser nulo");
        }
        if (quantity == null || quantity <= 0) {
            throw new CartInvalidStateException("La cantidad debe ser mayor a cero");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());

        cart.getItems().add(item);
        cartItemRepository.save(item);

        return cartRepository.save(cart);
    }

    @Override
    public Cart updateItem(Long cartId, Long itemId, Integer quantity) {
        if (cartId == null) {
            throw new IllegalArgumentException("El id del carrito no puede ser nulo");
        }
        if (itemId == null) {
            throw new IllegalArgumentException("El id del item no puede ser nulo");
        }
        if (quantity == null || quantity <= 0) {
            throw new CartInvalidStateException("La cantidad debe ser mayor a cero");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartId, itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new CartInvalidStateException("El item no pertenece a este carrito");
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return cart;
    }

    @Override
    public Cart removeItem(Long cartId, Long itemId) {
        if (cartId == null) {
            throw new IllegalArgumentException("El id del carrito no puede ser nulo");
        }
        if (itemId == null) {
            throw new IllegalArgumentException("El id del item no puede ser nulo");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartId, itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new CartInvalidStateException("El item no pertenece a este carrito");
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long cartId) {
        if (cartId == null) {
            throw new IllegalArgumentException("El id del carrito no puede ser nulo");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

}
