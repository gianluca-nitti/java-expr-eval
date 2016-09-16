package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when an unknown operator is found while parsing an expression.
 */
public class InvalidOperatorException extends ExpressionException{
  public InvalidOperatorException(char op){
    super("Unknown operator '" + op + "'");
  }
}
