package com.interpreter.JVessel;

import java.util.List;

abstract class stmt {
 interface visitor<R> {
    R visit_block_stmt(block stmt);
    R visit_expression_stmt(expression stmt);
    R visit_print_stmt(print stmt);
    R visit_Var_stmt(Var stmt);
 }
static class block extends stmt {
    block(List<stmt> statements) {
    this.statements = statements;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_block_stmt(this);
}

    final List<stmt> statements;
 }
static class expression extends stmt {
    expression(expr expression) {
    this.expression = expression;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_expression_stmt(this);
}

    final expr expression;
 }
static class print extends stmt {
    print(expr expression) {
    this.expression = expression;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_print_stmt(this);
}

    final expr expression;
 }
static class Var extends stmt {
    Var(token name, expr initializer) {
    this.name = name;
    this.initializer = initializer;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_Var_stmt(this);
}

    final token name;
    final expr initializer;
 }

 abstract <R> R accept(visitor<R> visitor);
}
