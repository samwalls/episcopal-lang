package ast.program.expression.constant;

import ast.program.expression.Expression;

public abstract class Constant<T> extends Expression {

    public T value;

    public Constant(T value) {
        this.value = value;
    }
}
