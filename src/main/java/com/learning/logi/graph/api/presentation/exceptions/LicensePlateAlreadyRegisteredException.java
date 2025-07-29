package com.learning.logi.graph.api.presentation.exceptions;

public class LicensePlateAlreadyRegisteredException extends RuntimeException{

    public LicensePlateAlreadyRegisteredException(final String msg) {
        super(msg);
    }
}
