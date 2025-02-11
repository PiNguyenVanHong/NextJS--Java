package com.tutorial.apidemo.exceptions;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(String message) {
        super(message);
    }
}