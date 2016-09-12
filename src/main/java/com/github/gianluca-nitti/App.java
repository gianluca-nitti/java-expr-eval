package com.github.gianlucanitti;

import com.github.gianlucanitti.javaexpreval.*;

/**
 * Hello world!
 *
 */
public class App{

    public static void main(String[] args){
        /*if(args.length != 1){
          System.err.println("Please specify expression as command line argument.");
          System.exit(1);
        }*/
        try{
          Expression expr = new BinaryOpExpression(new BinaryOpExpression(new ConstExpression(3), '+', new ConstExpression(2)), '*', new ConstExpression(4));
          System.out.println(expr.eval());
        }catch(ExpressionException e){
          e.printStackTrace();
        }
    }

}
