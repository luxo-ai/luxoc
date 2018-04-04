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
     * - EXP: scientific notation symbol.
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
     * - punctuation: a punctuation mapping.
     * - operators: an operator mapping.
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
     * getPrevToken: getter method for the previous Token.
     * @return the previous Token
     */
    public Token getPrevToken() { return this.prevToken; }

    /**
     * prevTokenT: returns the previous Token Type created.
     * @return the previous Token Type
     */
    public TokenType prevTokenT() { return this.prevToken.getTokenType(); }

    /**
     * isNumber: checks if the character is a number.
     *
     * @param chr, the character being checked.
     * @return True, if it's a number and False otherwise.
     */
    private boolean isNumber(char chr) { return (chr >= '0' && chr <= '9'); }

    /**
     * isLetter: checks if the character is letter.
     *
     * @param chr, the character being checked.
     * @return True if it's a letter, and False otherwise.
     */
    private boolean isLetter(char chr) { return ((chr >= 'a' && chr <= 'z' || chr >= 'A' && chr <= 'Z')); }

    /**
     * isSpecialChar: checks if the character is a special character.
     *
     * @param chr, the character being checked.
     * @return True, if valid, False otherwise.
     */
    private boolean isSpecialChar(char chr) { return ((chr == '_') || (chr == '-')); }

    /**
     * validIdentStart: checks if the character is a valid identifier start.
     *
     * @param chr, the character being checked.
     * @return True, if valid, False otherwise.
     */
    private boolean validIdentStart(char chr) { return (isLetter(chr)); }

    /**
     * validIdentBody: checks if the character is a valid identifier body.
     *
     * @param chr, the character being checked.
     * @return True, if valid, False otherwise.
     */
    private boolean validIdentBody(char chr) { return (isLetter(chr) || isNumber(chr)); }

    /**
     * isPlusOrMinus: checks if the character is either a plus or minus.
     *                The purpose of this method is for readability.
     * @param chr, the character being checked
     * @return True, if a plus or minus, False otherwise
     */
    private boolean isPlusOrMinus(char chr){ return (chr == '+' || chr == '-'); }

    /**
     * illegalAfterNum: checks if the current character is illegal after a number begining
     * @return True, if illegal after number, False otherwise
     */
    private boolean illegalAfterNum(char chr){ return (isLetter(chr) || chr == '.'); }

    /**
     * eofToken: routine if EOF has been reached. The routine closes the
     *           fStream upon initially reaching EOF.
     * @return an EOF Token.
     */
    private Token eofToken() {
        /* if bReader has already been closed, we've already set prevToken to EOF */
        if (this.fStream.getBReader() == null) { return this.prevToken; }
        /* otherwise, close the file */
        fStream.closeFile();
        return this.prevToken = new Token(TokenType.ENDOFFILE, null, fStream.getLineNum());
    }

    /**
     * Tokenizer is centered around this method.
     * getNextToken: retrieves the next Token in the file.
     * Note: contains small recursion.
     * @return the next Token in the file.
     */
    public Token getNextToken() {
        currentChar = fStream.nextChar();
        /* if SPACE, we get the next char and continue */
        if ((currentChar == SPACE || currentChar == '\n')) { return getNextToken(); } /* should only need do recursion once */
        /* if we've reached EOF just keep returning EOF */
        if (currentChar == EOF) { return eofToken(); }
        /* check if character is some kind of punctuation */
        if (punctuation.isPunctuation(currentChar)) {
            return this.prevToken = punctuation.getPunc(currentChar, fStream.getLineNum());
        }
        /* check it its an operator (both simple and complex) */
        if (operators.isSimpleOp(currentChar)) {
            return this.prevToken = operators.getSimpleOP(currentChar, fStream.getLineNum());
        }
        if (operators.isSpecialOP(currentChar)) {
            /* pass control to the operator map */
            return this.prevToken = operators.resolveSpecialOp(currentChar, prevToken, fStream, punctuation);
        }
        /* maybe it's a number. */
        if (isNumber(currentChar)) {
            return this.prevToken = getNumber(""+currentChar, fStream.getLineNum());
        }
        /* if not, try seeing if it's an identifier */
        if (validIdentStart(currentChar)) {
            return this.prevToken = getIdentifier(""+currentChar, fStream.getLineNum());
        }
        /* otherwise, it must have been something the language does not accept */
        throw LexerError.InvalidCharacter(fStream.getLineNum(), currentChar);
    }

    /**
     * getNumber: returns the constant at this point in the file.
     * Note: RECURSIVE
     * @param buffer: the number as a string.
     * @param lineNum: the line number where the number began in the file.
     * @return a real or integer constant Token.
     */
    private Token getNumber(String buffer, int lineNum){
        currentChar = fStream.nextChar();

        /* If the current character is a number */
        if(isNumber(currentChar)){
            buffer += currentChar;
            return getNumber(buffer, lineNum);
        }

        /* if dot is encountered */
        if(currentChar == '.'){
            currentChar = fStream.nextChar();
            /* check if a number follows the dot */
            if(isNumber(currentChar)) {
                buffer += ".";
                return getReal(buffer, lineNum);
            }
            /* else if the next char is a letter */
            if(isLetter(currentChar)){
                String illegalReal = illegalAccumulator(buffer+".");
                throw LexerError.IllegalRealConstant(lineNum, illegalReal);
            }
            /* otherwise, return an int constant */
            fStream.pushBack(currentChar);
            fStream.pushBack('.');
            return new Token(TokenType.INTCONSTANT, buffer, lineNum);
        }

        /* If the current character is an: e or E ==> Exp notation */
        if(Character.toUpperCase(currentChar) == EXP){ return getScientific(buffer, lineNum, true); }

        /* identifiers cannot begin with numbers */
        if(isLetter(currentChar)){
            String identifier = illegalAccumulator(buffer);
            throw LexerError.IllegalIdentifierName(lineNum, identifier);
        }

        /* return the general integer token */
        fStream.pushBack(currentChar);
        return new Token(TokenType.INTCONSTANT, buffer, lineNum);
    }

    /**
     * getReal: returns the real number at this point in the file.
     * @param buffer: the real number as a string.
     * @param lineNum: the line number where the real starts.
     * @return a real constant Token.
     */
    private Token getReal(String buffer, int lineNum) {
        /* accumulate the decimal digits for the real */
        while (isNumber(currentChar)) {
            buffer += currentChar;
            currentChar = fStream.nextChar();
        }
        /* if the current character is not a number */
        if (Character.toUpperCase(currentChar) == EXP) { return getScientific(buffer, lineNum, false); }

        /* real constants cannot cannot be followed by a letter immediately */
        if (isLetter(currentChar)) {
            String identifier = illegalAccumulator(buffer);
            throw LexerError.IllegalRealConstant(lineNum, identifier);
        }

        fStream.pushBack(currentChar);
        return new Token(TokenType.REALCONSTANT, buffer, lineNum);
    }

    /**
     * getScientific: returns the scientific notation number at this point in the file.
     * @param buffer: the SN number as a string.
     * @param lineNum: the line number where the SN number starts.
     * @param fromInt: if from integer context.
     * @return a real or integer constant Token.
     */
    private Token getScientific(String buffer, int lineNum, boolean fromInt) {
        char lookAhead = fStream.nextChar();

        if(isNumber(lookAhead)){
            buffer += "" + EXP + lookAhead; // e + the number currentChar is E
            currentChar = fStream.nextChar();
            return accumulateExp(buffer, lineNum, fromInt, false);
        }
        if(isPlusOrMinus(lookAhead)){
            char pastSign = fStream.nextChar();
            if(isNumber(pastSign)){
                buffer += "" + EXP + lookAhead + pastSign ; // e ; +/- ; (the number current is E)
                currentChar = fStream.nextChar();
                return accumulateExp(buffer, lineNum, fromInt, true);
            }
            else if(fromInt){ throw LexerError.IllegalIdentifierName(lineNum, buffer+currentChar); } // current char still e
            else{ throw LexerError.IllegalRealConstant(lineNum, buffer+currentChar); } // current char still e
        }
        fStream.pushBack(lookAhead);
        String illegalVal = illegalAccumulator(buffer); // will always be: E
        if(fromInt){ throw LexerError.IllegalIdentifierName(lineNum, illegalVal); }
        throw LexerError.IllegalRealConstant(lineNum, illegalVal);
    }

    /**
     * accumulateExp: an accumulator function for getScientific (helps out with the process).
     * @param buffer: the SN number as a string.
     * @param lineNum: the line number where the SN number starts.
     * @param fromInt: if from integer context.
     * @param withSign: if exp includes sign
     * @return a real or integer constant Token.
     */
    private Token accumulateExp(String buffer, int lineNum, boolean fromInt, boolean withSign){ // after exp sign
        String beforeSign = buffer.substring(0, buffer.length()-2); // remove the sign
        while(isNumber(currentChar)) {
            buffer += currentChar;
            currentChar = fStream.nextChar();
        }
        if(isLetter(currentChar) && withSign){
            if(fromInt){ throw  LexerError.IllegalIdentifierName(lineNum, beforeSign); }
            throw LexerError.IllegalRealConstant(lineNum, beforeSign);
        }

        if(isLetter(currentChar)){ /* used isLetter before */
            String ident = illegalAccumulator(buffer);
            if(fromInt){ throw LexerError.IllegalIdentifierName(lineNum, ident); }
            throw LexerError.IllegalRealConstant(lineNum, ident);
        }
        /* break loop when encounters a non number */
        fStream.pushBack(currentChar);
        return new Token(TokenType.REALCONSTANT, buffer, lineNum);
    }

    /**
     * getIdentifier: returns the identifier at this point in the file.
     * @param buffer: the identifier as a string.
     * @param lineNum: the line number where the identifier starts.
     * @return an identifier Token.
     */
    private Token getIdentifier(String buffer, int lineNum) {
        /* retrieve the next character after the start of the identifier */
        currentChar = fStream.nextChar();

        while (validIdentBody(currentChar) && buffer.length() <= ID_MAX) {
            buffer += currentChar;
            currentChar = fStream.nextChar();
        }
        /* check if the length exceeds the max */
        if (buffer.length() > ID_MAX) { throw LexerError.IllegalIdentifierLength(fStream.getLineNum(), buffer, ID_MAX); }

        /* otherwise must have not entered the loop for the other condition. Add back the current character */
        fStream.pushBack(currentChar);

        /* check if the identifier was actually a keyword */
        if (keywords.isKeyword(buffer)) { return keywords.getKeyword(buffer, lineNum); }

        /* otherwise was just a normal identifier */
        return new Token(TokenType.IDENTIFIER, buffer.toUpperCase(), lineNum);
    }

    /**
     * illegalAccumulator: accumulates the rest of an illegal identifier
     *                          after it's already been detected. Passed to
     *                          the LexicalError.
     * Note: RECURSIVE
     * @param buffer: the already accumulated illegal identifier up until the detection point.
     * @return the full illegal identifier.
     */
    private String illegalAccumulator(String buffer){
        if(isNumber(currentChar) || isLetter(currentChar)){
            buffer += currentChar;
            currentChar = fStream.nextChar();
            return illegalAccumulator(buffer);
        }
        return buffer;
    }

} /* end of Tokenizer class */