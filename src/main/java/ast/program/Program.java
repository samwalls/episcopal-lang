package ast.program;

import ast.ASTHost;
import ast.ASTVisitor;
import ast.program.expression.Expression;
import ast.program.expression.Identifier;

public class Program implements ASTHost {

    public Identifier id;
    public Expression e;
    public Query whereQuery;

    public Program(Identifier id, Expression e, Query whereQuery) {
        this.id = id;
        this.e = e;
        this.whereQuery = whereQuery;
    }

    public Program(Identifier id, Expression e) {
        this(id, e, null);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
