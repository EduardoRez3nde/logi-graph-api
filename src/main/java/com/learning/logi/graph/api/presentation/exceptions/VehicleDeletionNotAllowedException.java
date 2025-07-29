package com.learning.logi.graph.api.presentation.exceptions;

public class VehicleDeletionNotAllowedException extends RuntimeException{

    public VehicleDeletionNotAllowedException(final String msg) {
        super(msg);
    }
}
