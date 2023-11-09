package com.example.demo.api.exceptions;

public class ProductAlreadyExistsException extends Throwable {
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
