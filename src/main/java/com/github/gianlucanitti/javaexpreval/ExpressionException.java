package com.github.gianlucanitti.javaexpreval;

/**
 * Abstract class that represents an exception that can be thrown while parsing or evaluating expressions.
 */
public abstract class ExpressionException extends Exception{
  public ExpressionException(String msg){
    super("Error while parsing expression: " + msg);
  }
}
