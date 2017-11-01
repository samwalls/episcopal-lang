package ir;

import ir.expression.Expression;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Simple type for listing expression arguments.
 */
public class Arguments extends ArrayList<Expression> {

    public Arguments(Collection<Expression> expressions) {
        super(expressions);
    }
}
