package com.interpreter.JVessel;

import java.util.List;

abstract class expr {
 interface visitor<R> {
    R visit_assign_expr(assign expr);
    R visit_binary_expr(binary expr);
    R visit_grouping_expr(grouping expr);
    R visit_literal_expr(literal expr);
    R visit_unary_expr(unary expr);
    R visit_variable_expr(variable expr);
 }
static class assign extends expr {
    assign(token name, expr value) {
    this.name = name;
    this.value = value;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_assign_expr(this);
}

    final token name;
    final expr value;
 }
static class binary extends expr {
    binary(expr left, token operator, expr right) {
    this.left = left;
    this.operator = operator;
    this.right = right;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_binary_expr(this);
}

    final expr left;
    final token operator;
    final expr right;
 }
static class grouping extends expr {
    grouping(expr expression) {
    this.expression = expression;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_grouping_expr(this);
}

    final expr expression;
 }
static class literal extends expr {
    literal(Object value) {
    this.value = value;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_literal_expr(this);
}

    final Object value;
 }
static class unary extends expr {
    unary(token operator, expr right) {
    this.operator = operator;
    this.right = right;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_unary_expr(this);
}

    final token operator;
    final expr right;
 }
static class variable extends expr {
    variable(token name) {
    this.name = name;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visit_variable_expr(this);
}

    final token name;
 }

 abstract <R> R accept(visitor<R> visitor);
}
