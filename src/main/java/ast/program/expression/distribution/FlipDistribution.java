package ast.program.expression.distribution;

import ast.ASTVisitor;
import ast.program.expression.Expression;

public class FlipDistribution extends Distribution {

    public FlipDistribution(Expression e) {
        super(e);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
