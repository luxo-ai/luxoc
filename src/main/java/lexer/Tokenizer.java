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

    /**
     * Tokenizer constructor
     *
     * @param filename, a file path for initializing the FileStream.
     */
    public Tokenizer(String filename) {
        this.keywords = new KeywordMap();
        this.punctuation = new PunctuationMap();
        this.fStream = new FileStream(filename);
        this.currentChar = fStream.nextChar(); // skip the first, its a duplicate.
       // this.currentChar = fStream.getFileChar();

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
     * setPrevToken: setter method sets the previous Token.
     *
     * @param prevToken: the previous Token
     */
    private void setPrevToken(Token prevToken) {
        this.prevToken = prevToken;
    }


    /**
     * isKeyword:
     */
    private boolean isKeyword(String buffer) {
        buffer = buffer.toLowerCase();

        return (buffer.equals("program") ||
                buffer.equals("begin") ||
                buffer.equals("end") ||
                buffer.equals("var") ||
                buffer.equals("function") ||
                buffer.equals("procedure") ||
                buffer.equals("result") ||
                buffer.equals("integer") ||
                buffer.equals("real") ||
                buffer.equals("array") ||
                buffer.equals("of") ||
                buffer.equals("if") ||
                buffer.equals("then") ||
                buffer.equals("else") ||
                buffer.equals("while") ||
                buffer.equals("do") ||
                buffer.equals("not"));
    }


    /**
     * isSpecialOp
     * @param buffer a String buffer
     * @return True if the buffer is a special operator, False otherwise.
     */
    private boolean isSpecialOp(String buffer) {
        buffer = buffer.toLowerCase();
        return (buffer.equals("or") ||
                buffer.equals("div") ||
                buffer.equals("mod") ||
                buffer.equals("and"));
    }


    /**
     * isBinaryAddop: checks if the previousTokenType is a binary addition operator.
     *
     * @return True if binary and false if unary
     */
    private boolean isBinaryAddop() {
        return ((this.prevToken.getTokenType() == TokenType.RIGHTPAREN) ||
                (this.prevToken.getTokenType() == TokenType.RIGHTBRACKET) ||
                (this.prevToken.getTokenType() == TokenType.IDENTIFIER) ||
                (this.prevToken.getTokenType() == TokenType.INTCONSTANT) ||
                (this.prevToken.getTokenType() == TokenType.REALCONSTANT));
    }

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
            this.prevToken = new Token(TokenType.ENDOFFILE, null);
            return this.prevToken;
        }
    }

    /**
     * isPunctuation: determines if the character is a punctuation char.
     *
     * @return True if the char is punctuation, False otherwise.
     */
    private boolean isPunctuation(char chr) {
        return (chr == ',' ||
                chr == ';' ||
                chr == ')' ||
                chr == '(' ||
                chr == ']' ||
                chr == '[');
    }

    /**
     * isOperator: determines if the character is an operator.
     *
     * @return True if the char is an operator, False otherwise.
     */
    private boolean isOperator(char chr) {
        return (chr == '*' ||
                chr == '/' ||
                chr == '=');
    }


    /**
     * getOperator:
     *
     * @return the operator Token
     */
    private Token getOperator(char chr) {
        /* one big switch */
        switch (chr) {
            case '*':
                this.prevToken = new Token(TokenType.MULOP, "1");
                return this.prevToken;
            case '/':
                this.prevToken = new Token(TokenType.MULOP, "2");
                return this.prevToken;
            case '=':
                this.prevToken = new Token(TokenType.RELOP, "1");
                return this.prevToken;
            default:
                return null; /* this should never happen */
        }
    }

    /**
     * binaryDecision:
     *
     * @return True if binary decision, False otherwise.
     */
    private boolean binaryDecision(char chr) {
        return (chr == ':' ||
                chr == '.' ||
                chr == '+' ||
                chr == '-' ||
                chr == '<' ||
                chr == '>');
    }

    /**
     * resolveBinary:
     *
     * @return Token of the binary operator or etc
     */
    private Token resolveBinary(char chr) {

        switch (chr) {
            /* Punctuation */
            case ':':
                currentChar = fStream.nextChar();
                if (currentChar != '=') {
                    return new Token(TokenType.COLON, null);
                }
                currentChar = fStream.nextChar();
                this.prevToken = new Token(TokenType.ASSIGNOP, null);
                return this.prevToken;

            case '.':
                currentChar = fStream.nextChar();
                /* check if double dot (..) */
                if (currentChar == '.') {
                    /* remove the second dot */
                    fStream.popStack(1);
                    currentChar = fStream.nextChar();
                    this.prevToken = new Token(TokenType.DOUBLEDOT, null);
                    return this.prevToken;
                }
                /* if preceded by a number ?. No need to consider this case since buffer is "" */
                /* check if its an end marker */
                this.prevToken = new Token(TokenType.ENDMARKER, null);
                return this.prevToken;


            /* Operators */
            case '+':
                currentChar = fStream.nextChar();
                if (isBinaryAddop()) {
                    this.prevToken = new Token(TokenType.ADDOP, "1");
                    return this.prevToken;
                }
                this.prevToken = new Token(TokenType.UNARYPLUS, null);
                return this.prevToken;


            case '-':
                currentChar = fStream.nextChar();
                if (isBinaryAddop()) {
                    this.prevToken = new Token(TokenType.ADDOP, "2");
                    return this.prevToken;
                }
                this.prevToken = new Token(TokenType.UNARYMINUS, null);
                return this.prevToken;


            case '<':
                currentChar = fStream.nextChar();

                if (currentChar == '>') {
                    currentChar = fStream.nextChar();
                    this.prevToken = new Token(TokenType.RELOP, "2");
                    return this.prevToken;

                } else if (currentChar == '=') {
                    currentChar = fStream.nextChar();
                    this.prevToken = new Token(TokenType.RELOP, "5");
                    return this.prevToken;
                }
                this.prevToken = new Token(TokenType.RELOP, "3");
                return this.prevToken;

            case '>':
                currentChar = fStream.nextChar();
                if (currentChar == '=') {
                    currentChar = fStream.nextChar();
                    this.prevToken = new Token(TokenType.RELOP, "6");
                    return this.prevToken;
                }
                this.prevToken = new Token(TokenType.RELOP, "4");
                return this.prevToken;

            default:
                return null; /* don't expect to get here */
        }
    }


    /**
     * getNextToken: does the main work for tokenization.
     *
     * @return a Token
     */
    public Token getNextToken() {
        String buffer = "";
        /* main loop */
        while (true) {
            /* if we've reached EOF just keep returning EOF */
            if (currentChar == EOF) {
                return eofToken();
            }

            /* if SPACE, we get the next char and continue */
            if ((currentChar == SPACE || currentChar == '\n') && buffer.isEmpty()) {
                currentChar = fStream.nextChar();
            }

            if (isPunctuation(currentChar) && buffer.isEmpty()) {
                // jump to punctuation subroutine
                char prev = currentChar; /* this is confusing may want to put this in getPunc logic */
                currentChar = fStream.nextChar();
                this.prevToken = punctuation.getPunc(prev);
                return this.prevToken;
            }

            if (isOperator(currentChar) && buffer.isEmpty()) {
                // jump to operator subroutine // why do this? if not doing it else where (num + ident)
                char prev = currentChar;
                currentChar = fStream.nextChar();
                return getOperator(prev);
            }

            // NonTerminals
            if (binaryDecision(currentChar) && buffer.isEmpty()) {
                return resolveBinary(currentChar);
            }

            if (isNumber(currentChar)) {
                buffer += currentChar;
                currentChar = fStream.nextChar();

                if (currentChar == '.') {
                    char prev = currentChar;
                    currentChar = fStream.nextChar();
                    // check if numeric ?
                    if (isNumber(currentChar)) {
                        buffer += prev; /* add the decimal to the buffer */
                        return getReal(buffer);
                        // GO INTO REAL CASE
                    } else {
                        // Need to set the current char ????
                        /* anything else needs to be pushed back and must have been end of the number (double dot handled above) */
                        fStream.pushBack(currentChar);
                        fStream.pushBack(prev);
                        this.prevToken = new Token(TokenType.INTCONSTANT, buffer);
                        return this.prevToken;
                    }
                }
                /* anything else */
                else if (!isNumber(currentChar)) {
                    this.prevToken = new Token(TokenType.INTCONSTANT, buffer);
                    return this.prevToken;
                }
                continue;
            }

            if (validIdentStart(currentChar)) {
                buffer += currentChar;
                currentChar = fStream.nextChar();
                return getIdent(buffer);
            }
        }
    }

    /**
     * getIdent:
     */
    private Token getIdent(String buffer) {
        // MAYBE USE A STRING BUFFER....
        /* may not enter if just one char like: 'a;' */
        while (validIdentBody(currentChar) && buffer.length() <= ID_MAX) {
            buffer += currentChar;
            currentChar = fStream.nextChar();
        }
        if (buffer.length() > ID_MAX) {
            throw LexerError.IllegalIdentifierLength(fStream.getLineNum(), buffer, ID_MAX);
        }
        /* otherwise must have broken for the other condition */

        if (isKeyword(buffer) || isSpecialOp(buffer)) {
            this.prevToken = keywords.getKeyword(buffer);
            return this.prevToken;
        }
        /* otherwise was just a normal identifier */
        // push back ???
        this.prevToken = new Token(TokenType.IDENTIFIER, buffer);
        return this.prevToken;
    }


    private Token getReal(String buffer) {
        /* accumulate the decimal digits for the real */
        while (isNumber(currentChar)) {
            buffer += currentChar;
            currentChar = fStream.nextChar();
        }
        /* if the current character is not a number */
        if (currentChar == 'e') {
            char lookAhead = fStream.nextChar();
            if(isNumber(lookAhead)){
                buffer += currentChar; // e
                buffer += lookAhead;  // the number
                currentChar = fStream.nextChar();
                return getExp(buffer); // GET EXP
            }
            else if(isExpSign(lookAhead)){
                char pastSign = fStream.nextChar();
                if(isNumber(pastSign)){
                    buffer += currentChar; // e
                    buffer += lookAhead; // +/-
                    buffer += pastSign; // the number
                    currentChar = fStream.nextChar();
                    return getExp(buffer); // GET EXP
                }
                else{
                    fStream.pushBack(pastSign); // whatever it was
                    fStream.pushBack(lookAhead); // +/-
                    /* currentChar is left as e, as desired */
                    this.prevToken = new Token(TokenType.REALCONSTANT, buffer);
                    return this.prevToken;
                }
            }
            /* otherwise was just a subsequent identifier. */
            else{
                fStream.pushBack(lookAhead);
                this.prevToken = new Token(TokenType.REALCONSTANT, buffer);
                return this.prevToken;
            }
        } else {
            this.prevToken = new Token(TokenType.REALCONSTANT, buffer);
            return this.prevToken;
        }
    }

    private boolean isExpSign(char chr){
        return (chr == '+' || chr == '-');
    }

    private Token getExp(String buffer){
        while(isNumber(currentChar)){
            buffer += currentChar;
            currentChar = fStream.nextChar();
        }
        /* break loop when encounters a non number */
        this.prevToken = new Token(TokenType.REALCONSTANT, buffer);
        return this.prevToken;
    }

    public int getLineNum(){
        return fStream.getLineNum();
    }

} /* end of Tokenizer class */