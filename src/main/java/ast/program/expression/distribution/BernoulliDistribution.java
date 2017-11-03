package ast.program.expression.distribution;

import ast.ASTVisitor;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.Collections;
import java.util.List;

public class BernoulliDistribution extends Distribution {

    public static final String BUILTIN_NAME = "builtin_dist_binomial";

    public static final String BUILTIN_SIGNATURE = BUILTIN_NAME + "([F[F)[F";

    public Expression alpha, beta;

    public BernoulliDistribution(Expression probability) {
        super(new Identifier(BUILTIN_NAME), Collections.singletonList(probability));
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
