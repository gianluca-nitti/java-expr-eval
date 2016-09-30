package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class VariableExpressionTest extends TestCase{

    public void testEval(){
        try{
            VariableExpression exp = new VariableExpression("someVar");
            ExpressionContext c = new ExpressionContext();
            c.setVariable("someVar", 6.2);
            assertEquals(6.2, exp.eval(c));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
    }

    public void testToString(){
        try{
            String varName = "someVar";
            VariableExpression expr = new VariableExpression(varName);
            assertEquals(varName, expr.toString());
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
    }

    public void testIsValidSymbolName(){
        assertTrue(VariableExpression.isValidSymbolName("someVariable"));
        assertTrue(VariableExpression.isValidSymbolName("_"));
        assertTrue(VariableExpression.isValidSymbolName("some_other_variable"));
        assertTrue(VariableExpression.isValidSymbolName("var2"));
        assertFalse(VariableExpression.isValidSymbolName("a-variable"));
        assertFalse(VariableExpression.isValidSymbolName("   "));
        assertFalse(VariableExpression.isValidSymbolName("4var"));
    }

}
