package com.interpreter.JVessel;

import java.util.List;

abstract class expr {
 interface visitor<R> {
    R visitbinaryexpr(binary expr);
    R visitgroupingexpr(grouping expr);
    R visitliteralexpr(literal expr);
    R visitunaryexpr(unary expr);
 }
static class binary extends expr {
    binary(expr left, token operator, expr right) {
    this.left = left;
    this.operator = operator;
    this.right = right;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visitbinaryexpr(this);
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
      return visitor.visitgroupingexpr(this);
}

    final expr expression;
 }
static class literal extends expr {
    literal(Object value) {
    this.value = value;
    }

    @Override
    <R> R accept(visitor<R> visitor) {
      return visitor.visitliteralexpr(this);
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
      return visitor.visitunaryexpr(this);
}

    final token operator;
    final expr right;
 }

 abstract <R> R accept(visitor<R> visitor);
}
