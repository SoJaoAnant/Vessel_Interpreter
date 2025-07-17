package com.interpreter.vessel;

import java.util.List;

public class vessel_function implements vessel_callable {
    private final stmt.function declaration;
    private final environment closure;

    vessel_function(stmt.function declaration, environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public Object call(interpreter interpreter, List<Object> arguments) {
        environment environment = new environment(closure);
        for (int i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }

        try {

            interpreter.execute_block(declaration.body, environment);
        } catch (Return return_value) {
            return return_value.value;
        }

        return null;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}
