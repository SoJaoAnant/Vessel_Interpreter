package com.interpreter.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class generate_ast {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String output_dir = args[0];

        define_ast(output_dir, "expr", Arrays.asList(
                "assign: token name, expr value",
                "binary: expr left, token operator, expr right",
                "grouping: expr expression",
                "call: expr callee, token paren, List<expr> arguments",
                "literal: Object value",
                "logical: expr left, token operator, expr right",
                "unary: token operator, expr right",
                "variable: token name"));

        define_ast(output_dir, "stmt", Arrays.asList("block: List<stmt> statements",
                "expression : expr expression",
                "if_stmt: expr condition, stmt then_branch," + " stmt else_branch",
                "function: token name, List<token> params," + " List<stmt> body",
                "print: expr expression",
                "Return: token keyword, expr value",
                "Var: token name, expr initializer",
                "while_stmt: expr condition, stmt body"));
    }

    private static void define_ast(String output_dir, String base_name, List<String> types) throws IOException {
        String path = output_dir + "/" + base_name + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.interpreter.JVessel;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + base_name + " {");

        define_visitor(writer, base_name, types);

        for (String type : types) {
            String class_name = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            define_type(writer, base_name, class_name, fields);
        }

        writer.println();
        writer.println(" abstract <R> R accept(visitor<R> visitor);");

        writer.println("}");

        writer.close();
    }

    private static void define_visitor(PrintWriter writer, String base_name, List<String> types) {
        writer.println(" interface visitor<R> {");

        for (String type : types) {
            String type_name = type.split(":")[0].trim();
            writer.println(
                    "    R visit_" + type_name + "_" + base_name + "(" + type_name + " " + base_name.toLowerCase()
                            + ");");
        }

        writer.println(" }");
    }

    private static void define_type(PrintWriter writer, String base_name, String class_name, String field_list) {
        writer.println("static class " + class_name + " extends " + base_name + " {");

        writer.println("    " + class_name + "(" + field_list + ") {");

        String[] fields = field_list.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("    this." + name + " = " + name + ";");
        }

        writer.println("    }");

        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(visitor<R> visitor) {");
        writer.println("      return visitor.visit_" + class_name + "_" + base_name + "(this);");
        writer.println("}");

        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println(" }");
    }
}
