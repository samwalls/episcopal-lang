package compiler;

import ast.program.Program;
import ir.statement.Statement;
import ir.translation.EnvNode;
import ir.translation.IRTranslator;

import java.util.Map;

/**
 * All compiler steps are orchestrated from this class.
 */
public class Compiler {

    private Program program;

    private IRTranslator translator;

    public Compiler(Program program) {
        this.program = program;
    }

    public void run() throws Exception {
        this.translator = new IRTranslator();
        Map<EnvNode, Statement> methods = translator.visit(program);
        // TODO Jasmin assembler generation
    }
}
