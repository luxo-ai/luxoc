/*
 * File: VariableEntry.java
 *
 * Desc: describes a variable entry.
 *
 */

package main.java.table;

import main.java.token.TokenType;

/**
 * VariableEntry class
 * @author Luis Serazo
 */
public class VariableEntry extends SymbolTableEntry{

    /* the address of the variable */
    private int address;
    private boolean isParam;
    private boolean isFuncResult;

    /**
     * VariableEntry constructor
     * @param name: the String name of the entry
     * @param tokenType: the type of variable
     */
    public VariableEntry(String name, TokenType tokenType){
        super(name, tokenType);
        this.address = 0; /* 0x0 (default temporary spot) */
        this.isParam = false;
        this.isFuncResult = false;
    }

    /**
     * VariableEntry constructor
     * @param name: the String name of the entry
     * @param address: the address in memory of the variable
     * @param tokenType: the type of variable
     */
    public VariableEntry(String name, int address, TokenType tokenType){
        super(name, tokenType);
        this.address = address;
        this.isParam = false;
        this.isFuncResult = false;
    }

    /**
     * VariableEntry constructor
     * @param name: the String name of the entry
     * @param address: the address in memory of the variable
     * @param tokenType: the type of variable
     * @param isParam: boolean indicating if parameter
     * @param isFuncResult: boolean indicating if functionResult
     *
     * Note: favoring constructor overloading to setter method. This is a
     *       personal preference. I think that, e.g, a parameter should always be
     *       a parameter and not have the ability to mutate into a non-parameter.
     */
    public VariableEntry(String name, int address, TokenType tokenType, boolean isParam, boolean isFuncResult){
        super(name, tokenType);
        this.address = address;
        this.isParam = isParam;
        this.isFuncResult = isFuncResult;
    }

    /**
     * getAddress: getter method for the address
     * @return the address of the variable
     */
    public int getAddress(){ return this.address; }

    /**
     * setAddress: setter method for the memory address of the variable.
     * @param newAddress: the new address of the variable
     */
    protected void setAddress(int newAddress){ this.address = newAddress; }

    /**
     * isVariable: override the parent isVariable method
     * @return True
     */
    @Override
    public boolean isVariable(){ return true; }

    /**
     * isParameter: override the parent isParameter method
     * @return True, if parameter, False otherwise
     */
    @Override
    public boolean isParameter(){ return this.isParam; }

    /**
     * isFuncResult: override the parent isFuncResult method
     * @return True, if function result, False otherwise
     */
    @Override
    public boolean isFunctionResult(){ return this.isFuncResult; }

}
