/*
 * File: TableInterface.java
 *
 * Desc: describes how a SymbolTable should behave.
 *
 */
package main.java.routines.table;


/**
 * TableInterface
 * @author Luis Serazo
 */
public interface TableInterface{

    /* define method stubs */

    public TableEntryInterface lookup(String key);
    public void insert(String key, SymbolTableEntry value);
    public int size();
    public void dumpTable();
    public void installBuiltins();

}
