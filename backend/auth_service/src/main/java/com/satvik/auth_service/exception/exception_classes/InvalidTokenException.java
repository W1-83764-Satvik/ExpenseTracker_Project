package com.satvik.auth_service.exception.exception_classes;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message){
        super(message);
    }
}