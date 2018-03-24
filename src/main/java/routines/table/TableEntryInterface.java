/*
 * File: TableEntryInterface.java
 *
 * Desc: describes how a Symbol Table Entry should behave.
 *
 */

package main.java.routines.table;

/**
 * TableEntryInterface
 * @author Luis Serazo
 */
public interface TableEntryInterface {

    /* method stubs */
    public boolean isVariable();
    @Deprecated
    public boolean isKeyword();
    public boolean isProcedure();
    public boolean isFunction();
    public boolean isFunctionResult();
    public boolean isParameter();
    public boolean isArray();
    @Deprecated
    public boolean isConstant();
    public boolean isReserved();

}
