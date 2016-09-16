package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when the numbers of opened and closed parenthesis don't match while parsing an expression.
 */
public class MismatchedParenthesisException extends ExpressionException{
  public MismatchedParenthesisException(){
    super("The numbers of opened and closed parenthesis don't match");
  }
}
