package com.github.gianlucanitti.javaexpreval;

/**
 * Abstract class that represents an exception that can be thrown while parsing or evaluating expressions.
 */
public abstract class ExpressionException extends Exception{

  /**
   * Initializes an ExpressionException with the specified message.
   * It simply calls the superclass constructor ({@link Exception#Exception(String)})
   * with a message built by concatenating "Error while parsing expression: " with the specified message.
   * @param msg A message describing the occurred problem.
   */
  public ExpressionException(String msg){
    super("Error while parsing expression: " + msg);
  }
  
}
