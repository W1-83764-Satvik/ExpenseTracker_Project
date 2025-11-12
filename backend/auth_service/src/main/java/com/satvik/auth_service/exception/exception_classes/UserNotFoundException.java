package com.satvik.auth_service.exception.exception_classes;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message){
        super(message);
    }
}
