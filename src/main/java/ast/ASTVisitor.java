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
    void visit(Program program);
    void visit(Arguments arguments);
    void visit(Query query);

    // Definition
    void visit(DistributionDefinition distributionDefinition);
    void visit(FunctionDefinition functionDefinition);

    // Expression
    void visit(Identifier identifier);
    void visit(Let let);
    void visit(Observation observation);
    void visit(Sample sample);
    void visit(FunctionCall functionCall);

    // Expression: Distribution
    void visit(BernoulliDistribution bernoulliDistribution);
    void visit(BetaDistribution betaDistribution);
    void visit(FlipDistribution flipDistribution);
    void visit(NormalDistribution normalDistribution);

    // Expression: Constant
    void visit(BoolConstant boolConstant);
    void visit(FloatConstant floatConstant);
    void visit(IntConstant intConstant);
    void visit(PercentConstant percentConstant);

    // Expression: Binary Operation
    void visit(AddOp addOp);
    void visit(SubOp subOp);
    void visit(MulOp mulOp);
    void visit(EqualsOp equalsOp);
    void visit(GreaterThanOp greaterThanOp);
    void visit(LessThanOp lessThanOp);
    void visit(AndOp andOp);
    void visit(OrOp orOp);
    void visit(OverOp overOp);
}
