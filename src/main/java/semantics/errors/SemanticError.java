/*
 * File: SemanticError.java
 *
 * Desc: describes a semantic action error.
 *
 */
package main.java.semantics.errors;

/**
 * SemanticError
 * @author Luis Serazo
 */
public class SemanticError extends Error {

    /**
     * SemanticError constructor
     * @param msg: error message
     */
    private SemanticError(String msg){
        super(msg);
    }

    public static SemanticError ReservedName(String name, int lineNumber){
        return new SemanticError("Attempted to use a reserved name: "+name+ " on line: "+lineNumber+". Not allowed to use reserved names");
    }

    public static SemanticError NameAlreadyDeclared(String name, int lineNumber){
        return new SemanticError("The name: "+name+" has already been declared on line: "+lineNumber);
    }

}
