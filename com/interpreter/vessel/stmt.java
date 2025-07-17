package com.interpreter.vessel;

import java.util.List;

abstract class stmt {
 interface visitor<R> {
    R visit_block_stmt(block stmt);
    R visit_expression_stmt(expression stmt);
    R visit_if_stmt_stmt(if_stmt stmt);
    R visit_function_stmt(function stmt);
    R visit_print_stmt(print stmt);
    R visit_Return_stmt(Return stmt);
    R visit_Var_stmt(Var stmt);
    R visit_while_stmt_stmt(while_stmt stmt);
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
static class if_stmt extends stmt {
    if_stmt(expr condition, stmt then_branch, stmt else_branch) {
    this.condition = condition;
    this.then_branch = then_branch;
    this.else_branch = else_branch;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_if_stmt_stmt(this);
}

    final expr condition;
    final stmt then_branch;
    final stmt else_branch;
 }
static class function extends stmt {
    function(token name, List<token> params, List<stmt> body) {
    this.name = name;
    this.params = params;
    this.body = body;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_function_stmt(this);
}

    final token name;
    final List<token> params;
    final List<stmt> body;
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
static class Return extends stmt {
    Return(token keyword, expr value) {
    this.keyword = keyword;
    this.value = value;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_Return_stmt(this);
}

    final token keyword;
    final expr value;
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
static class while_stmt extends stmt {
    while_stmt(expr condition, stmt body) {
    this.condition = condition;
    this.body = body;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_while_stmt_stmt(this);
}

    final expr condition;
    final stmt body;
 }

 abstract <R> R accept(visitor<R> visitor);
}
