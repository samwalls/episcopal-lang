package ast.program.expression;

import ast.ASTVisitor;
import ast.program.definition.Definition;

import java.util.List;

public class Let extends Expression {

    public List<Definition> definitions;
    public Expression e;

    public Let(List<Definition> definitions, Expression e) {
        this.definitions = definitions;
        this.e = e;
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }

    @Override
    public String toString() {
        String result = "";
        for (Definition d : definitions)
            result += " let " + d.toString();
        return result.substring(1) + " in " + e.toString();
    }
}
