package ast.program.definition;

import ast.ASTVisitor;
import ast.program.Arguments;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.List;

public class FunctionDefinition extends Definition {

    public FunctionDefinition(Identifier id, Arguments args, List<Expression> expressions) {
        super(id, args, expressions);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
