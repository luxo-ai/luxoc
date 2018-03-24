/*
 * File: FunctionEntry.java
 *
 * Desc: describes a function entry.
 *
 */

package main.java.routines;

import main.java.routines.table.SymbolTableEntry;
import main.java.token.TokenType;

import java.util.LinkedList;
import java.util.List;

/**
 * FunctionEntry
 * @author Luis Serazo
 */
public class FunctionEntry extends SymbolTableEntry{

    private int numOfParameters;
    private LinkedList parameterInfo;
    private VariableEntry result;

    /**
     * FunctionEntry constructor
     * @param name: the String name of the function
     * @param numOfParameters: the number of paramaters of the function
     * @param parameterInfo: information about the parameters
     * @param result: a variable entry
     */
    public FunctionEntry(String name, int numOfParameters, LinkedList parameterInfo, VariableEntry result){
        super(name, TokenType.FUNCTION);
        this.numOfParameters = numOfParameters;
        this.parameterInfo = parameterInfo;
        this.result = result;
    }

    /**
     * getNumParam: getter method for the number of parameters
     * @return the number of parameters
     */
    public int getNumOfParam(){ return this.numOfParameters; }

    /**
     * getParamInfo: getter method for the parameter information
     * @return the parameter information
     */
    public LinkedList getParamInfo(){ return this.parameterInfo; }

    /**
     * getResult: variable entry for the result
     * @return the function result.
     */
    public VariableEntry getResult(){ return this.result; }

    /**
     * isFunction: override the parent isFunction method
     * @return True
     */
    @Override
    public boolean isFunction(){ return true; }
}
