package ast.program.expression.distribution;

import ast.program.expression.Expression;

public abstract class Distribution extends Expression {

    public Expression e;

    public Distribution(Expression e) {
        this.e = e;
    }
}
