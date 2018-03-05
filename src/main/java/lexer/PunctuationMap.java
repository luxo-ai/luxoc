/*
 * File: PunctuationMap.java
 *
 * Desc: a wrapper class for a HashMap.
 *
 */
package main.java.lexer;

import main.java.token.Token;
import main.java.token.TokenType;
import java.util.HashMap;

/**
 * PunctuationMap class
 * @author Luis Serazo
 */
public class PunctuationMap {

    private HashMap<Character, Token> punctuation;
    /**
     * PunctuationMap constructor
     */
    public PunctuationMap(){
        this.punctuation = new HashMap<>();

        /* populate the hashmap */
        punctuation.put(',', new Token(TokenType.COMMA, null));
        punctuation.put(';', new Token(TokenType.SEMICOLON, null));
        punctuation.put(')', new Token(TokenType.RIGHTPAREN, null));
        punctuation.put('(', new Token(TokenType.LEFTPAREN, null));
        punctuation.put(']', new Token(TokenType.RIGHTBRACKET, null));
        punctuation.put('[', new Token(TokenType.LEFTBRACKET, null));
    }

    /**
     * getPunc
     * @param punc: a punctuation character
     * @return the associated Token
     */
    public Token getPunc(char punc){
        return this.punctuation.get(punc);
    }
}
