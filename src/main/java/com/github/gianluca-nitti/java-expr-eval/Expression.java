package com.github.gianlucanitti.javaexpreval;

public abstract class Expression{

  public abstract double eval();

  public static Expression parse(String expr) throws ExpressionException{
    return parseRange(expr, 0, expr.length());
  }

  private static Expression parseRange(String expr, int begin, int end) throws ExpressionException{
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
        subExpressions.addItem(parseRange(expr, i + 1, closedIndex - 1));
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
    return subExpressions.simplify();
  }

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
