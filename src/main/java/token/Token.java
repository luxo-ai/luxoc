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


    /**
     * Token constructor
     * @param tType a TokenType
     * @param value a String with the TokenType valueue
     */
    public Token(TokenType tType, String value){
        this.tType = tType;
        this.value = value;
    }

    /**
     * getTokenType: getter method for the token type
     * @return the Token type
     */
    public TokenType getTokenType(){ return this.tType; }

    /**
     * getvalue: getter method for the valueue of the Token
     * @return the valueue of the Token
     */
    public String getvalue(){ return this.value; }

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
    public String toString(){ return "< "+this.tType.getName()+" , "+this.value+" >"; }

} /* end of Token class */
