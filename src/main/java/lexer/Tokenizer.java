/**
 * FILE: Tokenizer
 *
 * DESC: contains the tokenizer class.
 *
 * @author Luis Serazo
 *
 */
package main.java.lexer;
import main.java.lexer.errors.LexerError;
import main.java.token.Token;
import main.java.token.TokenType;

public class Tokenizer {

    /* constants */
    /* EOF: end of file character */
    private static final char EOF = (char) -1;
    /* BLANK: a blank space character */
    private static final char BLANK = ' ';
    /* ID_MAX_LEN: the max length of any identifier */
    private static final int ID_MAX_LEN = 100;
    /* EXP: scientific notation character */
    private static final char EXP = 'e';

    /* fStream, a FileStream */
    private FileStream fStream;
    /* currentChar, the current character being analyzed */
    private char currentChar;
    /* prevTokenType the TokenType of the last created Token */
    private TokenType prevTokenType;

    /**
     * Tokenizer constructor
     * @param filename, a file path for initializing the FileStream.
     */
    public Tokenizer(String filename) {
        this.fStream = new FileStream(filename);
        this.currentChar = fStream.getFileChar();
    }

    /**
     * isNumber: checks if the character is a number.
     * @param chr, the character being checked.
     * @return True, if it's a number and False otherwise.
     */
    private boolean isNumber(char chr) {
        return (chr >= '0' && chr <= '9');
    }

    /**
     * isLetter: checks if the character is letter.
     * @param chr, the character being checked.
     * @return True if it's a letter, and False otherwise.
     */
    private boolean isLetter(char chr) {
        return ((chr >= 'a' && chr <= 'z' || chr >= 'A' && chr <= 'Z'));
    }

    /**
     * isSpecialChar: checks if the character is a special character.
     * @param chr, the character being checked.
     * @return True, if valid, False otherwise.
     */
    private boolean isSpecialChar(char chr) {
        return ((chr == '_') || (chr == '-'));
    }

    /**
     * validIdentStart: checks if the character is a valid identifier start.
     * @param chr, the character being checked.
     * @return True, if valid, False otherwise.
     */
    private boolean validIdentStart(char chr) {
        return (isLetter(chr));
    }


    /**
     * validIdentBody: checks if the character is a valid identifier body.
     * @param chr, the character being checked.
     * @return True, if valid, False otherwise.
     */
    private boolean validIdentBody(char chr) {
        return (isLetter(chr) || isNumber(chr));
    }


    /**
     * getKeyword: checks the string buffer and decides which kind of keyword is represented.
     *             and returns a Token for that operator.
     * @param buffer, the string buffer.
     * @return a Token
     */
    private Token getKeyword(String buffer) {
        /* make adjustment to lowercase */
        buffer = buffer.toLowerCase();
        // just one big switch
        switch (buffer) {

            case "program":
                prevTokenType = TokenType.PROGRAM;
                return new Token(TokenType.PROGRAM, null);

            case "begin":
                prevTokenType = TokenType.BEGIN;
                return new Token(TokenType.BEGIN, null);

            case "end":
                prevTokenType = TokenType.END;
                return new Token(TokenType.END, null);

            case "var":
                prevTokenType = TokenType.VAR;
                return new Token(TokenType.VAR, null);

            case "function":
                prevTokenType = TokenType.FUNCTION;
                return new Token(TokenType.FUNCTION, null);

            case "procedure":
                prevTokenType = TokenType.PROCEDURE;
                return new Token(TokenType.PROCEDURE, null);

            case "result":
                prevTokenType = TokenType.RESULT;
                return new Token(TokenType.RESULT, null);

            case "integer":
                prevTokenType = TokenType.INTEGER;
                return new Token(TokenType.INTEGER, null);

            case "real":
                prevTokenType = TokenType.REAL;
                return new Token(TokenType.REAL, null);

            case "array":
                prevTokenType = TokenType.ARRAY;
                return new Token(TokenType.ARRAY, null);

            case "of":
                prevTokenType = TokenType.OF;
                return new Token(TokenType.OF, null);

            case "if":
                prevTokenType = TokenType.IF;
                return new Token(TokenType.IF, null);

            case "then":
                prevTokenType = TokenType.THEN;
                return new Token(TokenType.THEN, null);

            case "else":
                prevTokenType = TokenType.ELSE;
                return new Token(TokenType.ELSE, null);

            case "while":
                prevTokenType = TokenType.WHILE;
                return new Token(TokenType.WHILE, null);

            case "do":
                prevTokenType = TokenType.DO;
                return new Token(TokenType.DO, null);

            case "not":
                prevTokenType = TokenType.NOT;
                return new Token(TokenType.NOT, null);

            default:
                return null;
        }
    }

