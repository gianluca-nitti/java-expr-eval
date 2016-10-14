package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class ExpressionTest extends TestCase{

  private void assertExprValue(double expected, String expr, ExpressionContext context) throws ExpressionException{
      //The following line is an ugly hack to make comparisons between doubles with the loss of precision that floating point math has.
      //Future versions of the library will probably be based on a better math system with arbitrary precision (Bigdecimal or some external library).
      assertEquals(expected, (double)Math.round(Expression.parse(expr).eval(context) * 1000) / 1000);
  }

  public void testParse(){
    ExpressionContext c = new ExpressionContext();
    try {
      c.setVariable("some_Var", -45.8);
      c.setVariable("some_other_var", 3);
      assertExprValue(-1879080.904, "((50+2-(3*-some_Var))+sqrt(16)-6*7+0)^some_other_var", c);
    }catch(ExpressionException ex){
      fail(ex.getMessage());
    }
  }

}
