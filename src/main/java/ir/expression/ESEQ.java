package ir.expression;

import ir.statement.Statement;

public class ESEQ extends Expression {

    public Statement left;
    public Expression right;

    public ESEQ(Statement left, Expression right) {
        this.left = left;
        this.right = right;
    }
}
