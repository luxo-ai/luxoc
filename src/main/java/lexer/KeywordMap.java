/*
 * File: KeywordMap.java
 *
 * Desc: a wrapper class for a HashMap.
 *
 */
package main.java.lexer;

import main.java.token.Token;
import main.java.token.TokenType;
import java.util.HashMap;

/**
 * KeywordMap class
 * @author Luis Serazo
 */
public class KeywordMap {

    private HashMap<String, Token> keywords;

    /**
     * Keyword map constructor.
     * Note: constructor is package-private.
     */
    KeywordMap(){
        this.keywords = new HashMap<>();

        /* populate the hashmap */
        keywords.put("PROGRAM", new Token(TokenType.PROGRAM, null));
        keywords.put("BEGIN", new Token(TokenType.BEGIN, null));
        keywords.put("END", new Token(TokenType.END, null));
        keywords.put("VAR", new Token(TokenType.VAR, null));
        keywords.put("FUNCTION", new Token(TokenType.FUNCTION, null));
        keywords.put("PROCEDURE", new Token(TokenType.PROCEDURE, null));
        keywords.put("RESULT", new Token(TokenType.RESULT, null));
        keywords.put("INTEGER", new Token(TokenType.INTEGER, null));
        keywords.put("REAL", new Token(TokenType.REAL, null));
        keywords.put("ARRAY", new Token(TokenType.ARRAY, null));
        keywords.put("OF", new Token(TokenType.OF, null));
        keywords.put("IF", new Token(TokenType.IF, null));
        keywords.put("THEN", new Token(TokenType.THEN, null));
        keywords.put("ELSE", new Token(TokenType.ELSE, null));
        keywords.put("WHILE", new Token(TokenType.WHILE, null));
        keywords.put("DO", new Token(TokenType.DO, null));
        keywords.put("NOT", new Token(TokenType.NOT, null));

        /* special operators */
        keywords.put("OR", new Token(TokenType.ADDOP, "3"));
        keywords.put("DIV", new Token(TokenType.MULOP, "3"));
        keywords.put("MOD", new Token(TokenType.MULOP, "4"));
        keywords.put("AND", new Token(TokenType.MULOP, "5"));
    }

    /**
     * isKeyword: determines if the buffer is a keyword or not.
     * @param buffer: a collection of characters in the file.
     * @return True, if the buffer is a keyword, False otherwise.
     */
    public boolean isKeyword(String buffer){
        String upperCase = buffer.toUpperCase();
        return keywords.containsKey(upperCase);
    }

    /**
     * getKeyword: returns the correct keyword from the hashmap.
     * @param buffer: the accumulated keyword as a string.
     * @param lineNum: the line number where the keyword began in the file.
     * @return a keyword Token matching the buffer.
     */
    public Token getKeyword(String buffer, int lineNum){
        buffer = buffer.toUpperCase();
        Token keywordToken = keywords.get(buffer);
        keywordToken.setLineNum(lineNum);
        return keywordToken;
    }

} /* end of KeywordMap class */