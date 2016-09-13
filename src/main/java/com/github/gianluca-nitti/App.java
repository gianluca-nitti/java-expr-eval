package com.github.gianlucanitti;

import com.github.gianlucanitti.javaexpreval.*;

/**
 * Hello world!
 *
 */
public class App{

    public static void main(String[] args){
        if(args.length != 1){
          System.err.println("Please specify expression as command line argument.");
          System.exit(1);
        }
        try{
          System.out.println(Expression.parse(args[0]).eval());
        }catch(ExpressionException e){
          e.printStackTrace();
        }
    }

}
