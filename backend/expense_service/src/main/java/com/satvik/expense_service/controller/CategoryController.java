package com.satvik.expense_service.controller;

import com.satvik.expense_service.dto.ResponceEntityDto.ApiResponse;
import com.satvik.expense_service.dto.category.request.AddCategoryRequestDto;
import com.satvik.expense_service.dto.category.response.CategoryResponseDto;
import com.satvik.expense_service.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/v1")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> addCategory(@RequestBody AddCategoryRequestDto dto) {
        CategoryResponseDto created = categoryService.addCategory(dto);
        ApiResponse<CategoryResponseDto> response = ApiResponse.<CategoryResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Category created successfully")
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> list = categoryService.getAllCategories();
        ApiResponse<List<CategoryResponseDto>> response = ApiResponse.<List<CategoryResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetched all categories")
                .data(list)
                .build();
        return ResponseEntity.ok(response);
    }


}

