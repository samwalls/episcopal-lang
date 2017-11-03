package compiler;

import ast.program.expression.distribution.BernoulliDistribution;
import ast.program.expression.distribution.BetaDistribution;
import ast.program.expression.distribution.FlipDistribution;
import ast.program.expression.distribution.NormalDistribution;
import ir.expression.*;
import ir.statement.EXP;
import ir.statement.SEQ;
import ir.statement.Statement;
import ir.translation.EnvNode;
import ir.translation.LabelNotFoundException;
import ir.translation.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EpiscopalMuncher {

    /**
     * The number of array elements in an array representing a constant value for episcopal.
     */
    public static final int CONSTANT_SIZE = 2;

    public static final String BUILTIN_CLASS_PATH = "compiler/helpers/Episcopal";
    public static final String BUILTIN_LT = BUILTIN_CLASS_PATH + "/" + "builtin_lt(FF)F";
    public static final String BUILTIN_GT = BUILTIN_CLASS_PATH + "/" + "builtin_gt(FF)F";
    public static final String BUILTIN_EQ = BUILTIN_CLASS_PATH + "/" + "builtin_eq(FF)F";
    public static final String BUILTIN_OR = BUILTIN_CLASS_PATH + "/" + "builtin_or(FF)F";
    public static final String BUILTIN_AND = BUILTIN_CLASS_PATH + "/" + "builtin_and(FF)F";

    public static final int MAX_LIMIT = 256;

    private JasminBuilder jasmin;

    private Method method;

    private String className;

    private int usedLocals = 0;

    private Map<String, Integer> argumentLocals;

    public EpiscopalMuncher(JasminBuilder jasmin, Method method, String className) {
        this.jasmin = jasmin;
        this.method = method;
        this.className = className;
    }

    public void munchMethod() throws LabelNotFoundException {
        int i = 0;
        argumentLocals = new HashMap<>();
        jasmin.methodStackLimit(MAX_LIMIT);
        jasmin.methodLocalsLimit(MAX_LIMIT);
        for (String label : method.environment.keySet()) {
            EnvNode node = method.environment.get(label);
            // put the variable values (array references) onto the stack
            if (node.value.equals(label) && node.keySet().size() <= 0)
                argumentLocals.put(method.environment.canonicalLabel(label), i++);
        }
        usedLocals = argumentLocals.size();
        munchStatement(method.statement);
        jasmin.areturn();
    }

    private void munchStatement(Statement stmt) throws LabelNotFoundException {
        if (stmt instanceof SEQ) {
            munchSeq((SEQ)stmt);
        } else if (stmt instanceof EXP) {
            munchExpression(((EXP)stmt).expression);
        }
    }

    private void munchSeq(SEQ seq) throws LabelNotFoundException {
        if (seq.left != null)
            munchStatement(seq.left);
        if (seq.right != null)
            munchStatement(seq.right);
    }

    private void munchExpression(Expression exp) throws LabelNotFoundException {
        if (exp instanceof ESEQ) {
            munchEseq((ESEQ)exp);
        } else if (exp instanceof CALL) {
            munchCall((CALL)exp);
        } else if (exp instanceof BINOP) {
            munchBinOp((BINOP)exp);
        } else if (exp instanceof CONST) {
            munchConst((CONST)exp);
        } else if (exp instanceof VAR) {
            munchVar((VAR)exp);
        }
    }

    private void munchEseq(ESEQ eseq) throws LabelNotFoundException {
        // perform the left expression, evaluate the right
        munchStatement(eseq.left);
        munchExpression(eseq.right);
    }

    private void munchCall(CALL call) throws LabelNotFoundException {
        // evaluate all the arguments, such that the values are left on the stack
        for (Expression e : call.arguments)
            munchExpression(e);
        // call the given method (check first for built-in methods)
        if (call.function.equals(FlipDistribution.BUILTIN_NAME)) {
            jasmin.invokestatic(BUILTIN_CLASS_PATH + "/" + FlipDistribution.BUILTIN_SIGNATURE);
        } else if (call.function.equals(NormalDistribution.BUILTIN_NAME)) {
            jasmin.invokestatic(BUILTIN_CLASS_PATH + "/" + NormalDistribution.BUILTIN_SIGNATURE);
        } else if (call.function.equals(BetaDistribution.BUILTIN_NAME)) {
            jasmin.invokestatic(BUILTIN_CLASS_PATH + "/" + BetaDistribution.BUILTIN_SIGNATURE);
        } else if (call.function.equals(BernoulliDistribution.BUILTIN_NAME)) {
            jasmin.invokestatic(BUILTIN_CLASS_PATH + "/" + BernoulliDistribution.BUILTIN_SIGNATURE);
        } else if (call.function.equals(Compiler.BUILTIN_OBSERVE)) {
            jasmin.invokestatic(BUILTIN_CLASS_PATH + "/" + Compiler.BUILTIN_OBSERVE_SIGNATURE);
        } else if (call.function.equals(Compiler.BUILTIN_FINISH)) {
            jasmin.invokestatic(BUILTIN_CLASS_PATH + "/" + Compiler.BUILTIN_FINISH_SIGNATURE);
        } else {
            ArrayList<JasminBuilder.ArgumentType> argumentSignature = new ArrayList<>();
            for (Expression e : call.arguments)
                argumentSignature.add(JasminBuilder.ArgumentType.FLOAT_ARRAY);
            jasmin.invokestatic(className + "/" + call.function, argumentSignature, JasminBuilder.ArgumentType.FLOAT_ARRAY);
        }
    }

    private void munchOp(BinaryOperationType op) {
        // perform operation on values on stack
        switch (op) {
            case ADD:
                jasmin.fadd();
                break;
            case SUB:
                jasmin.fsub();
                break;
            case MUL:
                jasmin.fmul();
                break;
            case DIV:
                jasmin.fdiv();
                break;
            case GT:
                jasmin.invokestatic(BUILTIN_GT);
                break;
            case LT:
                jasmin.invokestatic(BUILTIN_LT);
                break;
            case EQ:
                jasmin.invokestatic(BUILTIN_EQ);
                break;
            case OR:
                jasmin.invokestatic(BUILTIN_OR);
                break;
            case AND:
                jasmin.invokestatic(BUILTIN_AND);
                break;
        }
    }

    private void munchBinOp(BINOP binop) throws LabelNotFoundException {
        int arrayLocal = usedLocals++;
        jasmin.newarray(JasminBuilder.ArgumentType.FLOAT, CONSTANT_SIZE);
        jasmin.astore(arrayLocal);
        int leftLocal = usedLocals++;
        int rightLocal = usedLocals++;
        munchExpression(binop.left);
        jasmin.astore(leftLocal);
        munchExpression(binop.right);
        jasmin.astore(rightLocal);
        // put values from left and right values on the stack
        arrayGet(rightLocal, 0);
        int e2v = usedLocals++;
        jasmin.fstore(e2v);
        arrayGet(leftLocal, 0);
        int e1v = usedLocals++;
        jasmin.fstore(e1v);
        // get probability of right expression
        arrayGet(rightLocal, 1);
        int e2p = usedLocals++;
        jasmin.fstore(e2p);
        // get probability of left expression
        arrayGet(leftLocal, 1);
        int e1p = usedLocals++;
        jasmin.fstore(e1p);
        // op on values
        jasmin.fload(e2v);
        jasmin.fload(e1v);
        munchOp(binop.op);
        arraySet(arrayLocal, 0);
        // multiply the probabilities
        jasmin.fload(e2p);
        jasmin.fload(e1p);
        jasmin.fmul();
        arraySet(arrayLocal, 1);
        // leave behind an array value
        jasmin.aload(arrayLocal);
        // forget about used locals for this section of code
        usedLocals--; // e1v
        usedLocals--; // e2v
        usedLocals--; // e1p
        usedLocals--; // e2p
        usedLocals--; // leftLocal
        usedLocals--; // rightLocal
        usedLocals--; // arrayLocal
    }

    private void munchConst(CONST constant) {
        // create an array with the value, and associated probability
        jasmin.newarray(JasminBuilder.ArgumentType.FLOAT, CONSTANT_SIZE);
        int arrayLocal = usedLocals++;
        jasmin.astore(arrayLocal);
        // constant value
        arraySet(arrayLocal, 0, constant.value);
        // constant probability
        arraySet(arrayLocal, 1, constant.probability);
        // leave reference to the constant array on the stack
        --usedLocals;
        jasmin.aload(arrayLocal);
    }

    private void munchVar(VAR var) throws LabelNotFoundException {
        if (!argumentLocals.containsKey(var.id))
            throw new LabelNotFoundException("label " + var.id + " was not found in local variable set for method " + method.name);
        jasmin.aload(argumentLocals.get(var.id));
    }

    private void arraySet(int local, int index, float value) {
        jasmin.aload(local);
        jasmin.ldc(index);
        jasmin.ldc(value);
        jasmin.fastore();
    }

    private void arraySet(int local, int index) {
        // expect value on stack
        jasmin.aload(local);
        jasmin.swap();
        jasmin.ldc(index);
        jasmin.swap();
        jasmin.fastore();
    }

    private void arrayGet(int local, int index) {
        jasmin.aload(local);
        jasmin.ldc(index);
        jasmin.faload();
    }
}
