# java-expr-eval [![Build Status](https://travis-ci.org/gianluca-nitti/java-expr-eval.svg?branch=master)](https://travis-ci.org/gianluca-nitti/java-expr-eval) [![Release](https://jitpack.io/v/gianluca-nitti/java-expr-eval.svg)](https://jitpack.io/#gianluca-nitti/java-expr-eval)
Simple Java library and command line tool to evaluate math expressions.

# Features
* Supports +, -, *, /, ^ operators with correct precedence (first ^ is evaluated, followed by * and / and then + and -).
* Supports logging the parsing/evaluation steps done to a `Writer` or an `OutputStream`.
* Multiple custom exceptions to accurately report syntax errors in expressions.

#Documentation
Full javadoc is available [here](https://jitpack.io/com/github/gianluca-nitti/java-expr-eval/-SNAPSHOT/javadoc/overview-summary.html).
This is always updated from latest commit. See releases page for javadoc of specific versions.

#Using as command line tool
Example:
```
java -jar javaexpreval-1.0.jar "4+(5^2)" -v
```
Replace 4+(5^2) with the expression you want to evaluate; you may also need to adjust the JAR filename. Remove the `-v` switch if you don't want the steps to be logget to stdout.
**Warning:** The quotes may be necessary or not depending on your shell. For example, on Windows cmd.exe, ^ is a special shell character and won't be passed to the JVM (and to the expression parser), so for example if you write 2^3 the application will read 23. This is easily solved using quotes ("2^3").

#Code example
Code:
```
try {
  Expression expr = Expression.parse("3.2+4*5-6^((15+(-5)*2)-3)", System.out); //Log parsing steps to System.out (no 2nd parameter can be specified if no logging is desidered)
  double result = expr.eval(System.out); //Log evaluation steps to System.out (same here)
  System.out.println("Result = " + result);
}catch(ExpressionException e){
  e.printStackTrace();
}
```
Output:
```
-5 can be rewritten as -5.0
15+(-5)*2 can be rewritten as (15.0+(-5.0*2.0))
(15+(-5)*2)-3 can be rewritten as ((15.0+(-5.0*2.0))-3.0)
3.2+4*5-6^((15+(-5)*2)-3) can be rewritten as ((3.2+(4.0*5.0))-(6.0^((15.0+(-5.0*2.0))-3.0)))
(4.0*5.0) evaluates to 20.0
(3.2+(4.0*5.0)) evaluates to 23.2
(-5.0*2.0) evaluates to -10.0
(15.0+(-5.0*2.0)) evaluates to 5.0
((15.0+(-5.0*2.0))-3.0) evaluates to 2.0
(6.0^((15.0+(-5.0*2.0))-3.0)) evaluates to 36.0
((3.2+(4.0*5.0))-(6.0^((15.0+(-5.0*2.0))-3.0))) evaluates to -12.8
Result = -12.8
```

#Installing the library to your project
This project is built and published with [jitpack](https://jitpack.io).
If you use maven, add this repository
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
and this dependency (adjust the version if needed, according to the releases page; a commit hash can be used too)
```
<dependency>
    <groupId>com.github.gianluca-nitti</groupId>
    <artifactId>java-expr-eval</artifactId>
    <version>1.0</version>
</dependency>
```
See the [jitpack page](https://jitpack.io/#gianluca-nitti/java-expr-eval/) for instructions for other build systems (gradle, sbt or leiningen).

#Building from source/contributing
This library uses [Apache Maven](https://maven.apache.org/) as build system.
To build you simply need to `mvn package` in the project root (where you cloned this repository). This will compile, run tests, and put the JAR in the `target/` subdirectory.
You can run `mvn install` if you want to install to your local maven repository.