package ir.expression;

import ir.Arguments;

public class CALL extends Expression {

    public String function;
    public Arguments arguments;

    public CALL(String function, Arguments arguments) {
        this.function = function;
        this.arguments = arguments;
    }
}
