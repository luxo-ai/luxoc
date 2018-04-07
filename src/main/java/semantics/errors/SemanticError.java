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
        return new SemanticError("The name: "+name+" on line: "+lineNumber+" has already been declared.");
    }

    public static SemanticError ActionDoesNotExist(int num){
        return new SemanticError("The semantic action: "+num+" does not exist");
    }

    public static SemanticError InputOutputNotSpecified(){
        return new SemanticError("Input and Output not specified in Program identifier list.");
    }

}
