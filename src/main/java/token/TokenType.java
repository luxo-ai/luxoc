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
    PROGRAM(0, "program"),
    BEGIN(1, "begin"),
    END(2, "end"),
    VAR(3, "var"),
    FUNCTION(4, "function"),
    PROCEDURE(5, "procedure"),
    RESULT(6, "result"),
    INTEGER(7, "integer"),
    REAL(8, "real"),
    ARRAY(9, "array"),
    OF(10, "of"),
    IF(11, "if"),
    THEN(12, "then"),
    ELSE(13, "else"),
    WHILE(14, "while"),
    DO(15, "do"),
    NOT(16, "not"),
    IDENTIFIER(17, "identifier"),
    INTCONSTANT(18, "intconstant"),
    REALCONSTANT(19, "realconstant"),
    RELOP(20, "relop"),
    MULOP(21, "mulop"),
    ADDOP(22, "addop"),
    ASSIGNOP(23, "assignop"),
    COMMA(24, "comma"),
    SEMICOLON(25, "semicolon"),
    COLON(26, "colon"),
    RIGHTPAREN(27, "rightparen"),
    LEFTPAREN(28, "leftparen"),
    RIGHTBRACKET(29, "rightbracket"),
    LEFTBRACKET(30, "leftbracket"),
    UNARYMINUS(31, "unaryminus"),
    UNARYPLUS(32, "unaryplus"),
    DOUBLEDOT(33, "doubledot"),
    ENDMARKER(34, "endmarker"),
    ENDOFFILE(35, "endoffile");

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
    public String getName(){
        return this.name;
    }


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
