package ast.program.expression.binop;

import ast.program.expression.Expression;

public abstract class BinOp extends Expression {

    public Expression lhs, rhs;

    public BinOp(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
