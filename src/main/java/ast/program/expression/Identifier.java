package ast.program.expression;

import ast.ASTVisitor;

public class Identifier extends Expression {

    public String value;

    public Identifier(String value) {
        // TODO restrict ID values with regex?
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
