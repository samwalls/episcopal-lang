package ast.program.expression.binop;

import ast.program.expression.Expression;

public abstract class BinOp extends Expression {

    protected Expression lhs, rhs;

    public BinOp(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
