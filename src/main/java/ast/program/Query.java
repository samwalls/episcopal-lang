package ast.program;

import ast.ASTHost;
import ast.ASTVisitor;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.List;

public class Query implements ASTHost {

    public Identifier id;
    public Arguments args;
    public List<Expression> expressions;

    public Query(Identifier id, Arguments args, List<Expression> expressions) {
        this.id = id;
        this.args = args;
        this.expressions = expressions;
    }

    public Query(Identifier id, Arguments args) {
        this(id, args, null);
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return "query " + id.toString() + " " + args.toString();
    }
}
