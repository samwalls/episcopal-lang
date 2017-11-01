package ast.program.definition;

import ast.ASTVisitor;
import ast.program.Arguments;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.List;

public class DistributionDefinition extends Definition {

    public DistributionDefinition(Identifier id, Arguments args, List<Expression> expressions) {
        super(id, args, expressions);
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
