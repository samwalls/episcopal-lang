package compiler;

import ast.program.Arguments;
import ast.program.Program;
import ast.program.Query;
import ast.program.definition.FunctionDefinition;
import ast.program.expression.FunctionCall;
import ast.program.expression.Identifier;
import ast.program.expression.Let;
import ast.program.expression.Observation;
import ast.program.expression.binop.AddOp;
import ast.program.expression.binop.EqualsOp;
import ast.program.expression.binop.GreaterThanOp;
import ast.program.expression.binop.LessThanOp;
import ast.program.expression.constant.FloatConstant;
import ast.program.expression.constant.IntConstant;
import ast.program.expression.distribution.FlipDistribution;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static sun.misc.PostVMInitHook.run;

public class CompilerTestGenerator {

    private static final String OUTPUT_DIRECTORY = "episcopal_out";

    private static final String JASMIN = "lib/jasmin-2.4/jasmin.jar";

    private static final Program[] tests = new Program[] {
            new Program(
                    // program test1 = add 3 4
                    new Identifier("test1"), new FunctionCall(new Identifier("add"), Arrays.asList(new FloatConstant(3f), new FloatConstant(4f))),
                    Arrays.asList(
                            // query add a b = ...
                            new Query(new Identifier("add"), new Arguments(Arrays.asList(new Identifier("a"), new Identifier("b"))), Arrays.asList(
                                    new AddOp(new Identifier("a"), new Identifier("b"))
                            ))
                    )
            ),

            new Program(
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
            ),

            new Program(
                    // program test3 = 42
                    new Identifier("test3"), new IntConstant(42)
            ),

            new Program(
                    // program test4 = Flip 0.5
                    new Identifier("test4"), new FlipDistribution(new FloatConstant(0.5f))
            ),

            new Program(
                    // program test5 = observe (1 = 2) in 1
                    new Identifier("test5"), new Observation(new EqualsOp(new IntConstant(1), new IntConstant(2)), new FloatConstant(1f))
            ),

            new Program(
                    // program test6 = observe (2 > 1) in 1
                    new Identifier("test6"), new Observation(new GreaterThanOp(new IntConstant(2), new IntConstant(1)), new FloatConstant(1f))
            )
    };

    /**
     * Compiles all test programs specified here, and write them to the output directory.
     * @param args command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        File outputDirectory = new File(OUTPUT_DIRECTORY);
        // delete the output directory
        System.out.println("deleting output directory");
        if (outputDirectory.exists()) {
            deleteDir(outputDirectory);
        }
        System.out.println("creating output directory");
        File tmp = new File(OUTPUT_DIRECTORY + "/tmp");
        if (!tmp.mkdirs())
            throw new RuntimeException("could not create output directory " + tmp.getPath());
        System.out.println("compiling helper classes...");
        compileHelpers();
        // compile the various test sources defined here
        System.out.println("starting assembly...");
        for (int i = 0; i < tests.length; i++) {
            Compiler c = new Compiler(tests[i]);
            String asm = c.run();
            String newFileName = OUTPUT_DIRECTORY + "/tmp/Episcopal_" + tests[i].id.value + ".j";
            System.out.println("writing to file \"" + newFileName + "\"");
            PrintWriter out = new PrintWriter(newFileName);
            out.write(asm);
            out.flush();
            out.close();
            System.out.println("compiling " + newFileName);
            compileJasmin(newFileName);
        }
        System.out.println("finished compilation");
    }

    /**
     * This function is copied directly from the top answer (as of 3/11/2017) on this stack overflow question
     * https://stackoverflow.com/questions/12835285/create-directory-if-exists-delete-directory-and-its-content-and-create-new-one
     * @param dir the directory to delete recursively
     * @return true if the directory is successfully deleted
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private static void compileHelpers() throws IOException, InterruptedException {
        Runtime cmd = Runtime.getRuntime();
        cmd.exec("javac -cp src/main/java src/main/java/compiler/helpers/Episcopal.java").waitFor();
        Path classPath = new File(OUTPUT_DIRECTORY + "/compiler/helpers/").toPath();
        if (!classPath.toFile().mkdirs())
            throw new RuntimeException("could not move compiled helper class to classpath \"" + classPath.toFile().getPath() + "\"");
        File helper = new File("src/main/java/compiler/helpers/Episcopal.class");
        Files.copy(helper.toPath(), classPath.resolve(helper.getName()));
    }

    private static void compileJasmin(String file) throws IOException, InterruptedException {
        Runtime cmd = Runtime.getRuntime();
        cmd.exec("java -jar " + JASMIN + " " + file + " -d " + OUTPUT_DIRECTORY).waitFor();
    }
}
