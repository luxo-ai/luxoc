/*
 * File: SemanticError.java
 *
 * Desc: describes a semantic action error.
 *
 */
package main.java.semantics.errors;

import main.java.token.TokenType;

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

    public static SemanticError ReservedName(String name){
        return new SemanticError("Attempted to use a reserved name: "+name+ ". Not allowed to use reserved names");
    }

    public static SemanticError NameAlreadyDeclared(String name){
        return new SemanticError("The name: "+name+" has already been declared.");
    }

    public static SemanticError ActionDoesNotExist(int num){
        return new SemanticError("The semantic action: "+num+" does not exist");
    }

    public static SemanticError UnrecognizedTypes(TokenType tt1, TokenType tt2){
        return new SemanticError("The combination of Type: "+tt1+" and Type: "+tt2+" is unrecognized");
    }

    // TODO: line number?
    public static SemanticError InputOutputNotSpecified(){
        return new SemanticError("Input and Output not specified in program identifier list");
    }

    public static SemanticError UndeclaredVariabe(String name, int lineNumber){
        return new SemanticError("Undeclared variable: "+name+" on line: "+lineNumber);
    }

    public static SemanticError UnmatchedTypes(String name1, String name2, int linNumber){
        return new SemanticError("Type of: "+name1+" and of: "+name2+" don't match on line: "+linNumber);
    }

    public static SemanticError BadMod(){
        return new SemanticError("Bad Mod Setup");
    }

}
