/**
 * File: StateType.java
 *
 * Desc: Contains the types of states that fsm can be in
 *
 * @author Luis Serazo
 *
 */

package main.java.lexer;

public enum StateType {
    TERMINAL,
    DECISION,
    NUMERIC,
    INTEGER,
    REAL,
    EXPON,
    ALPHABETIC,
    IDENTIFIER
}
