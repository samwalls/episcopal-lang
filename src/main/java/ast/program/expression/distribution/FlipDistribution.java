package ast.program.expression.distribution;

import ast.ASTVisitor;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.Collections;

public class FlipDistribution extends Distribution {

    public static final String BUILTIN_NAME = "builtin_dist_binomial";

    public static final String BUILTIN_SIGNATURE = BUILTIN_NAME + "([F)[F";

    public FlipDistribution(Expression probability) {
        super(new Identifier(BUILTIN_NAME), Collections.singletonList(probability));
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
