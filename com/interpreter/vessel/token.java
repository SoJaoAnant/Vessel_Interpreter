package com.interpreter.vessel;

class token {
    final token_type type; // What kind of token it is?
    final String lexeme; // The raw text
    final Object literal; // The actual value
    final int line; // Which line in the source code this token was found

    token(token_type type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String to_string() {
        return type + " " + lexeme + " " + literal;
    }
}
