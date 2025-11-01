package com.satvik.expense_service.exception.exception_classes;

public class ExpenseSaveException extends RuntimeException{

    public ExpenseSaveException(String message){
        super(message);
    }
}