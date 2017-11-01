package ast.program.expression.constant;

import ast.ASTVisitor;

public class IntConstant extends Constant<Integer> {

    public IntConstant(Integer value) {
        super(value);
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
