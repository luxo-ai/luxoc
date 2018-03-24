/*
 * File: VariableEntry.java
 *
 * Desc: describes a variable entry.
 *
 */

package main.java.routines;

import main.java.routines.table.SymbolTableEntry;
import main.java.token.TokenType;

public class VariableEntry extends SymbolTableEntry{

    /* the address of the variable */
    int address;

    /**
     * VariableEntry constructor
     * @param name: the String name of the entry
     * @param address: the address in memory of the variable
     * @param tokenType: the type of variable
     */
    public VariableEntry(String name, int address, TokenType tokenType){
        super(name, tokenType);
        this.address = address;
    }

    /**
     * getAddress: getter method for the address
     * @return the address of the variable
     */
    public int getAddress(){ return this.address; }

    /**
     * setAddress: setter method for the memory address of the variable.
     * @param newAddress: the new address of the variable
     * Note: probably don't need this, but it seemed like a good measure for now.
     */
    protected void setAddress(int newAddress){ this.address = newAddress; }

    /**
     * isVariable: override the parent isVariable method
     * @return True
     */
    @Override
    public boolean isVariable(){ return true; }

}
