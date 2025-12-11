package com.talento.crud.service;

import com.talento.crud.model.Product;
import com.talento.crud.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.talento.crud.exception.ProductExceptions.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del producto no puede ser nulo");
        }

        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("El id de la categoría no puede ser nulo");
        }

        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public Product create(Product product) {
        validateProduct(product);

        if (productRepository.existsByNameIgnoreCase(product.getName())) {
            throw new ProductAlreadyExistsException("nombre: " + product.getName());
        }

        product.setId(null);

        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        if (id == null) {
            throw new IllegalArgumentException("El id del producto no puede ser nulo");
        }
        validateProduct(product);

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (!existing.getName().equalsIgnoreCase(product.getName())
                && productRepository.existsByNameIgnoreCase(product.getName())) {
            throw new ProductAlreadyExistsException("nombre: " + product.getName());
        }

        existing.setName(product.getName());
        existing.setVram(product.getVram());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setImageUrl(product.getImageUrl());
        existing.setCategory(product.getCategory());

        return productRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del producto no puede ser nulo");
        }

        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ProductInUseException(id);
        }
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo");
        }
        if (product.getStock() != null && product.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}
