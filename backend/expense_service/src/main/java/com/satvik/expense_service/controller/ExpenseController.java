package com.satvik.expense_service.controller;

import com.satvik.expense_service.dto.ResponceEntityDto.ApiResponse;
import com.satvik.expense_service.dto.expense.request.ExpenseRequestDto;
import com.satvik.expense_service.dto.expense.request.UpdateRequestDto;
import com.satvik.expense_service.dto.expense.response.ExpenseResponseDto;
import com.satvik.expense_service.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/expenses/v1")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    // ✅ ADD expense (email injected from JWT, not from client)
    @PostMapping("/addexpense")
    public ResponseEntity<ApiResponse<ExpenseResponseDto>> addExpense(
            @Valid @RequestBody ExpenseRequestDto dto,
            Authentication authentication
    ) {
        String email = authentication.getName();

        // force userEmail from JWT (override client input)
        dto.setUserEmail(email);

        ExpenseResponseDto saved = expenseService.addExpense(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ExpenseResponseDto>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Expense added successfully")
                        .data(saved)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ✅ GET all expenses for logged-in user
    @GetMapping("/getallexpenses")
    public ResponseEntity<ApiResponse<List<ExpenseResponseDto>>> getAllExpenses(
            Authentication authentication
    ) {
        String email = authentication.getName();

        List<ExpenseResponseDto> list =
                expenseService.getAllExpensesByUserEmail(email);

        return ResponseEntity.ok(
                ApiResponse.<List<ExpenseResponseDto>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Expenses fetched successfully")
                        .data(list)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ⚠️ GET by id (NO ownership enforcement at service level yet)
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponseDto>> getExpenseById(
            @PathVariable Long id
    ) {
        ExpenseResponseDto expense = expenseService.getExpenseById(id);

        return ResponseEntity.ok(
                ApiResponse.<ExpenseResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("Expense fetched successfully")
                        .data(expense)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ⚠️ UPDATE by id (NO ownership enforcement yet)
    @PutMapping("/updatebyid/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponseDto>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRequestDto dto
    ) {
        ExpenseResponseDto updated = expenseService.updateExpense(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<ExpenseResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("Expense updated successfully")
                        .data(updated)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ⚠️ DELETE by id (NO ownership enforcement yet)
    @DeleteMapping("/deletebyid/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(
            @PathVariable Long id
    ) {
        expenseService.deleteExpenseById(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Expense deleted successfully")
                        .data("Deleted")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
