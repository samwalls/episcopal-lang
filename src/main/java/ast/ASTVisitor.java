package ast;

import ast.program.Arguments;
import ast.program.Program;
import ast.program.Query;
import ast.program.definition.DistributionDefinition;
import ast.program.definition.FunctionDefinition;
import ast.program.expression.*;
import ast.program.expression.binop.*;
import ast.program.expression.constant.*;
import ast.program.expression.distribution.*;

/**
 * Defines the base for an implementation of a set of operations step to be applied to a program AST.
 */
public interface ASTVisitor {

    // Top level AST constructs
    Object visit(Program program) throws Exception;
    Object visit(Arguments arguments) throws Exception;
    Object visit(Query query) throws Exception;

    // Definition
    Object visit(DistributionDefinition distributionDefinition) throws Exception;
    Object visit(FunctionDefinition functionDefinition) throws Exception;

    // Expression
    Object visit(Identifier identifier) throws Exception;
    Object visit(Let let) throws Exception;
    Object visit(Observation observation) throws Exception;
    Object visit(Sample sample) throws Exception;
    Object visit(FunctionCall functionCall) throws Exception;

    // Expression: Distribution
    Object visit(BernoulliDistribution bernoulliDistribution) throws Exception;
    Object visit(BetaDistribution betaDistribution) throws Exception;
    Object visit(FlipDistribution flipDistribution) throws Exception;
    Object visit(NormalDistribution normalDistribution) throws Exception;

    // Expression: Constant
    Object visit(BoolConstant boolConstant) throws Exception;
    Object visit(FloatConstant floatConstant) throws Exception;
    Object visit(IntConstant intConstant) throws Exception;
    Object visit(PercentConstant percentConstant) throws Exception;

    // Expression: Binary Operation
    Object visit(AddOp addOp) throws Exception;
    Object visit(SubOp subOp) throws Exception;
    Object visit(MulOp mulOp) throws Exception;
    Object visit(EqualsOp equalsOp) throws Exception;
    Object visit(GreaterThanOp greaterThanOp) throws Exception;
    Object visit(LessThanOp lessThanOp) throws Exception;
    Object visit(AndOp andOp) throws Exception;
    Object visit(OrOp orOp) throws Exception;
    Object visit(OverOp overOp) throws Exception;
}
