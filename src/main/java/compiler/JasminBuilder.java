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

    public JasminBuilder header(String source, String pack, String clazz) {
        output.append(".source ").append(source);
        newline();
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
        output.append(".method public ").append(name).append("(");
        for (ArgumentType type : inputSignature)
            output.append(type.value());
        output.append(")");
        output.append(outputSignature.value());
        return newline();
    }

    public JasminBuilder methodEnd() {
        output.append(".end method");
        return newline();
    }

    public JasminBuilder methodStackLimit(int limit) {
        output.append(".limit stack ").append(limit);
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
        output.append("\t").append(instruction).append(var);
        return newline();
    }

    // TODO bipush, sipush

    // TODO iinc

    // TODO branch instructions

    // TODO method invocation

    @Override
    public String toString() {
        return output.toString();
    }
}
