package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when the parser is expecting an operator but an expression is found, or vice-versa.
 */
public class UnexpectedTokenException extends ExpressionException{

  /**
   * Initializes an UnexpectedTokenException, an exception stating that the parser was expecting a sub-expression but an operator was found, or vice-versa.
   * @param operatorExpected If <code>true</code>, the exception will indicate that an operator was found, but an expression was expected;
   * if <code>false</code>, the exception will indicate that an expression was expected, but an operator was found.
   */
  public UnexpectedTokenException(boolean operatorExpected){
    super(operatorExpected ? "An operator was expected, but an expression was found" : "An expression was expected, but an operator was found");
  }
  
}
