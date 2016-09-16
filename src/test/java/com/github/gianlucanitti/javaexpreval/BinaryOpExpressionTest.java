package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class BinaryOpExpressionTest extends TestCase{

  public void testEval(){
    try{
      BinaryOpExpression exp = new BinaryOpExpression(new ConstExpression(20), '+', new ConstExpression(15));
      assertEquals(20.0+15.0, exp.eval());
      exp = new BinaryOpExpression(new ConstExpression(1), '-', new ConstExpression(6));
      assertEquals(1.0-6.0, exp.eval());
      exp = new BinaryOpExpression(new ConstExpression(3), '*', new ConstExpression(8));
      assertEquals(3.0*8.0, exp.eval());
      exp = new BinaryOpExpression(new ConstExpression(14), '/', new ConstExpression(6));
      assertEquals(14.0/6.0, exp.eval());
      exp = new BinaryOpExpression(new ConstExpression(2), '^', new ConstExpression(10));
      assertEquals(Math.pow(2.0, 10.0), exp.eval());
    }catch(InvalidOperatorException ex){
      fail(ex.getMessage());
    }
  }

  public void testToString(){
    //TODO
  }

}
