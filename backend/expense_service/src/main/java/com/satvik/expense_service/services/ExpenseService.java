package com.satvik.expense_service.services;

import com.satvik.expense_service.dto.request.AddExpenseRequestDto;
import com.satvik.expense_service.entities.Category;
import com.satvik.expense_service.entities.Expense;
import com.satvik.expense_service.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;

    public void addExpense(AddExpenseRequestDto addExpenseRequestDto){

        Optional<Category> category = categoryRepository.findByName(addExpenseRequestDto.getCategory());

        if(category.isEmpty()){
            throw new RuntimeException();
        }

    }

}
