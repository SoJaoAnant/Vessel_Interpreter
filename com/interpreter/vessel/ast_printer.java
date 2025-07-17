package com.interpreter.vessel;

class ast_printer implements expr.visitor<String> {
    String print(expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visit_binary_expr(expr.binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visit_grouping_expr(expr.grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visit_literal_expr(expr.literal expr) {
        if (expr.value == null)
            return "hollow";
        return expr.value.toString();
    }

    @Override
    public String visit_unary_expr(expr.unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visit_call_expr(expr.call expr)
    {
        return ":3";
    }

    @Override
    public String visit_variable_expr(expr.variable expr) {
        return ":3";
    }

    @Override
    public String visit_assign_expr(expr.assign expr) {
        return ":3";
    }

    @Override
    public String visit_logical_expr(expr.logical expr) {
        return ":3";
    }

    private String parenthesize(String name, expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    public static void main(String[] args) {
        expr expression = new expr.binary(
                new expr.unary(new token(token_type.MINUS, "-", null, 1), new expr.literal(123)),
                new token(token_type.STAR, "*", null, 1), new expr.grouping(new expr.literal(45.67)));

        System.out.println(new ast_printer().print(expression));
    }
}