package compiler;

import ast.program.Program;
import ir.translation.EnvNode;
import ir.translation.IRTranslator;
import ir.translation.Method;

import java.util.ArrayList;
import java.util.Map;

/**
 * All compiler steps are orchestrated from this class.
 */
public class Compiler {

    private Program program;

    private IRTranslator translator;

    private String programName;

    private JasminBuilder jasmin;

    public Compiler(Program program) {
        this.program = program;
        this.jasmin = new JasminBuilder();
        this.programName = program.id.value;
    }

    public void run() throws Exception {
        this.translator = new IRTranslator();
        this.jasmin = new JasminBuilder();
        emitAssembler(translator.visit(program));
        String result = jasmin.toString();
    }

    private void emitAssembler(Map<String, Method> methods) {
        jasmin.header(programName, null, "Episcopal_" + programName);
        for (String methodName : methods.keySet())
            emitMethod(methods.get(methodName));
    }

    private void emitMethod(Method method) {
        ArrayList<JasminBuilder.ArgumentType> argumentSignature = new ArrayList<>();
        // all arguments are float arrays, all return values are float arrays
        for (String label : method.environment.keySet()) {
            EnvNode node = method.environment.get(label);
            // variables in the environment are denoted by a child which links to a value the same as it's key
            if (node.value.equals(label))
                argumentSignature.add(JasminBuilder.ArgumentType.FLOAT_ARRAY);
        }
        JasminBuilder.ArgumentType outputSignature = JasminBuilder.ArgumentType.FLOAT_ARRAY;
        jasmin.methodSignature(method.name, argumentSignature, outputSignature);
        // generate jasmin instructions for the method here
        emitMethodContent(method);
        jasmin.methodEnd();
        jasmin.newline();
    }

    private void emitMethodContent(Method method) {

    }
}
