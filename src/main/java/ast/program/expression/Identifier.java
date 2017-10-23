package ast.program.expression;

import ast.ASTVisitor;

public class Identifier extends Expression {

    private String id;

    public Identifier(String id) {
        // TODO restrict ID types?
        this.id = id;
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
