package com.satvik.expense_service.services;

import com.satvik.expense_service.dto.expense.request.ExpenseRequestDto;
import com.satvik.expense_service.dto.expense.request.UpdateRequestDto;
import com.satvik.expense_service.dto.expense.response.ExpenseResponseDto;
import com.satvik.expense_service.entities.Category;
import com.satvik.expense_service.entities.Expense;
import com.satvik.expense_service.exception.exception_classes.CategoryNotFoundException;
import com.satvik.expense_service.exception.exception_classes.ExpenseNotFoundException;
import com.satvik.expense_service.exception.exception_classes.ExpenseSaveException;
import com.satvik.expense_service.repositories.CategoryRepository;
import com.satvik.expense_service.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpenseService {
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    public ExpenseResponseDto addExpense(ExpenseRequestDto dto) {
        Category category = findCategoryByName(dto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Expense expense = Expense.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .category(category)
                .userEmail(dto.getUserEmail())
                .date(LocalDate.now())
                .build();

        Expense savedExpense = null;

        try {
            savedExpense =  expenseRepository.save(expense);
        } catch (DataIntegrityViolationException ex) {
            log.error("Data integrity violation while saving expense: {}", dto, ex);
            throw new ExpenseSaveException("Invalid data — please check inputs");
        } catch (Exception ex) {
            log.error("Unexpected DB error while saving expense: {}", dto, ex);
            throw new ExpenseSaveException("Could not save expense, please try again later");
        }

        return ExpenseResponseDto.builder()
                .id(savedExpense.getId())
                .title(savedExpense.getTitle())
                .category(savedExpense.getCategory().getName())
                .date(savedExpense.getDate())
                .amount(savedExpense.getAmount())
                .description(savedExpense.getDescription())
                .userEmail(savedExpense.getUserEmail())
                .build();
    }

    private Optional<Category> findCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    public void deleteExpense(Long id) {
        try{
            expenseRepository.deleteById(id);
        }catch (DataIntegrityViolationException ex) {
            log.error("Data integrity violation while deleting expense having id : {}", id, ex);
            throw new ExpenseSaveException("Invalid data — please check inputs");
        } catch (Exception ex) {
            log.error("Unexpected DB error while saving expense having id : {}", id, ex);
            throw new ExpenseSaveException("Could not save expense, please try again later");
        }
    }

    public ExpenseResponseDto updateExpense(Long id, UpdateRequestDto dto) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id: " + id));

        Category category = categoryRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        expense.setTitle(dto.getTitle());
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setCategory(category);
        expense.setUpdatedAt(LocalDateTime.now());

        try {
            expenseRepository.save(expense);
        } catch (DataIntegrityViolationException ex) {
            log.error("Data integrity violation while updating expense id: {}", id, ex);
            throw new ExpenseSaveException("Invalid data — please check inputs");
        }

        return ExpenseResponseDto.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .category(expense.getCategory().getName())
                .date(expense.getDate())
                .userEmail(expense.getUserEmail())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .build();
    }


}



