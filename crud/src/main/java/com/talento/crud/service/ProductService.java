package com.talento.crud.service;

import com.talento.crud.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    List<Product> findByCategoryId(Long categoryId);

    Product create(Product product);

    Product update(Long id, Product product);

    void delete(Long id);
}
