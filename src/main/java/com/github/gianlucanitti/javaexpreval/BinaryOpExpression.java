package com.github.gianlucanitti.javaexpreval;

import java.util.Arrays;
import java.io.PrintWriter;

/**
 * An expression defined as an operation between two other expressions.
 */
public final class BinaryOpExpression extends Expression{

  private final Expression left;
  private final Expression right;
  private final char op;

  private static final Character[] allowedOperators = new Character[]{'+', '-', '*', '/', '^'};

  /**
   * Check if the specified char is a supported binary operator. Currently the supported operators are '+', '-', '*', '/', '^'.
   * @param c The character to check for (you can pass a char or a Character thanks to autoboxing).
   * @return <code>true</code> if the argument is a binary operator supported by this class, otherwise <code>false</code>.
   */
  public static boolean isAllowedOperator(Character c){
    return Arrays.asList(allowedOperators).contains(c);
  }

  /**
   * Initializes a BinaryOpExpression representing the specified operation.
   * @param left The left operand expression.
   * @param op The char that represents the operator. Must be one of the supported operators (see {@link #isAllowedOperator(Character)}).
   * @param right The right operand expression.
   * @throws InvalidOperatorException if the operator is not supported (see {@link #isAllowedOperator(Character)}).
   */
  public BinaryOpExpression(Expression left, char op, Expression right) throws InvalidOperatorException{
    this.left = left;
    this.right = right;
    if(!isAllowedOperator(op))
      throw new InvalidOperatorException(op);
    this.op = op;
  }

  /**
   * Evaluates this expression by computing the binary operation.
   * This is used internally by the library to correctly manage logging of each step.
   * The correct way to evaluate an expression from outside the package is by using {@link Expression#eval()}, {@link Expression#eval(Writer)} or {@link Expression#eval(OutputStream)}.
   * @param logWriter A {@link PrintWriter} to log the steps done.
   * This method doesn't actually log anything (this is done by {@link Expression#eval(PrintWriter)}), but passes the logWriter to the underlying calls to {@link Expression#eval(PrintWriter)} of the operand expressions.
   * @return The value of this expression.
   */
  @Override
  protected double evalExpr(PrintWriter logWriter){
    double a = left.eval(logWriter);
    double b = right.eval(logWriter);
    double result = 0;
    switch(op){
      case '+': result = a + b; break;
      case '-': result = a - b; break;
      case '*': result = a * b; break;
      case '/': result = a / b; break;
      //case '%': result = a % b; break;
      case '^': result = Math.pow(a, b); break;
    }
    return result;
  }

  /**
   * Returns a string representation of this binary operation, enclosed in parenthesis.
   * @return The string is built by concatenating the string representations of the two operand expressions separed by the character representing the operator.
   */
  @Override
  public String toString(){
    return "(" + left.toString() + op + right.toString() + ")";
  }
}
