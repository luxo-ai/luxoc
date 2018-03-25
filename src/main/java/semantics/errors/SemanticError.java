/*
 * File: SemanticError.java
 *
 * Desc: describes a semantic action error
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
    SemanticError(String msg){
        super(msg);
    }

}
