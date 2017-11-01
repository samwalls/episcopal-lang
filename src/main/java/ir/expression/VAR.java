package ir.expression;

public class VAR extends Expression {

    public String id;

    public VAR(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
