package ast.program.expression.distribution;

import ast.ASTVisitor;
import ast.program.expression.Expression;

public class BernoulliDistribution extends Distribution {

    public BernoulliDistribution(Expression e) {
        super(e);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
