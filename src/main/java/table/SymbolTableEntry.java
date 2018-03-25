/*
 * File: SymbolTableEntry.java
 *
 * Desc: an entry in the symbol table
 *
 * Remarks: Used instead of an interface, in
 * order to avoid boilerplate. Would otherwise
 * have to rewrite false for most of the methods
 * of all the other subclasses.
 *
 */
package main.java.table;

import main.java.token.TokenType;

/**
 * SymbolTableEntry
 * @author Luis Serazo
 */
public class SymbolTableEntry implements TableEntryInterface{

    /* metdata */
    private boolean reserved;

    /* string component */
    private String name;
    /* token component */
    private TokenType tokenType;

    /**
     * SymbolTableEntry constructor
     * @param name: the String name of the entry.
     * @param tokenType: the TokenType of the entry.
     */
    public SymbolTableEntry(String name, TokenType tokenType){
        this.name = name;
        this.tokenType = tokenType;
        this.reserved = false;
    }

    /**
     * SymbolTableEntry constructor
     * @param name: the String name of the entry.
     */
    public SymbolTableEntry(String name){
        this.name = name;
        this.reserved = false;
    }


    /**
     * getName: getter method for the name of the entry
     * @return the name of the entry
     */
    public String getName(){ return this.name; }

    /**
     * getType: getter method for the Token Type of the entry
     * @return the Token Type of this entry.
     */
    public TokenType getType(){ return this.tokenType; }


    /**
     * isVariable: determines if the entry is a variable
     * @return True if the entry is a variable, False otherwise.
     */
    public boolean isVariable(){ return false; }

    /**
     * isKeyword: determines if the entry is a keyword
     * @return True if the entry is a keyword, False otherwise.
     */
    public boolean isKeyword(){ return false; }

    /**
     * isProcedure: determines if the entry is a procedure
     * @return True if the entry is a procedure, False otherwise.
     */
    public boolean isProcedure(){ return false; }

    /**
     * isFunction: determines if the entry is a function
     * @return True if the entry is a function, False otherwise.
     */
    public boolean isFunction(){ return false; }

    /**
     * isFunctionResult: determines if the entry is a Result.
     * @return True if the entry is a result, False otherwise.
     */
    public boolean isFunctionResult(){ return false; }

    /**
     * isParameter: determines if the entry is a parameter.
     * @return True if the entry is a parameter, False otherwise.
     */
    public boolean isParameter(){ return false; }

    /**
     * isArray: determines if the entry is an array.
     * @return True if the entry is an array, False otherwise.
     */
    public boolean isArray(){ return false; }

    /**
     * isConstant: determines if the entry is a constant.
     * @return True if the entry is a constant, False otherwise.
     */
    public boolean isConstant(){ return false; }

    /**
     * isReserved: determines if the entry is reserved.
     * @return True if the entry is reserved, False otherwise.
     */
    public boolean isReserved(){ return this.reserved; }

    /**
     * setReserved: sets the reserved flag
     * @param newFlag: the new boolean indicating reservation.
     */
    public void setReserved(boolean newFlag){ this.reserved = newFlag; }
}
