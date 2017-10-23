package ast.program.expression.constant;

import ast.ASTVisitor;

public class IntConstant extends Constant<Integer> {

    public IntConstant(Integer value) {
        super(value);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
