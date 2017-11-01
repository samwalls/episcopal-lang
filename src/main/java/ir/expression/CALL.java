package ir.expression;

import ir.Arguments;

import java.util.ArrayList;

public class CALL extends Expression {

    public String function;
    public Arguments arguments;

    public CALL(String function, Arguments arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public CALL(String function) {
        this(function, new Arguments(new ArrayList<>()));
    }

    @Override
    public String toString() {
        return function + " " + arguments.toString();
    }
}
