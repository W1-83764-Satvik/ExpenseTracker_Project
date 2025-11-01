package com.satvik.expense_service.repositories;

import com.satvik.expense_service.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
List<Expense> findByUserEmail (String email);
}
