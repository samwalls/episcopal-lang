package ast.program.expression.constant;

import ast.ASTVisitor;

public class FloatConstant extends Constant<Float> {

    public FloatConstant(Float value) {
        super(value);
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
