package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

/**
 An expression representing a variable. It's value is determined by reading from the expression context.
 */
public class VariableExpression extends Expression{

    private String varName;

    /**
     * Initializes a new VariableExpression with the specified variable name.
     * @param var The variable's name.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     */
    public VariableExpression(String var) throws InvalidSymbolNameException{
        assertValidSymbolName(var);
        varName = var;
    }

    /**
     * Checks if the specified character can be part of a symbol (variable) name.
     * @param c The character to verify.
     * @return <code>true</code> if <code>c</code> can be used in symbol names (<code>c</code> is a letter or an underscore), <code>false</code> otherwise.
     */
    public static boolean isValidSymbolChar(char c){
        return c == '_' || Character.isLetter(c);
    }

    /**
     * If the specified string is an invalid symbol name, returns the index of the first character that isn't a letter or an underscore.
     * @param s The symbol name.
     * @return <code>-1</code> if <code>s</code> is a valid symbol name, otherwise the index of the first invalid character in <code>s</code>.
     */
    public static int firstInvalidSymbolNameCharIndex(String s){
        for(int i = 0; i < s.length(); i++)
            if(!isValidSymbolChar(s.charAt(i)))
                return i;
        return -1;
    }

    /**
     * Checks if the specified string is a valid symbol (variable) name.
     * @param name The string to verify.
     * @return <code>true</code> if <code>name</code> is a valid symbol name (<code>name</code> is made of letters and underscores only), <code>false</code> otherwise.
     */
    public static boolean isValidSymbolName(String name){
        return firstInvalidSymbolNameCharIndex(name) == -1;
    }

    /**
     * Does nothing if the specified string is a valid symbol name; otherwise, throws an appropriate exception.
     * @param s The string to verify.
     * @throws InvalidSymbolNameException if <code>s</code> isn't a valid symbol name.
     */
    public static void assertValidSymbolName(String s) throws InvalidSymbolNameException{
        int index = firstInvalidSymbolNameCharIndex(s);
        if(index != -1)
            throw new InvalidSymbolNameException(s, index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
        return context.getVariable(varName);
    }

    /**
     * Returns a string representation of this variable expression.
     * @return The string passed to {@link #VariableExpression(String)}.
     */
    @Override
    public String toString() {
        return varName;
    }

}
