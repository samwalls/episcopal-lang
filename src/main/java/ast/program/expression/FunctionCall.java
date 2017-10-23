package ast.program.expression;

import ast.ASTVisitor;

import java.util.List;

public class FunctionCall extends Expression {

    private Identifier id;
    private List<Expression> argValues;

    public FunctionCall(Identifier id, List<Expression> argValues) {
        this.id = id;
        this.argValues = argValues;
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}