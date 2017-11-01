package ir.expression;

public class MEM extends Expression {

    public Expression location;

    public MEM(Expression location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "MEM (" + location.toString() + ")";
    }
}
