package com.github.gianlucanitti.javaexpreval;

public class InvalidOperatorException extends ExpressionException{
  public InvalidOperatorException(char op){
    super("Unknown operator '" + op + "'");
  }
}
