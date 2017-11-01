package compiler;

import ast.program.Arguments;
import ast.program.Program;
import ast.program.Query;
import ast.program.definition.FunctionDefinition;
import ast.program.expression.FunctionCall;
import ast.program.expression.Identifier;
import ast.program.expression.Let;
import ast.program.expression.binop.AddOp;
import ast.program.expression.binop.MulOp;
import ast.program.expression.constant.FloatConstant;
import ast.program.expression.constant.IntConstant;

import java.util.ArrayList;
import java.util.Arrays;

public class CompilerTestGenerator {

    private static Compiler compiler;

    private static Program test1 = new Program(
            // program test1 = add 3 4
            new Identifier("test1"), new FunctionCall(new Identifier("add"), Arrays.asList(new FloatConstant(3f), new FloatConstant(4f))),
            Arrays.asList(
                    // query add a b = ...
                    new Query(new Identifier("add"), new Arguments(Arrays.asList(new Identifier("a"), new Identifier("b"))), Arrays.asList(
                            new AddOp(new Identifier("a"), new Identifier("b"))
                    ))
            )
    );

    private static Program test2 = new Program(
            // program test1 = add 3 4
            new Identifier("test1"), new FunctionCall(new Identifier("callsub"), new ArrayList<>()),
            Arrays.asList(
                    // query callsub = ...
                    new Query(new Identifier("callsub"), new Arguments(), Arrays.asList(
                            // let x = 42 in...
                            // (define a function called "x" which returns 42)
                            new Let(Arrays.asList(new FunctionDefinition(new Identifier("x"), new Arguments(), Arrays.asList(new IntConstant(42)))),
                            // let f x = 2 * x in...
                            // (outer function "x" is shadowed by this inner argument "x")
                            new Let(Arrays.asList(new FunctionDefinition(new Identifier("f"), new Arguments(Arrays.asList(new Identifier("x"))), Arrays.asList(
                                new MulOp(new IntConstant(2), new Identifier("x"))
                            ))),
                            // f x
                            new FunctionCall(new Identifier("f"), Arrays.asList(new Identifier("x"))))
                    ))
            ))
    );

    public static void main(String[] args) throws Exception {
        // compile the various test sources defined here
        compiler = new Compiler(test1);
        compiler.run();
        compiler = new Compiler(test2);
        compiler.run();
    }
}
