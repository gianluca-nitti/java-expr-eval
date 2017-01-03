package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when the numbers of opened and closed parenthesis don't match while parsing an expression.
 */
public class MismatchedParenthesisException extends ExpressionException{

  /**
   * Initializes a MismatchedParenthesisException, an exception that describes
   * that the numbers of opened and closed parenthesis in an expression don't match.
   */
  public MismatchedParenthesisException(){
    super(LocalizationHelper.getMessage(LocalizationHelper.Message.PARENTHESIS_MISMATCH));
  }

}
