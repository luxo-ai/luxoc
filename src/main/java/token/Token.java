/*
 * FILE: Token.java
 *
 * DESC: contains the Token class describing a token.
 *       A token is a <TokenType, value> pair.
 *
 */
package main.java.token;

/**
 * Token class
 * @author Luis Serazo
 */
public class Token{

    /* TokenType of a type */
    private final TokenType tType;
    /* value of a token */
    private final String value;
    /* metadata */
    private int lineNum;

    private OperatorType opT;
    /// TODO ?????
    public enum OperatorType{
        EQ,
        NEQ,
        LESS_THAN,
        LEQ,
        GREATER_THAN,
        GEQ,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        DIV,
        MOD,
        AND,
        OR,
        NOT
    }
    /**
     * Token constructor
     * @param tType a TokenType
     * @param value a String with the TokenType valueue
     */
    public Token(TokenType tType, String value, int lineNum){
        this.tType = tType;
        this.value = value;
        this.lineNum = lineNum;
        this.opT = null;
    }

    /**
     * second Token constructor
     * @param tType a TokenType
     * @param value a String with the TokenType valueue
     */
    public Token(TokenType tType, String value){
        this.tType = tType;
        this.value = value;
        this.lineNum = 0; // TODO: this must be modified later.
        this.opT = null;
    }

    /**
     * Token constructor
     * @param tType a TokenType
     * @param value a String with the TokenType valueue
     */
    public Token(TokenType tType, String value, int lineNum, OperatorType opT){
        this.tType = tType;
        this.value = value;
        this.lineNum = lineNum;
        this.opT = opT;
    }

    /**
     * second Token constructor
     * @param tType a TokenType
     * @param value a String with the TokenType valueue
     */
    public Token(TokenType tType, String value, OperatorType opT){
        this.tType = tType;
        this.value = value;
        this.lineNum = 0; // TODO: this must be modified later.
        this.opT = opT;
    }

    public OperatorType getOpType(){ return this.opT; }


    /**
     * getTokenType: getter method for the token type
     * @return the Token type
     */
    public TokenType getTokenType(){ return this.tType; }

    /**
     * getValue: getter method for the value of the Token
     * @return the value of the Token
     */
    public String getValue(){ return this.value; }

    /**
     * isEOF: checks if the current Token is EOF
     * @return True if the Token is EOF, False otherwise.
     */
    public boolean isEOF(){ return this.tType == TokenType.ENDOFFILE; }

    /**
     * getTypeIndex:
     * @return the Token type index
     */
    public int getTypeIndex(){ return this.tType.getIndex(); }

    /**
     * toString: returns a string representation of this Token
     * @return a String representation of this Token.
     */
    @Override
    public String toString(){ return "[ "+this.tType.getName()+" , "+this.value+" ]"; }

    /**
     * getLineNum:
     */
    public int getLineNum(){ return this.lineNum; }

    /**
     * setLineNum:
     */
    public void setLineNum(int lineNum){ this.lineNum = lineNum; }

} /* end of Token class */
