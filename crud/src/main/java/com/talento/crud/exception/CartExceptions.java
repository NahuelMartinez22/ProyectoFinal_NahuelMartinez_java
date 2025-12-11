package com.talento.crud.exception;

public final class CartExceptions {

    private CartExceptions() {
    }

    public static class CartNotFoundException extends RuntimeException {
        public CartNotFoundException(Long id) {
            super("Carrito con id " + id + " no encontrado");
        }
    }

    public static class CartItemNotFoundException extends RuntimeException {
        public CartItemNotFoundException(Long cartId, Long itemId) {
            super("Item con id " + itemId + " no encontrado en el carrito " + cartId);
        }
    }

    public static class CartInvalidStateException extends RuntimeException {
        public CartInvalidStateException(String message) {
            super(message);
        }
    }
}
