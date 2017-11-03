package ast.program;

import ast.ASTHost;
import ast.ASTVisitor;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Program implements ASTHost {

    public Identifier id;
    public Expression expression;
    public List<Query> queries;

    public Program(Identifier id, Expression expression, List<Query> queries) {
        this.id = id;
        this.expression = expression;
        this.queries = queries;
    }

    public Program(Identifier id, Expression e) {
        this(id, e, new ArrayList<>());
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
