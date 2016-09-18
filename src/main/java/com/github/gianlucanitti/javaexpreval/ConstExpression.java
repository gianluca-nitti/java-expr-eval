package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

/**
 * An expression representing a constant value.
 */
public final class ConstExpression extends Expression{

  private double value;

  /**
   * Initializes a ConstExpression object that represents the specified value.
   * @param val The numerical value the new expression must represent.
   */
  public ConstExpression(double val){
    value = val;
  }

  /**
   * Returns the message to log when this expression is evaluates.
   * This overrides {@link Expression#getEvalMsg(double val)} to avoid pointless log entries like "0.0 evaluates to 0.0".
   * @return an empty string.
   */
  @Override
  public String getEvalMsg(double val){
    return "";
  }

  /**
   * Evaluates this expression (in this case, it simply returns the value passed to the constructor).
   * This is used internally by the library to correctly manage logging of each step.
   * The correct way to evaluate an expression from outside the package is by using {@link Expression#eval()}, {@link Expression#eval(Writer)} or {@link Expression#eval(OutputStream)}.
   * @param logWriter A {@link PrintWriter} to log the steps done.
   * This method doesn't actually log anything (this is done by {@link Expression#eval(PrintWriter)});
   * it's here to pass logWriter to the underlying calls to {@link Expression#eval(PrintWriter)} of hypothetical sub-expressions.
   * Since the type of expressions represented by instances of this class are simply numerical constants, this parameter is unused here.
   * @return The value of this expression; since this is a constant, no calculations are done and the value passed to {@link #ConstExpression(double)} is returned.
   */
  @Override
  protected double evalExpr(PrintWriter logWriter){
    return value;
  }

  /**
   * Returns a string representation of this constant expression.
   * @return The string representation of the value passed to {@link #ConstExpression(double)}.
   */
  @Override
  public String toString(){
    return Double.toString(value);
  }

}
