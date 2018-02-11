/**
 * FILE: TokenType.java
 *
 * DESC: Contains TokenType enum
 *
 * @author Luis Serazo
 */
package main.java.lexer;

/**
 * TokenType: enum describing the token types.
 */
public enum TokenType {
    PROGRAM(0,"program"),
    BEGIN(1,"begin"),
    END(2,"end"),
    VAR(3,"var"),
    FUNCTION(4,"function"),
    PROCEDURE(5,"procedure"),
    RESULT(6,"result"),
    INTEGER(7,"integer"),
    REAL(8,"real"),
    ARRAY(9,"array"),
    OF(10,"of"),
    IF(11,"if"),
    THEN(12,"then"),
    ELSE(13,"else"),
    WHILE(14,"while"),
    DO(15,"do"),
    NOT(16,"not"),
    IDENTIFIER(17,"identifier"),
    INTCONSTANT(18,"intconstant"),
    REALCONSTANT(19,"realconstant"),
    RELOP(20,"relop"),
    MULOP(21,"mulop"),
    ADDOP(22,"addop"),
    ASSIGNOP(23,"assignop"),
    COMMA(24,"comma"),
    SEMICOLON(25,"semicolon"),
    COLON(26,"colon"),
    RIGHTPAREN(27,"rightparen"),
    LEFTPAREN(28,"leftparen"),
    RIGHTBRACKET(29,"rightbracket"),
    LEFTBRACKET(30,"leftbracket"),
    UNARYMINUS(31,"unaryminus"),
    UNARYPLUS(32,"unaryplus"),
    DOUBLEDOT(33,"doubledot"),
    ENDMARKER(34,"endmarker"),
    ENDOFFILE(35,"endoffile");

    /* name is the name of each type */
    private String name;
    private int num;

    /**
     * TokenType: enum constructor.
     * @param name, the name of the Token.
     */
    TokenType(int num, String name){
        this.num = num;
        this.name = name;
    }

    /**
     * getName: getter method for the name of the TokenType.
     * @return the name of the TokenType
     */
    public String getName(){
        return this.name;
    }

} /* end of TokenType enum */
