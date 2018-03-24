/*
 * File: SymbolTable.java
 *
 * Desc: symbol table with all the necessary routines
 *
 */
package main.java.routines.table;

import main.java.routines.table.errors.SymbolTableError;

import java.util.Hashtable;

/**
 * SymbolTable
 * @author Luis Serazo
 */
public class SymbolTable implements TableInterface{

    /* hashtable */
    private Hashtable<String, TableEntryInterface> table;

    /**
     * SymbolTable constructor
     * @param size: the size of the SymbolTable
     */
    protected SymbolTable(int size){
        this.table = new Hashtable<>(size);
    }

    /**
     * lookup: look up an entry in the symbol table
     * @param key: a string key.
     * @return an entry in the SymbolTable.
     */
    public TableEntryInterface lookup(String key){
        return this.table.get(key.toUpperCase());
    }

    /**
     * insert: insert a value into the symbol table.
     * @param value: a TableEntry
     */
    public void insert(String key, SymbolTableEntry value){
        key = key.toUpperCase();
        /* make sure that the name doesn't already exist */
        if(table.containsKey(key)){
            table.put(key, value);
        }
        else{
            throw SymbolTableError.EntryAlreadyExists(value.getName());
        }
    }

    /**
     * size: return the size of the symbol table.
     * @return the size of the symbol table.
     */
    public int size(){
        return table.size();
    }

    /**
     * dumpTable: routine prints the symbol table contents
     */
    public void dumpTable(){}

    /**
     * installBuiltins: install the following reserved names in the symbol table.
     */
    public void installBuiltins(){}
}
