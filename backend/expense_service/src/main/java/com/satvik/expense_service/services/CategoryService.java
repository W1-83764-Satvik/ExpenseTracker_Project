package com.satvik.expense_service.services;

import com.satvik.expense_service.dto.category.request.AddCategoryRequestDto;
import com.satvik.expense_service.dto.category.request.UpdateCategoryRequestDto;
import com.satvik.expense_service.dto.category.response.CategoryResponseDto;
import com.satvik.expense_service.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.satvik.expense_service.entities.Category;
import com.satvik.expense_service.exception.exception_classes.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDto addCategory(AddCategoryRequestDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new DataIntegrityViolationException("Category already exists");
        }

        Category category = Category.builder()
                .name(dto.getName())
                .build();

        Category saved = categoryRepository.save(category);

        log.info("New category created: {}", saved.getName());

        return CategoryResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cat -> CategoryResponseDto.builder()
                        .id(cat.getId())
                        .name(cat.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public CategoryResponseDto updateCategory(Long id, UpdateCategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: "));

        category.setName(dto.getNewName());
        categoryRepository.save(category);

        log.info("Category updated: {}", dto.getNewName());

        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: "));

        categoryRepository.delete(category);

        log.info("Category deleted: {}", category.getName());
    }
}

