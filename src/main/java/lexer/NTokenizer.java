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
public class NTokenizer {

    /*
     * Constants:
     * - EOF: end of file character.
     * - SPACE: whitespace character.
     * - ID_MAX: the max length of an identifier.
     *
     */
    private static final char SPACE = ' ';
    private static final char EOF = (char) -1;
    private static final char BLANK = ' ';
    private static final int ID_MAX = 20;

    /*
     * Structures:
     * - fStream: a FileStream for file handling
     * - currentChar: the current character being analyzed (from file and push back)
     * - prevToken: the previous Token.
     *
     */
    private FileStream fStream;
    private char currentChar;
    private Token prevToken;

    /**
     * Tokenizer constructor
     *
     * @param filename, a file path for initializing the FileStream.
     */
    public Tokenizer(String filename) {
        this.fStream = new FileStream(filename);
        this.currentChar = fStream.getFileChar();
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
     * getKeyword: checks the string buffer and decides which kind of keyword is
     * represented. and returns a Token for that operator.
     *
     * @param buffer, the string buffer.
     * @return a Token
     */
    private Token getKeyword(String buffer) {
        /* make adjustment to lowercase */
        buffer = buffer.toLowerCase();
        // just one big switch
        switch (buffer) {

            case "program":
                this.prevToken = new Token(TokenType.PROGRAM, null);
                return this.prevToken;

            case "begin":
                this.prevToken = new Token(TokenType.BEGIN, null);
                return this.prevToken;

            case "end":
                this.prevToken = new Token(TokenType.END, null);
                return this.prevToken;

            case "var":
                this.prevToken = new Token(TokenType.VAR, null);
                return this.prevToken;

            case "function":
                this.prevToken = new Token(TokenType.FUNCTION, null);
                return this.prevToken;

            case "procedure":
                this.prevToken = new Token(TokenType.PROCEDURE, null);
                return this.prevToken;

            case "result":
                this.prevToken = new Token(TokenType.RESULT, null);
                return this.prevToken;

            case "integer":
                this.prevToken = new Token(TokenType.INTEGER, null);
                return this.prevToken;

            case "real":
                this.prevToken = new Token(TokenType.REAL, null);
                return this.prevToken;

            case "array":
                this.prevToken = new Token(TokenType.ARRAY, null);
                return this.prevToken;

            case "of":
                this.prevToken = new Token(TokenType.OF, null);
                return this.prevToken;

            case "if":
                this.prevToken = new Token(TokenType.IF, null);
                return this.prevToken;

            case "then":
                this.prevToken = new Token(TokenType.THEN, null);
                return this.prevToken;

            case "else":
                this.prevToken = new Token(TokenType.ELSE, null);
                return this.prevToken;

            case "while":
                this.prevToken = new Token(TokenType.WHILE, null);
                return this.prevToken;

            case "do":
                this.prevToken = new Token(TokenType.DO, null);
                return this.prevToken;

            case "not":
                this.prevToken = new Token(TokenType.NOT, null);
                return this.prevToken;

            default:
                return null;
        }
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
     * getSpecialOP: checks the string buffer and decides which kind of operator is
     * represented and returns a Token for that operator.
     *
     * @param buffer, the string buffer.
     * @return a Token
     */
    private Token getSpecialOP(String buffer) {
        /* make adjustment to lowercase */
        buffer = buffer.toLowerCase();
        /* little switch */
        switch (buffer) {
            case "or":
                this.prevToken = new Token(TokenType.ADDOP, "3");
                return this.prevToken;

            case "div":
                this.prevToken = new Token(TokenType.MULOP, "3");
                return this.prevToken;

            case "mod":
                this.prevToken = new Token(TokenType.MULOP, "4");
                return this.prevToken;

            case "and":
                this.prevToken = new Token(TokenType.MULOP, "5");
                return this.prevToken;

            default:
                return null;
        }
    }

    /**
     * isSpecialOp
     *
     * @param buffer
     * @return
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
    private TokenType prevTokenT() {
        return this.prevToken.getTokenType();
    }

    /**
     * eofToken: routine if we've reached the EOF
     *
     * @return an EOF Token.
     */
    private Token eofToken() {
        /* bReader has already been closed */
        if (this.fStream.getReader() == null) {
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
     * getPunctuation:
     *
     * @return the punctuation Token.
     */
    private Token getPunctuation(char chr) {
        /* one big switch */
        switch (chr) {
            case ',':
                this.prevToken = new Token(TokenType.COMMA, null);
                return this.prevToken;
            case ';':
                this.prevToken = new Token(TokenType.SEMICOLON, null);
                return this.prevToken;
            case ')':
                this.prevToken = new Token(TokenType.RIGHTPAREN, null);
                return this.prevToken;
            case '(':
                this.prevToken = new Token(TokenType.LEFTPAREN, null);
                return this.prevToken;
            case ']':
                this.prevToken = new Token(TokenType.RIGHTBRACKET, null);
                return this.prevToken;
            case '[':
                this.prevToken = new Token(TokenType.LEFTBRACKET, null);
                return this.prevToken;
            default:
                return null; /* should never reach default */

        }
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
                if (!isBinaryAddop()) {
                    this.prevToken = new Token(TokenType.UNARYPLUS, null);
                    return this.prevToken;
                }
                this.prevToken = new Token(TokenType.ADDOP, "1");
                return this.prevToken;

            case '-':
                currentChar = fStream.nextChar();
                if (!isBinaryAddop()) {
                    this.prevToken = new Token(TokenType.UNARYMINUS, null);
                    return this.prevToken;
                }
                this.prevToken = new Token(TokenType.ADDOP, "2");
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
        boolean real = false;
        boolean ident = false;

        /* main loop */
        while (true) {
            /* if we've reached EOF just keep returning EOF */
            if (currentChar == EOF) {
                return eofToken();
            }

            /* if SPACE, we get the next char and continue */
            if (currentChar == SPACE || currentChar == '\n') {
                currentChar = fStream.nextChar();
                continue;
            }

            if (isPunctuation(currentChar) && buffer.isEmpty()) {
                // jump to punctuation subroutine
                char prev = currentChar; /* this is confusing may want to put this in getPunc logic */
                currentChar = fStream.nextChar();
                return getPunctuation(prev);

            }

            if (isOperator(currentChar) && buffer.isEmpty()) {
                // jump to operator subroutine
                char prev = currentChar;
                currentChar = fStream.nextChar();
                return getOperator(prev);
            }

            // NonTerminals
            if (binaryDecision(currentChar) && buffer.isEmpty()) {
                return resolveBinary(currentChar);
            }

            /* IDENTIFIERS AND NUMBERS */
            if (isNumber(currentChar) && !real && !ident) {
                buffer += currentChar;
                currentChar = fStream.nextChar();

                /* this is a parse error .. but could handle here I guess? */
                // if(isLetter(currentChar)){ throw LexerError.IllegalIdentifierName(3, "Identifiers cannot start with numbers"); }

                if (currentChar == '.') {
                    char prev = currentChar;
                    currentChar = fStream.nextChar();
                    // check if numeric ?
                    if (isNumber(currentChar)) {
                        real = true;
                        buffer += prev; /* add the decimal to the buffer */
                    } else {
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

            if (real && !ident) {
                /* came here because we saw a .  (this was added to the buffer). current char is a number */
                buffer += currentChar; // add the number following the decimal point.
                currentChar = fStream.nextChar();

                if (!isNumber(currentChar)) {
                    /* scientific notation */
                    if (currentChar == 'e') {
                        // handle this.
                    } else {
                        // push current ?
                        this.prevToken = new Token(TokenType.REALCONSTANT, buffer);
                        return this.prevToken;
                    }
                }
                /* otherwise was a number and was keep adding stuff on */
                else {
                    continue;
                }
            }

            if (isLetter(currentChar) && !ident) {
                if (validIdentStart(currentChar)) {
                    ident = true;
                    buffer += currentChar;
                    currentChar = fStream.nextChar();
                } else {
                    throw LexerError.IllegalIdentifierName(3, "illegal");
                }
            }

            if (ident) {
                buffer += currentChar;
                currentChar = fStream.nextChar();
                if (buffer.length() > ID_MAX) {
                    throw LexerError.IllegalIdentifierLength(fStream.getLineNum(), buffer, ID_MAX);
                }

                if (!validIdentBody(currentChar)) {
                    if (isKeyword(buffer)) {
                        this.prevToken = getKeyword(buffer);
                        return this.prevToken;
                    }
                    if (isSpecialOp(buffer)) {
                        this.prevToken = getSpecialOP(buffer);
                        return this.prevToken;
                    }
                    // push current back on stack?
                    /* otherwise */
                    this.prevToken = new Token(TokenType.IDENTIFIER, buffer);
                    return this.prevToken;
                }
                /* otherwise if valid ident body */
            }
        }
    }
}









