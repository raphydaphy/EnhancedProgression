package com.raphydaphy.vitality.core;

public class ASMTransformationException extends RuntimeException {

    public ASMTransformationException() {
    }

    public ASMTransformationException(String message) {
        super(message);
    }

    public ASMTransformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ASMTransformationException(Throwable cause) {
        super(cause);
    }

    public ASMTransformationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}