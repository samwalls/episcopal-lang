package ast.program.expression.distribution;

import ast.ASTVisitor;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.List;

public class BernoulliDistribution extends Distribution {

    public static final String BUILTIN_NAME = "builtin_bernoulli";

    public Expression alpha, beta;

    public BernoulliDistribution(List<Expression> expressions) {
        super(new Identifier(BUILTIN_NAME), expressions);
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
