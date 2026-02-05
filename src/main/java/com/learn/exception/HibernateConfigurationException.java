package com.learn.exception;

public class HibernateConfigurationException extends RuntimeException {
    public HibernateConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
