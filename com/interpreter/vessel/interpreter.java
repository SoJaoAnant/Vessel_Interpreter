package com.interpreter.vessel;

import java.util.List;
import java.util.ArrayList;

public class interpreter implements expr.visitor<Object>, stmt.visitor<Void> {

    final environment globals = new environment();
    private environment environment = globals;

    interpreter() {
        globals.define("clock", new vessel_callable() {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(interpreter interpreter,
                    List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        });
    }

    void interpret(List<stmt> statements) {
        // try {
        // Object value = evaluate(expression);
        // System.out.println(stringify(value));
        // } catch (runtime_error error) {
        // JVessel.runtime_error(error);
        // }
        try {
            for (stmt statement : statements) {
                execute(statement);
            }
        } catch (runtime_error error) {
            vessel.runtime_error(error);
        }
    }

    @Override
    public Object visit_literal_expr(expr.literal expr) {
        return expr.value;
    }

    @Override
    public Object visit_logical_expr(expr.logical expr) {
        Object left = evaluate(expr.left);

        if (expr.operator.type == token_type.OR) {
            if (is_truthy(left))
                return left;
        } else {
            if (!is_truthy(left))
                return left;
        }

        return evaluate(expr.right);
    }

    @Override
    public Object visit_grouping_expr(expr.grouping expr) {
        return evaluate(expr.expression);
    }

    private Object evaluate(expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visit_unary_expr(expr.unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !is_truthy(right);
            case MINUS:
                return -(double) right;
        }

        return null;
    }

    @Override
    public Object visit_binary_expr(expr.binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG_EQUAL:
                return !is_equal(left, right);
            case EQUAL_EQUAL:
                return is_equal(left, right);
            case GREATER:
                check_number_operands(expr.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                check_number_operands(expr.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                check_number_operands(expr.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                check_number_operands(expr.operator, left, right);
                return (double) left <= (double) right;
            case MINUS:
                check_number_operands(expr.operator, left, right);
                // check_number_operand(expr.operator, right);
                return (double) left - (double) right;
            case SLASH:
                check_number_operands(expr.operator, left, right);
                return (double) left / (double) right;
            case STAR:
                check_number_operands(expr.operator, left, right);
                return (double) left * (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
                throw new runtime_error(expr.operator, "Operands must be the same types >:(");
        }

        return null;
    }

    @Override
    public Void visit_expression_stmt(stmt.expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visit_print_stmt(stmt.print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visit_Var_stmt(stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }

        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Object visit_variable_expr(expr.variable expr) {
        return environment.get(expr.name);
    }

    @Override
    public Object visit_assign_expr(expr.assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }

    @Override
    public Void visit_block_stmt(stmt.block stmt) {
        execute_block(stmt.statements, new environment(environment));
        return null;
    }

    @Override
    public Void visit_if_stmt_stmt(stmt.if_stmt stmt) {
        if (is_truthy(evaluate(stmt.condition))) {
            execute(stmt.then_branch);
        } else if (stmt.else_branch != null) {
            execute(stmt.else_branch);
        }

        return null;
    }

    @Override
    public Void visit_while_stmt_stmt(stmt.while_stmt stmt) {
        while (is_truthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }

    @Override
    public Object visit_call_expr(expr.call expr) {
        Object callee = evaluate(expr.callee);

        List<Object> arguments = new ArrayList<>();
        for (expr argument : expr.arguments) {
            arguments.add(evaluate(argument));
        }

        if (!(callee instanceof vessel_callable)) {
            throw new runtime_error(expr.paren, "What are you doing bro >:( can only call functions and classes!!");
        }

        vessel_callable function = (vessel_callable) callee;

        if (arguments.size() != function.arity()) {
            throw new runtime_error(expr.paren,
                    "Expected " + function.arity() + " arguments, but got " + arguments.size() + ". >:(");
        }

        return function.call(this, arguments);
    }

    @Override
    public Void visit_function_stmt(stmt.function stmt) {
        vessel_function function = new vessel_function(stmt, environment);
        environment.define(stmt.name.lexeme, function);
        return null;
    }

    @Override
    public Void visit_Return_stmt(stmt.Return stmt)
    {
        Object value = null;
        if(stmt.value != null)
        {
            value = evaluate(stmt.value);
        }

        throw new Return(value);
    }

    /*
     * private void check_number_operands(token operator, Object operand) {
     * if (operand instanceof Double)
     * return;
     * throw new runtime_error(operator, "Operand must be an number >:(");
     * }
     */

    private void execute(stmt stmt) {
        stmt.accept(this);
    }

    void execute_block(List<stmt> statements, environment environment) {
        environment previous = this.environment;
        try {
            this.environment = environment;
            for (stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    private void check_number_operands(token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return;
        }
        throw new runtime_error(operator, "Operands must be numbers >:(");
    }

    private boolean is_equal(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;

        return a.equals(b);
    }

    private boolean is_truthy(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Boolean)
            return (boolean) obj;

        return true;
    }

    private String stringify(Object object) {
        if (object == null)
            return "Hollow";
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }
}
