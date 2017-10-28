package ast.program.expression;

import ast.ASTVisitor;

public class Sample extends Expression {

    public Expression e;

    public Sample(Expression e) {
        this.e = e;
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
