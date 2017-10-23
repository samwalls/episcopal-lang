package ast.program.expression.constant;

import ast.ASTVisitor;

public class FloatConstant extends Constant<Float> {

    public FloatConstant(Float value) {
        super(value);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
