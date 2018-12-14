package com.gwu.cs6431.client.service.exception;

import java.io.IOException;

public class CanNotResolveException extends IOException {
    public CanNotResolveException() {
    }

    public CanNotResolveException(String message) {
        super(message);
    }
}
