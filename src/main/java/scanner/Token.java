/**
 * FILE: Token.java
 *
 * DESC: contains the Token class describing a token.
 *       A token is a <TokenType, val> pair.
 *
 * @author Luis Serazo
 *
 */
package main.java.scanner;

/**
 * Token class
 */
public class Token{

    /* TokenType of a type */
    private TokenType tType;
    /* value of a token */
    private String val;

    /**
     * Token constructor
     * @param token a TokenType
     * @param val a String with the TokenType value
     */
    public Token(TokenType tType, String val){
        this.tType = tType;
        this.val = val;
    }

    /**
     * getTokenType: getter method for the token type
     * @reutrn the Token type
     */
    public TokenType getTokenType(){
        return this.tType;
    }

    /**
     * getVal: getter method for the value of the Token
     * @return the value of the Token
     */
    public String getVal(){
        return this.val;
    }

    /**
     * setTokenType: setter method for the Token type
     * @param tType, a TokenType
     */
    public void setTokenType(TokenType newType){
        this.tType = newType;
    }

    /**
     * setVal: setter method for the Token value
     * @param val, a String representation
     */
    public void setVal(String newVal){
        this.val = newVal;
    }

    /**
     * toString: returns a string representation of this Token
     * @return a String representation of this Token.
     */
    @Override
    public String toString(){
        return "< "+this.tType.getName()+" , "+this.val+" >";
    }

} /* end of Token class */
