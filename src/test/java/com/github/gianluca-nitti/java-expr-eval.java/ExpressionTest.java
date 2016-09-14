package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class ExpressionTest extends TestCase{

  public void testParse(){
    try{
      Expression exp = Expression.parse("(50+2-(3*45.8))+4-6*7+0");
      double val = exp.eval();
      assertEquals(-123.4, (double)Math.round(val * 10) / 10); //TODO: this probably isn't the right way of testing it
    }catch(Exception ex){
      fail(ex.getMessage());
    }
  }

}
