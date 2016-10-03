package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class ExpressionListTest extends TestCase{

  public void testAdd(){
    ExpressionList expList = new ExpressionList();
    try{
      expList.addItem(new ConstExpression(1));
    }catch(Exception ex){
      fail(ex.getMessage());
    }
    try{
      expList.addOperator('+');
    }catch(Exception ex){
      fail(ex.getMessage());
    }
    try{
      expList.addItem(new ConstExpression(2));
    }catch(Exception ex){
      fail(ex.getMessage());
    }
    try{
      expList.addItem(new ConstExpression(1));
      fail("ExpressionList.addItem is allowing insertion of invalid tokens");
    }catch(Exception ex){
      assertTrue(ex instanceof UnexpectedTokenException);
    }
  }

  public void testSimplify(){
    try{
      ExpressionList expList = new ExpressionList();
      expList.addItem(new ConstExpression(5));
      expList.addOperator('+');
      expList.addItem(new ConstExpression(3));
      expList.addOperator('*');
      expList.addItem(new ConstExpression(2));
      expList.addOperator('^');
      expList.addItem(new BinaryOpExpression(new ConstExpression(3), '+', new ConstExpression(1)));
      assertEquals(53.0, expList.simplify().eval());
    }catch(Exception ex){
      fail(ex.getMessage());
    }
    try{
      ExpressionList expList = new ExpressionList();
      expList.addItem(new ConstExpression(1));
      expList.addOperator('/');
      expList.simplify();
      fail("An invalid expression list doesn't throw an exception when simplified.");
    }catch(Exception ex){
      assertTrue(ex instanceof ExpressionException);
    }
  }

}
