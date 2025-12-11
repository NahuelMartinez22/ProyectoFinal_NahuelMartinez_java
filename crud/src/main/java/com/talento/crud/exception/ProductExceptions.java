package com.talento.crud.exception;

public final class ProductExceptions {

    private ProductExceptions() {
    }

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(Long id) {
            super("Producto con id " + id + " no encontrado");
        }
    }

    public static class ProductAlreadyExistsException extends RuntimeException {
        public ProductAlreadyExistsException(String fieldDescription) {
            super("Ya existe un producto con " + fieldDescription);
        }
    }

    public static class ProductInUseException extends RuntimeException {
        public ProductInUseException(Long id) {
            super("No se puede eliminar el producto " + id + " porque está siendo utilizado.");
        }
    }
}
