package com.tuananh.companyservice.advice.exception;

public class PermissionException extends Exception {
    // Constructor that accepts a message
    public PermissionException(String message) {
        super(message);
    }
}