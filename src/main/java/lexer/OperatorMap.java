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
 * OperatorMap class
 * @author Luis Serazo
 */
public class OperatorMap {
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

    public boolean isSimpleOp(char chr){
        return (chr == '*' ||
                chr == '/' ||
                chr == '=');
    }

    public boolean isSpecialOP(char chr){
        return (chr == ':' ||
                chr == '.' ||
                chr == '+' ||
                chr == '-' ||
                chr == '<' ||
                chr == '>');
    }

    /**
     * isBinaryAddop: checks if the previousTokenType is a binary addition operator.
     *
     * @return True if binary and false if unary
     */
    private boolean isBinaryAddop(Token prevToken) {
        return ((prevToken.getTokenType() == TokenType.RIGHTPAREN) ||
                (prevToken.getTokenType() == TokenType.RIGHTBRACKET) ||
                (prevToken.getTokenType() == TokenType.IDENTIFIER) ||
                (prevToken.getTokenType() == TokenType.INTCONSTANT) ||
                (prevToken.getTokenType() == TokenType.REALCONSTANT));
    }

    protected Token getSimpleOP(char op, int lineNum){
        Token opToken = this.simpleOp.get(op);
        opToken.setLineNum(lineNum);
        return opToken;
    }
    
    
    private Token getSpecialOP(String op, int lineNum){
        Token opToken = this.specialOP.get(op);
        opToken.setLineNum(lineNum);
        return opToken;
    }
    

    public Token resolveSpecialOp(char op, Token prevToken, FileStream fStream, PunctuationMap punctuation){
        int lineNum = fStream.getLineNum();
        char next = fStream.nextChar();
        switch(op){
            case ':':
                if(next != '='){ return punctuation.getPunc(':', lineNum); }
                return getSpecialOP(":=", lineNum);
                
            case '.':
                if(next != '.'){ return punctuation.getPunc('.', lineNum); } // end marker.
                fStream.popPushBack(); /* may be able to get rid of this? Doesn't hurt to add it though */
                return getSpecialOP("..", lineNum);
            
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
                return getSimpleOP('<', lineNum);
                
            case '>': /* must be followed by a number? or identifier? Or is this not a part of the lexer */
                if(next == '='){ return getSpecialOP(">=", lineNum); }
                return getSimpleOP('>', lineNum);
                
            default:
                return null; /* should never reach here */
        }
    }
}
