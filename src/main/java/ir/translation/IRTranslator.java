package ir.translation;

import ast.ASTVisitor;
import ast.program.Arguments;
import ast.program.Program;
import ast.program.Query;
import ast.program.definition.Definition;
import ast.program.definition.DistributionDefinition;
import ast.program.definition.FunctionDefinition;
import ast.program.expression.*;
import ast.program.expression.Expression;
import ast.program.expression.binop.*;
import ast.program.expression.constant.BoolConstant;
import ast.program.expression.constant.FloatConstant;
import ast.program.expression.constant.IntConstant;
import ast.program.expression.constant.PercentConstant;
import ast.program.expression.distribution.BernoulliDistribution;
import ast.program.expression.distribution.BetaDistribution;
import ast.program.expression.distribution.FlipDistribution;
import ast.program.expression.distribution.NormalDistribution;
import ir.expression.*;
import ir.statement.EXP;
import ir.statement.SEQ;
import ir.statement.Statement;
import compiler.Compiler;

import java.util.*;

/**
 * Visits an Episcopal AST, in order to generate an appropriate set of intermediate representation statements.
 */
public class IRTranslator implements ASTVisitor {

    private EnvNode<String> globalEnvironment;
    private EnvNode<String> currentEnvironment;

    private Map<String, Method> methods;

    private Method mainMethod;

    //******** ACCESSORS ********//

