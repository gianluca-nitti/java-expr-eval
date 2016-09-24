package com.github.gianlucanitti.javaexpreval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a list of expressions with the operators that concatenate them.
 * This class implements the logic to split an expression in multiple binary operations according to operator precedence.
 */
public final class ExpressionList{

  //operator at index i is between item i and item i+1
  private ArrayList<Expression> items;
  private ArrayList<Character> operators;

  private boolean expectOperator = false; //When true, it's expected that an operator is added; othervise, an expression is expected.

  /**
   * Initializes a new instance of this class.
   */
  public ExpressionList(){
    items = new ArrayList<Expression>();
    operators = new ArrayList<Character>();
  }

  /**
   * Adds an expression to the list.
   * @param item The expression to add.
   * @throws UnexpectedTokenException if an operator is expected. An operator must always be added before adding another expression.
   */
  public void addItem(Expression item) throws UnexpectedTokenException{
    if(expectOperator)
      throw new UnexpectedTokenException(expectOperator);
    items.add(item);
    expectOperator = true;
  }

  /**
   * Adds an operator to the list. The operator is applied between the last inserted expression and the next inserted expression.
   * A call to {@link #addOperator(char)} must always be followed by a call to {@link #addItem(Expression)}.
   * @param op The operator to add.
   * @throws UnexpectedTokenException if an expression is expected. An expression must always be added before adding another operator.
   */
  public void addOperator(char op) throws UnexpectedTokenException{
    if(!expectOperator)
      throw new UnexpectedTokenException(expectOperator);
    operators.add(op);
    expectOperator = false;
  }

  /**
   * Returns a boolean stating whether this {@link ExpressionList} is expecting an operator or an expression.
   * @return <code>true</code> if an operator is expected, <code>false</code> if an expression is expected.
   */
  public boolean isExpectingOperator(){
    return expectOperator;
  }

  /**
   * Evaluates the specified operators, by replacing any expression-operator-expression structure with a properly initialized {@link BinaryOpExpression}.
   * @param ops A char or {@link Character} or an array of them that represent the operators to evaluate.
   * @throws InvalidOperatorException if an unknown operator is found.
   */
  private void evalOperators(Character ... ops) throws InvalidOperatorException{
    List<Character> opsList = Arrays.asList(ops);
    int i = 0;
    while(i < operators.size()){
      Character op = operators.get(i);
      if(opsList.contains(op)){
        operators.remove(i);
        Expression left = items.remove(i);
        Expression right = items.remove(i);
        items.add(i, new BinaryOpExpression(left, op, right));
      }else{
        i++;
      }
    }
  }

  /**
   * Simplifies this list of expression by returning an equivalent expression.
   * @throws InvalidOperatorException if an unknown operator is found.
   * @throws EmptyExpressionException if no expression were added.
   * @return An expression equivalent to the list of expressions and operators represented by this object.
   */
  public Expression simplify() throws InvalidOperatorException, EmptyExpressionException{
    evalOperators('^');
    evalOperators('*', '/');
    evalOperators('+', '-');
    if(items.size() == 0)
      throw new EmptyExpressionException();
    if(items.size() != 1 || operators.size() != 0) throw new InvalidOperatorException(operators.get(0));
    return items.get(0);
  }

}
