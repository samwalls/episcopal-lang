package ir.expression;

public class CONST extends Expression {

    public float value;
    public float probability;

    public CONST(float value, float probability) {
        this.value = value;
        this.probability = probability;
    }
}
