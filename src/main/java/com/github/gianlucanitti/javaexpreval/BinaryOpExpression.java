package com.github.gianlucanitti.javaexpreval;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
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
   * @return The two operands passed to the {@link #BinaryOpExpression(Expression, char, Expression)} constructor.
   */
  @Override
  public Expression[] getSubExpressions(){
    return new Expression[]{left, right};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected BigDecimal evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
    BigDecimal a = left.eval(context, logWriter);
    BigDecimal b = right.eval(context, logWriter);
    BigDecimal result = null;
    switch(op){
      case '+': result = a.add(b); break;
      case '-': result = a.subtract(b); break;
      case '*': result = a.multiply(b); break;
      case '/': result = a.divide(b); break;
      //case '%': result = a % b; break;
      case '^': throw new NotImplementedException(); //break; //TODO
    }
    return result;
  }

  /**
   * Returns a string representation of this binary operation, enclosed in parenthesis.
   * @return The string is built by concatenating the string representations of the two operand expressions separated by the character representing the operator.
   */
  @Override
  public String toString(){
    return "(" + left.toString() + op + right.toString() + ")";
  }
  
}
