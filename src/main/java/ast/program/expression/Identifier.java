package ast.program.expression;

import ast.ASTVisitor;

public class Identifier extends Expression {

    public String value;

    public Identifier(String value) {
        // TODO restrict ID values with regex?
        this.value = value;
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
