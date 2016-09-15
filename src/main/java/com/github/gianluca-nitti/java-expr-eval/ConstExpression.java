package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

public final class ConstExpression extends Expression{

  private double value;

  public ConstExpression(double val){
    value = val;
  }

  @Override
  public String getEvalMsg(double val){
    return ""; //avoid log entries like "0.0 evaluates to 0.0"
  }

  @Override
  protected double evalExpr(PrintWriter logWriter){
    return value;
  }

  @Override
  public String toString(){
    return Double.toString(value);
  }
}
