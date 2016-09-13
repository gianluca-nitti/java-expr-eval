package com.github.gianlucanitti.javaexpreval;

public class UnexpectedTokenException extends ExpressionException{
  public UnexpectedTokenException(boolean operatorExpected){
    super(operatorExpected ? "An operator was expected, but an expression was found" : "An expression was expected, but an operator was found");
  }
}
