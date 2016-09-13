package com.github.gianlucanitti.javaexpreval;

public class MismatchedParenthesisException extends ExpressionException{
  public MismatchedParenthesisException(){
    super("The numbers of opened and closed parenthesis don't match");
  }
}
