package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when the expression parser encounters a character that is neither a number, decimal separator or a known operator.
 */
public class UnknownCharException extends ExpressionException{

  /**
   * Initializes an UnknownCharException describing that the specified character isn't supported.
   * @param c The unknown character.
   */
  public UnknownCharException(char c){
    super(LocalizationHelper.getMessage(LocalizationHelper.Message.UNKNOWN_CHAR, Character.toString(c)));
  }

}
