package ast.program.expression.binop;

import ast.ASTVisitor;
import ast.program.expression.Expression;

public class OverOp extends BinOp {

    public OverOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
