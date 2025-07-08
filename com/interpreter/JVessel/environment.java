package com.interpreter.JVessel;

import java.util.HashMap;
import java.util.Map;

public class environment {
    private final Map<String, Object> values = new HashMap<>();

    Object get(token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        throw new runtime_error(name, "Undefined Variable '" + name.lexeme + "' >:(");
    }

    void define(String name, Object value) {
        values.put(name, value);
    }

    void assign(token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        throw new runtime_error((name), "Undefined variable '" + name.lexeme + "' >:(");
    }
}
