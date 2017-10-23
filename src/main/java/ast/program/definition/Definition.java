package ast.program.definition;

import ast.ASTHost;
import ast.program.Arguments;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.List;

public abstract class Definition implements ASTHost {

    protected Identifier id;
    protected Arguments args;
    protected List<Expression> expressions;

    public Definition(Identifier id, Arguments args, List<Expression> expressions) {
        this.id = id;
        this.args = args;
        this.expressions = expressions;
    }
}
