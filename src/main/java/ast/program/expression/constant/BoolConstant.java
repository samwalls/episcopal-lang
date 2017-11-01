package ast.program.expression.constant;

import ast.ASTVisitor;

public class BoolConstant extends Constant<Boolean> {

    public BoolConstant(Boolean value) {
        super(value);
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
