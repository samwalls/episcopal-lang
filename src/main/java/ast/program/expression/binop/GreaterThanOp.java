package ast.program.expression.binop;

import ast.ASTVisitor;
import ast.program.expression.Expression;

public class GreaterThanOp extends BinOp {

    public GreaterThanOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
