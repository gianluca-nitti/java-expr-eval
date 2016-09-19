package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when the expression parser encounters a character that is neither a number, decimal separator or a known operator.
 */
public class UnknownCharException extends ExpressionException{

  private static String newLine = System.getProperty("line.separator");

  /**
   * Adds the specified number of spaces to the left of the specified string.
   * @param s The input string.
   * @param n The number of spaces to add.
   * @return The modified string.
   */
  private static String padLeft(String s, int n){
    for(int i = 0; i < n; i++)
      s = " " + s;
    return s;
  }

  /**
   * Initializes an UnknownCharException describing that the specified character isn't supported.
   * @param expr The string representing the expression.
   * @param index The index of the unknown character in the string.
   */
  public UnknownCharException(String expr, int index){
    super("Unrecognized character '" + expr.charAt(index) + "'" + newLine + "    " + expr + newLine + padLeft("^", index + 4));
  }

}