    /**
     * getSpecialOP: checks the string buffer and decides which kind of operator is represented.
     *             and returns a Token for that operator.
     * @param buffer, the string buffer.
     * @return a Token
     */
    private Token getSpecialOP(String buffer) {
        /* make adjustment to lowercase */
        buffer = buffer.toLowerCase();
        /* little switch */
        switch (buffer) {
            case "or":
                prevTokenType = TokenType.ADDOP;
                return new Token(TokenType.ADDOP, "3");

            case "div":
                prevTokenType = TokenType.MULOP;
                return new Token(TokenType.MULOP, "3");

            case "mod":
                prevTokenType = TokenType.MULOP;
                return new Token(TokenType.MULOP, "4");

            case "and":
                prevTokenType = TokenType.MULOP;
                return new Token(TokenType.MULOP, "5");

            default:
                return null;
        }
    }


    /**
     * isBinaryAddop: checks if the previousTokenType is a binary addition operator.
     * @return True if binary and false if unary
     */
    private boolean isBinaryAddop(){
        return ((prevTokenType == TokenType.RIGHTPAREN) ||
                (prevTokenType == TokenType.RIGHTBRACKET) ||
                (prevTokenType == TokenType.IDENTIFIER) ||
                (prevTokenType == TokenType.INTCONSTANT) ||
                (prevTokenType == TokenType.REALCONSTANT));
    }

    /**
     * getPrevTokenType: getter method for the previous Token created.
     * @return this.prevTokenType
     */
    public TokenType getPrevTokenType(){
        return this.prevTokenType;
    }


