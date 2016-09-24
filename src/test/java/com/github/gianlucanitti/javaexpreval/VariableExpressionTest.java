package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class VariableExpressionTest extends TestCase{

    public void testEval(){
        VariableExpression exp = new VariableExpression("someVar");
        ExpressionContext c = new ExpressionContext();
        c.setVariable("someVar", 6.2);
        try {
            assertEquals(6.2, exp.eval(c));
        }catch(UndefinedException ex){
            fail(ex.getMessage());
        }
    }

    public void testToString(){
        String varName = "someVar";
        VariableExpression expr = new VariableExpression(varName);
        assertEquals(varName, expr.toString());
    }

}
