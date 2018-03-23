/*
 * File: LexerError.java
 *
 * Desc: Reports lexical errors.
 *
 */
package main.java.lexer.errors;

/**
 * LexerError
 * @author Luis Serazo
 */
public class LexerError extends Error {

    private LexerError(String msg){
        super(msg);
    }

    public static LexerError InvalidCharacter(int lineNum, char character){
        return new LexerError("Invalid character: "+character+" found on line number: "+lineNum+".");
    }

    public static LexerError IllegalComment(int lineNum, String form){
        return new LexerError("Ill-formed comment on line: "+lineNum+". Comments cannot contain brackets. Cannot be of the form: "+form);
    }

    public static LexerError UnclosedComment(int lineStart){
        return new LexerError("Comment beginning on line: "+lineStart+" does not end. Please close all comments.");
    }

    public static LexerError IllegalIdentifierName(int lineNum, String ident){
        return new LexerError("Illegal identifier: "+ident+" on line: "+lineNum+". Identifiers must be begin with a letter and have a body of only alphabetical and numerical characters");
    }

    public static LexerError IllegalIdentifierLength(int lineNum, String ident, int max){
        return new LexerError("Illegal identifier: "+ident+"... on line: "+lineNum+". Identifiers can not be longer than "+max+" characters.");
    }

    public static LexerError IllegalRealConstant(int lineNum, String constant){
        return new LexerError("Illegal real constant: "+constant+" on line: "+lineNum+". Real constants must be contain only one decimal point and each decimal point must be followed by at least one number.");
    }
}