package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when an unknown operator is found while parsing an expression.
 */
public class InvalidOperatorException extends ExpressionException{
  
  /**
   * Initializes an InvalidOperatorException describing that the specified character isn't a supported operator.
   * @param op The character representing the unsupported operator.
   */
  public InvalidOperatorException(char op){
    super(LocalizationHelper.getMessage(LocalizationHelper.Message.INVALID_OPERATOR, Character.toString(op)));
  }

}
