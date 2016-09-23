package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class ConstExpressionTest extends TestCase{

  public void testEval(){
    ConstExpression exp = new ConstExpression(25);
    try {
      assertEquals(25.0, exp.eval());
    }catch(UndefinedException ex){
      fail(ex.getMessage());
    }
  }

  public void testToString(){
    ConstExpression exp = new ConstExpression(40);
    assertEquals("40.0", exp.toString());
  }

}
