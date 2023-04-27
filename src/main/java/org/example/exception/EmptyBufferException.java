package org.example.exception;

public class EmptyBufferException extends RuntimeException {

    public EmptyBufferException() {
    }

    public EmptyBufferException(String message) {
        super(message);
    }

    public EmptyBufferException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyBufferException(Throwable cause) {
        super(cause);
    }

    public EmptyBufferException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
