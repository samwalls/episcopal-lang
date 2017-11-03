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
            new Identifier("test2"), new FunctionCall(new Identifier("callsub"), Arrays.asList(new IntConstant(3))),
            Arrays.asList(
                    // query callsub = ...
                    new Query(new Identifier("callsub"), new Arguments(Arrays.asList(new Identifier("x"))), Arrays.asList(
                            // let f x = x + 2 in...
                            new Let(Arrays.asList(new FunctionDefinition(new Identifier("f"), new Arguments(Arrays.asList(new Identifier("x"))), Arrays.asList(
                                new AddOp(new Identifier("x"), new IntConstant(2))
                            ))),
                            // f (x + 3)
                            new FunctionCall(new Identifier("f"), Arrays.asList(
                                    new AddOp(new Identifier("x"), new IntConstant(3))
                            )))
                    ))
            )
    );

    private static Program test3 = new Program(
            // program test1 = add 3 4
            new Identifier("test3"), new FunctionCall(new Identifier("addMultiple"), Arrays.asList()),
            Arrays.asList(
                    // query callsub = ...
                    new Query(new Identifier("addMultiple"), new Arguments(), Arrays.asList(
                            // let x = 2
                            new Let(Arrays.asList(new FunctionDefinition(new Identifier("f"), new Arguments(Arrays.asList(new Identifier("x"))), Arrays.asList(
                                    new AddOp(new Identifier("x"), new IntConstant(2))
                            ))),
                            // f (x + 3)
                            new FunctionCall(new Identifier("f"), Arrays.asList(
                                    new AddOp(new Identifier("x"), new IntConstant(3))
                            )))
                    ))
            )
    );

    public static void main(String[] args) throws Exception {
        // compile the various test sources defined here
        compiler = new Compiler(test1);
        compiler.run();
        compiler = new Compiler(test2);
        compiler.run();
    }
}
