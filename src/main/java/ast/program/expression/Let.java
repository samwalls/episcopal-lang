package ast.program.expression;

import ast.ASTVisitor;
import ast.program.definition.Definition;

import java.util.List;

public class Let extends Expression {

    private List<Definition> definitions;
    private Expression e;

    public Let(List<Definition> definitions, Expression e) {
        this.definitions = definitions;
        this.e = e;
    }

    @Override
    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
