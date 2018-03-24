/*
 * File: ArrayEntry.java
 *
 * Desc: describes an array entry.
 *
 */

package main.java.routines;

import main.java.routines.table.SymbolTableEntry;
import main.java.token.TokenType;

/**
 * ArrayEntry
 * @author Luis Serazo
 */
public class ArrayEntry extends SymbolTableEntry{

    /* array data */
    private int address;
    private int upperBound;
    private int lowerBound;

    /**
     * ArrayEntry constructor
     * @param name: the String name of the entry
     * @param address: the address in memory of the array
     * @param tokenType: the Token Type of the entry.
     * @param upperBound: the upper bound of the array.
     * @param lowerBound: the lower bound of the array.
     */
    public ArrayEntry(String name, int address, TokenType tokenType, int upperBound, int lowerBound){
        super(name, tokenType);
        this.address = address;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    /**
     * getAddress: getter method for the address
     * @return the address of the array
     */
    public int getAddress(){ return this.address; }

    /**
     * getUpperBound: getter method for the upper bound of the array
     * @return the upper bound of the array
     */
    public int getUpperBound(){ return this.upperBound; }

    /**
     * getLowerBound: getter method for the lower bound of the array
     * @return the lower bound of the array
     */
    public int getLowerBound(){ return this.lowerBound; }

    /**
     * setAddress: setter method for the memory address of the array.
     * @param newAddress: the new address of the array
     * Note: probably don't need this, but it seemed like a good measure for now.
     */
    protected void setAddress(int newAddress){ this.address = newAddress; }

    /**
     * isArray: override the parent method to indicate that this is an array.
     * @return True
     */
    @Override
    public boolean isArray(){ return true; }
}
