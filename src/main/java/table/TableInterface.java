/*
 * File: TableInterface.java
 *
 * Desc: describes how a SymbolTable should behave.
 *
 */
package main.java.table;


/**
 * TableInterface
 * @author Luis Serazo
 */
public interface TableInterface{

    /* define method stubs. */

    TableEntryInterface lookup(String key);
    void insert(String key, SymbolTableEntry value);
    int size();
    void dumpTable();
}
