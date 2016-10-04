package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when the parser is expecting an operator but an expression is found, or vice-versa,
 * or if a sub-expression was expected but nothing was found (i.e. the expression ends with a binary operator).
 */
public class UnexpectedTokenException extends ExpressionException{

  /**
   * Initializes an UnexpectedTokenException according to the specified parameters.
   * @param operatorExpected If <code>true</code>, the exception will indicate that an operator was expected but a sub-expression was found.
   * if <code>false</code>, the exception will indicate that a sub-expression was expected, but an operator was found (if <code>endOfExpression == false</code>)
   * or the end of the expression was reached (if <code>endOfExpression == true</code>).
   * @param endOfExpression If <code>true</code>, the exception will indicate that a sub-expression was expected but the end of the expression was reached;
   * if <code>false</code>, it will indicate that a sub-expression was expected but an operator was found. This parameter is ignored if <code>operatorExpected == true</code>.
   */
  public UnexpectedTokenException(boolean operatorExpected, boolean endOfExpression){
    super(operatorExpected ? "An operator was expected, but an expression was found" : "A sub-expression was expected, but " +
            (endOfExpression ? "the end of the expression was reached" : "an operator was found"));
  }

}
