package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class FunctionExpressionTest extends TestCase{

    public void testEval() throws UndefinedException{
        assertEquals(4.0, new FunctionExpression("sqrt", new ConstExpression(16)).eval());
    }

    public void testToString() throws InvalidSymbolNameException{
        assertEquals("cos(pi)", new FunctionExpression("cos", new VariableExpression("pi")).toString());
    }

}
