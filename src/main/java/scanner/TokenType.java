/**
 * FILE: TokenType.java
 *
 * DESC: Contains TokenType enum
 *
 * @author Luis Serazo
 */
package main.java.scanner;

/**
 * TokenType: enum describing the token types.
 */
public enum TokenType {
    PROGRAM("program"),
    BEGIN("begin"),
    END("end"),
    VAR("var"),
    FUNCTION("function"),
    PROCEDURE("procedure"),
    RESULT("result"),
    INTEGER("integer"),
    REAL("real"),
    ARRAY("array"),
    OF("of"),
    IF("if"),
    THEN("then"),
    ELSE("else"),
    WHILE("while"),
    DO("do"),
    NOT("not"),
    IDENTIFIER("identifier"),
    INTCONSTANT("intconstant"),
    REALCONSTANT("realconstant"),
    RELOP("relop"),
    MULOP("mulop"),
    ADDOP("addop"),
    ASSIGNOP("assignop"),
    COMMA("comma"),
    SEMICOLON("semicolon"),
    COLON("colon"),
    RIGHTPAREN("rightparen"),
    LEFTPAREN("leftparen"),
    RIGHTBRACKET("rightbracket"),
    LEFTBRACKET("leftbracket"),
    UNARYMINUS("unaryminus"),
    UNARYPLUS("unaryplus"),
    DOUBLEDOT("doubledot"),
    ENDMARKER("endmarker"),
    ENDOFFILE("endoffile");

    /* name is the name of each type */
    private String name;

    /**
     * TokenType: enum constructor.
     * @param name, the name of the Token.
     */
    TokenType(String name){
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
