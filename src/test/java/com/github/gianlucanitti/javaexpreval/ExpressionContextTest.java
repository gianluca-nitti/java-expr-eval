package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

import java.io.PrintWriter;

public class ExpressionContextTest extends TestCase{

    public void testGetSetVariable(){
        ExpressionContext c = new ExpressionContext();
        try {
            c.setVariable("someVar", 5);
            c.setVariable("some_other_variable123", new ConstExpression(1));
            assertEquals(5.0, c.getVariable("someVar"));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
        try{
            c.setVariable("123some_invalid_variable123", new ConstExpression(1));
            fail("A variable name with invalid characters is being accepted.");
        }catch(ExpressionException ex){
            assertEquals(ex.getMessage(), "Expression error: \"123some_invalid_variable123\" isn't a valid symbol name because it contains the '1' character.");
        }
        try {
            double x = c.getVariable("someUndefinedVar");
            fail("Undefined variable is bound to value " + x);
        }catch(UndefinedException ex){
            assertEquals("Expression error: The variable \"someUndefinedVar\" is not defined.", ex.getMessage());
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
        try {
            c.setVariable("someVar", 1);
            c.setVariable("someOtherVar", 2);
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
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
        try {
            c.setVariable("a", 4);
            c.setVariable("b", 8);
        }catch(InvalidSymbolNameException ex){
            fail(ex.getMessage());
        }
        assertTrue(c.toString().contains("a=4.0"));
        assertTrue(c.toString().contains("b=8.0"));
    }

    public void testClear(){
        ExpressionContext c = new ExpressionContext();
        try {
            c.setVariable("someVar", 1);
            c.setFunction("some_function", new VariableExpression("x"), "x");
        }catch(InvalidSymbolNameException ex){
            fail(ex.getMessage());
        }
        c.clear();
        try{
            c.getVariable("someVar");
            fail("A variable is still accessible after clear.");
        }catch(UndefinedException ex){
            //ok (variable has been deleted)
        }
        try{
            c.getFunction("some_function", 1);
            fail("A function is still accessible after clear.");
        }catch(UndefinedException ex){
            //ok (function has been deleted)
        }
    }

    public void testGetSetFunction(){
        ExpressionContext c = new ExpressionContext();
        PrintWriter nullWriter = new PrintWriter(NullOutputStream.getWriter());
        try {
            c.setFunction("someFunction", new ConstExpression(1), "x");
            c.setFunction("someFunction", new BinaryOpExpression(new VariableExpression("arg1"), '+', new VariableExpression("arg2")), "arg1", "arg2");
            //must replace the first one (same name and number of arguments)
            c.setFunction("someFunction", new BinaryOpExpression(new ConstExpression(2), '^', new VariableExpression("argument")), "argument");
            assertEquals(8.0, c.getFunction("someFunction", 1).eval(new double[]{3}, c, nullWriter));
            assertEquals(10.0, c.getFunction("someFunction", 2).eval(new double[]{4, 6}, c, nullWriter));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
    }

    public void testDelFunction(){
        ExpressionContext c = new ExpressionContext();
        c.setFunction("someFunction", new ConstExpression(1), "arg");
        c.delFunction("someFunction", 1);
        try{
            c.getFunction("someFunction", 1);
            fail("A deleted function is still accessible.");
        }catch(UndefinedException ex){
            //ok
        }
    }

}
