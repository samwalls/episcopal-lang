package ast.program.expression;

import ast.ASTVisitor;

public class Sample extends Expression {

    public Expression e;

    public Sample(Expression e) {
        this.e = e;
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
