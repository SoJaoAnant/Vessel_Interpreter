package com.interpreter.JVessel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JVessel {
    static boolean had_error = false;

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

        // for (token token : tokens) {
        // System.out.println(token.to_string());
        // }

        parser parser = new parser(tokens);
        expr expression = parser.parse();

        if (had_error) {
            System.out.println("hello");
            return;
        }

        System.out.println(new ast_printer().print(expression));
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
}
