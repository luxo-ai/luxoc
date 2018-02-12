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
import java.util.HashMap;

public class Tokenizer {

    /* constants */
    private static final char EOF = (char) -1;
    private static final char BLANK = ' ';


    private FileStream fStream;
    private HashMap symbolTable; // TODO: specify what kind of HashMap
    private char currentChar;



    public Tokenizer(String filename) {

        this.fStream = new FileStream(filename);
        this.symbolTable = new HashMap();
        this.currentChar = fStream.getFileChar();

    }

    /**
     * test if numeric value
     */
    private boolean isNumber(char chr) {
        return (chr >= '0' && chr <= '9');
    }

    /**
     * test if alphabetical
     */
    private boolean isLetter(char chr) {
        return ((chr >= 'a' && chr <= 'z' || chr >= 'A' && chr <= 'Z'));
    }

    /**
     *
     */
    private boolean isSpecialChar(char chr) {
        return ((chr == '_') || (chr == '-'));
    }

    /**
     *
     */
    private boolean validIdentStart(char chr) {
        return (isLetter(chr));
    }

    /**
     *
     */
    private boolean validIdentBody(char chr) {
        return (isLetter(chr) || isNumber(chr));
    }

    private boolean validIdentTerminal(char chr) {
        return (chr == BLANK || chr == ';' || chr == ':' || chr == '(' || chr == '[' || chr == '.');
    }


    /**
     * return null if not a keyword
     */
    private Token getKeyword(String buffer) {
        /* make adjustment to lowercase */
        buffer = buffer.toLowerCase();
        // just one big switch
        switch (buffer) {

            case "program":
                return new Token(TokenType.PROGRAM, null);

            case "begin":
                return new Token(TokenType.BEGIN, null);

            case "end":
                return new Token(TokenType.END, null);

            case "var":
                return new Token(TokenType.VAR, null);

            case "function":
                return new Token(TokenType.FUNCTION, null);

            case "procedure":
                return new Token(TokenType.PROCEDURE, null);

            case "result":
                return new Token(TokenType.RESULT, null);

            case "integer":
                return new Token(TokenType.INTEGER, null);

            case "real":
                return new Token(TokenType.REAL, null);

            case "array":
                return new Token(TokenType.ARRAY, null);

            case "of":
                return new Token(TokenType.OF, null);

            case "if":
                return new Token(TokenType.IF, null);

            case "then":
                return new Token(TokenType.THEN, null);

            case "else":
                return new Token(TokenType.ELSE, null);

            case "while":
                return new Token(TokenType.WHILE, null);

            case "do":
                return new Token(TokenType.DO, null);

            case "not":
                return new Token(TokenType.NOT, null);

            default:
                return null;
        }
    }

    private Token getSpecialOP(String buffer) {
        /* make adjustment to lowercase */
        buffer = buffer.toLowerCase();
        /* little switch */
        switch (buffer) {
            case "or":
                return new Token(TokenType.ADDOP, "3");

            case "div":
                return new Token(TokenType.MULOP, "3");

            case "mod":
                return new Token(TokenType.MULOP, "4");

            case "and":
                return new Token(TokenType.MULOP, "5");

            default:
                return null;
        }
    }


    public int strToInt(String buffer) {
        try {
            return Integer.parseInt(buffer);
        } catch (NumberFormatException ex) {
            System.out.println("Unable To Convert Int" + ex);
        }
        return -1;
    }

    public long strToReal(String buffer) {
        try {
            return Long.parseLong(buffer);
        } catch (NumberFormatException ex) {
            System.out.println("Unable To Convert Real" + ex);
        }
        return -1;
    }


