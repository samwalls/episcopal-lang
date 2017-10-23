package ast.program.expression.distribution;

import ast.ASTVisitor;
import ast.program.expression.Expression;

public class BetaDistribution extends Distribution {

    public BetaDistribution(Expression e) {
        super(e);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
