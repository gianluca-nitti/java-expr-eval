package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class NegatedExpressionTest extends TestCase {

    public void testEval(){
        NegatedExpression expr = new NegatedExpression(new ConstExpression(10));
        try {
            assertEquals(-10.0, expr.eval());
        }catch(UndefinedException ex){
            fail(ex.getMessage());
        }
    }

    public void testToString(){
        NegatedExpression expr = new NegatedExpression(new ConstExpression(10));
        assertEquals("(-(10.0))", expr.toString());
    }

}
