package com.github.gianlucanitti.javaexpreval;

import java.io.Writer;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import static com.github.gianlucanitti.javaexpreval.LocalizationHelper.*;

/**
 * Represents a generic arithmetical expression.
 */
public abstract class Expression{

  /**
   * Returns a string representation of the expression.
   * This is abstract so that any concrete type of expression is forced to return a meaningful representation for logging purposes.
   * @return A string representation of the expression. This is implemented in different ways in each subclass.
   */
  @Override
  public abstract String toString();

  /**
   * Returns the sub-expressions underlying this expression. This is used to recursively walk a tree of expressions, for example to perform variable substitution.
   * @return An array of the {@link Expression}s underlying this one (if any; an empty array of Expressions otherwise).
   */
  public abstract Expression[] getSubExpressions();

  /**
   * Evaluates this expression. This is used internally by the library to correctly manage logging of each step.
   * The correct way to evaluate an expression from outside the package is by using {@link #eval()}, {@link #eval(ExpressionContext)}, {@link #eval(Writer)}, {@link #eval(ExpressionContext, Writer)}.
   * This method doesn't actually log anything (this is done by {@link #eval(ExpressionContext, PrintWriter)});
   * the parameter is passed to the underlying calls to {@link Expression#eval(ExpressionContext, PrintWriter)} of the sub-expressions (if any) to properly log each step.
   * @param context The {@link ExpressionContext} to evaluate the expression in.
   * @param logWriter A {@link PrintWriter} to log the steps done.
   * @throws UndefinedException if the expression can't be evaluated because it contains a symbol (function or variable) not defined in the context.
   * @return The computed value of this expression.
   */
  protected abstract BigDecimal evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException;

  /**
   * Returns a string representing the log entry corresponding to the evaluation of this expression.
   * Can be overridden by expression that need custom log messages (see for example {@link ConstExpression#getEvalMsg(BigDecimal)});
   * @param val the value of the expression.
   * This method doesn't obtain it by calling {@link #eval()} to avoid computing the value twice (one time to get the result and the other for logging).
   * @return A string built by concatenating the string representation of the expression, the " evaluates to " literal, and the value (<code>val</code> parameter).
   */
  public String getEvalMsg(BigDecimal val){
    return getMessage(Message.EVAL_STEP, toString(), val.toString()) + System.getProperty("line.separator");
  }

  /**
   * Binds the variables with the specified names to the specified values, according to their order.
   * @param varNames An array containing the names of the variables to bind.
   * @param values An array containing the values to bind the variables to.
   * @throws IllegalArgumentException if <code>varNames</code> and <code>values</code> have different lengths.
   */
  public void bindVariables(String[] varNames, BigDecimal[] values){
    if(varNames.length != values.length)
      throw new IllegalArgumentException();
    if(this instanceof VariableExpression) {
      for (int i = 0; i < varNames.length; i++)
        if (((VariableExpression) this).getName().equals(varNames[i]))
          ((VariableExpression) this).bind(new ConstExpression(values[i]));
    }else
      for(Expression var: getSubExpressions())
        var.bindVariables(varNames, values);
  }

  /**
   * Evaluates this expression and logs the steps done to the specified {@link PrintWriter};
   * the result is returned and stored in the "ans" variable in the context.
   * @param context The {@link ExpressionContext} to evaluate the expression in.
   * @param logWriter A {@link PrintWriter} to write the evaluation steps to.
   * @throws UndefinedException if the expression can't be evaluated because it contains a symbol (function or variable) not defined in the context.
   * @return The computed value of this expression.
   */
  protected final BigDecimal eval(ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
    BigDecimal val = evalExpr(context, logWriter);
    logWriter.print(getEvalMsg(val));
    logWriter.flush();
    try {
      context.setVariable("ans", val);
    }catch(ExpressionException ex){
      logWriter.println(getMessage(Message.FAILED_STORE_RESULT, ex.getMessage()));
    }
    return val;
  }

  /**
   * Evaluates this expression and logs the steps done to the specified {@link Writer}.
   * @param context The {@link ExpressionContext} to evaluate the expression in.
   * @param logWriter A {@link Writer} to write the evaluation steps to.
   * @throws UndefinedException if the expression can't be evaluated because it contains a symbol (function or variable) not defined in the context.
   * @return The computed value of this expression.
   */
  public final BigDecimal eval(ExpressionContext context, Writer logWriter) throws UndefinedException{
    return eval(context, new PrintWriter(logWriter));
  }

  /**
   * Evaluates this expression in the specified context without logging the steps done.
   * @param context The {@link ExpressionContext} to evaluate the expression in.
   * @throws UndefinedException if the expression can't be evaluated because it contains a symbol (function or variable) not defined in the context.
   * @return The computed value of this expression.
   */
  public final BigDecimal eval(ExpressionContext context) throws UndefinedException{
    return eval(context, NullOutputStream.getWriter());
  }

