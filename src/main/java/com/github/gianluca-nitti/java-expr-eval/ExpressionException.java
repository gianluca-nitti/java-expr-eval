package com.github.gianlucanitti.javaexpreval;

public abstract class ExpressionException extends Exception{
  public ExpressionException(String msg){
    super("Error while parsing expression: " + msg);
  }
}
