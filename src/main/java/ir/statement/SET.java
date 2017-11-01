package ir.statement;

import ir.expression.Expression;

public class SET extends Statement {

    public Expression location, value;

    public SET(Expression location, Expression value) {
        this.location = location;
        this.value = value;
    }
}
