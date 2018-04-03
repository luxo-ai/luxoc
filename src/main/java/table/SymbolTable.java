/*
 * File: SymbolTable.java
 *
 * Desc: symbol table with all the necessary routines
 *
 */
package main.java.table;

import main.java.table.errors.SymbolTableError;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * SymbolTable
 * @author Luis Serazo
 */
public class SymbolTable implements TableInterface{

    /* hash table */
    private Hashtable<String, TableEntryInterface> table;

    /**
     * SymbolTable constructor
     * @param size: the size of the SymbolTable
     */
    public SymbolTable(int size){ this.table = new Hashtable<>(size); }

    /**
     * lookup: look up an entry in the symbol table
     * @param key: a string key.
     * @return an entry in the SymbolTable.
     */
    public TableEntryInterface lookup(String key){ return this.table.get(key.toUpperCase()); }

    /**
     * insert: insert a value into the symbol table.
     * @param value: a TableEntry
     */
    public void insert(SymbolTableEntry value){
        String key = value.getName().toUpperCase();
        /* make sure that the name doesn't already exist */
        if(!table.containsKey(key)){ table.put(key, value); }
        else{ throw SymbolTableError.EntryAlreadyExists(value.getName()); }
    }

    /**
     * insert: insert a value into the symbol table.
     * @param key: a String
     * @param value: a TableEntry
     */
    public void insert(String key, SymbolTableEntry value){
        key = key.toUpperCase();
        /* make sure that the name doesn't already exist */
        if(!table.containsKey(key)){ table.put(key, value); }
        else{ throw SymbolTableError.EntryAlreadyExists(value.getName()); }
    }


    /**
     * size: return the size of the symbol table.
     * @return the size of the symbol table.
     */
    public int size(){ return table.size(); }

    /**
     * dumpTable: routine prints the symbol table contents
     */
    public void dumpTable(){
        System.out.println("Dumping Table ...");
        System.out.println("KEY --> ENTRY");
        for(String key : table.keySet()){
            System.out.println(key + " --> " + lookup(key));
        }

    }

    /**
     * installBuiltins: install the following reserved names in the symbol table.
     * @param symbolTable: a SymbolTable
     * The default -->
     */
    public static void installBuiltins(SymbolTable symbolTable) throws SymbolTableError{
        /* ::: reserved procedure names ::: */
        SymbolTableEntry main  = new ProcedureEntry("MAIN", 0, new LinkedList<ParameterInfo>());
        SymbolTableEntry read = new ProcedureEntry("READ", 0, new LinkedList<ParameterInfo>());
        SymbolTableEntry write = new ProcedureEntry("WRITE", 0, new LinkedList<ParameterInfo>());
        /* ::: reserved IO names ::: */
        SymbolTableEntry in = new IODeviceEntry("INPUT");
        SymbolTableEntry out = new IODeviceEntry("OUTPUT");
        /* Set reservation flag */
        main.setToReserved();
        read.setToReserved();
        write.setToReserved();
        in.setToReserved();
        out.setToReserved();
        /* add to the table */
        symbolTable.insert(main);
        symbolTable.insert(read);
        symbolTable.insert(write);
        symbolTable.insert(in);
        symbolTable.insert(out);
    }

    /**
     * installBuiltins: install the following reserved names in the symbol table.
     * @param symbolTable: a SymbolTable.
     * @param entryList: a list of reserved entries entries.
     * Method overload.
     * Customizable -->
     */
    public static void installBuiltins(SymbolTable symbolTable, LinkedList<SymbolTableEntry> entryList) throws SymbolTableError{
        /* ::: reserved names ::: */
        for(SymbolTableEntry entry : entryList){
            entry.setToReserved();
            symbolTable.insert(entry);
        }
    }
}