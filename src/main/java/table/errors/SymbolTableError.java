/*
 * File: SymbolTableError.java
 *
 * Desc: contains the errors associated with
 *       symbol table routines.
 */
package main.java.table.errors;

/**
 * SymbolTableError
 * @author Luis Serazo
 */

public class SymbolTableError extends Error{

    /**
     * SymbolTableError constructor
     * @param msg: the error message.
     */
    private SymbolTableError(String msg){
        super(msg);
    }

    /**
     * EntryAlreadyExists: throw error when an entry already exists in the table
     * @param name: the string name of the entry.
     * @return a SymbolTableError
     */
    public static SymbolTableError EntryAlreadyExists(String name){
        return new SymbolTableError("The Entry: "+name+" already exists in the Symbol Table.");
    }

}
