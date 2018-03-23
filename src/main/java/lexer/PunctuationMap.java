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
    PunctuationMap(){
        this.punctuation = new HashMap<>();

        /* populate the hashmap */
        punctuation.put(',', new Token(TokenType.COMMA, null));
        punctuation.put(';', new Token(TokenType.SEMICOLON, null));
        punctuation.put(':', new Token(TokenType.COLON, null));
        punctuation.put(')', new Token(TokenType.RIGHTPAREN, null));
        punctuation.put('(', new Token(TokenType.LEFTPAREN, null));
        punctuation.put(']', new Token(TokenType.RIGHTBRACKET, null));
        punctuation.put('[', new Token(TokenType.LEFTBRACKET, null));
        punctuation.put('.', new Token(TokenType.ENDMARKER, null));
    }

    /**
     * getPunc
     * @param punc: a punctuation character
     * @return the associated Token
     */
    public Token getPunc(char punc, int lineNum){
        Token puncToken = this.punctuation.get(punc);
        puncToken.setLineNum(lineNum);
        return puncToken;
    }


    /**
     * isPunctuation: determines if the character is a punctuation char.
     *
     * @return True if the char is punctuation, False otherwise.
     */
    protected boolean isPunctuation(char chr) {
        return (chr == ',' ||
                chr == ';' ||
                chr == ')' ||
                chr == '(' ||
                chr == ']' ||
                chr == '[');
    }
}