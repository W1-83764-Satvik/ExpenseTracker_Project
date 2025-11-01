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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/expenses/v1")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/addexpense")
    public ResponseEntity<ApiResponse<ExpenseResponseDto>> addExpense(@RequestBody ExpenseRequestDto dto) {
        ExpenseResponseDto expenseResponseDto = expenseService.addExpense(dto);

        ApiResponse<ExpenseResponseDto> response = ApiResponse.<ExpenseResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Expense added successfully")
                .data(expenseResponseDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/getallexpensesbyemail/{email}")
    public ResponseEntity<ApiResponse<List<ExpenseResponseDto>>> getExpensesByUser(
            @PathVariable String email) {
        List<ExpenseResponseDto> list = expenseService.getAllExpensesByUserEmail(email);
        ApiResponse<List<ExpenseResponseDto>> response = ApiResponse.<List<ExpenseResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Expenses fetched successfully for " + email)
                .data(list)
                .build();
        return ResponseEntity.ok(response);
    }



    @GetMapping("/getbyid/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponseDto>> getExpenseById(@PathVariable Long id) {
        ExpenseResponseDto expense = expenseService.getExpenseById(id);
        ApiResponse<ExpenseResponseDto> response = ApiResponse.<ExpenseResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Expense fetched successfully")
                .data(expense)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/updatebyid/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponseDto>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRequestDto dto) {
        ExpenseResponseDto updated = expenseService.updateExpense(id, dto);
        ApiResponse<ExpenseResponseDto> response = ApiResponse.<ExpenseResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Expense updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/deletebyid/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpenseById(id);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Expense deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}



