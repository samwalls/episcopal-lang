package ast.program.expression.distribution;

import ast.ASTVisitor;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.Arrays;

public class NormalDistribution extends Distribution {

    public static final String BUILTIN_NAME = "builtin_dist_normal";

    public static final String BUILTIN_SIGNATURE = BUILTIN_NAME + "([F[F)[F";

    public NormalDistribution(Expression mean, Expression standardDeviation) {
        super(new Identifier(BUILTIN_NAME), Arrays.asList(mean, standardDeviation));
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
