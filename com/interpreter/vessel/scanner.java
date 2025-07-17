package com.interpreter.vessel;

import static com.interpreter.vessel.token_type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class scanner {
    private final String source;
    private final List<token> tokens = new ArrayList<>();
    private static final Map<String, token_type> keywords;
    private int start = 0;
    private int current = 0;
    private int line = 1;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("hollow", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    scanner(String source) {
        this.source = source;
    }

    List<token> scan_tokens() {
        while (!is_at_end()) {
            start = current;
            scan_token();
        }

        tokens.add(new token(EOF, "", null, line));

        return tokens;
    }

    private void scan_token() {
        char c = advance();
        switch (c) {
            case '(':
                add_token(LEFT_PAREN);
                break;
            case ')':
                add_token(RIGHT_PAREN);
                break;
            case '{':
                add_token(LEFT_BRACE);
                break;
            case '}':
                add_token(RIGHT_BRACE);
                break;
            case ',':
                add_token(COMMA);
                break;
            case '.':
                add_token(DOT);
                break;
            case '-':
                add_token(MINUS);
                break;
            case '+':
                add_token(PLUS);
                break;
            case ';':
                add_token(SEMICOLON);
                break;
            case '*':
                add_token(STAR);
                break;
            case '!':
                add_token(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                add_token(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                add_token(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                add_token(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !is_at_end())
                        advance();
                } else {
                    add_token(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if (is_digit(c)) {
                    number();
                } else if (is_alpha(c)) {
                    identifier();
                } else {
                    vessel.error(line, "Unexpected Character!! >:(");
                }
                break;
        }
    }

    private void identifier() {
        while (is_alpha_numeric(peek())) {
            advance();
        }

        String text = source.substring(start, current);
        token_type type = keywords.get(text);
        if(type == null) type = IDENTIFIER;
        add_token(type);
    }

    private void number() {
        while (is_digit(peek())) {
            advance();
        }

        if (peek() == '.' && is_digit(peek_next())) {
            advance();
            while (is_digit(peek())) {
                advance();
            }
        }

        add_token(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !is_at_end()) {
            if (peek() == '\n') {
                line++;
            }
            advance();
        }
        if (is_at_end()) {
            vessel.error(line, "Unterminated String!! >:(");
        }

        advance();

        String value = source.substring(start + 1, current - 1);
        add_token(STRING, value);
    }

    private boolean match(char expected) {
        if (is_at_end())
            return false;
        if (source.charAt(current) != expected)
            return false;

        current++;
        return true;
    }

    private char peek() {
        if (is_at_end()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peek_next() {
        if (current + 1 >= source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    private boolean is_digit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean is_alpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c == '_');
    }

    private boolean is_alpha_numeric(char c) {
        return is_alpha(c) || is_digit(c);
    }

    private boolean is_at_end() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void add_token(token_type type) {
        add_token(type, null);
    }

    private void add_token(token_type type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new token(type, text, literal, line));
    }

}
