/*
 * File: ParameterInfo.java
 *
 * Desc: structure to contain parameter information
 *
 */
package main.java.table;

/**
 * ParameterInfo class
 * @author Luis Serazo
 */
public class ParameterInfo {

    private String type;
    private String scope;
    private String[] metaData;

    /**
     * ParameterInfo constructor
     * @param type: a string represention of the type.
     */
    public ParameterInfo(String type){
        this.type = type;
        this.scope = null;
        this.metaData = null;
    }

    /**
     * ParameterInfo constructor (verbose)
     * @param type: a string representation of the type.
     * @param scope: the scope of the parameter.
     * @param metaData: list of relevant metadata.
     *
     */
    public ParameterInfo(String type, String scope, String[] metaData){
        this.type = type;
        this.scope = scope;
        this.metaData = metaData;
    }

    /**
     * toString: override the default toString method.
     */
    @Override
    public String toString(){
        String str = "Type: "+this.type+" Scope: "+this.scope+" MetaData: ";
        if(metaData != null){ for(String dat : metaData){ str += dat+" "; } }
        else{ str += "null"; }
        return str;
    }
}
