package ast.program.expression.distribution;

import ast.ASTVisitor;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.Arrays;
import java.util.List;

public class BetaDistribution extends Distribution {

    public static final String BUILTIN_NAME = "builtin_dist_beta";

    public static final String BUILTIN_SIGNATURE = BUILTIN_NAME + "([F[F)[F";

    public BetaDistribution(Expression alpha, Expression beta) {
        super(new Identifier(BUILTIN_NAME), Arrays.asList(alpha, beta));
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
