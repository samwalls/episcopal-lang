package ast.program;

import ast.ASTHost;
import ast.ASTVisitor;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.List;

public class Query implements ASTHost {

    private Identifier id;
    private Arguments args;
    private List<Expression> expressions;

    public Query(Identifier id, Arguments args, List<Expression> expressions) {
        this.id = id;
        this.args = args;
        this.expressions = expressions;
    }

    public Query(Identifier id, Arguments args) {
        this(id, args, null);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
