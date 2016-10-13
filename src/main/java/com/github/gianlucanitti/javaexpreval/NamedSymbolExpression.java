package com.github.gianlucanitti.javaexpreval;

/**
 * Represents a generic expression that has a name, like a {@link VariableExpression} and {@link FunctionExpression}.
 * Symbol/identifier names can contain letters, underscores and digits only, and the first character can't be a digit. Symbol names are case-sensitive.
 */
public abstract class NamedSymbolExpression extends Expression{

    private String symbolName;

    /**
     * Instantiates a new NamedSymbolExpression with the specified name.
     * @param symbolName The name associated with the expression; must be a valid symbol name (see {@link #isValidSymbolName(String)}).
     * @throws InvalidSymbolNameException if <code>symbolName</code> isn't a valid name (see {@link NamedSymbolExpression}).
     */
    public NamedSymbolExpression(String symbolName) throws InvalidSymbolNameException{
        assertValidSymbolName(symbolName);
        this.symbolName = symbolName;
    }

    /**
     * Returns the name associated with this expression.
     * @return The string passed to the {@link #NamedSymbolExpression(String)} constructor.
     */
    public String getName(){
        return symbolName;
    }

    /**
     * Checks if the specified character can be part of a symbol name.
     * The first character as more restrictions; <code>isValidSymbolChar(c)</code> doesn't guarantee that <code>c</code> can be used as the first character of a symbol name;
     * see {@link #isValidSymbolFirstChar(char)} for this.
     * @param c The character to verify.
     * @return <code>true</code> if <code>c</code> can be used in symbol names (<code>c</code> is a letter, an underscore or a digit), <code>false</code> otherwise.
     */
    public static boolean isValidSymbolChar(char c){
        return Character.isDigit(c) || isValidSymbolFirstChar(c);
    }

    /**
     * Checks if the specified character can be the first character of a symbol name.
     * @param c The character to verify.
     * @return <code>true</code> if <code>c</code> can be the first character of a symbol names (<code>c</code> is a letter or an underscore), <code>false</code> otherwise.
     */
    public static boolean isValidSymbolFirstChar(char c){
        return c == '_' || Character.isLetter(c);
    }

    /**
     * If the specified string is an invalid symbol name, returns the index of the first character that isn't a letter or an underscore.
     * @param s The symbol name.
     * @return <code>-1</code> if <code>s</code> is a valid symbol name, <code>-2</code> if <code>s</code> is empty (and thus not a valid symbol name),otherwise the index of the first invalid character in <code>s</code>.
     */
    public static int firstInvalidSymbolNameCharIndex(String s){
        if(s.length() == 0)
            return -2;
        if(!isValidSymbolFirstChar(s.charAt(0)))
            return 0;
        for(int i = 1; i < s.length(); i++)
            if(!isValidSymbolChar(s.charAt(i)))
                return i;
        return -1;
    }

    /**
     * Checks if the specified string is a valid symbol name.
     * @param name The string to verify.
     * @return <code>true</code> if <code>name</code> is a valid symbol name (<code>name</code> is made of letters and underscores only; digits are allowed too, but not as first character), <code>false</code> otherwise.
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
        if (index == -2)
            throw new InvalidSymbolNameException();
        if(index != -1)
            throw new InvalidSymbolNameException(s, index);
    }

}
