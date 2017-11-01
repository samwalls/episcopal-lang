package ast.program.expression;

import ast.ASTVisitor;

import java.util.List;

public class FunctionCall extends Expression {

    public Identifier id;
    public List<Expression> argValues;

    public FunctionCall(Identifier id, List<Expression> argValues) {
        this.id = id;
        this.argValues = argValues;
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
