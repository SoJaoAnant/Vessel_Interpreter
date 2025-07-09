package com.interpreter.JVessel;

import java.util.List;
import java.util.ArrayList;
import static com.interpreter.JVessel.token_type.*;

public class parser {

    private static class parse_error extends RuntimeException {
    }

    private final List<token> tokens;
    private int current = 0;

    parser(List<token> tokens) {
        this.tokens = tokens;
    }

    // expr parse() {
    // try {
    // return expression();
    // } catch (parse_error error) {
    // return null;
    // }
    // }

    List<stmt> parse() {
        List<stmt> statements = new ArrayList<>();
        while (!is_at_end()) {
            statements.add(declaration());
        }
        return statements;
    }

    private stmt declaration() {
        try {
            if (match(VAR))
                return var_declaration();

            return statement();
        } catch (parse_error error) {
            synchronize();
            return null;
        }
    }

    private stmt var_declaration() {
        token name = consume(IDENTIFIER, "Expect variable name >:(");

        expr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }

        consume(SEMICOLON, "Expect ';' after variable declaration >:(");

        return new stmt.Var(name, initializer);
    }

    private stmt statement() {
        if (match(PRINT)) {
            return print_statement();
        }
        if (match(LEFT_BRACE)) {
            return new stmt.block(block());
        }
        return expression_statement();
    }

    private stmt print_statement() {
        expr value = expression();
        consume(SEMICOLON, "Expect ';' after value >:(");
        return new stmt.print(value);
    }

    private stmt expression_statement() {
        expr expr = expression();
        consume(SEMICOLON, "Expect ';' after value >:(");
        return new stmt.expression(expr);
    }

    private List<stmt> block() {
        List<stmt> statements = new ArrayList<>();

        while (!check(RIGHT_BRACE) && !is_at_end()) {
            statements.add(declaration());
        }

        consume(RIGHT_BRACE, "Expect '}' after block >:(");

        return statements;
    }

    private expr expression() {
        return assignment();
    }

    private expr assignment() {
        expr expr = equality();

        if (match(EQUAL)) {
            token equals = previous();
            expr value = assignment();

            if (expr instanceof expr.variable) {
                token name = ((expr.variable) expr).name;
                return new expr.assign(name, value);
            }

            error(equals, "Invalid assigment target >:(");
        }

        return expr;
    }

    private expr equality() {
        expr expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            token operator = previous();
            expr right = comparison();
            expr = new expr.binary(expr, operator, right);
        }

        return expr;
    }

    private expr comparison() {
        expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            token operator = previous();
            expr right = term();
            expr = new expr.binary(expr, operator, right);
        }

        return expr;
    }

    private expr term() {
        expr expr = factor();

        while (match(PLUS, MINUS)) {
            token operator = previous();
            expr right = factor();
            expr = new expr.binary(expr, operator, right);
        }

        return expr;
    }

    private expr factor() {
        expr expr = unary();

        while (match(STAR, SLASH)) {
            token operator = previous();
            expr right = unary();
            expr = new expr.binary(expr, operator, right);
        }

        return expr;
    }

    private expr unary() {
        if (match(BANG, MINUS)) {
            token operator = previous();
            expr right = unary();
            return new expr.unary(operator, right);
        }

        return primary();
    }

    private expr primary() {
        if (match(FALSE))
            return new expr.literal(false);
        if (match(TRUE))
            return new expr.literal(true);
        if (match(NIL))
            return new expr.literal(null);

        if (match(NUMBER, STRING))
            return new expr.literal(previous().literal);

        if (match(IDENTIFIER)) {
            return new expr.variable(previous());
        }
        if (match(LEFT_PAREN)) {
            expr expr = expression();
            consume(RIGHT_PAREN, "expect ')' after expression.");
            return new expr.grouping(expr);
        }

        throw error(peek(), "Expect expression >:(");
    }

    private token consume(token_type type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private parse_error error(token token, String message) {
        JVessel.error(token, message);
        return new parse_error();
    }

    private void synchronize() {
        advance();

        while (!is_at_end()) {
            if (previous().type == SEMICOLON) {
                return;
            }

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            advance();
        }
    }

    private boolean match(token_type... types) {
        for (token_type type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(token_type type) {
        if (is_at_end())
            return false;
        return peek().type == type;
    }

    private token advance() {
        if (!is_at_end())
            current++;
        return previous();
    }

    private boolean is_at_end() {
        return peek().type == EOF;
    }

    private token peek() {
        return tokens.get(current);
    }

    private token previous() {
        return tokens.get(current - 1);
    }
}
