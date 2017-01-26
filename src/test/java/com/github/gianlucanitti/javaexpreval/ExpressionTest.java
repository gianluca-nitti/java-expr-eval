package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

import java.math.BigDecimal;

public class ExpressionTest extends TestCase{

  private void assertExprValue(BigDecimal expected, String expr, ExpressionContext context) throws ExpressionException{
      //assertEquals(expected, (BigDecimal)Math.round(Expression.parse(expr).eval(context) * 1000) / 1000); //TODO
  }

  public void testParse(){
    ExpressionContext c = new ExpressionContext();
    try {
      c.setVariable("some_Var", -45.8);
      c.setVariable("some_other_var", 3);
      c.setFunction("square", new BinaryOpExpression(new VariableExpression("number"), '^', new ConstExpression(2)), "number");
      assertExprValue(new BigDecimal(-1879080.904), "((50+2-(3*-some_Var))+sqrt(square(4))-6*7+0)^some_other_var", c);
    }catch(ExpressionException ex){
      fail(ex.getMessage());
    }
  }

}
