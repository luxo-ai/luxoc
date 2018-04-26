/*
 * File: RoutineEntry.java
 * Desc: Base class for procedures and functions.
 */
package main.java.table;

import main.java.token.TokenType;
import java.util.LinkedList;

/**
 * RoutineEntry base class for procedures and functions.
 * @author Luis Serazo
 */
public class RoutineEntry extends SymbolTableEntry{

    private int numOfParameters;
    private LinkedList<ParameterInfo> parameterInfo;

    RoutineEntry(String name, TokenType type, int numOfParameters, LinkedList<ParameterInfo> paramInfo){
        super(name, type);
        this.numOfParameters = numOfParameters;
        this.parameterInfo = paramInfo;
    }

    /**
     * getNumParam: getter method for the number of parameters
     * @return the number of parameters
     */
    public int getNumOfParam(){ return this.numOfParameters; }

    /**
     * setNumParam: setter method of the number of parameters
     * @param numParam: the new number of Parameters
     */
    public void setNumOfParam(int numParam){ this.numOfParameters = numParam; }

    /**
     * getParamInfo: getter method for the parameter information
     * @return the parameter information
     */
    public LinkedList<ParameterInfo> getParamInfo(){ return this.parameterInfo; }

    /**
     * setParamInfo: setter method for the parameter information
     * @param newInfo: the new param info
     */
    public void setParamInfo(LinkedList<ParameterInfo> newInfo){
        this.parameterInfo = newInfo;
    }

    /**
     * appendParamInfo
     */
    public void appendParamInfo(LinkedList<ParameterInfo> paramInfo){
        this.parameterInfo.addAll(paramInfo);
    }
}
