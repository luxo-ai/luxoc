/*
 * File: ParameterInfo.java
 *
 * Desc: structure to contain parameter information
 *
 */
package main.java.table;

import main.java.token.Token;
import main.java.token.TokenType;

/**
 * ParameterInfo class
 * @author Luis Serazo
 */
public class ParameterInfo {

    private String name;
    private TokenType type;
    private String scope;
    private String[] metaData;
    // TODO: add address of the parameter ?

    /**
     * ParameterInfo constructor
     * @param name: the name of the parameter
     */
    public ParameterInfo(String name, TokenType type){
        this.name = name;
        this.type = type;
        this.scope = null;
        this.metaData = null;
    }

    /**
     * ParameterInfo constructor (verbose)
     * @param name: the name of the parameter
     * @param scope: the scope of the parameter.
     * @param metaData: list of relevant metadata.
     *
     */
    public ParameterInfo(String name, TokenType type, String scope, String[] metaData){
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.metaData = metaData;
    }

    /**
     * toString: override the default toString method.
     */
    @Override
    public String toString(){
        String str = "Name: "+this.name+" Type: "+this.type+" Scope: "+this.scope+"\nMetaData: ";
        if(metaData != null){ for(String dat : metaData){ str += dat+" "; } }
        else{ str += "null"; }
        return str;
    }
}
