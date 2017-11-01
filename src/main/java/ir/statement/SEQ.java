package ir.statement;

public class SEQ extends Statement {

    public Statement left, right;

    public SEQ(Statement left, Statement right) {
        this.left = left;
        this.right = right;
    }
}
