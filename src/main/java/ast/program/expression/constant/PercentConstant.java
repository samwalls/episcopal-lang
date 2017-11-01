package ast.program.expression.constant;

import ast.ASTVisitor;

public class PercentConstant extends Constant<Float> {

    public PercentConstant(Float value) {
        // clip value to [0, 1]
        super(Math.max(0f, Math.min(value, 1f)));
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
