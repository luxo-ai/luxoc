/*
 * File: ParseError.java
 *
 * Desc: parsing errors
 *
 */
package main.java.parser.errors;
import main.java.grammar.GrammarSymbol;

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
     * NoMatch: parse error when there is no error.
     * @param g1
     * @param g2
     * @return
     */
    public static ParseError NoMatch(GrammarSymbol g1, GrammarSymbol g2){
        return new ParseError(g1+" Does not match: "+g2);
    }

    /**
     * Unexpected: parse error when an unexpected symbol appears.
     * @param g
     * @param g2
     * @return
     */
    public static ParseError Unexpected(GrammarSymbol g, GrammarSymbol g2){
        return new ParseError(""+g+" and "+g2);
    }

}
