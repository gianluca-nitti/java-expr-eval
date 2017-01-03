package com.github.gianlucanitti.javaexpreval;

/**
 * This exception is thrown when a user tries to define a symbol with a name that contains invalid characters.
 */
public class InvalidSymbolNameException extends ExpressionException {

    /**
     * Initializes a new InvalidSymbolNameException related to specified string.
     * @param name The string that is being used as symbol name.
     * @param badCharIndex The index of the character in <code>name</code> that isn't a valid character for symbol names.
     */
    public InvalidSymbolNameException(String name, int badCharIndex){
        super(LocalizationHelper.getMessage(LocalizationHelper.Message.INVALID_SYM_NAME, name, Character.toString(name.charAt(badCharIndex))));
    }

    /**
     * Initializes a new InvalidSymbolNameException related to the empty string.
     */
    public InvalidSymbolNameException(){
        super(LocalizationHelper.getMessage(LocalizationHelper.Message.EMPTY_SYM_NAME));
    }

}
