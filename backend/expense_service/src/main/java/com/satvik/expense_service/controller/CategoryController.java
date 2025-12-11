package com.satvik.expense_service.controller;

import com.satvik.expense_service.dto.ResponceEntityDto.ApiResponse;
import com.satvik.expense_service.dto.category.request.AddCategoryRequestDto;
import com.satvik.expense_service.dto.category.request.UpdateCategoryRequestDto;
import com.satvik.expense_service.dto.category.response.CategoryResponseDto;
import com.satvik.expense_service.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/v1")
public class CategoryController {

    private final CategoryService categoryService;

    // ✅ ADD category (authenticated user, global category)
    @PostMapping("/addcategory")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> addCategory(
            @Valid @RequestBody AddCategoryRequestDto dto,
            Authentication authentication
    ) {
        // authentication present mainly for security consistency
        // can later restrict by role if needed

        CategoryResponseDto created = categoryService.addCategory(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<CategoryResponseDto>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Category created successfully")
                        .data(created)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ✅ GET all categories (authenticated)
    @GetMapping("/getallcategories")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories(
            Authentication authentication
    ) {
        List<CategoryResponseDto> list = categoryService.getAllCategories();

        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponseDto>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Fetched all categories")
                        .data(list)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ✅ UPDATE category (exists in your service)
    @PutMapping("/updatecategory/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequestDto dto,
            Authentication authentication
    ) {
        CategoryResponseDto updated =
                categoryService.updateCategory(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<CategoryResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("Category updated successfully")
                        .data(updated)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ✅ DELETE category
    @DeleteMapping("/deletecategory/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(
            @PathVariable Long id,
            Authentication authentication
    ) {
        categoryService.deleteCategory(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Category deleted successfully")
                        .data("Deleted")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
