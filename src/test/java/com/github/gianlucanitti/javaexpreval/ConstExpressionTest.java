package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

import java.math.BigDecimal;

public class ConstExpressionTest extends TestCase{

  public void testEval(){
    ConstExpression exp = new ConstExpression(25);
    try {
      assertEquals(new BigDecimal(25.0), exp.eval());
    }catch(UndefinedException ex){
      fail(ex.getMessage());
    }
  }

  public void testToString(){
    ConstExpression exp = new ConstExpression(40);
    assertEquals("40", exp.toString());
  }

}
