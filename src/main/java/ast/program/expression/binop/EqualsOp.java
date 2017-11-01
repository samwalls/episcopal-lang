package ast.program.expression.binop;

import ast.ASTVisitor;
import ast.program.expression.Expression;

public class EqualsOp extends BinOp {

    public EqualsOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
