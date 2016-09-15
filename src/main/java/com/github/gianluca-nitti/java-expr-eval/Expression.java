package com.github.gianlucanitti.javaexpreval;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public abstract class Expression{

  private static class NullOutputStream extends OutputStream{
    @Override
    public void write(int b){}
  }

  @Override
  public abstract String toString();

  protected abstract double evalExpr(PrintWriter logWriter);

  public String getEvalMsg(double val){
    return toString() + " evaluates to " + val + System.getProperty("line.separator");
  }

  protected final double eval(PrintWriter logWriter){
    double val = evalExpr(logWriter);
    logWriter.print(getEvalMsg(val));
    logWriter.flush();
    return val;
  }

  public final double eval(Writer logWriter){
    return eval(new PrintWriter(logWriter));
  }

  public final double eval(OutputStream logStream){
    return eval(new OutputStreamWriter(logStream));
  }

  public final double eval(){
    return eval(new NullOutputStream());
  }

  public static final Expression parse(String expr) throws ExpressionException{
    return parse(expr, new NullOutputStream());
  }

  public static final Expression parse(String expr, OutputStream logStream) throws ExpressionException{
    return parse(expr, new OutputStreamWriter(logStream));
  }

  public static final Expression parse(String expr, Writer logWriter) throws ExpressionException{
    return parseRange(expr, 0, expr.length(), new PrintWriter(logWriter));
  }

  private static final Expression parseRange(String expr, int begin, int end, PrintWriter logWriter) throws ExpressionException{
    int i = begin;
    ExpressionList subExpressions = new ExpressionList();
    String currentNumber = "";
    while(i < end){
      char c = expr.charAt(i);
      if(c == ' ')
        continue;
      if(c == '.' || Character.isDigit(c)){ //TODO use system decimal separator
        currentNumber += c;
      }
      if(c == '('){
        int closedIndex = findCloseParenthesis(expr, i);
        subExpressions.addItem(parseRange(expr, i + 1, closedIndex, logWriter));
        i = closedIndex;
      }else if(BinaryOpExpression.isAllowedOperator(c)){
        if(currentNumber.length() != 0){
          subExpressions.addItem(new ConstExpression(Double.parseDouble(currentNumber)));
          currentNumber = "";
        }
        subExpressions.addOperator(c);
      }else{
        //throw
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
