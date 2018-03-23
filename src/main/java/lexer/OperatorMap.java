/*
 * File: OperatorMap.java
 *
 * Desc: a wrapper class for a HashMap.
 *
 */
package main.java.lexer;

import main.java.token.Token;
import main.java.token.TokenType;
import java.util.HashMap;

/**
 * OperatorMap class
 * @author Luis Serazo
 */
public class OperatorMap {
    /* Hash aggregates used as dictionaries */
    private HashMap<Character, Token> simpleOp;
    private HashMap<String, Token> specialOP;

    /**
     * OperatorMap Constructor
     */
    OperatorMap(){
        this.simpleOp = new HashMap<>();
        this.specialOP = new HashMap<>();

        /* populate the map */
        simpleOp.put('*', new Token(TokenType.MULOP, "1"));
        simpleOp.put('/', new Token(TokenType.MULOP, "2"));
        simpleOp.put('=', new Token(TokenType.RELOP, "1"));
        simpleOp.put('+', new Token(TokenType.ADDOP, "1"));
        simpleOp.put('-', new Token(TokenType.ADDOP, "2"));
        simpleOp.put('<', new Token(TokenType.RELOP, "3"));
        simpleOp.put('>', new Token(TokenType.RELOP, "4"));
        /* populate the map with double take operators */
        specialOP.put(":=", new Token(TokenType.ASSIGNOP, null));
        specialOP.put("..", new Token(TokenType.DOUBLEDOT, null));
        specialOP.put("<=", new Token(TokenType.RELOP, "5"));
        specialOP.put(">=", new Token(TokenType.RELOP, "6"));
        specialOP.put("<>", new Token(TokenType.RELOP, "2"));
        specialOP.put("^+", new Token(TokenType.ADDOP, "1"));
        specialOP.put("^-", new Token(TokenType.ADDOP, "2"));
    }

    /**
     * isSimpleOP: indicates if a character is an operator that can be determined right away.
     * @param chr: the character being investigated.
     * @return True, if the character can be determined right away, False otherwise.
     */
    public boolean isSimpleOp(char chr){
        return (chr == '*' || chr == '/' || chr == '=');
    }

    /**
     * isSpecialOP: indicates if a character is an operator that needs some investigation
     *              be determined.
     * @param chr: the character being investigated.
     * @return True, if the character cannot be determined right away, False otherwise.
     */
    public boolean isSpecialOP(char chr){
        return (chr == ':' || chr == '.' || chr == '+' || chr == '-' || chr == '<' || chr == '>');
    }

    /**
     * isBinaryAddop: checks if the previous TokenType is a binary addition operator.
     * @return True if meets the conditions for a binary operator, False otherwise.
     */
    private boolean isBinaryAddop(Token prevToken) {
        return ((prevToken.getTokenType() == TokenType.RIGHTPAREN) ||
                (prevToken.getTokenType() == TokenType.RIGHTBRACKET) ||
                (prevToken.getTokenType() == TokenType.IDENTIFIER) ||
                (prevToken.getTokenType() == TokenType.INTCONSTANT) ||
                (prevToken.getTokenType() == TokenType.REALCONSTANT));
    }

    /**
     * getSimpleOP: returns a simple operator from the hashmap.
     * @param op: the operator in question.
     * @param lineNum: the line number where the operator occurs in the file.
     * @return an operator Token.
     */
    protected Token getSimpleOP(char op, int lineNum){
        Token opToken = this.simpleOp.get(op);
        opToken.setLineNum(lineNum);
        return opToken;
    }

    /**
     * getSpecialOP: returns one of the "complex" operators from the hashmap.
     * @param op: the operator in question
     * @param lineNum: the line number where the operator starts in the file.
     * @return an operator Token.
     */
    private Token getSpecialOP(String op, int lineNum){
        Token opToken = this.specialOP.get(op);
        opToken.setLineNum(lineNum);
        return opToken;
    }

    /**
     * resolveSpecialOp: uses look ahead and push back to determine the operator.
     * @param op: the first char of the operator.
     * @param prevToken: the previous Token created (used by isBinaryAddop)
     * @param fStream: the fileStream --> control passed from the Tokenizer.
     * @param punctuation: the Tokenizer punctuation map.
     * @return a operator Token.
     *
     * Note: the default is null but this should not be a problem, since we only enter this
     *       function if isSpecialOp returns true. Thus, one of the cases must match.
     */
    public Token resolveSpecialOp(char op, Token prevToken, FileStream fStream, PunctuationMap punctuation){
        int lineNum = fStream.getLineNum();
        char next = fStream.nextChar();
        switch(op){
            case ':':
                if(next == '='){ return getSpecialOP(":=", lineNum); }
                fStream.pushBack(next);
                return punctuation.getPunc(':', lineNum);
                
            case '.':
                if(next == '.'){ return getSpecialOP("..", lineNum); }
               // fStream.popPushBack(); /* may be able to get rid of this? Doesn't hurt to add it though */
                fStream.pushBack(next);
                return punctuation.getPunc('.', lineNum); // end marker
            
            case '+':
                fStream.pushBack(next);
                if(isBinaryAddop(prevToken)){ return getSimpleOP('+', lineNum); }
                return getSpecialOP("^+", lineNum);
                
            case '-':
                fStream.pushBack(next);
                if(isBinaryAddop(prevToken)){ return getSimpleOP('-', lineNum); }
                return getSpecialOP("^-", lineNum);
            
            case '<':
                if(next == '>'){ return getSpecialOP("==", lineNum); }
                if(next == '='){ return getSpecialOP("<=", lineNum); }
                fStream.pushBack(next);
                return getSimpleOP('<', lineNum);
                
            case '>': /* must be followed by a number? or identifier? Or is this not a part of the lexer */
                if(next == '='){ return getSpecialOP(">=", lineNum); }
                fStream.pushBack(next);
                return getSimpleOP('>', lineNum);
                
            default:
                return null; /* should never reach here */
        }
    }
}