    public Method getMainMethod() {
        return mainMethod;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public EnvNode<String> getEnvironment() {
        return globalEnvironment;
    }

    //******** VISITOR DEFINITION ********//

    @Override
    public Map<String, Method> visit(Program program) throws Exception {
        globalEnvironment = new EnvNode<>(null);
        globalEnvironment.value = program.id.value;
        currentEnvironment = globalEnvironment;
        methods = new HashMap<>();
        for (Query q : program.queries)
            q.accept(this);
        mainMethod = new Method(Compiler.MAIN, globalEnvironment, new EXP(new CALL(Compiler.BUILTIN_FINISH, new ir.Arguments((ir.expression.Expression)program.expression.accept(this)))));
        return methods;
    }

    @Override
    public Object visit(Arguments arguments) throws Exception {
        arguments.identifiers.forEach(i -> {
            if (!currentEnvironment.contains(i.value))
                currentEnvironment.add(i.value, new EnvNode<>(i.value, true));
        });
        return null;
    }

    private EnvNode visitDefinition(Identifier id, Arguments args, List<Expression> expressions) throws Exception {
        EnvNode<String> parentEnvironment = currentEnvironment;
        EnvNode<String> newEnvironment = new EnvNode<>(id.value);
        parentEnvironment.add(id.value, newEnvironment);
        // work on the new environment
        currentEnvironment = newEnvironment;
        // add the args to the environment
        args.accept(this);
        // construct an SEQ from the definition expressions for this definition
        if (expressions.size() <= 0)
            throw new IRTranslateException("declaration of environment " + newEnvironment.value + " has no body");
        // this statement represents the definition in its entirety
        Statement stmt;
        if (expressions.size() > 1) {
            SEQ root = new SEQ(null, null);
            SEQ parent = root;
            SEQ current = parent;
            for (int i = 0; i < expressions.size() - 1; i++) {
                EXP exp = new EXP((ir.expression.Expression)expressions.get(i).accept(this));
                // if this is the last expression in the list, put exp on the right, otherwise put a new SEQ on the right
                if (i + 1 >= expressions.size() - 1) {
                    parent.right = exp;
                } else {
                    parent = current;
                    current.left = exp;
                    SEQ right = new SEQ(null, null);
                    current.right = right;
                    current = right;
                }
            }
            stmt = root;
        } else {
            stmt = new EXP((ir.expression.Expression)expressions.get(0).accept(this));
        }
        // switch context back to the old environment, having created the new one
        currentEnvironment = parentEnvironment;
        // add to the set of methods
        String methodLabel = currentEnvironment.canonicalLabel(newEnvironment.value);
        methods.put(methodLabel, new Method(methodLabel, newEnvironment, stmt));
        return newEnvironment;
    }

    @Override
    public Object visit(Query query) throws Exception {
        // queries are the same as other definition types, but it's important that we know a query is a query
        return visitDefinition(query.id, query.args, query.expressions);
    }

    @Override
    public EnvNode visit(DistributionDefinition def) throws Exception {
        return visitDefinition(def.id, def.args, def.expressions);
    }

    @Override
    public EnvNode visit(FunctionDefinition def) throws Exception {
        return visitDefinition(def.id, def.args, def.expressions);
    }

    @Override
    public VAR visit(Identifier identifier) throws LabelNotFoundException {
        return new VAR(currentEnvironment.canonicalLabel(identifier.value));
    }

    @Override
    public Object visit(Let let) throws Exception {
        // all let definitions, including for "variables" actually define methods
        // these methods take in an amount of arguments equal to the number of distinct variables from the
        for (Definition d : let.definitions)
            d.accept(this);
        return let.e.accept(this);
    }

    @Override
    public ESEQ visit(Observation observation) throws Exception {
        // if the observation is not true; need to call builtin function to generate an exception
        ir.expression.Expression left = (ir.expression.Expression) observation.e1.accept(this);
        ir.expression.Expression right = (ir.expression.Expression)observation.e2.accept(this);
        // call builtin_observe on the left argument, then evaluate the right
        return new ESEQ(new EXP(new CALL(Compiler.BUILTIN_OBSERVE, new ir.Arguments(left))), right);
    }

    @Override
    public Object visit(Sample sample) {
        return null;
    }

    private ir.Arguments visitArgList(List<Expression> args) throws Exception {
        ArrayList<ir.expression.Expression> evaluated = new ArrayList<>();
        for (Expression e : args)
            evaluated.add((ir.expression.Expression)e.accept(this));
        return new ir.Arguments(evaluated);
    }

    @Override
    public CALL visit(FunctionCall functionCall) throws Exception {
        return new CALL(currentEnvironment.canonicalLabel(functionCall.id.value), visitArgList(functionCall.argValues));
    }

    @Override
    public Object visit(BernoulliDistribution bernoulliDistribution) throws Exception {
        return new CALL(bernoulliDistribution.id.value, visitArgList(bernoulliDistribution.argValues));
    }

    @Override
    public Object visit(BetaDistribution betaDistribution) throws Exception {
        return new CALL(betaDistribution.id.value, visitArgList(betaDistribution.argValues));
    }

    @Override
    public Object visit(FlipDistribution flipDistribution) throws Exception {
        return new CALL(flipDistribution.id.value, visitArgList(flipDistribution.argValues));
    }

    @Override
    public Object visit(NormalDistribution normalDistribution) throws Exception {
        return new CALL(normalDistribution.id.value, visitArgList(normalDistribution.argValues));
    }

    @Override
    public CONST visit(BoolConstant boolConstant) {
        // create a boolean with the value 1.0f for true, and 0.0f for false; with 100% probability
        return new CONST(boolConstant.value ? 1f : 0f, 1f);
    }

    @Override
    public CONST visit(FloatConstant floatConstant) {
        // create a value with the given float, at 100% probability
        return new CONST(floatConstant.value, 1f);
    }

    @Override
    public CONST visit(IntConstant intConstant) {
        // create a value with the given integer, at 100% probability
        return new CONST((float)intConstant.value, 1f);
    }

    @Override
    public CONST visit(PercentConstant percentConstant) {
        // create a zero value, with the given percentage as probability
        return new CONST(0, percentConstant.value);
    }

    private BINOP visitBinOp(BinaryOperationType type, BinOp binop) throws Exception {
        ir.expression.Expression lhs = (ir.expression.Expression)binop.lhs.accept(this);
        ir.expression.Expression rhs = (ir.expression.Expression)binop.rhs.accept(this);
        return new BINOP(type, lhs, rhs);
    }

    @Override
    public BINOP visit(AddOp addOp) throws Exception {
        return visitBinOp(BinaryOperationType.ADD, addOp);
    }

    @Override
    public Object visit(SubOp subOp) throws Exception {
        return visitBinOp(BinaryOperationType.SUB, subOp);
    }

    @Override
    public Object visit(MulOp mulOp) throws Exception {
        return visitBinOp(BinaryOperationType.MUL, mulOp);
    }

    @Override
    public Object visit(EqualsOp equalsOp) throws Exception {
        return visitBinOp(BinaryOperationType.EQ, equalsOp);
    }

    @Override
    public Object visit(GreaterThanOp greaterThanOp) throws Exception {
        return visitBinOp(BinaryOperationType.GT, greaterThanOp);
    }

    @Override
    public Object visit(LessThanOp lessThanOp) throws Exception {
        return visitBinOp(BinaryOperationType.LT, lessThanOp);
    }

    @Override
    public Object visit(AndOp andOp) throws Exception {
        return visitBinOp(BinaryOperationType.AND, andOp);
    }

    @Override
    public Object visit(OrOp orOp) throws Exception {
        return visitBinOp(BinaryOperationType.OR, orOp);
    }

    @Override
    public BINOP visit(OverOp overOp) throws Exception {
        return visitBinOp(BinaryOperationType.DIV, overOp);
    }
}
