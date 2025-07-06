package com.interpreter.JVessel;

public class interpreter implements expr.visitor<Object> {

    void interpret(expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (runtime_error error) {
            JVessel.runtime_error(error);
        }
    }

    @Override
    public Object visitliteralexpr(expr.literal expr) {
        return expr.value;
    }

    @Override
    public Object visitgroupingexpr(expr.grouping expr) {
        return evaluate(expr.expression);
    }

    private Object evaluate(expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitunaryexpr(expr.unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !is_truthy(right);
            case MINUS:
                return -(double) right;
        }

        return null;
    }

    private boolean is_truthy(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Boolean)
            return (boolean) obj;

        return true;
    }

    @Override
    public Object visitbinaryexpr(expr.binary expr) {
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

    /*
     * private void check_number_operands(token operator, Object operand) {
     * if (operand instanceof Double)
     * return;
     * throw new runtime_error(operator, "Operand must be an number >:(");
     * }
     */

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
