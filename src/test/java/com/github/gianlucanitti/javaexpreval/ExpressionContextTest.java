package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

public class ExpressionContextTest extends TestCase{

    public void testGetSetVariable(){
        ExpressionContext c = new ExpressionContext();
        c.setVariable("someVar", 5);
        try {
            assertEquals(5.0, c.getVariable("someVar"));
        }catch(UndefinedException ex){
            fail(ex.getMessage());
        }
        try {
            double x = c.getVariable("someUndefinedVar");
            fail("Undefined variable is bound to value " + x);
        }catch(UndefinedException ex){
            assertEquals("Error while parsing expression: The variable \"someUndefinedVar\" is not defined.", ex.getMessage());
        }
    }

    public void testSetVariableExpression(){
        try {
            ExpressionContext c = new ExpressionContext();
            c.setVariable("a", new BinaryOpExpression(new ConstExpression(3), '+', new ConstExpression(5)));
            assertEquals(8.0, c.getVariable("a"));
            c.setVariable("b", new NegatedExpression(new VariableExpression("a")));
            assertEquals(-8.0, c.getVariable("b"));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
    }

    public void testDelVariable(){
        ExpressionContext c = new ExpressionContext();
        c.setVariable("someVar", 1);
        c.setVariable("someOtherVar", 2);
        c.delVariable("someVar");
        try {
            assertEquals(2.0, c.getVariable("someOtherVar"));
        }catch(UndefinedException ex){
            fail("The wrong variable was deleted.");
        }
        try{
            c.getVariable("someVar");
            fail("A variable is still accessible after being deleted.");
        }catch(UndefinedException ex){
            //ok (right variable was deleted)
        }
    }

    public void testToString(){
        ExpressionContext c = new ExpressionContext();
        c.setVariable("a", 4);
        c.setVariable("b", 8);
        assertTrue(c.toString().contains("a=4.0"));
        assertTrue(c.toString().contains("b=8.0"));
    }

    public void testClear(){
        ExpressionContext c = new ExpressionContext();
        c.setVariable("someVar", 1);
        c.clear();
        try{
            c.getVariable("someVar");
            fail("A variable is still accessible after clear.");
        }catch(UndefinedException ex){
            //ok (context has been cleared)
        }
    }

}
