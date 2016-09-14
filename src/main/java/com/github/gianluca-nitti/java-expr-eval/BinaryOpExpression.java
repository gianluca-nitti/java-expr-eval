package com.github.gianlucanitti.javaexpreval;

import java.util.Arrays;

public final class BinaryOpExpression extends Expression{

  private final Expression left;
  private final Expression right;
  private final char op;

  private static final Character[] allowedOperators = new Character[]{'+', '-', '*', '/', '^'};

  public static boolean isAllowedOperator(Character c){
    return Arrays.asList(allowedOperators).contains(c);
  }

  public BinaryOpExpression(Expression left, char op, Expression right) throws InvalidOperatorException{
    this.left = left;
    this.right = right;
    if(!isAllowedOperator(op))
      throw new InvalidOperatorException(op);
    this.op = op;
  }

  @Override
  public double eval(){
    double a = left.eval();
    double b = right.eval();
    double result = 0;
    switch(op){
      case '+': result = a + b; break;
      case '-': result = a - b; break;
      case '*': result = a * b; break;
      case '/': result = a / b; break;
      //case '%': result = a % b; break;
      case '^': result = Math.pow(a, b); break;
    }
    return result;
  }
}
