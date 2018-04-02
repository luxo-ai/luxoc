/*
 * File: Parser.java
 *
 * Desc: Contains the main parsing algorithm
 *
 */
package main.java.parser;

import main.java.lexer.Tokenizer;
import main.java.grammar.GrammarSymbol;
import main.java.lexer.errors.LexerError;
import main.java.parser.errors.ParseError;
import main.java.token.Token;
import main.java.token.TokenType;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Parser does the parse work of the compiler analysis
 * @author Luis Serazo
 *
 */
public class Parser {

    /* Constants:
     * ERROR: the form of errors in the parse table
     * RHS_TABLE: a RHS table (No need to mutate, so keep final)
     * PARSE_TABLE: a ParseTable (No need to mutate, so keep final)
     */
    private final static int ERROR = 999; /* errors look like this */
    private final RHS RHS_TABLE;
    private final ParseTable PARSE_TABLE;

    /* objects to work with */
    private Tokenizer tokenizer;
    private Stack<GrammarSymbol> parStack;

    /* current Token */
    private Token currentToken;
    /* linked list of errors for recovery */
    private LinkedList<ParseError> errorList;

    /* flag indicating if we're in debug mode or not (default is false) */
    private boolean debug = false;

    /**
     * Parser constructor
     * @param pascalFile: the path to the pascal source file
     *
     * Desc: Initializes the tokenizer and stack.
     *       Returns a Parser object.
     */
    public Parser(String pascalFile) throws FileNotFoundException{
        this.tokenizer = new Tokenizer(pascalFile);
        /* set the current token */
        this.currentToken = tokenizer.getNextToken();
        /* initialize the other assets */
        this.PARSE_TABLE = new ParseTable();
        this.RHS_TABLE = new RHS();
        this.parStack = new Stack<>();
        this.errorList = new LinkedList<>();
        /* push the initial elements */
        parStack.push(TokenType.ENDOFFILE); /* push eof */
        parStack.push(NonTerminal.Goal);    /* push start symbol */
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
        this.PARSE_TABLE = new ParseTable(parseTablePath);
        this.RHS_TABLE = new RHS();
        this.parStack = new Stack<>();
        this.errorList = new LinkedList<>();
        /* push the initial elements */
        parStack.push(TokenType.ENDOFFILE); /* push eof */
        parStack.push(NonTerminal.Goal);    /* push start symbol */
    }

    /**
     * debugMode: sets the debug mode to true
     */
    public void debugMode(){ this.debug = true; }

    /**
     * run: routine that runs the parser. Implementation
     *      of the LL(1) Push-down parser. The pseudo-code
     *      can be found in the same directory as this file.
     */
    public void run(){
        /* predicted grammar symbol */
        GrammarSymbol predicted;

        /* while the current token type is not an EOF */
        while(!parStack.isEmpty()){
            /* for each iteration, print the contents of the stack */
            if(debug){ dumpStack(); }

            /* set the predicted symbol to the top of the stack */
            predicted = parStack.pop();
            if(debug){ System.out.print("Predicted: "+predicted+ " with Token: "+currentToken.toString()+" ==> "); }

            /* ::: PREDICTED: TOKEN (TERMINAL) ::: */
            if(predicted.isToken()){

                /* if it is, we try to match the move */
                if(predicted == currentToken.getTokenType()){
                    /* print that the mach was found */
                    if(debug){ System.out.println("MATCH FOUND\n"); }
                    currentToken = tokenizer.getNextToken(); /* match found */
                }
                /* otherwise, the match was bad and we record the error */
                else {
                    panicMode(ParseError.NoMatch(predicted, currentToken));
                }
            }
            /* ::: PREDICTED: NON-TERMINAL ::: */
            else if(predicted.isNonTerminal()){

                /* get the table rule for this non-terminal */
                NonTerminal trm = (NonTerminal) predicted;

                /* Obtain the production rule given the current token and non-terminal */
                int tableRule = PARSE_TABLE.getRule(currentToken.getTypeIndex(), trm.getIndex());

                /* check if the table rule is an error */
                if(tableRule == ERROR){ panicMode(ParseError.Unexpected(currentToken)); }

                /* otherwise, we're okay and we proceed */
                else{
                    /* we only care about productions whose RHS is not the empty string (epsilon) */
                    if(tableRule > 0){
                        /* get the RHS symbols of the production (different directions we can take) */
                        GrammarSymbol[] gRules = RHS_TABLE.getRules(tableRule);

                        if(debug){ System.out.println("PUSHING: "+RHS_TABLE.rulesToString(tableRule)+"\n"); }
                        /* push the symbols on the stack: make sure to do this with respect to FIFO */
                        for(int k = gRules.length-1; k > -1; k--){ parStack.push(gRules[k]); }
                    }
                    /* otherwise was an empty string and we're done with the current production. Pop next predicted */
                    else{ if(debug){ System.out.println("EPSILON\n"); } }
                }
            }
            /* ::: PREDICTED: SEMANTIC-ACTION ::: */
            // for now: if the predicted symbol is a semantic action, we pop by just continuing.
            else if(predicted.isSemAction()){
                if(debug){ System.out.println("POP UNUSED ACTION\n"); }
                continue;
            }
        }
        /* if there were errors, print them */
        printErrors();
    }

    /**
     * panic: the panic mode routine discussed in the assignment instructions.
     * @param error: a ParseError
     */
    private void panicMode(ParseError error) throws LexerError, ParseError {
        /* add this error to the list */
        this.errorList.add(error);

        /* skip until you find the next semicolon */
        while (currentToken.getTokenType() != TokenType.SEMICOLON && !currentToken.isEOF()) {
            currentToken = tokenizer.getNextToken();
        }

        /* if EOF, error cannot be recovered from */
        if (currentToken.isEOF()) {
            throw error;
        }

        /* if semicolon, pop the stack until you find the non-terminal <statement_list_tail> */
        while (!parStack.isEmpty() && parStack.pop() != NonTerminal.statement_list_tail) {
            if (parStack.isEmpty()) {
                throw error;
            }
        }
    }

    /**
     * printErrors: routine prints error list.
     * Note: if there are not errors, then we simply report the parse success.
     */
    private void printErrors(){
        if(!errorList.isEmpty()){
            /* print all the errors in the error list */
            for(ParseError error : errorList){ System.out.println(error.toString()); }
        }
        else{
            System.out.println("Parsed Successfully!");
            dumpStack();
        }
    }

    /**
     * dumpStack: routine that prints the contents of the stack.
     */
    private void dumpStack(){
        if(!parStack.isEmpty()) {
            System.out.println("Parse Stack: ");
            /* make a copy of the stack */
            Stack<GrammarSymbol> stackCopy = new Stack<>();
            stackCopy.addAll(parStack);

            /* print the first element on the stack */
            System.out.print("[ " + stackCopy.pop());
            while (!stackCopy.isEmpty()) {
                /* print the remaining elements */
                System.out.print(", "+stackCopy.pop());
            }
            /* finish off by closing out the stack brackets */
            System.out.println(" ]");
        }
        /* otherwise, report an empty stack */
        else{ System.out.println("Empty Stack"); }
    }

} /* end of Parser class */