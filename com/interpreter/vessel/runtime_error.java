package com.interpreter.vessel;

public class runtime_error extends RuntimeException {
    final token token;

    runtime_error(token token, String message) {
        super(message);
        this.token = token;
    }
}
