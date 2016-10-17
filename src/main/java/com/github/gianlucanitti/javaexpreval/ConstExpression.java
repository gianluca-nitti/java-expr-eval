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
   * @return An empty array of {@link Expression}s.
   */
  @Override
  public Expression[] getSubExpressions(){
    return new Expression[0];
  }

  /**
   * Returns the message to log when this expression is evaluated.
   * This overrides {@link Expression#getEvalMsg(double val)} to avoid pointless log entries like "0.0 evaluates to 0.0".
   * @return an empty string.
   */
  @Override
  public String getEvalMsg(double val){
    return "";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected double evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
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
