package compiler;

import java.util.List;

/**
 * A class for building the relevant jasmin assembler strings in a slightly more repeatable, type-safe manner. This
 * does not guarantee the safety or correctness of the output jasmin of course.
 */
public class JasminBuilder {

    public enum ArgumentType {
        FLOAT,
        FLOAT_ARRAY;

        public String value() {
            switch (this) {
                case FLOAT:
                    return "F";
                case FLOAT_ARRAY:
                    return "[F";
                default:
                    // this should never happen
                    return "?";
            }
        }
    }

    private StringBuilder output;

    public JasminBuilder() {
        output = new StringBuilder();
    }

    public JasminBuilder newline() {
        output.append("\n");
        return this;
    }

    public JasminBuilder header(String pack, String clazz) {
        if (pack != null) {
            output.append(".class public ").append(pack).append("/").append(clazz);
            newline();
        } else {
            output.append(".class public ").append(clazz);
            newline();
        }
        output.append(".super ").append("java/lang/Object");
        newline();
        return newline();
    }

    public JasminBuilder methodSignature(String name, List<ArgumentType> inputSignature, ArgumentType outputSignature) {
        String fullyQualifiedName = name + "(";
        for (ArgumentType type : inputSignature)
            fullyQualifiedName += type.value();
        fullyQualifiedName += ")" + outputSignature.value();
        output.append(fullyQualifiedName);
        return this;
    }

    public JasminBuilder methodHeader(String fullyQualifiedName) {
        output.append(".method public static ").append(fullyQualifiedName);
        return newline();
    }

    public JasminBuilder methodHeader(String name, List<ArgumentType> inputSignature, ArgumentType outputSignature) {
        output.append(".method public static ");
        methodSignature(name, inputSignature, outputSignature);
        return newline();
    }

    public JasminBuilder methodEnd() {
        output.append(".end method");
        return newline();
    }

    public JasminBuilder methodStackLimit(int limit) {
        output.append("\t.limit stack ").append(limit);
        return newline();
    }

    public JasminBuilder methodLocalsLimit(int limit) {
        output.append("\t.limit locals ").append(limit);
        return newline();
    }

    // local variable instructions

    public JasminBuilder ret(int var) {
        return localVariableInstruction("ret", var);
    }

    public JasminBuilder aload(int var) {
        return localVariableInstruction("aload", var);
    }

    public JasminBuilder astore(int var) {
        return localVariableInstruction("astore", var);
    }

    public JasminBuilder dload(int var) {
        return localVariableInstruction("dload", var);
    }

    public JasminBuilder dstore(int var) {
        return localVariableInstruction("dstore", var);
    }

    public JasminBuilder fload(int var) {
        return localVariableInstruction("fload", var);
    }

    public JasminBuilder fstore(int var) {
        return localVariableInstruction("fstore", var);
    }

    public JasminBuilder iload(int var) {
        return localVariableInstruction("iload", var);
    }

    public JasminBuilder istore(int var) {
        return localVariableInstruction("istore", var);
    }

    public JasminBuilder lload(int var) {
        return localVariableInstruction("lload", var);
    }

    public JasminBuilder lstore(int var) {
        return localVariableInstruction("lstore", var);
    }

    private JasminBuilder localVariableInstruction(String instruction, int var) {
        output.append("\t").append(instruction).append(" ").append(var);
        return newline();
    }

    // push

    public JasminBuilder bipush(int n) {
        output.append("\tbipush ").append(n);
        return newline();
    }

    public JasminBuilder ldc(String c) {
        output.append("\tldc ").append(c);
        return newline();
    }

    public JasminBuilder ldc(int c) {
        output.append("\tldc ").append(c);
        return newline();
    }

    public JasminBuilder ldc(float c) {
        output.append("\tldc ").append(c);
        return newline();
    }

    // basic instructions

    public JasminBuilder dup() {
        return basicInstruction("dup");
    }

    public JasminBuilder dup2() {
        return basicInstruction("dup2");
    }

    public JasminBuilder swap() {
        return basicInstruction("swap");
    }

    public JasminBuilder fadd() {
        return basicInstruction("fadd");
    }

    public JasminBuilder fsub() {
        return basicInstruction("fsub");
    }

    public JasminBuilder fmul() {
        return basicInstruction("fmul");
    }

    public JasminBuilder fdiv() {
        return basicInstruction("fdiv");
    }

    private JasminBuilder basicInstruction(String name) {
        output.append("\t").append(name);
        return newline();
    }

    // method invocation

    public JasminBuilder invokestatic(String method) {
        output.append("\tinvokestatic ").append(method);
        return newline();
    }

    public JasminBuilder invokestatic(String name, List<ArgumentType> inputSignature, ArgumentType outputSignature) {
        output.append("\tinvokestatic ");
        methodSignature(name, inputSignature, outputSignature);
        return newline();
    }

    public JasminBuilder methodReturn() {
        return basicInstruction("return");
    }

    // arrays

    public JasminBuilder newarray(ArgumentType type, int size) {
        output.append("\tldc ").append(size);
        newline();
        output.append("\tnewarray ");
        switch (type) {
            case FLOAT:
                output.append("float");
                break;
            default:
                throw new IllegalArgumentException("type " + type.name() + " is not valid for newarray");
        }
        return newline();
    }

    public JasminBuilder areturn() {
        return basicInstruction("areturn");
    }

    public JasminBuilder faload() {
        // index
        // arrayref
        // ->
        // value
        return basicInstruction("faload");
    }

    public JasminBuilder fastore() {
        // value
        // index
        // arrayref
        // ->
        // ...
        return basicInstruction("fastore");
    }

    @Override
    public String toString() {
        return output.toString();
    }
}
