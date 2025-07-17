package com.interpreter.vessel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class vessel {
    private static final interpreter interpreter = new interpreter();
    static boolean had_error = false;
    static boolean had_runtime_error = false;

    public static void main(String[] args) throws IOException {
        // System.out.println("Shishtem Hang");
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            run_file(args[0]);
        } else {
            run_prompt();
        }
    }

    private static void run_file(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (had_error) {
            System.exit(65);
        }
        if (had_runtime_error) {
            System.exit(70);
        }
    }

    private static void run_prompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null)
                break;
            run(line);
            had_error = false;
        }
    }

    private static void run(String source) {
        scanner scanner = new scanner(source);
        List<token> tokens = scanner.scan_tokens();

        // can print all the tokens
        // for (token token : tokens) {
        // System.out.println(token.to_string());
        // }

        // can print the expression all grouped up
        parser parser = new parser(tokens);
        // expr expression = parser.parse();
        List<stmt> statements = parser.parse();

        if (had_error) {
            return;
        }

        // for (stmt statement : statements) {
            // stmt.expression exprStmt = (stmt.expression) statement;
            // expr expression = exprStmt.expression;
            // System.out.println(new ast_printer().print(expression));
        // }

        // can interpret the expression
        interpreter.interpret(statements);
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        had_error = true;
    }

    static void error(token token, String message) {
        if (token.type == token_type.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    static void runtime_error(runtime_error error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
        had_runtime_error = true;
    }
}
