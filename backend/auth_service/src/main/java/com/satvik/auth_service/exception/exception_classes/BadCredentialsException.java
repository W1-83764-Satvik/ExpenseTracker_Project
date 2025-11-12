package com.satvik.auth_service.exception.exception_classes;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException(String message){
        super(message);
    }
}
