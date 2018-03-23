/*
 * File: Tokenizer.java
 *
 * Desc: contains the tokenizer with the get
 *       next Token method. Used in parsing.
 */
package main.java.lexer;

import main.java.lexer.errors.LexerError;
import main.java.token.Token;
import main.java.token.TokenType;

/**
 * Tokenizer class
 * @author Luis Serazo
 *
 */
public class Tokenizer {

    /*
     * Constants:
     * - SPACE: whitespace character.
     * - EOF: end of file character.
     * - ID_MAX: the max length of an identifier.
     *
     */
    private static final char SPACE = ' ';
    private static final char EXP = 'E';
    private static final char EOF = (char) -1;
    private static final int ID_MAX = 20;

    /*
     * Structures:
     * - fStream: a FileStream for file handling
     * - currentChar: the current character being analyzed (from file and push back)
     * - prevToken: the previous Token.
     * - keywords: a keyword map.
     * - punctuation: a punctuation map.
     *
     */
    private FileStream fStream;
    private char currentChar;
    private Token prevToken;
    private KeywordMap keywords;
    private PunctuationMap punctuation;
    private OperatorMap operators;

    /**
     * Tokenizer constructor
     *
     * @param filename, a file path for initializing the FileStream.
     */
    public Tokenizer(String filename) {
        this.keywords = new KeywordMap();
        this.punctuation = new PunctuationMap();
        this.operators = new OperatorMap();
        this.fStream = new FileStream(filename);
    }

    /**
     * isNumber: checks if the character is a number.
     *
     * @param chr, the character being checked.
     * @return True, if it's a number and False otherwise.
     */
    private boolean isNumber(char chr) {
        return (chr >= '0' && chr <= '9');
    }

    /**
     * isLetter: checks if the character is letter.
     *
     * @param chr, the character being checked.
     * @return True if it's a letter, and False otherwise.
     */
    private boolean isLetter(char chr) {
        return ((chr >= 'a' && chr <= 'z' || chr >= 'A' && chr <= 'Z'));
    }

    /**
     * isSpecialChar: checks if the character is a special character.
     *
     * @param chr, the character being checked.
     * @return True, if valid, False otherwise.
     */
    private boolean isSpecialChar(char chr) {
        return ((chr == '_') || (chr == '-'));
    }

    /**
     * validIdentStart: checks if the character is a valid identifier start.
     *
     * @param chr, the character being checked.
     * @return True, if valid, False otherwise.
     */
    private boolean validIdentStart(char chr) {
        return (isLetter(chr));
    }


    /**
     * validIdentBody: checks if the character is a valid identifier body.
     *
     * @param chr, the character being checked.
     * @return True, if valid, False otherwise.
     */
    private boolean validIdentBody(char chr) {
        return (isLetter(chr) || isNumber(chr));
    }


    /**
     * getPrevToken: getter method for the previous Token.
     * @return the previous Token
     */
    public Token getPrevToken() { return prevToken; }


    private boolean isPlusOrMinus(char chr){ return (chr == '+' || chr == '-'); }


    /**
     * getPrevTokenType: getter method for the previous Token created.
     *
     * @return this.prevTokenType
     */
    public TokenType prevTokenT() {
        return this.prevToken.getTokenType(); //.getTokenType();
    }

    /**
     * eofToken: routine if we've reached the EOF
     *
     * @return an EOF Token.
     */
    private Token eofToken() {
        /* bReader has already been closed */
        if (this.fStream.getBReader() == null) {
            /* then we've already set this.prevToken to EOF */
            return this.prevToken;
        } else {
            /* otherwise, close the file */
            fStream.closeFile();
            this.prevToken = new Token(TokenType.ENDOFFILE, null, fStream.getLineNum());
            return this.prevToken;
        }
    }


    /**
     * getNextToken: does the main work for tokenization.
     *
     * @return a Token
     */
    public Token getNextToken() {
        currentChar = fStream.nextChar();

        /* if SPACE, we get the next char and continue */
        if ((currentChar == SPACE || currentChar == '\n')) { return getNextToken(); }

        /* if we've reached EOF just keep returning EOF */
        if (currentChar == EOF) { return eofToken(); }

        if (punctuation.isPunctuation(currentChar)) {
            return this.prevToken = punctuation.getPunc(currentChar, fStream.getLineNum());
        }

        if (operators.isSimpleOp(currentChar)) {
            return this.prevToken = operators.getSimpleOP(currentChar, fStream.getLineNum());
        }

        if (operators.isSpecialOP(currentChar)) {
            /* pass control to the operator map */
            return this.prevToken = operators.resolveSpecialOp(currentChar, prevToken, fStream, punctuation);
        }

        if (isNumber(currentChar)) {
            return this.prevToken = getNumber(""+currentChar, fStream.getLineNum());
        }

        if (validIdentStart(currentChar)) {
            return this.prevToken = getIdent(""+currentChar, fStream.getLineNum());
        }

        throw LexerError.InvalidCharacter(fStream.getLineNum(), currentChar);
    }

