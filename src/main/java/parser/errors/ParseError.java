/*
 * File: ParseError.java
 *
 * Desc: parsing errors
 *
 */
package main.java.parser.errors;
import main.java.grammar.GrammarSymbol;
import main.java.token.Token;

/**
 * ParseError class
 * @author Luis Serazo
 */
public class ParseError extends Error{

    /**
     * ParseError constructor
     * @param msg: error message
     */
    private ParseError(String msg){ super(msg); }

    /**
     * NoMatch: parse error when there is no match between two GrammarSymbols.
     * @param gm: the first GrammarSymbol
     * @param tok: the second Token (GrammarSymbol)
     * @return a ParseError
     */
    public static ParseError NoMatch(GrammarSymbol gm, Token tok){
        return new ParseError(gm+" Does not match: "+tok.getTokenType()+". On line: "+tok.getLineNum());
    }

    /**
     * Unexpected: parse error when an unexpected Token appears.
     * @param tok: Token in question
     * @return a ParseError
     */
    public static ParseError Unexpected(Token tok){
        return new ParseError("Unexpected Token: "+tok.toString()+". On line: "+tok.getLineNum());
    }

}