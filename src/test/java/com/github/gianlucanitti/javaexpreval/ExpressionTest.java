package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class ExpressionTest extends TestCase{

  private void assertExprValue(double expected, String expr){
    try{
      //The following line a ugly hack to make comparisons between doubles with the loss of precision that floating point math has.
      //Future versions of the library will probably be based on a better math system with arbitrary precision (Bigdecimal or some external library).
      assertEquals(expected, (double)Math.round(Expression.parse(expr).eval() * 1000) / 1000);
    }catch(Exception ex){
      fail(ex.getMessage());
    }
  }

  public void testParse(){
    assertExprValue(-1879080.904, "((50+2-(3*45.8))+4-6*7+0)^3");
  }

}
