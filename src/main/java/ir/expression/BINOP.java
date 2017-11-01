package ir.expression;

public class BINOP extends Expression {

    public BinaryOperationType op;
    public Expression left, right;

    public BINOP(BinaryOperationType op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "BINOP (" + op.name() + "(" + left.toString() + ")" + "(" + right.toString() + "))";
    }
}
