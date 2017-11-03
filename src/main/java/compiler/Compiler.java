package compiler;

import ast.program.Program;
import ir.translation.EnvNode;
import ir.translation.IRTranslator;
import ir.translation.LabelNotFoundException;
import ir.translation.Method;

import java.util.ArrayList;

/**
 * All compiler steps are orchestrated from this class.
 */
public class Compiler {

    public static final String MAIN = "episcopal_main";
    public static final String MAIN_SIGNATURE = MAIN + "()[F";

    public static final String BUILTIN_OBSERVE = "builtin_observe";
    public static final String BUILTIN_OBSERVE_SIGNATURE = BUILTIN_OBSERVE + "([F)V";

    public static final String BUILTIN_FINISH = "builtin_finish";
    public static final String BUILTIN_FINISH_SIGNATURE = BUILTIN_FINISH + "([F)[F";

    private Program program;

    private IRTranslator translator;

    private String programName, className;

    private JasminBuilder jasmin;

    public Compiler(Program program) {
        this.program = program;
        this.jasmin = new JasminBuilder();
        this.programName = program.id.value;
        this.className = "Episcopal_" + programName;
    }

    public String run() throws Exception {
        System.out.println("building program: \"" + programName + "\"");
        this.translator = new IRTranslator();
        this.jasmin = new JasminBuilder();
        translator.visit(program);
        emitAssembler();
        return jasmin.toString();
    }

    private void emitAssembler() throws LabelNotFoundException {
        jasmin.header(null, "Episcopal_" + programName);
        for (String methodName : translator.getMethods().keySet())
            emitMethod(translator.getMethods().get(methodName));
        emitMethod(translator.getMainMethod());
        jasmin.newline();
        emitMainMethod();
        jasmin.newline();
    }

    private void emitMainMethod() {
        jasmin.methodHeader("main([Ljava/lang/String;)V");
        jasmin.methodStackLimit(2);
        jasmin.methodLocalsLimit(2);
        jasmin.invokestatic(className + "/" + MAIN_SIGNATURE);
        jasmin.methodReturn();
        jasmin.methodEnd();
    }

    private void emitMethod(Method method) throws LabelNotFoundException {
        ArrayList<JasminBuilder.ArgumentType> argumentSignature = new ArrayList<>();
        // all arguments are float arrays, all return values are float arrays
        for (String label : method.environment.keySet()) {
            EnvNode node = method.environment.get(label);
            // variables in the environment are denoted by a child which links to a value the same as it's key
            // (and where linked node has no children)
            if (node.value.equals(label) && node.keySet().size() <= 0)
                argumentSignature.add(JasminBuilder.ArgumentType.FLOAT_ARRAY);
        }
        JasminBuilder.ArgumentType outputSignature = JasminBuilder.ArgumentType.FLOAT_ARRAY;
        jasmin.methodHeader(method.name, argumentSignature, outputSignature);
        // generate jasmin instructions for the method here
        new EpiscopalMuncher(jasmin, method, className).munchMethod();
        jasmin.methodEnd();
        jasmin.newline();
    }
}
