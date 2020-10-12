package com.example.restExceptionHandler;

import java.io.Serializable;

public class RestApplicationException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public RestApplicationException() {
        super();
    }

    public RestApplicationException(String message) {
        super(message);
    }

    public RestApplicationException(String message, Exception e) {
        super(message, e);
    }

}
