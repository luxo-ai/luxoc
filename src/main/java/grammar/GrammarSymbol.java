/*
 * File: GrammarSymbol.java
 *
 * Desc: contains the interface for a grammar symbol.
 *
 */

package main.java.grammar;

/**
 * GrammarSymbol interface
 * @author Luis Serazo
 */
public interface GrammarSymbol {

    /* method stubs */
    int getIndex();
    boolean isNonTerminal();
    boolean isToken();
    boolean isSemAction();

}
