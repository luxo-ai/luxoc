/*
 * FILE: TokenType.java
 *
 * DESC: Contains TokenType enum
 *
 */
package main.java.token;

import main.java.grammar.GrammarSymbol;

/**
 * TokenType: enum describing the token types.
 * @author Luis Serazo
 */
public enum TokenType implements GrammarSymbol {
    PROGRAM(0, "PROGRAM"),
    BEGIN(1, "BEGIN"),
    END(2, "END"),
    VAR(3, "VAR"),
    FUNCTION(4, "FUNCTION"),
    PROCEDURE(5, "PROCEDURE"),
    RESULT(6, "RESULT"),
    INTEGER(7, "INTEGER"),
    REAL(8, "REAL"),
    ARRAY(9, "ARRAY"),
    OF(10, "OF"),
    IF(11, "IF"),
    THEN(12, "THEN"),
    ELSE(13, "ELSE"),
    WHILE(14, "WHILE"),
    DO(15, "DO"),
    NOT(16, "NOT"),
    IDENTIFIER(17, "IDENTIFIER"),
    INTCONSTANT(18, "INTCONSTANT"),
    REALCONSTANT(19, "REALCONSTANT"),
    RELOP(20, "RELOP"),
    MULOP(21, "MULOP"),
    ADDOP(22, "ADDOP"),
    ASSIGNOP(23, "ASSIGNOP"),
    COMMA(24, "COMMA"),
    SEMICOLON(25, "SEMICOLON"),
    COLON(26, "COLON"),
    RIGHTPAREN(27, "RIGHTPAREN"),
    LEFTPAREN(28, "LEFTPAREN"),
    RIGHTBRACKET(29, "RIGHTBRACKET"),
    LEFTBRACKET(30, "LEFTBRACKET"),
    UNARYMINUS(31, "UNARYMINUS"),
    UNARYPLUS(32, "UNARYPLUS"),
    DOUBLEDOT(33, "DOUBLEDOT"),
    ENDMARKER(34, "ENDMARKER"),
    ENDOFFILE(35, "ENDOFFILE");

    /* name is the name of each type */
    private String name;
    private int index;

    /**
     * TokenType: enum constructor.
     * @param index: the index of the Token.
     * @param name: the name of the Token.
     */
    TokenType(int index, String name){
        this.index = index;
        this.name = name;
    }

    /**
     * getIndex: returns the index of this GrammarSymbol
     * @return the GS index
     */
    public int getIndex(){ return this.index; }

    /**
     * getName: getter method for the name of the TokenType.
     * @return the name of the TokenType
     */
    public String getName(){ return this.name; }

    /**
     * isToken: determines if this GrammarSymbol is a Token
     * @return True if the GS is a Token, False otherwise
     */
    public boolean isToken(){ return true; }

    /**
     * isNonTerminal: determines if this GrammarSymbol is a NonTerminal
     * @return True if the GS is a NonTerminal, False otherwise
     */
    public boolean isNonTerminal(){ return false; }

    /**
     * isSemAction: determines if the GrammarSymbol is a SemanticAction
     * @return True if the GS is a SemanticAction, False otherwise
     */
    public boolean isSemAction(){ return false; }

} /* end of TokenType enum */