  /**
   * Evaluates this expression in an empty context and logs the steps done to the specified {@link Writer}.
   * @param logWriter A {@link Writer} to write the evaluation steps to.
   * @throws UndefinedException if the expression can't be evaluated because it contains a symbol (function or variable) not defined in the context.
   * @return The computed value of this expression.
   */
  public final BigDecimal eval(Writer logWriter) throws UndefinedException{
    return eval(new ExpressionContext(), logWriter);
  }

  /**
   * Evaluates this expression in an empty context without logging the steps done.
   * @throws UndefinedException if the expression can't be evaluated because it contains a symbol (function or variable) not defined in the context.
   * @return The computed value of this expression.
   */
  public final BigDecimal eval() throws UndefinedException{
    return eval(new ExpressionContext());
  }

  /**
   * Parses the given {@link String} into an {@link Expression} object without logging the steps done.
   * @param expr The string representation of the expression to parse.
   * @return An {@link Expression} object representing the expression given as string.
   * @throws ExpressionException if the parsing process failed, i.e. the given string isn't a well-formed expression.
   */
  public static final Expression parse(String expr) throws ExpressionException{
    return parse(expr, NullOutputStream.getWriter());
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
  private static Expression parseRange(String expr, int begin, int end, PrintWriter logWriter) throws ExpressionException{
    int i = begin;
    ExpressionList subExpressions = new ExpressionList();
    boolean negate = false;
    boolean expectExpression;
    while(i < end){
      expectExpression = !subExpressions.isExpectingOperator();
      char c = expr.charAt(i);
      Expression itemToAdd = null;
      if(expectExpression && (c == '+' || c == '-')) {
        if(c == '-')
          negate = !negate;
        i++;
      }else if(c == '.' || Character.isDigit(c)) {
        String number = "";
        while (c == '.' || Character.isDigit(c)) {
          number += c;
          i++;
          if (i >= end)
            break;
          c = expr.charAt(i);
        }
        itemToAdd = new ConstExpression(new BigDecimal(number));
      }else if (NamedSymbolExpression.isValidSymbolFirstChar(c)){
        String symName = "";
        while(NamedSymbolExpression.isValidSymbolChar(c)){
          symName += c;
          i++;
          if(i >= end)
            break;
          c = expr.charAt(i);
        }
        if(i < end && expr.charAt(i) == '(') { //it's a function
          int closedIndex = findCloseParenthesis(expr, i);
          int argBegin = i + 1;
          int argEnd;
          ArrayList<Expression> args = new ArrayList<Expression>();
          do{
            argEnd = expr.indexOf(',', argBegin);
            if(argEnd == -1 || argEnd > closedIndex)
              argEnd = closedIndex;
            if(argBegin != argEnd)
              args.add(parseRange(expr, argBegin, argEnd, logWriter));
            argBegin = argEnd + 1;
          }while(argEnd != closedIndex);
          itemToAdd = new FunctionExpression(symName, args.toArray(new Expression[args.size()]));
          i = closedIndex + 1;
        }else //it's a variable
          itemToAdd = new VariableExpression(symName);
      }else if(c == '('){
        int closedIndex = findCloseParenthesis(expr, i);
        itemToAdd = parseRange(expr, i + 1, closedIndex, logWriter);
        i = closedIndex + 1;
      }else if(BinaryOpExpression.isAllowedOperator(c)) {
        subExpressions.addOperator(c);
        i++;
      }else if(c == ')'){ //if a closed parenthesis is found here instead that in findCloseParenthesis, it means there are more closed than opened ones
        throw new MismatchedParenthesisException();
      }else if(c == ' ') { //spaces are allowed and ignored
        i++;
      }else{
        throw new UnknownCharException(c);
      }
      if(itemToAdd != null) {
        subExpressions.addItem(negate ? new NegatedExpression(itemToAdd) : itemToAdd);
        negate = false;
      }
    }
    Expression result = subExpressions.simplify();
    String userStr = expr.substring(begin, end);
    String parsedStr = result.toString();
    if(!userStr.equals(parsedStr)) { //log only if the string representation of the parsed expression is different from the input string
      logWriter.println(getMessage(Message.REWRITE_STEP, userStr, parsedStr));
      logWriter.flush();
    }
    return result;
  }

  /**
   * Given a string and the index to a open parenthesis in it, computes the respective closed parenthesis.
   * @param s The string containing the parenthesis.
   * @param openIndex The index of the opened parenthesis character in the string (if s.charAt(openIndex)!='(', the call will result in undefined behaviour).
   * @return The index of the corresponding closed parenthesis.
   * @throws MismatchedParenthesisException if the numbers of opened and closed parenthesis in the string after <code>openIndex</code> don't match.
   */
  private static int findCloseParenthesis(String s, int openIndex) throws MismatchedParenthesisException{
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
