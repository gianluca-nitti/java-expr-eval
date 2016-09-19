package com.github.gianlucanitti.javaexpreval;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Represents a generic arithmetical expression.
 */
public abstract class Expression{

  /**
   * This class represents an OutputStream that discards anything that gets written to it. Like /dev/null but cross-platform.
   */
  private static class NullOutputStream extends OutputStream{
    @Override
    public void write(int b){}
  }

  /**
   * Returns a string representation of the expression.
   * This is abstract so that any concrete type of expression is forced to return a meaningful representation for logging purposes.
   * @return A string representation of the expression. This is implemented in different ways in each subclass.
   */
  @Override
  public abstract String toString();

  /**
   * Evaluates this expression. This is used internally by the library to correctly manage logging of each step.
   * The correct way to evaluate an expression from outside the package is by using {@link #eval()}, {@link #eval(Writer)} or {@link #eval(OutputStream)}.
   * @param logWriter A {@link PrintWriter} to log the steps done.
   * This method doesn't actually log anything (this is done by {@link #eval(PrintWriter)});
   * the parameter must be passed to the underlying calls to {@link Expression#eval(PrintWriter)} of the sub-expressions (if any) to properly log each step.
   * @return The computed value of this expression.
   */
  protected abstract double evalExpr(PrintWriter logWriter);

  /**
   * Returns a string representing the log entry corresponding to the evaluation of this expression.
   * Can be overridden by expression that need custom log messages (see for example {@link ConstExpression#getEvalMsg(double)});
   * @param val the value of the expression.
   * This method doesn't obtain it by calling {@link #eval()} to avoid computing the value twice (one time to get the result and the other for logging).
   * @return A string built by concatenating the string representation of the expression, the " evaluates to " literal, and the value (<code>val</code> parameter).
   */
  public String getEvalMsg(double val){
    return toString() + " evaluates to " + val + System.getProperty("line.separator");
  }

  /**
   * Evaluates this expression and logs the steps done to the specified {@link PrintWriter}.
   * @param logWriter A {@link PrintWriter} to write the evaluation steps to.
   * @return The computed value of this expression.
   */
  protected final double eval(PrintWriter logWriter){
    double val = evalExpr(logWriter);
    logWriter.print(getEvalMsg(val));
    logWriter.flush();
    return val;
  }

  /**
   * Evaluates this expression and logs the steps done to the specified {@link Writer}.
   * @param logWriter A {@link Writer} to write the evaluation steps to.
   * @return The computed value of this expression.
   */
  public final double eval(Writer logWriter){
    return eval(new PrintWriter(logWriter));
  }

  /**
   * Evaluates this expression and logs the steps done to the specified {@link OutputStream}.
   * @param logStream An {@link OutputStream} to write the evaluation steps to.
   * @return The computed value of this expression.
   */
  public final double eval(OutputStream logStream){
    return eval(new OutputStreamWriter(logStream));
  }

  /**
   * Evaluates this expression without logging the step dones.
   * @return The computed value of this expression.
   */
  public final double eval(){
    return eval(new NullOutputStream());
  }

  /**
   * Parses the given {@link String} into an {@link Expression} object without logging the steps done.
   * @param expr The string representation of the expression to parse.
   * @return An {@link Expression} object representing the expression given as string.
   * @throws ExpressionException if the parsing process failed, i.e. the given string isn't a well-formed expression.
   */
  public static final Expression parse(String expr) throws ExpressionException{
    return parse(expr, new NullOutputStream());
  }

  /**
   * Parses the given {@link String} into an {@link Expression} object and logs the steps done to the specified {@link OutputStream}.
   * @param expr The string representation of the expression to parse.
   * @param logStream An {@link OutputStream} to write the parsing steps to.
   * @return An {@link Expression} object representing the expression given as string.
   * @throws ExpressionException if the parsing process failed, i.e. the given string isn't a well-formed expression.
   */
  public static final Expression parse(String expr, OutputStream logStream) throws ExpressionException{
    return parse(expr, new OutputStreamWriter(logStream));
  }

  /**
   * Parses the given {@link String} into an {@link Expression} object and logs the steps done to the specified {@link Writer}.
   * @param expr The string representation of the expression to parse.
   * @param logWriter A {@link Writer} to write the parsing steps to.
   * @return An {@link Expression} object representing the expression given as string.
   * @throws ExpressionException if the parsing process failed, i.e. the given string isn't a well-formed expression.
   */
  public static final Expression parse(String expr, Writer logWriter) throws ExpressionException{
    return parseRange(expr, 0, expr.length(), new PrintWriter(logWriter));
  }

  /**
   * Parses a range of the given {@link String} into an {@link Expression} object and logs the steps done to the specified {@link PrintWriter}.
   * @param expr The string representation of the expression to parse.
   * @param begin Index of the first character to parse.
   * @param end Index of the first character to ignore (the first in the string after the last in the parsed substring).
   * @param logWriter A {@link PrintWriter} to write the parsing steps to.
   * @return An {@link Expression} object representing the expression given as string.
   * @throws ExpressionException if the parsing process failed, i.e. the given string isn't a well-formed expression.
   */
  private static final Expression parseRange(String expr, int begin, int end, PrintWriter logWriter) throws ExpressionException{
    int i = begin;
    ExpressionList subExpressions = new ExpressionList();
    String currentNumber = "";
    while(i < end){
      char c = expr.charAt(i);
      if(c == '.' || Character.isDigit(c)){
        currentNumber += c;
      }else if(c == '('){
        int closedIndex = findCloseParenthesis(expr, i);
        subExpressions.addItem(parseRange(expr, i + 1, closedIndex, logWriter));
        i = closedIndex;
      }else if(BinaryOpExpression.isAllowedOperator(c)){
        if(currentNumber.length() != 0){
          subExpressions.addItem(new ConstExpression(Double.parseDouble(currentNumber)));
          currentNumber = "";
        }
        subExpressions.addOperator(c);
      }else if(c == ')'){ //of a closed parenthesis is foun here instead that in findCloseParenthesis, it means there are more closed than opened ones
        throw new MismatchedParenthesisException();
      }else if(c != ' '){ //spaces are allowed and ignored
        throw new UnknownCharException(expr, i);
      }
      i++;
    }
    if(currentNumber.length() != 0)
      subExpressions.addItem(new ConstExpression(Double.parseDouble(currentNumber)));
    Expression result = subExpressions.simplify();
    logWriter.println(expr.substring(begin, end) + " can be rewritten as " + result.toString());
    logWriter.flush();
    return result;
  }

  /**
   * Given a string and the index to a open parenthesis in it, computes the respective closed parenthesis.
   * @param s The string containing the parenthesis.
   * @param openIndex The index of the opened parenthesis character in the string (if s.charAt(openIndex)!='(', the call will result in undefined behaviour).
   * @return The index of the corresponding closed parenthesis.
   * @throws MismatchedParenthesisException if the numbers of opened and closed parenthesis in the string after <code>openIndex</code> don't match.
   */
  private static final int findCloseParenthesis(String s, int openIndex) throws MismatchedParenthesisException{
    int openedCount = 1;
    for(int i = openIndex + 1; i < s.length(); i++){
      if(s.charAt(i) == '(')
        openedCount++;
      else if(s.charAt(i) == ')')
        openedCount--;
      if(openedCount == 0)
        return i;
    }
    throw new MismatchedParenthesisException();
  }

}
