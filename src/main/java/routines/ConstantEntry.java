/*
 * File: ConstantEntry.java
 *
 * Desc: describes a constant entry.
 *
 */

package main.java.routines;

import main.java.routines.table.SymbolTableEntry;
import main.java.token.TokenType;

/**
 * ConstantEntry
 * @author Luis Serazo
 */
public class ConstantEntry extends SymbolTableEntry{

    /**
     * ConstantEntry constructor
     * @param name: the String name of the entry
     * @param tokenType: the Token Type of the entry.
     */
    public ConstantEntry(String name, TokenType tokenType){
        super(name, tokenType);
    }

    /**
     * isConstant: override the parent isConstant method
     * @return True
     */
    @Override
    public boolean isConstant(){ return true; }
}
