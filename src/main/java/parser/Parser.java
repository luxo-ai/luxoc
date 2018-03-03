/*
 * File: Parser.java
 *
 * Desc: Contains the main parsing algorithm
 *
 */
package main.java.parser;

import main.java.lexer.Tokenizer;
import main.java.grammar.GrammarSymbol;
import main.java.parser.errors.ParseError;
import main.java.token.Token;
import main.java.token.TokenType;
import java.io.FileNotFoundException;
import java.util.Stack;

/**
 * Parser does the parse work of the compiler analysis
 * @author Luis Serazo
 *
 */
public class Parser {

    /* constants */
    private final static int ERROR = 999; /* errors look like this */

    /* objects to work with */
    private Tokenizer tokenizer;
    private ParseTable parseTable;
    private Stack<GrammarSymbol> parStack;
    private RHS rhsTable;

    /* current Token */
    private Token currentToken;

    /* predicted grammar symbol */
    private GrammarSymbol predicted;

    /**
     * Parser constructor
     * @param pascalFile: the path to the pascal file
     *
     * Desc: Initializes the tokenizer and stack.
     *       Returns a Parser object.
     */
    public Parser(String pascalFile) throws FileNotFoundException{
        this.tokenizer = new Tokenizer(pascalFile);
        /* set the current token */
        this.currentToken = tokenizer.getNextToken();
        /* initialize the other assets */
        this.parseTable = new ParseTable();
        this.rhsTable = new RHS();
        this.parStack = new Stack<>();
        parStack.push(TokenType.ENDOFFILE); /* push eof */
        parStack.push(NonTerminal.Goal); /* push start symbol */
    }


    /**
     * Parser constructor
     * @param pascalFile: the path to the pascal file
     * @param parseTablePath: the path to the parse table
     *
     * Desc: Initializes the tokenizer and stack.
     *       Returns a Parser object.
     */
    public Parser(String pascalFile, String parseTablePath) throws FileNotFoundException{
        this.tokenizer = new Tokenizer(pascalFile);
        /* set the current token */
        this.currentToken = tokenizer.getNextToken();
        /* initialize the other assets */
        this.parseTable = new ParseTable(parseTablePath);
        this.rhsTable = new RHS();
        this.parStack = new Stack<>();
        parStack.push(TokenType.ENDOFFILE); /* push eof */
        parStack.push(NonTerminal.Goal); /* push start symbol */
    }

    /**
     * run: routine that runs the parser. Implementation
     *      of the LL(1) Push-down parser. The pseudo-code
     *      can be found in the same directory as this file.
     */
    public void run(){
        /* while the current token type is not an EOF */
        while(currentToken.getTokenType() != TokenType.ENDOFFILE){
            /* set the predicted symbol to the top of the stack */
            predicted = parStack.pop();

            /* check if the predicted symbol is a token type */
            if(predicted.isToken()){
                /* if it is, we try to match the move */
                if(predicted == currentToken.getTokenType()){
                    currentToken = tokenizer.getNextToken(); /* match found */
                }
                /* otherwise, the match was bad and we record the error */
                else {
                    throw ParseError.NoMatch(predicted, currentToken.getTokenType());
                }
            }
            /* if the predicted symbol is a non-terminal type */
            else if(predicted.isNonTerminal()){
                /* get the table rule for this non-terminal */
                NonTerminal trm = (NonTerminal) predicted;
                int tableRule = parseTable.getRule(currentToken.getTypeIndex(), trm.getIndex());

                /* check if the table rule is an error */
                if(tableRule == ERROR){
                    dumpStack();
                    throw ParseError.Unexpected(predicted, currentToken.getTokenType());
                }
                else{
                    /* we only care about productions whose RHS is not the empty string */
                    if(tableRule > 0){
                        /* get the RHS symbols */
                        GrammarSymbol[] gRules = rhsTable.getRules(tableRule);
                        /* push the symbols on the stack */
                        for(int k = gRules.length-1; k > -1; k--){
                            parStack.push(gRules[k]);
                        }
                    }
                }
            }
            /* if the predicted symbol is a semantic action, continue */
            else if(predicted.isSemAction()){
                continue;
            }
        }
        System.out.println("Parsed Successfully!");
        dumpStack();
    }


    /**
     * dumpStack: routine that prints the contents of the stack.
     *
     */
    public void dumpStack(){
        System.out.println();
        System.out.println("Dumping Stack: ");
        Stack<GrammarSymbol> stackCopy = new Stack<>();
        stackCopy.addAll(parStack);

        while(!stackCopy.isEmpty()){
            System.out.println("--> "+stackCopy.pop());
        }
    }


    public static void main(String[] args) throws FileNotFoundException{
        Parser ps = new Parser("examples/program.pas");
        ps.run();
    }
}

