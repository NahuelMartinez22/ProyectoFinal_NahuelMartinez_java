package com.talento.crud.exception;

public final class CategoryExceptions {

    private CategoryExceptions() {
        // evitar instanciación
    }

    public static class CategoryNotFoundException extends RuntimeException {
        public CategoryNotFoundException(Long id) {
            super("Categoría con id " + id + " no encontrada");
        }
    }

    public static class CategoryAlreadyExistsException extends RuntimeException {
        public CategoryAlreadyExistsException(String name) {
            super("Ya existe una categoría con nombre: " + name);
        }
    }

    public static class CategoryInUseException extends RuntimeException {
        public CategoryInUseException(Long id) {
            super("No se puede eliminar la categoría " + id + " porque está siendo utilizada.");
        }
    }
}
