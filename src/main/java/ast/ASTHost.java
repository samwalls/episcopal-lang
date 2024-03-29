package ast;

public interface ASTHost {

    /**
     * Accept a visitor to this object.
     * @param v the visitor
     */
    Object accept(ASTVisitor v) throws Exception;
}
