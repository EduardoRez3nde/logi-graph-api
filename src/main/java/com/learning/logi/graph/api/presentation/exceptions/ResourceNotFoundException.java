package com.learning.logi.graph.api.presentation.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(final String msg) {
        super(msg);
    }
}