    /**
     * getNextToken: does the main work for tokenization.
     * @return a Token
     */
    public Token getNextToken() {
        String buffer = "";
        int buffer_len = 0;
        /* we always start in this state */
        StateType state = StateType.TERMINAL;

        /* loop through the DFA until we arrive at a final state */
        while (true) {
            if (currentChar == EOF) {
                fStream.closeFile();
                prevTokenType = TokenType.ENDOFFILE;
                return new Token(TokenType.ENDOFFILE, null);
            }
            /* otherwise */
            switch (state) {

                /* terminal symbols */
                case TERMINAL:
                    /* types of terminal symbols */
                    switch (currentChar) {

                        case BLANK:
                            currentChar = fStream.nextChar(); /* skip blanks */
                            continue;

                            /* Punctuation (have null values) */
                        case ',':
                            currentChar = fStream.nextChar();
                            prevTokenType = TokenType.COMMA;
                            return new Token(TokenType.COMMA, null); /* returning breaks all */

                        case ';':
                            currentChar = fStream.nextChar();
                            prevTokenType = TokenType.SEMICOLON;
                            return new Token(TokenType.SEMICOLON, null);

                        case ')':
                            currentChar = fStream.nextChar();
                            prevTokenType = TokenType.RIGHTPAREN;
                            return new Token(TokenType.RIGHTPAREN, null);

                        case '(':
                            currentChar = fStream.nextChar();
                            prevTokenType = TokenType.LEFTPAREN;
                            return new Token(TokenType.LEFTPAREN, null);

                        case ']':
                            currentChar = fStream.nextChar();
                            prevTokenType = TokenType.RIGHTBRACKET;
                            return new Token(TokenType.RIGHTBRACKET, null);

                        case '[':
                            currentChar = fStream.nextChar();
                            prevTokenType = TokenType.LEFTBRACKET;
                            return new Token(TokenType.LEFTBRACKET, null);

                        /* Operators */
                        case '*':
                            currentChar = fStream.nextChar();
                            prevTokenType = TokenType.MULOP;
                            return new Token(TokenType.MULOP, "1");

                        case '/':
                            currentChar = fStream.nextChar();
                            prevTokenType = TokenType.MULOP;
                            return new Token(TokenType.MULOP, "2");

                        case '=':
                            currentChar = fStream.nextChar();
                            prevTokenType = TokenType.RELOP;
                            return new Token(TokenType.RELOP, "1");

                        default:
                            state = StateType.DECISION; /* jump to the next types */
                            continue;
                    }

                case DECISION:
                    /* types of double takes */
                    switch (currentChar) {

                        /* Punctuation */
                        case ':':
                            currentChar = fStream.nextChar();
                            if (currentChar != '=') {
                                return new Token(TokenType.COLON, null);
                            } else {
                                currentChar = fStream.nextChar();
                                prevTokenType = TokenType.ASSIGNOP;
                                return new Token(TokenType.ASSIGNOP, null);
                            }

                        case '.':
                            currentChar = fStream.nextChar();

                            /* check if double dot (..) */
                            if (currentChar == '.') {
                                /* remove the second dot */
                                fStream.popStack(1);
                                currentChar = fStream.nextChar();
                                prevTokenType = TokenType.DOUBLEDOT;
                                return new Token(TokenType.DOUBLEDOT, null);
                            }
                            /* check if real */

                            /* check if its an end marker */
                            else {
                                prevTokenType = TokenType.ENDMARKER;
                                return new Token(TokenType.ENDMARKER, null);
                            }

                            /* Operators */
                        case '+':
                            currentChar = fStream.nextChar();
                            //if (validIdentStart(currentChar) || isNumber(currentChar)) {
                            if(!isBinaryAddop()){
                                prevTokenType = TokenType.UNARYPLUS;
                                return new Token(TokenType.UNARYPLUS, null);
                            }
                            prevTokenType = TokenType.ADDOP;
                            return new Token(TokenType.ADDOP, "1");

                        case '-':
                            currentChar = fStream.nextChar();
                            if(!isBinaryAddop()){
                                prevTokenType = TokenType.UNARYMINUS;
                                return new Token(TokenType.UNARYMINUS, null);
                            }
                            prevTokenType = TokenType.ADDOP;
                            return new Token(TokenType.ADDOP, "2");


                        case '<':
                            currentChar = fStream.nextChar();

                            if (currentChar == '>') {
                                currentChar = fStream.nextChar();
                                prevTokenType = TokenType.RELOP;
                                return new Token(TokenType.RELOP, "2");
                            } else if (currentChar == '=') {
                                currentChar = fStream.nextChar();
                                prevTokenType = TokenType.RELOP;
                                return new Token(TokenType.RELOP, "5");
                            }
                            prevTokenType = TokenType.RELOP;
                            return new Token(TokenType.RELOP, "3");


                        case '>':
                            currentChar = fStream.nextChar();
                            if (currentChar == '=') {
                                currentChar = fStream.nextChar();
                                prevTokenType = TokenType.RELOP;
                                return new Token(TokenType.RELOP, "6");
                            }
                            prevTokenType = TokenType.RELOP;
                            return new Token(TokenType.RELOP, "4");

                        default:
                            state = StateType.NUMERIC; /* move into next state */
                            continue;
                    }

                    /* checks to see if things are numeric */
                case NUMERIC:
                    if (isNumber(currentChar)) {
                        /* if the char is a number, we add it to the buffer */
                        buffer += currentChar;
                        currentChar = fStream.nextChar();
                        /* move to state int */
                        state = StateType.INTEGER;
                    } else {
                        /* otherwise, probably is alphabetic */
                        state = StateType.ALPHABETIC;
                    }
                    continue;

                    /* checks for integers or numeric things in general */
                case INTEGER:
                    if (isNumber(currentChar)) {
                        /* keep adding to the buffer thinking its an int */
                        buffer += currentChar;
                        currentChar = fStream.nextChar();
                    }
                    /* either an array or a real */
                    else if (currentChar == '.') {
                        /* record this dot */
                        char prevChar = currentChar;
                        /* move to the next */
                        currentChar = fStream.nextChar();
                        /* check if it's also a dot */
                        if (currentChar == '.') {
                            /* if it is, then we push back the double dot */
                            fStream.pushBack(currentChar);
                            fStream.pushBack(currentChar);
                            /* and return the int constant */
                            prevTokenType = TokenType.INTCONSTANT;
                            return new Token(TokenType.INTCONSTANT, buffer); /* TODO: is this ok? (buffer is string) */
                        }
                        /* otherwise, this must have been a real */
                        if(isNumber(currentChar)) {
                            state = StateType.REAL;
                            buffer += prevChar;
                        }
                        else{

                            fStream.pushBack(prevChar);
                            prevTokenType = TokenType.INTCONSTANT;
                            return new Token(TokenType.INTCONSTANT, buffer);
                        }
                    }
                    /* if its a blank, then we're done with the int */
                    else {
                        prevTokenType = TokenType.INTCONSTANT;
                        return new Token(TokenType.INTCONSTANT, buffer); /* TODO: is this ok? (buffer is string) */
                    }
                    continue;

                case EXPON:
                    String old_buff = buffer;
                    buffer += EXP;
                    char prev;
                    if(isNumber(currentChar) || currentChar == '+' || currentChar == '-'){
                        prev = currentChar;
                        buffer+=currentChar;
                        currentChar = fStream.nextChar();
                        if(isNumber(currentChar)){
                            buffer+=currentChar;
                            while(isNumber(currentChar)){
                                currentChar = fStream.nextChar();
                                if(!isNumber(currentChar)){
                                    prevTokenType = TokenType.REALCONSTANT;
                                    return new Token(TokenType.REALCONSTANT, buffer);
                                }
                                buffer += currentChar;
                            }
                        }
                        else if(isNumber(prev)){
                            prevTokenType = TokenType.REALCONSTANT;
                            return new Token(TokenType.REALCONSTANT, buffer);
                        }
                        else{
                           fStream.pushBack(currentChar);
                           fStream.pushBack(prev);

                           currentChar = EXP;
                           prevTokenType = TokenType.REALCONSTANT;
                           return new Token(TokenType.REALCONSTANT, old_buff);
                        }

                    }
                    else{
                        fStream.pushBack(currentChar);
                        currentChar = EXP;
                        prevTokenType = TokenType.REALCONSTANT;
                        return new Token(TokenType.REALCONSTANT, old_buff);

                    }

                case REAL:
                    if(Character.toLowerCase(currentChar) == EXP){
                        currentChar = fStream.nextChar();
                        state = StateType.EXPON;
                    }

                    else if (isNumber(currentChar)) {
                        /* keep adding to the buffer */
                        buffer += currentChar;
                        currentChar = fStream.nextChar();
                    } else {
                        /* real ended */
                        prevTokenType = TokenType.REALCONSTANT;
                        return new Token(TokenType.REALCONSTANT, buffer);
                    }
                    continue;

                    /* check if things are alphabetic  */
                case ALPHABETIC:
                    buffer += currentChar;
                    if (validIdentStart(currentChar)) {
                        state = StateType.IDENTIFIER;
                        /* move on */
                        currentChar = fStream.nextChar();
                        buffer_len++;
                    } else {
                        throw LexerError.IllegalIdentifierName(fStream.getLineNum(), buffer);
                    }
                    continue;

                    /* must be an identifier or keyword */
                case IDENTIFIER:

                    if (validIdentBody(currentChar)) {
                        /* collect everything */
                        buffer += currentChar;
                        buffer_len++;
                        currentChar = fStream.nextChar();
                    }
                    else{
                        /* if its a blank, we're done with the identifier */
                        Token keyword = getKeyword(buffer);
                        /* first, we check if its a keyword */
                        if (keyword != null) {
                            return keyword;
                        }

                        /* if not a keyword, we check if its a special operator */
                        Token oper = getSpecialOP(buffer);
                        if (oper != null) {
                            return oper;
                        }

                        if(buffer_len > ID_MAX_LEN){ throw LexerError.IllegalIdentifierLength(fStream.getLineNum(), buffer, ID_MAX_LEN); }

                        /* otherwise, it was just a normal identifier */
                        prevTokenType = TokenType.IDENTIFIER;
                        return new Token(TokenType.IDENTIFIER, buffer);
                    }
                    continue;

                /* never should need to get here */
                default:
                    state = StateType.TERMINAL;

            } /* end of state switch */
        } /* end of while loop */
    } /* end of getNextToken method */
} /* end of Tokenizer class */