/*
 * File: FunctionEntry.java
 *
 * Desc: describes a function entry.
 *
 */

package main.java.table;

import main.java.token.TokenType;
import java.util.LinkedList;

/**
 * FunctionEntry class
 * @author Luis Serazo
 */
public class FunctionEntry extends RoutineEntry {

    private VariableEntry result;

    /**
     * FunctionEntry constructor
     * @param name: the String name of the function
     * @param numOfParameters: the number of parameters of the function
     * @param parameterInfo: information about the parameters
     * @param result: a variable entry
     */
    public FunctionEntry(String name, int numOfParameters, LinkedList<ParameterInfo> parameterInfo, VariableEntry result){
        super(name, TokenType.FUNCTION, numOfParameters, parameterInfo);
        this.result = result;
    }

    /**
     * getNumParam: getter method for the number of parameters
     * @return the number of parameters
     */
    @Override
    public int getNumOfParam(){ return super.getNumOfParam(); }


    /**
     * getParamInfo: getter method for the parameter information
     * @return the parameter information
     */
    @Override
    public LinkedList<ParameterInfo> getParamInfo(){ return super.getParamInfo(); }


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
