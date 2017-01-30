package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class ExpressionContextTest extends TestCase{

    public void testMathContext() throws InvalidOperatorException, UndefinedException {
        ExpressionContext c = new ExpressionContext(MathContext.DECIMAL32);
        Expression exp1 = new BinaryOpExpression(new ConstExpression(new BigDecimal(3)), '/', new ConstExpression(new BigDecimal(4)));
        Expression exp2 = new BinaryOpExpression(new ConstExpression(new BigDecimal("123456789987654321123456789987654321")), '*', new ConstExpression(new BigDecimal("987654321123456789")));
        assertEquals(new BigDecimal(0.75, MathContext.DECIMAL32), exp1.eval(c));
        assertEquals(new BigDecimal(1.219326e+53, MathContext.DECIMAL32), exp2.eval(c));
        c.setMathContext(MathContext.DECIMAL64);
        assertEquals(new BigDecimal(0.75, MathContext.DECIMAL64), exp1.eval(c));
        assertEquals(new BigDecimal(1.219326321033379e+53, MathContext.DECIMAL64), exp2.eval(c));
        c.setMathContext(MathContext.DECIMAL128);
        assertEquals(new BigDecimal(0.75, MathContext.DECIMAL128), exp1.eval(c));
        assertEquals(new BigDecimal("1.219326321033379057840268252159732e+53", MathContext.DECIMAL128), exp2.eval(c));
        c.setMathContext(MathContext.UNLIMITED);
        assertEquals(new BigDecimal(0.75, MathContext.UNLIMITED), exp1.eval(c));
        assertEquals(new BigDecimal("121932632103337905784026825215973174662094193112635269", MathContext.UNLIMITED), exp2.eval(c));
    }

    public void testGetVariables() throws ExpressionException{
        ExpressionContext c = new ExpressionContext();
        c.setVariable("a", 5);
        for(Map.Entry<String, ExpressionContext.VariableValue> v: c.getVariables().entrySet()) {
            assertEquals("a", v.getKey());
            assertEquals(new BigDecimal(5.0), v.getValue().getValue());
            assertFalse(v.getValue().isReadOnly());
            try{
                v.setValue(new ExpressionContext.VariableValue(2, false));
                fail("An item was edited in the result of ExpressionContext.getVariables(), which should be read-only.");
            }catch(Exception ex){
                assertTrue(ex instanceof UnsupportedOperationException);
            }
        }
        try {
            c.getVariables().put("test", new ExpressionContext.VariableValue(1, false));
            fail("An item was added to the result of ExpressionContext.getVariables(), which should be read-only.");
        }catch(Exception ex){
            assertTrue(ex instanceof UnsupportedOperationException);
        }
    }

    public void testGetFunctions() throws ExpressionException{
        ExpressionContext c = new ExpressionContext();
        List<Function> builtInFunctions = new ArrayList<Function>();
        for(Function f: BuiltInFunctions.getList())
            builtInFunctions.add(f);
        for(Function f: c.getFunctions()) {
            assertTrue(builtInFunctions.contains(f));
            assertTrue(f.isReadOnly());
        }
        try {
            c.getFunctions().add(BuiltInFunctions.getList().get(0));
            fail("An item was added to the result of ExpressionContext.getFunctions(), which should be read-only.");
        }catch(Exception ex){
            assertTrue(ex instanceof UnsupportedOperationException);
        }
    }

    public void testGetSetVariable(){
        ExpressionContext c = new ExpressionContext();
        try {
            c.setVariable("someVar", 5);
            c.setVariable("some_other_variable123", true, 1);
            assertEquals(new BigDecimal(5.0), c.getVariable("someVar"));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
        try{
            c.setVariable("some_other_variable123", 2);
        }catch(ExpressionException ex){
            assertTrue(ex instanceof ReadonlyException);
        }
        try{
            c.setVariable("123some_invalid_variable123", 1);
            fail("A variable name with invalid characters is being accepted.");
        }catch(ExpressionException ex){
            assertEquals(ex.getMessage(), "Expression error: \"123some_invalid_variable123\" isn't a valid symbol name because it contains the '1' character.");
        }
        try {
            BigDecimal x = c.getVariable("someUndefinedVar");
            fail("Undefined variable is bound to value " + x);
        }catch(UndefinedException ex){
            assertEquals("Expression error: The variable \"someUndefinedVar\" is not defined.", ex.getMessage());
        }
    }

    public void testSetVariableExpression(){
        try {
            ExpressionContext c = new ExpressionContext();
            c.setVariable("a", false, new BinaryOpExpression(new ConstExpression(3), '+', new ConstExpression(5)));
            assertEquals(new BigDecimal(8.0), c.getVariable("a"));
            c.setVariable("b", false, new NegatedExpression(new VariableExpression("a")));
            assertEquals(new BigDecimal(-8.0), c.getVariable("b"));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
    }

    public void testDelVariable(){
        ExpressionContext c = new ExpressionContext();
        try {
            c.setVariable("someVar", 1);
            c.setVariable("someOtherVar", 2);
            c.delVariable("someVar");
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
        try {
            assertEquals(new BigDecimal(2.0), c.getVariable("someOtherVar"));
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
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
        assertTrue(c.toString().contains("a=4"));
        assertTrue(c.toString().contains("b=8"));
    }

    public void testClear(){
        ExpressionContext c = new ExpressionContext();
        try {
            c.setVariable("someVar", 1);
            c.setVariable("someReadonlyVar", true, 0);
            c.setFunction("some_function", new VariableExpression("x"), "x");
            c.setFunction("some_readonly_function", new VariableExpression("x"), true, "x");
        }catch(ExpressionException ex){
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
        try{
            c.getVariable("someReadonlyVar");
        }catch(UndefinedException ex){
            fail("A read-only variable has been deleted during clear.");
        }
        try{
            c.getFunction("some_readonly_function", 1);
        }catch(UndefinedException ex){
            fail("A read-only function has been deleted during clear.");
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
            //TODO //assertEquals(new BigDecimal(8.0), c.getFunction("someFunction", 1).eval(new BigDecimal[]{new BigDecimal(3)}, c, nullWriter));
            //TODO //assertEquals(new BigDecimal(10.0), c.getFunction("someFunction", 2).eval(new BigDecimal[]{new BigDecimal(4), new BigDecimal(6)}, c, nullWriter));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
    }

    public void testDelFunction() throws ExpressionException{
        ExpressionContext c = new ExpressionContext();
        c.setFunction("someFunction", new ConstExpression(1), "arg");
        c.setFunction("someFunction", new ConstExpression(1), true, "arg1", "arg2");
        c.delFunction("someFunction", 1);
        try{
            c.getFunction("someFunction", 1);
            fail("A deleted function is still accessible.");
        }catch(UndefinedException ex){
            //ok
        }
        try {
            c.delFunction("someFunction", 2);
            fail("A read-only function has been deleted.");
        }catch(ReadonlyException ex){
            //ok
        }
    }

    public void testObservable() throws InvalidSymbolNameException, ReadonlyException{
        final List<String> vars = new ArrayList<String>();
        final List<String> functions = new ArrayList<String>();
        class ob implements Observer{
            public void update(Observable o, Object arg){
                vars.clear();
                functions.clear();
                for(Map.Entry<String, ExpressionContext.VariableValue> v: ((ExpressionContext)o).getVariables().entrySet())
                    vars.add(v.getKey());
                for(Function f: ((ExpressionContext)o).getFunctions())
                    if(f instanceof CustomFunction) //ignore built-in functions
                        functions.add(f.getName() + f.getArgCount());
            }
        }
        ExpressionContext c = new ExpressionContext();
        c.addObserver(new ob());
        c.setVariable("a", 1);
        assertTrue(vars.size() == 1 && vars.get(0).equals("a"));
        c.setVariable("b", 1);
        assertTrue(vars.size() == 2 && vars.contains("a") && vars.contains("b"));
        c.setFunction("fun", new ConstExpression(1));
        assertTrue(functions.size() == 1 && functions.get(0).equals("fun0"));
        assertTrue(vars.size() == 2 && vars.contains("a") && vars.contains("b"));
    }

}
