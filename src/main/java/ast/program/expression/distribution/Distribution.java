package ast.program.expression.distribution;

import ast.program.expression.Expression;
import ast.program.expression.FunctionCall;
import ast.program.expression.Identifier;

import java.util.List;

public abstract class Distribution extends FunctionCall {

    public Distribution(Identifier id, List<Expression> expressions) {
        super(id, expressions);
    }
}
