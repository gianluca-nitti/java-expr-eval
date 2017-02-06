package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * An expression representing a constant value.
 */
public final class ConstExpression extends Expression{

  private BigDecimal value;

  /**
   * Initializes a ConstExpression object that represents the specified value.
   * @param val The numerical value the new expression must represent.
   */
  public ConstExpression(BigDecimal val){
    value = val;
  }

  /**
   * Initializes a ConstExpression object that represents the specified value.
   * @param val The numerical value the new expression must represent; will be converted to a {@link BigDecimal}.
   */
  public ConstExpression(double val){
    value = new BigDecimal(val);
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
   * This overrides {@link Expression#getEvalMsg(BigDecimal val)} to avoid pointless log entries like "0.0 evaluates to 0.0".
   * @return an empty string.
   */
  @Override
  public String getEvalMsg(BigDecimal val){
    return "";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected BigDecimal evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
    return value.round(context.getMathContext());
  }

  /**
   * Returns a string representation of this constant expression.
   * @return The string representation of the value passed to {@link #ConstExpression(BigDecimal)}.
   */
  @Override
  public String toString(){
    return value.toString();
  }

}
