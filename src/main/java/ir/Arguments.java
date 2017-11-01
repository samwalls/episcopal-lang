package ir;

import ir.expression.Expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Simple type for listing expression arguments.
 */
public class Arguments extends ArrayList<Expression> {

    public Arguments(Collection<Expression> expressions) {
        super(expressions);
    }

    public Arguments(Expression e) {
        super(Collections.singletonList(e));
    }

    @Override
    public String toString() {
        String result = "";
        if (size() > 0) {
            for (Expression e : this)
                result += " (" + e.toString() + ")";
            result = result.substring(1);
        }
        return result;
    }
}
