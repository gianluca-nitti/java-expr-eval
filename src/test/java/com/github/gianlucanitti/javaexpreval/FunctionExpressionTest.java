package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class FunctionExpressionTest extends TestCase{

    public void testEval() throws ExpressionException{
        assertEquals(4.0, new FunctionExpression("sqrt", new ConstExpression(16)).eval());
        try{
            new FunctionExpression("someRandomFunction", new ConstExpression(1)).eval();
            fail("An undefined function is evaluated without throwing exception.");
        }catch(Exception ex){
            assertTrue(ex instanceof UndefinedException);
        }
        try{
            new FunctionExpression("log", new ConstExpression(1), new ConstExpression(1)).eval();
            fail("An function is evaluated without throwing exception on a number of arguments for which is undefined.");
        }catch(Exception ex){
            assertTrue(ex instanceof UndefinedException);
        }
    }

    public void testToString() throws InvalidSymbolNameException{
        assertEquals("cos(pi)", new FunctionExpression("cos", new VariableExpression("pi")).toString());
    }

}
