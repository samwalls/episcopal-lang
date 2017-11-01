package ast.program;

import ast.ASTHost;
import ast.ASTVisitor;
import ast.program.expression.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Arguments implements ASTHost {

    public List<Identifier> identifiers;

    public Arguments(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Arguments() {
        this(new ArrayList<>());
    }

    @Override
    public Object accept(ASTVisitor v) throws Exception {
        return v.visit(this);
    }
}
