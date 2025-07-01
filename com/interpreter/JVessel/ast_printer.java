package com.interpreter.JVessel;

class ast_printer implements expr.visitor<String> {
    String print(expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitbinaryexpr(expr.binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitgroupingexpr(expr.grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitliteralexpr(expr.literal expr) {
        if (expr.value == null)
            return "hollow";
        return expr.value.toString();
    }

    @Override
    public String visitunaryexpr(expr.unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
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