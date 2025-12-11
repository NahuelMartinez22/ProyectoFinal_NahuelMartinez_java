package com.talento.crud.service;

import com.talento.crud.model.Category;
import com.talento.crud.repository.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.talento.crud.exception.CategoryExceptions.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la categoría no puede ser nulo");
        }

        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category create(Category category) {
        validateCategory(category);

        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new CategoryAlreadyExistsException(category.getName());
        }

        category.setId(null);

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la categoría no puede ser nulo");
        }
        validateCategory(category);

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!existing.getName().equalsIgnoreCase(category.getName())
                && categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new CategoryAlreadyExistsException(category.getName());
        }

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());

        return categoryRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la categoría no puede ser nulo");
        }

        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }

        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new CategoryInUseException(id);
        }
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
    }
}
