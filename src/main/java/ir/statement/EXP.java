package ir.statement;

import ir.expression.Expression;

public class EXP extends Statement {

    public Expression expression;

    public EXP(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
