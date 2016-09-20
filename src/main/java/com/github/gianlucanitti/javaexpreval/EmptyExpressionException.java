package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when an empty expression is found, for example if an empty string is tried to be parsed as an expression
 * or if an opened parenthesis is immediately followed by a closed one.
 */
public class EmptyExpressionException extends ExpressionException{

  /**
   * Initializes a new EmptyExpressionException.
   */
  public EmptyExpressionException(){
    super("An empty expression was found");
  }

}
