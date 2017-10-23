package ast.program.expression.binop;

import ast.ASTVisitor;
import ast.program.expression.Expression;

public class AndOp extends BinOp {

    public AndOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
