/*
 * File: ProcedureEntry.java
 *
 * Desc: describes a procedure entry.
 *
 */

package main.java.table;

import main.java.token.TokenType;

import java.util.LinkedList;

/**
 * ProcedureEntry class
 * @author Luis Serazo
 */
public class ProcedureEntry extends SymbolTableEntry {

    private int numOfParameters;
    private LinkedList parameterInfo;

    /**
     * ProcedureEntry constructor
     * @param name: the String name of the entry.
     * @param numOfParameters: the number of parameters to the procedure
     * @param parameterInfo: information about the parameters.
     */
    public ProcedureEntry(String name, int numOfParameters, LinkedList<ParameterInfo> parameterInfo){
        super(name, TokenType.PROCEDURE);
        this.numOfParameters = numOfParameters;
        this.parameterInfo = parameterInfo;
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
     * isProcedure: override the parent isProcedure method.
     * @return True
     */
    @Override
    public boolean isProcedure(){ return true; }

}
