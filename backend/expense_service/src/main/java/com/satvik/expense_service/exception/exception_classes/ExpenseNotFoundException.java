package com.satvik.expense_service.exception.exception_classes;

public class ExpenseNotFoundException extends RuntimeException{

    public ExpenseNotFoundException(String message){
        super(message);
    }
}