    /**
     * getIdent:
     */
    private Token getIdent(String buffer, int lineNum) {
        currentChar = fStream.nextChar();
        /* consider using a String buffer instead */
        /* may not enter if just one char like: 'a;' */
        while (validIdentBody(currentChar) && buffer.length() <= ID_MAX) {
            buffer += currentChar;
            currentChar = fStream.nextChar();
        }
        if (buffer.length() > ID_MAX) {
            throw LexerError.IllegalIdentifierLength(fStream.getLineNum(), buffer, ID_MAX);
        }
        /* otherwise must have broken for the other condition */

        if (keywords.isKeyword(buffer)) { return keywords.getKeyword(buffer, lineNum); }

        /* otherwise was just a normal identifier */
        fStream.pushBack(currentChar);
        return new Token(TokenType.IDENTIFIER, buffer, lineNum);
    }


    private Token getReal(String buffer, int lineNum) {
        /* accumulate the decimal digits for the real */
        while (isNumber(currentChar)) {
            buffer += currentChar;
            currentChar = fStream.nextChar();
        }
        /* if the current character is not a number */
        if (Character.toUpperCase(currentChar) == EXP) { return getScientific(buffer, lineNum, false); }

        if (isLetter(currentChar)) {
            String identifier = illegalIdentAccumulator(buffer);
            throw LexerError.IllegalIdentifierName(lineNum, identifier);
        }
        // if letter ! identifiers must start with letters!!!! and can't have .

        fStream.pushBack(currentChar);
        return new Token(TokenType.REALCONSTANT, buffer, lineNum);
    }

    private Token getScientific(String buffer, int lineNum, boolean fromInt) {
        char lookAhead = fStream.nextChar();

        if(isNumber(lookAhead)){
            buffer += "" + EXP + lookAhead; // e + the number currentChar is E
            currentChar = fStream.nextChar();
            return getExp(buffer, lineNum, fromInt, true); // >>????
        }

        if(isPlusOrMinus(lookAhead)){
            char pastSign = fStream.nextChar();
            if(isNumber(pastSign)){
                buffer += ""+ EXP + lookAhead + pastSign ; // e ; +/- ; the number current is E
                currentChar = fStream.nextChar();
                return getExp(buffer, lineNum, fromInt, lookAhead == '+'); // GET EXP
            }
            else{
                throw LexerError.IllegalIdentifierName(lineNum, buffer+currentChar); // current char still e
            } // whatever it was
        }
        fStream.pushBack(lookAhead);
        String identifier = illegalIdentAccumulator(buffer+currentChar);
        throw LexerError.IllegalIdentifierName(lineNum, identifier);
    }


    private Token getExp(String buffer, int lineNum, boolean fromInt, boolean plusSign){ // after exp sign
        while(isNumber(currentChar)){
            buffer += currentChar;
            currentChar = fStream.nextChar();
        }
        if(isLetter(currentChar)){ throw LexerError.IllegalIdentifierName(lineNum, buffer); }
        /* break loop when encounters a non number */
        fStream.pushBack(currentChar);
        if(fromInt && plusSign){
            return new Token(TokenType.INTCONSTANT, buffer, lineNum);
        }
        return new Token(TokenType.REALCONSTANT, buffer, lineNum);
    }


    /**
     * RECURSIVE --- maybe use some dynamic programming.
     * @param buffer
     * @return
     */
    private Token getNumber(String buffer, int lineNum){
        currentChar = fStream.nextChar();

        if(currentChar == '.'){
            currentChar = fStream.nextChar();
            if(isNumber(currentChar)) {
                buffer += ".";
                return getReal(buffer, lineNum);
            }
            else{
                fStream.pushBack(currentChar);
                fStream.pushBack('.');
                return new Token(TokenType.INTCONSTANT, buffer, lineNum);
            }
        }

        if(Character.toUpperCase(currentChar) == EXP){ return getScientific(buffer, lineNum, true); }

        if(isNumber(currentChar)){
            buffer += currentChar;
            return getNumber(buffer, lineNum);
        }

        if(isLetter(currentChar)){
            String identifier = illegalIdentAccumulator(buffer);
            throw LexerError.IllegalIdentifierName(lineNum, identifier);
        }
        // check if its a letter >> identifiers can't start with numbers!!!!!!

        fStream.pushBack(currentChar);
        return new Token(TokenType.INTCONSTANT, buffer, lineNum);
    }

    private String illegalIdentAccumulator(String buffer){
        currentChar = fStream.nextChar();
        if(isNumber(currentChar) || isLetter(currentChar)){
            buffer += currentChar;
            return illegalIdentAccumulator(buffer);
        }
        return buffer;
    }

} /* end of Tokenizer class */