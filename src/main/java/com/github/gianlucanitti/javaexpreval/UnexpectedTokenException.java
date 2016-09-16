package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when the parser is expecting an operator but an expression is found, or vice-versa.
 */
public class UnexpectedTokenException extends ExpressionException{
  public UnexpectedTokenException(boolean operatorExpected){
    super(operatorExpected ? "An operator was expected, but an expression was found" : "An expression was expected, but an operator was found");
  }
}
