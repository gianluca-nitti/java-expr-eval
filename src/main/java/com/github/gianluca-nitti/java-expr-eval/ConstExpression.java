package com.github.gianlucanitti.javaexpreval;

public final class ConstExpression extends Expression{

  private double value;

  public ConstExpression(double val){
    value = val;
  }

  @Override
  public double eval(){
    return value;
  }
}
