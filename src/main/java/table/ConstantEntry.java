/*
 * File: ConstantEntry.java
 *
 * Desc: describes a constant entry.
 *
 */

package main.java.table;

import main.java.token.TokenType;

/**
 * ConstantEntry class
 * @author Luis Serazo
 */
public class ConstantEntry extends SymbolTableEntry{

    /* flag indicating if this is a parameter */
    private boolean isParam;

    /**
     * ConstantEntry constructor
     * @param name: the String name of the entry
     * @param tokenType: the Token Type of the entry.
     */
    public ConstantEntry(String name, TokenType tokenType){
        super(name, tokenType);
        this.isParam = false;
    }

    /**
     * ConstantEntry constructor
     * @param name: the String name of the entry
     * @param tokenType: the Token Type of the entry.
     * @param isParam: boolean indicating if parameter.
     *
     * Note: favoring constructor overloading to setter method. This is a
     *       personal preference. I think that, e.g, a parameter should always be
     *       a parameter and not have the ability to mutate into a non-parameter.
     */
    public ConstantEntry(String name, TokenType tokenType, boolean isParam){
        super(name, tokenType);
        this.isParam = isParam;
    }

    /**
     * isConstant: override the parent isConstant method.
     * @return True
     */
    @Override
    public boolean isConstant(){ return true; }

    /**
     * isParameter: override the parent isParameter method.
     * @return True, if parameter, False otherwise
     */
    @Override
    public boolean isParameter(){ return this.isParam; }
}