    /**
     *
     */
    public Token getNextToken() {
        String buffer = "";
        /* we always start in this state */
        StateType state = StateType.TERMINAL;

        /* loop through the DFA until we arrive at a final state */
        while (true) {
            if (currentChar == EOF) {
                fStream.closeFile();
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
                            return new Token(TokenType.COMMA, null); /* returning breaks all */

                        case ';':
                            currentChar = fStream.nextChar();
                            return new Token(TokenType.SEMICOLON, null);

                        case ')':
                            currentChar = fStream.nextChar();
                            return new Token(TokenType.RIGHTPAREN, null);

                        case '(':
                            currentChar = fStream.nextChar();
                            return new Token(TokenType.LEFTPAREN, null);

                        case ']':
                            currentChar = fStream.nextChar();
                            return new Token(TokenType.RIGHTBRACKET, null);

                        case '[':
                            currentChar = fStream.nextChar();
                            return new Token(TokenType.LEFTBRACKET, null);

                        /* Operators */
                        case '*':
                            currentChar = fStream.nextChar();
                            return new Token(TokenType.MULOP, "1");

                        case '/':
                            currentChar = fStream.nextChar();
                            return new Token(TokenType.MULOP, "2");

                        case '=':
                            currentChar = fStream.nextChar();
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
                                return new Token(TokenType.ASSIGNOP, null);
                            }

                        case '.':
                            currentChar = fStream.nextChar();

                            /* check if double dot (..) */
                            if (currentChar == '.') {
                                /* remove the second dot */
                                fStream.popStack(1);
                                currentChar = fStream.nextChar();
                                return new Token(TokenType.DOUBLEDOT, null);
                            }
                            /* check if real */
                            else if (isNumber(currentChar)) {
                                state = StateType.REAL;
                            }

                            /* check if its an end marker */
                            else if (currentChar == EOF || currentChar == BLANK) {
                                return new Token(TokenType.ENDMARKER, null);
                            } else {
                                /* otherwise its probably something illegal */
                                throw new LexerError("Illegal Syntax");
                            }
                            continue;

                            /* Operators */
                        case '+':
                            currentChar = fStream.nextChar();
                            if (validIdentStart(currentChar) || isNumber(currentChar)) {
                                return new Token(TokenType.UNARYPLUS, null);
                            }
                            return new Token(TokenType.ADDOP, "1");

                        case '-':
                            currentChar = fStream.nextChar();
                            if (validIdentStart(currentChar) || isNumber(currentChar)) {
                                return new Token(TokenType.UNARYMINUS, null);
                            }
                            return new Token(TokenType.ADDOP, "2");


                        case '<':
                            currentChar = fStream.nextChar();

                            if (currentChar == '>') {
                                currentChar = fStream.nextChar();
                                return new Token(TokenType.RELOP, "2");
                            } else if (currentChar == '=') {
                                currentChar = fStream.nextChar();
                                return new Token(TokenType.RELOP, "5");
                            }
                            return new Token(TokenType.RELOP, "3");
                               // throw new LexerError("Illegal Syntax"); /* can't have '<4'? */
                            //continue; /* TODO: move into Error State tell user the line number */


                        case '>':
                            currentChar = fStream.nextChar();
                            if (currentChar == '=') {
                                currentChar = fStream.nextChar();
                                return new Token(TokenType.RELOP, "6");
                            }
                            return new Token(TokenType.RELOP, "4");
                               // throw new LexerError("Illegal Syntax");
                           // continue; /* TODO: move into Error state tell user the line number */

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
                            return new Token(TokenType.INTCONSTANT, buffer); /* TODO: is this ok? (buffer is string) */
                        }
                        /* otherwise, this must have been a real */
                        buffer += prevChar;
                        state = StateType.REAL;
                    }
                    /* if its a blank, then we're done with the int */
                    else {
                        return new Token(TokenType.INTCONSTANT, buffer); /* TODO: is this ok? (buffer is string) */
                    }
                    continue;



                    /* if things are real (after the decimal point) */
                    /* TODO: scientific notation ????? */
                case REAL:
                    if (isNumber(currentChar)) {
                        /* keep adding to the buffer */
                        buffer += currentChar;
                        currentChar = fStream.nextChar();
                    } else {
                        /* real ended */
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
                    } else {
                        throw new LexerError("Invalid Syntax (invalid name)"); // TODO: handle comments like: {comment }}
                        /* TODO: ^ the same */
                        // TODO: look at number 2 for lex conventions.
                        // TODO: handle scientific notation
                        // TODO: handle identifier too long
                        // TODO: handle error recovery.
                    }
                    continue;

                    /* must be an identifier or keyword */
                case IDENTIFIER:
                    if (validIdentBody(currentChar)) {
                        /* collect everything */
                        buffer += currentChar;
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

                        /* otherwise, it was just a normal identifier */
                        return new Token(TokenType.IDENTIFIER, buffer);
                    }
                    continue;

                    /* something went wrong */
                case ERROR:
                    return null;


                /* never should need to get here */
                default:
                    return null;

            } /* end of state switch */
        } /* end of while loop */
    } /* end of getNextToken method */
} /* end of Tokenizer class */