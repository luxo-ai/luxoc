/*
 * File: SemanticActions.java
 *
 * Desc: contains the semantic actions of the compiler.
 *
 * - Semantic Actions 1 [x]
 * - Semantic Actions 2 [x]
 * - Semantic Actions 3 [ ]
 *
 * Sem 1: actions 1, 2, 3, 4, 6, 7, 9, 13.
 * Sem 2: actions 55, 56, 30, 31, 40, 41, 42, 43, 44, 45, 46, and 48.
 */
package main.java.semantics;

import main.java.lexer.Tokenizer;
import main.java.semantics.errors.SemanticError;
import main.java.table.*;
import main.java.table.errors.SymbolTableError;
import main.java.token.Token;
import main.java.token.TokenType;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

/**
 * SemanticActions
 * @author Luis Serazo
 */
public class SemanticActions {

    /**
     * Actions class to represent a specific action.
     * Run method: overridden when needed.
     */
    class Action{ void run(Token token){ } }

    /*
     * Constants:
     * - INITIAL_SIZE: the initial size of each symbol table.
     * - NUM_ACTIONS: the number of actions in the grammar.
     */
    private final int INITIAL_SIZE = 250;
    private final int NUM_ACTIONS = 58;

    /*
     * Flags:
     * insert: INSERT/SEARCH
     * global: GLOBAL/LOCAL
     * array:  ARRAY/SIMPLE
     */
    private boolean insert;
    private boolean global;
    private boolean array;

    /*
     * Current Memory Sizes:
     * - globalMem: global memory
     * - localMem:  local memory (functions)
     */
    private int globalMem;
    private int localMem;

    /*
     * Symbol Tables:
     * - localTable: stores local variables
     * - globalTable: stores global variables
     */
    private SymbolTable localTable;
    private SymbolTable globalTable;
    private SymbolTable constTable;

    /*
     * Semantic Data Structures:
     * - semanticsStack: stack for controlling semantic action routines.
     * - actions: the list of all actions.
     * - quads: Quadruples scratch pad for the TVI code
     */
    private Stack<Object> semanticsStack;
    private Action[] actions;
    private Quadruples quads;
    private int globalStore;
    private Tokenizer lexer;
    private int tempCount = 0;

    /**
     * SemanticAction constructor
     * @param lexer: control of the lexer is passed to the semantic actions.
     */
    public SemanticActions(Tokenizer lexer){
        semanticsStack = new Stack<>();
        localTable = new SymbolTable(INITIAL_SIZE);
        globalTable = new SymbolTable(INITIAL_SIZE);
        constTable = new SymbolTable(INITIAL_SIZE);

        /* set the flags */
        this.insert = true; // = INSERT
        this.global = true; // = GLOBAL
        this.array = false; // = SIMPLE

        /* initialize memory sizes to zero */
        this.globalMem = 0;
        this.localMem = 0;

        /* initialize quadruples */
        quads = new Quadruples();
        globalStore = 0;

        /* pass control of the lexer */
        this.lexer = lexer;

        /* populate the actions */
        actions = new Action[NUM_ACTIONS]; // Note: there are 58 actions.
        init();
    }

    /**
     * init: initializes the list of actions
     *
     */
    private void init(){
        // 1
        this.actions[0] = new Action(){ @Override public void run(Token token) { action_1(token); } };
        // 2
        this.actions[1] = new Action(){ @Override public void run(Token token) { action_2(token); } };
        // 3
        this.actions[2] = new Action(){ @Override public void run(Token token) { action_3(token); } };
        // 4
        this.actions[3] = new Action(){ @Override public void run(Token token) { action_4(token); } };
        // 5
        this.actions[4] = new Action();
        // 6
        this.actions[5] = new Action(){ @Override public void run(Token token) { action_6(token); } };
        // 7
        this.actions[6] = new Action(){ @Override public void run(Token token) { action_7(token); } };
        // 8
        this.actions[7] = new Action();
        // 9
        this.actions[8] = new Action(){ @Override public void run(Token token) { action_9(token); } };
        // 10
        this.actions[9] = new Action();
        // 11
        this.actions[10] = new Action();
        // 12
        this.actions[11] = new Action();
        // 13
        this.actions[12] = new Action(){ @Override public void run(Token token) { action_13(token); } };
        // 14
        this.actions[13] = new Action();
        // 15
        this.actions[14] = new Action();
        // 16
        this.actions[15] = new Action();
        // 17
        this.actions[16] = new Action();
        // 18
        this.actions[17] = new Action();
        // 19
        this.actions[18] = new Action();
        // 20
        this.actions[19] = new Action();
        // 21
        this.actions[20] = new Action();
        // 22
        this.actions[21] = new Action();
        // 23
        this.actions[22] = new Action();
        // 24
        this.actions[23] = new Action();
        // 25
        this.actions[24] = new Action();
        // 26
        this.actions[25] = new Action();
        // 27
        this.actions[26] = new Action();
        // 28
        this.actions[27] = new Action();
        // 29
        this.actions[28] = new Action();
        // 30
        this.actions[29] = new Action(){ @Override void run(Token token) { action_30(token); } };
        // 31
        this.actions[30] = new Action(){ @Override void run(Token token) { action_31(token); } };
        // 32
        this.actions[31] = new Action();
        // 33
        this.actions[32] = new Action();
        // 34
        this.actions[33] = new Action();
        // 35
        this.actions[34] = new Action();
        // 36
        this.actions[35] = new Action();
        // 37
        this.actions[36] = new Action();
        // 38
        this.actions[37] = new Action();
        // 39
        this.actions[38] = new Action();
        // 40
        this.actions[39] = new Action(){ @Override void run(Token token) { action_40(token); } };
        // 41
        this.actions[40] = new Action();
        // 42
        this.actions[41] = new Action(){ @Override void run(Token token) { action_42(token); } };
        // 43
        this.actions[42] = new Action(){ @Override void run(Token token) { action_43(token); } };
        // 44
        this.actions[43] = new Action(){ @Override void run(Token token) { action_44(token); } };
        // 45
        this.actions[44] = new Action(){ @Override void run(Token token) { action_45(token); } };
        // 46
        this.actions[45] = new Action(){ @Override void run(Token token) { action_46(token); } };
        // 47
        this.actions[46] = new Action();
        // 48
        this.actions[47] = new Action(){ @Override void run(Token token) { action_48(token); } };
        // 49
        this.actions[48] = new Action();
        // 50
        this.actions[49] = new Action();
        // 51
        this.actions[50] = new Action();
        // 52
        this.actions[51] = new Action();
        // 53
        this.actions[52] = new Action();
        // 54
        this.actions[53] = new Action();
        // 55
        this.actions[54] = new Action(){ @Override void run(Token token) { action_55(token); } };
        // 56
        this.actions[55] = new Action(){ @Override void run(Token token) { action_56(token); } };
        // 57
        this.actions[56] = new Action();
        // 58
        this.actions[57] = new Action();
    } // end of INIT

    /**
     * execute: runs the semantic analyzer routine.
     * @param actionID: the action number passed by the parser.
     * @param token: the current Token being considered.
     * @throws SemanticError when a semantic error occurs in one of the IDs.
     * @throws SymbolTableError when the Symbol Table encounters a problem.
     *
     * Note: pseudo code is delineated by ':::'
     */
    public void execute(int actionID, Token token) throws SemanticError, SymbolTableError{
        try{ this.actions[actionID - 1].run(token); }
        catch (ArrayIndexOutOfBoundsException ex){ throw SemanticError.ActionDoesNotExist(actionID); }
    }


    /* -------------------- SEMANTIC ACTIONS ------------------- */

    /**
     * action_1: semantic action 1
     * @param token: the Token in question.
     */
    private void action_1(Token token){
        /* INSERT/SEARCH = INSERT */
        this.insert = true;
    }

    /**
     * action_2: semantic action 2
     * @param token: the Token in question.
     */
    private void action_2(Token token){
        /* INSERT/SEARCH = SEARCH */
        this.insert = false;
    }

    /**
     * action_3: semantic action 3
     * @param token: the Token in question.
     */
    private void action_3(Token token){
        /*  TYP = pop TYPE  */
        Token tok = (Token) semanticsStack.pop();
        /* obtain the type of the Token */
        TokenType TYP = tok.getTokenType();
        /* if ARRAY/SIMPLE = ARRAY */
        if(this.array){
            /*  UB (LB) = pop CONSTANT (upper bound, lower bound) */
            ConstantEntry UB = (ConstantEntry) semanticsStack.pop();
            ConstantEntry LB = (ConstantEntry) semanticsStack.pop();
            /*  MSIZE = (UB - LB) + 1  */
            int MEM_SIZE = intDistance(UB, LB) + 1;
            storeArray(UB, LB, MEM_SIZE, TYP);
        }
        else{ storeVariable(TYP); }
        /* ARRAY/SIMPLE = SIMPLE */
        this.array = false;
    }

    /**
     * action_4: semantic action 4
     * @param token: the Token in question.
     */
    private void action_4(Token token){
        /* push TYPE through the full Token */
        semanticsStack.push(token);
    }

    /**
     * action_6: semantic action 6
     * @param token: the Token in question.
     */
    private void action_6(Token token){
        /* ARRAY/SIMPLE = ARRAY */
        this.array = true;
    }

    /**
     * action_7: semantic action 7
     * @param token: the Token in question.
     */
    private void action_7(Token token){
        /* push constant (real or int constant) */
        ConstantEntry constant = new ConstantEntry(token.getValue(), token.getTokenType());
        semanticsStack.push(constant);
    }

    /**
     * action_9: semantic action 9
     * @param token: the Token in question.
     */
    private void action_9(Token token){
        try{
            insertIO((Token) semanticsStack.pop()); // must pop but insert io can be handled in install builtins.
            insertIO((Token) semanticsStack.pop()); // or can be handled here, depends on what you think is best.
            insertProcedure((Token) semanticsStack.pop()); // third one, so this is the the bottom most.
            /* INSERT/SEARCH = SEARCH */
            this.insert = false;
        }
        catch (EmptyStackException ex){ throw SemanticError.InputOutputNotSpecified(); }
    }

    /**
     * action_13: semantic action 13
     * @param token: the Token in question.
     */
    private void action_13(Token token){
        /* ::: push id (identifier) ::: */
        semanticsStack.push(token);
    }

    /**
     * action_30: semantic action 30
     * @param token: the Token in question.
     */
    private void action_30(Token token){
        /* lookup id in symbol table */
        SymbolTableEntry id = lookupEntry(token.getValue());
        /* if not found, ERROR (undeclared variable) */
        if(id == null){ throw SemanticError.UndeclaredVariabe(token.getValue(), token.getLineNum()); }
        semanticsStack.push(id);
    }

    /**
     * action_31: semantic action 31
     * @param token: the Token in question.
     */
    private void action_31(Token token) {
        /* Grab the operands and offset off the stack */
        SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();
        SymbolTableEntry offset = (SymbolTableEntry) semanticsStack.pop();
        SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();
        generationRoutineSA31(id1, offset, id2, token.getLineNum());
    }

    /**
     * action_40:
     */
    private void action_40(Token token){
        /* push sign on the stack */
        semanticsStack.push(token);
    }

    /**
     * action_42:
     */
    private void action_42(Token token){
        /* push operator --> a token */
        semanticsStack.push(token);
    }

    /**
     * action_43:
     * Note: always favor converting to real, if a real present. Think scope.
     */
    private void action_43(Token token) {
        /* obtain ids and the operator between them */
        SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();
        Token operator = (Token) semanticsStack.pop();
        SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();
        generationRoutineSA43(id1, operator, id2);
    }

    /**
     * action_44
     */
    private void action_44(Token token){
        /* push operator --> a token */
        semanticsStack.push(token);
    }

    /**
     * action_45
     */
    private void action_45(Token token) {
        /* obtain ids and the operator between them */
        SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();
        Token operator = (Token) semanticsStack.pop();
        SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();

        /* check if a bad mod (id1 and id2) are not ints */
        if (typeCheck(id1, id2) != 0 && operator.getOpType() == Token.OperatorType.MOD) {
            throw SemanticError.BadMod(operator.getLineNum());
        }
        generationRoutineSA45(id1, operator, id2);
    }


    /**
     * action_46:
     */
    private void action_46(Token token){
        if(token.getTokenType() == TokenType.IDENTIFIER){ findAndPushID(token); }
        else if(isConstant(token)){ findAndPushConst(token); }
    }


    /**
     * action_48
     */
    private void action_48(Token token){
        /* obtain the offset */
        SymbolTableEntry offset = (SymbolTableEntry) semanticsStack.pop();
        /* if the offset is not null, move from src + offset into dest -> push dest. */
        if(offset != null){
            SymbolTableEntry id = (SymbolTableEntry) semanticsStack.pop();
            SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, token.getTokenType());
            tempCount++;
            generate("load", id, offset, $$TEMP);
            semanticsStack.push($$TEMP);
        }
    }

    /**
     * action_55: semantic action 55
     * @param token: the Token in question.
     */
    private void action_55(Token token){
        /* BACKPATCH(GLOBAL_STORE,GLOBAL_MEM) */
        backPatch(globalStore, globalMem);
        /* GEN(free GLOBAL_MEM) */
        generate("free", this.globalMem);
        /* GEN(PROCEND) */
        generate("PROCEND");
    }

    /**
     * action_56: semantic action 56
     * @param token: the Token in question.
     */
    private void action_56(Token token){
        /* GEN(PROCBEGIN main) */
        generate("PROCBEGIN", "main");
        /* GLOBAL_STORE = NEXTQUAD */
        globalStore = quads.nextQuadIndex();
        /* GEN(alloc,_) */
        generate("alloc", "_");
    }


    /* ------------------ GENERATION ROUTINES ------------------ */

    /**
     *
     */
    private void generationRoutineSA31(SymbolTableEntry id1, SymbolTableEntry offset, SymbolTableEntry id2, int lineNum){
        switch (typeCheck(id1, id2)) {
            /* id1.TYPE == id2.TYPE */
            case 0:
            case 1:
                /* there is no need for conversion */
                if (offset != null) { generate("stor", id2, offset, id1); }
                else { generate("move", id2, id1); }
                break;

            /* id1 = REAL , id2 = INTEGER */
            case 2:
                SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, TokenType.REAL);
                tempCount++;
                /* convert id2 to a REAL and store in $$TEMP */
                generate("ltof", id2, $$TEMP);
                /*
                 * Check if the offset into the current stack frame is null.
                 * Store --> move from src into dest + offset.
                 * Move  --> moves from src to dest.
                 */
                if (offset != null) { generate("stor", $$TEMP, offset, id1); }
                else { generate("move", $$TEMP, id1); }
                break;

            /* id1 = INTEGER , id2 = REAL */
            default:
                /* case 3 */
                throw SemanticError.UnmatchedTypes(id1.getName(), id2.getName(), lineNum);
        }
    }


    /**
     *
     */
    private void generationRoutineSA43(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        SymbolTableEntry $$TEMP;
        SymbolTableEntry $$TEMP1;
        SymbolTableEntry $$TEMP2;
        switch (typeCheck(id1, id2)) {

            /* id1 = INTEGER , id2 = INTEGER */
            case 0:
                /* RESULT := */
                $$TEMP = create("$$TEMP" + tempCount, TokenType.INTEGER);
                tempCount++;
                /* generate the tvi target code */
                generate(opToString(operator), id1, id2, $$TEMP);
                /* push result */
                semanticsStack.push($$TEMP);
                break;

            /* id1 = REAL , id2 = REAL */
            case 1:
                /* RESULT := */
                $$TEMP = create("$$TEMP" + tempCount, TokenType.REAL);
                tempCount++;
                generate("f" + opToString(operator), id1, id2, $$TEMP);
                /* push result */
                semanticsStack.push($$TEMP);
                break;

            /* id1 = REAL , id2 = INTEGER */
            case 2:
                /* TEMP1 for casting id2 to REAL */
                $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
                tempCount++;
                /* RESULT := */
                $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);
                tempCount++;

                generate("ltof", id2, $$TEMP1);
                generate("f" + opToString(operator), id1, $$TEMP1, $$TEMP2); // float operator
                /* push result */
                semanticsStack.push($$TEMP2);
                break;

            /* id1 = INTEGER , id2 = REAL */
            default:
                /* TEMP1 for casting id1 to REAL */
                $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
                tempCount++;
                /* RESULT := */
                $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);
                tempCount++;

                generate("ltof", id1, $$TEMP1);
                generate("f" + opToString(operator), $$TEMP1, id2, $$TEMP2); // float operator
                /* push result */
                semanticsStack.push($$TEMP2);
                break;
        }
    }

    /**
     *
     */
    private void generationRoutineSA45(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        switch (typeCheck(id1, id2)) {
            /* id1 = INTEGER , id2 = INTEGER */
            case 0:
                generationRoutineSA45a(id1, operator, id2);
                break;
            /* id1 = REAL , id2 = REAL */
            case 1:
                generationRoutineSA45b(id1, operator, id2);
                break;

            /* id1 = REAL , id2 = INTEGER */
            case 2:
                generationRoutineSA45c(id1, operator, id2);
                break;

            /* id1 = INTEGER , id2 = REAL */
            default:
                generationRoutineSA45d(id1, operator, id2);
                break;
        }
    }


    /**
     *
     */
    private void generationRoutineSA45a(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        if (operator.getOpType() == Token.OperatorType.MOD) {
            /* */
            SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;
            SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;

            generate("move", id1, $$TEMP1);
            generate("move", $$TEMP1, $$TEMP2);
            generate("sub", $$TEMP2, id2, $$TEMP1);
            /* bge: jump to label index - 2 if $$TEMP1 >= id2 */
            generate("bge", $$TEMP1, id2, quads.nextQuadIndex() - 2);
            /* push result */
            semanticsStack.push($$TEMP1);
        } else if (operator.getOpType() == Token.OperatorType.DIVIDE) {
            SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
            tempCount++;
            SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);
            tempCount++;
            SymbolTableEntry $$TEMP3 = create("$$TEMP" + tempCount, TokenType.REAL);
            tempCount++;

            generate("ltof", id1, $$TEMP1);
            generate("ltof", id2, $$TEMP2);
            generate("fdiv", $$TEMP1, $$TEMP2, $$TEMP3);

            /* push result */
            semanticsStack.push($$TEMP3);
        } else {
            SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;

            generate(opToString(operator), id1, id2, $$TEMP);
            semanticsStack.push($$TEMP);
        }
    }

    /**
     *
     */
    private void generationRoutineSA45b(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        if (operator.getOpType() == Token.OperatorType.DIV) {
            /* coerce into integers */
            SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;
            SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;
            /* RESULT := */
            SymbolTableEntry $$TEMP3 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;

            generate("ftol", id1, $$TEMP1);
            generate("ftol", id2, $$TEMP2);
            generate("div", $$TEMP1, $$TEMP2, $$TEMP3);
            semanticsStack.push($$TEMP3);
        } else {
            SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, TokenType.REAL);
            tempCount++;

            generate("f" + opToString(operator), id1, id2, $$TEMP);
            semanticsStack.push($$TEMP);
        }
    }

    /**
     *
     */
    private void generationRoutineSA45c(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        if (operator.getOpType() == Token.OperatorType.DIV) {
            SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;
            SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;

            generate("ftol", id1, $$TEMP1);
            generate("div", $$TEMP1, id2, $$TEMP2);
            semanticsStack.push($$TEMP2);
        } else {
            SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
            tempCount++;
            SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);
            tempCount++;

            generate("ltof", id2, $$TEMP1);
            generate("f" + opToString(operator), id1, $$TEMP1, $$TEMP2);
            semanticsStack.push($$TEMP2);
        }
    }

    /**
     *
     */
    private void generationRoutineSA45d(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        if (operator.getOpType() == Token.OperatorType.DIV) {
            SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;
            SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            tempCount++;

            generate("ftol", id2, $$TEMP1);
            generate("div", id1, $$TEMP1, $$TEMP2);
            semanticsStack.push($$TEMP2);
        } else {
            SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
            tempCount++;
            SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);
            tempCount++;

            generate("ltof", id1, $$TEMP1);
            generate("f" + opToString(operator), $$TEMP1, id2, $$TEMP2);
            semanticsStack.push($$TEMP2);
        }
    }


    /**
     *
     */
    private void findAndPushID(Token token){
        SymbolTableEntry id = lookupEntry(token.getValue());
        /* Throw error if id is not in Symbol Table */
        if(id == null){ throw SemanticError.UndeclaredVariabe(token.getValue(), token.getLineNum()); }
        semanticsStack.push(id);
    }

    /**
     *
     */
    private boolean isConstant(Token token){
        return (token.getTokenType() == TokenType.INTCONSTANT ||
                token.getTokenType() == TokenType.REALCONSTANT);
    }

    /**
     *
     */
    private void findAndPushConst(Token token){
        /* look up the constant in the constant Symbol Table */
        ConstantEntry constnt = (ConstantEntry) constTable.lookup(token.getValue().toUpperCase());

        /* if constant not found, create it and insert it */
        if(constnt == null){
            if(token.getTokenType() == TokenType.INTCONSTANT){
                constnt = new ConstantEntry(token.getValue(), TokenType.INTEGER);
            }
            else{ constnt = new ConstantEntry(token.getValue(), TokenType.REAL); }
            insertToConst(constnt);
        }
        /* push the constant */
        semanticsStack.push(constnt);
    }




    /* -------------------  HELPERS  ---------------------- */

    /**
     * intDistance: the intefer distance of two int constants
     * @param x: a ConstantEntry
     * @param y: a ConstantEntry
     */
    private int intDistance(ConstantEntry x, ConstantEntry y){
        return (Integer.parseInt(x.getName()) - Integer.parseInt(y.getName()));
    }

    /**
     * intValue: get the integer value of a constants
     * @param x: a ConstantEntry
     */
    private int intValue(ConstantEntry x){ return Integer.parseInt(x.getName()); }

    /**
     * storeArray: store the array in memory.
     * @param UB: upper bound, a ConstantEntry.
     * @param LB: lower bound, a ConstantEntry.
     * @param MEM_SIZE: the memory size.
     * @param TYP: the token type
     */
    private void storeArray(ConstantEntry UB, ConstantEntry LB, int MEM_SIZE, TokenType TYP){
        /*  For each id on the semantic stack  */
        while(!semanticsStack.isEmpty()){ // TODO: maybe use the new pseudo code? is identifier?
            Token ID = (Token) semanticsStack.pop();

            /* if GLOBAL/LOCAL = GLOBAL (store in global memory) */
            ArrayEntry arryEntry;
            if(global){
                /*  insert id in global symbol table (Array_entry)  */
                arryEntry = new ArrayEntry(ID.getValue(), globalMem, TYP, intValue(UB), intValue(LB));
                insertToGlobal(arryEntry, ID.getLineNum());
                globalMem += MEM_SIZE;
            }
            /*  else insert id in local symbol table (Array_entry) */
            else{
                arryEntry = new ArrayEntry(ID.getValue(), localMem, TYP, intValue(UB), intValue(LB));
                insertToLocal(arryEntry, ID.getLineNum());
                localMem += MEM_SIZE;
            }
        }
    }

    /**
     * storeVariable: store the simple variable in memory.
     * @param TYP: the TokenType that we're going to store as a var.
     */
    private void storeVariable(TokenType TYP){
        /*  For each id on the semantic stack  */
        while(!semanticsStack.isEmpty()){ // TODO: maybe use the new pseudo code? is identifier?
            Token ID = (Token) semanticsStack.pop();

            /* if GLOBAL/LOCAL = GLOBAL (store in global memory) */
            VariableEntry var;
            if(global){
                /* insert id in global symbol table (Variable_entry) */
                var = new VariableEntry(ID.getValue(), globalMem, TYP);
                System.out.println(var.getName());
                insertToGlobal(var, ID.getLineNum());
                globalMem += 1;
            }
            /* else insert id in local symbol table (Variable_entry) */
            else{
                var = new VariableEntry(ID.getValue(), localMem, TYP);
                insertToLocal(var, ID.getLineNum());
                localMem += 1;
            }
        }
    }

    /**
     * insertToGlobal: insert into the global symbol table
     * @param entry: the entry being inserting.
     * @param lineNum: the line number of the error.
     * Wrapper for SymbolTable insert (specified).
     */
    private void insertToGlobal(SymbolTableEntry entry, int lineNum) throws SemanticError, SymbolTableError{
        entry.nameToUpperCase();
        if(globalTable.lookup(entry.getName()) != null){
            if(lookupEntry(true, entry.getName()).isReserved()){
                throw SemanticError.ReservedName(entry.getName(), lineNum);
            }
            else{ throw SemanticError.NameAlreadyDeclared(entry.getName(), lineNum); }
        }
        globalTable.insert(entry);
    }

    /**
     * insertToGlobal: insert into the global symbol table
     * @param entry: the entry being inserting.
     * Wrapper for SymbolTable insert (specified).
     */
    private void insertToGlobal(SymbolTableEntry entry) throws SemanticError, SymbolTableError{
        entry.nameToUpperCase();
        if(globalTable.lookup(entry.getName()) != null){
            if(lookupEntry(true, entry.getName()).isReserved()){
                throw SemanticError.ReservedName(entry.getName());
            }
            else{ throw SemanticError.NameAlreadyDeclared(entry.getName()); }
        }
        globalTable.insert(entry);
    }

    /**
     * insertToLocal: insert into the local symbol table
     * @param entry: the entry being inserting.
     * @param lineNum: the line number of the error.
     * Wrapper for SymbolTable insert (specified).
     */
    private void insertToLocal(SymbolTableEntry entry, int lineNum) throws SemanticError, SymbolTableError{
        entry.nameToUpperCase();
        if(localTable.lookup(entry.getName()) != null){
            if(lookupEntry(false, entry.getName()).isReserved()){
                throw SemanticError.ReservedName(entry.getName(), lineNum);
            }
            else{ throw SemanticError.NameAlreadyDeclared(entry.getName(), lineNum); }
        }
        localTable.insert(entry);
    }

    /**
     * insertToLocal: insert into the local symbol table
     * @param entry: the entry being inserting.
     * Wrapper for SymbolTable insert (specified).
     */
    private void insertToLocal(SymbolTableEntry entry) throws SemanticError, SymbolTableError{
        entry.nameToUpperCase();
        if(localTable.lookup(entry.getName()) != null){
            if(lookupEntry(false, entry.getName()).isReserved()){
                throw SemanticError.ReservedName(entry.getName());
            }
            else{ throw SemanticError.NameAlreadyDeclared(entry.getName()); }
        }
        localTable.insert(entry);
    }

    /**
     * insertToConst: inserts into the constant symbol table
     * @param entry: the entry being inserted
     */
    private void insertToConst(SymbolTableEntry entry) throws SemanticError, SymbolTableError{
        entry.nameToUpperCase();
        constTable.insert(entry);
    }

    /**
     * lookupEntry: lookup wrapper for specified symbol tables.
     * @param globalEntry: boolean indicating if a global entry or local entry
     * @param name: the String name of the entry.
     * @return a corresponding SymbolTableEntry or null (if doesn't exist).
     */
    private SymbolTableEntry lookupEntry(boolean globalEntry, String name) {
        if (globalEntry) { return globalTable.lookup(name.toUpperCase()); }
        return localTable.lookup(name.toUpperCase());
    }

    /**
     * lookupEntry: lookup wrapper for specified symbol tables.
     * @param name: the String name of the entry.
     * @return a corresponding SymbolTableEntry or null (if doesn't exist).
     */
    private SymbolTableEntry lookupEntry(String name){
        if(global){ return globalTable.lookup(name.toUpperCase()); }
        return localTable.lookup(name.toUpperCase());
    }

    /**
     * insertIO: insert the reserved file IO.
     * @param token: a Token
     */
    private void insertIO(Token token) throws SymbolTableError, SemanticError{
        IODeviceEntry entry = new IODeviceEntry(token.getValue());
        entry.setToReserved();
        insertToGlobal(entry, token.getLineNum());
    }

    /**
     * insertProcedure: insert any reserved procedures.
     * @param token: a Token
     */
    private void insertProcedure(Token token) throws SymbolTableError, SemanticError{
        ProcedureEntry entry = new ProcedureEntry(token.getValue(), 0, new LinkedList<ParameterInfo>());
        entry.setToReserved();
        insertToGlobal(entry, token.getLineNum());
    }



    /*  -----------  GEN  ------------ */

    /**
     * SEMANTIC ACTION #31
     * generate -> TYPE CONVERSION
     */
    private void generate(String tviCode, SymbolTableEntry operand1, SymbolTableEntry operand2){
        String src = toAddress(tviCode, operand1);
        String dest = toAddress(tviCode, operand2);
        String[] quadrpl = {tviCode, src, dest, null};
        quads.addQuad(quadrpl);
    }

    /**
     * SEMANTIC ACTION #31
     * generate -> MOVING MEMORY
     */
    private void generate(String tviCode, SymbolTableEntry operand1, SymbolTableEntry operand2, SymbolTableEntry operand3){
        String src = toAddress(tviCode, operand1);
        String offset = toAddress(tviCode, operand2);
        String dest = toAddress(tviCode, operand3);
        String[] quadrpl = {tviCode, src, offset, dest};
        quads.addQuad(quadrpl);
    }

    /**
     * SEMANTIC ACTION #45
     * bge
     */
    private void generate(String tviCode, SymbolTableEntry operand1, SymbolTableEntry operand2, int label){
        String op1 = toAddress(tviCode, operand1);
        String op2 = toAddress(tviCode, operand2);
        String[] quadrpl = {tviCode, op1, op2, String.valueOf(label)};
        quads.addQuad(quadrpl);
    }

    /**
     * SEMANTIC ACTION #55
     * generate: generates TVI code using Quadruples.
     * @param tviCode: String representation of TVI
     * @param memFree: the amount of memory to be freed.
     */
    private void generate(String tviCode, int memFree){
        String[] quadrpl = {tviCode, String.valueOf(memFree), null,  null};
        quads.addQuad(quadrpl);
    }

    /**
     * Used in SEMANTIC ACTION #55
     * generate: generates TVI code using Quadruples.
     * @param tviCode: String representation of TVI
     */
    private void generate(String tviCode){
        String[] quadrpl = {tviCode, null, null, null};
        quads.addQuad(quadrpl);
    }

    /**
     * SEMANTIC ACTION #56
     * generate: generates TVI code using Quadruples.
     * @param tviCode: String representation of TVI
     * @param idName: the operand in question.
     */
    private void generate(String tviCode, String idName){
        String [] quadrpl = {tviCode, idName, null, null};
        quads.addQuad(quadrpl);
    }

    /**
     * used in ToAddress
     * generate: generates TVI code using Quadruples.
     */
    private void generate(String tviCode, String value, SymbolTableEntry operand){
        String op = toAddress(tviCode, operand);
        String[] quadrpl = {tviCode, value, op, null};
        quads.addQuad(quadrpl);
    }

    /*  -----------  End of GEN  ------------ */



    /**
     * create: creates a new memory location.
     * @param name:
     * @param type:
     */
    private VariableEntry create(String name, TokenType type) throws SymbolTableError, SemanticError{
        /*
         * Creates a new memory location by doing the following:
         * - insert $$NAME in symbol table (Variable_entry)
         * - $$NAME.type = type
         * - $$NAME.address = NEGATIVE value of GLOBAL_MEM
         * - increment GLOBAL_MEM
         * - return $$NAME
         */
        VariableEntry $$NAME = new VariableEntry(name, -globalMem, type);
        insertToGlobal($$NAME);
        globalMem++;
        return $$NAME;
    }

    /**
     * toAddress: converts an entry to a address.
     * @param tviCode: tviCode as a String
     * @param op: operand in question
     * @return mem address as String.
     */
    private String toAddress(String tviCode, SymbolTableEntry op) {
        if (op.isProcedure() || op.isFunction()) {
            /* functions and procedures begin like: PROCBEGIN function name */
            return (op.getName().toLowerCase());
        }
        if (op.isVariable()) {
            int addr = Math.abs(((VariableEntry) op).getAddress());
            return (getPrefix(tviCode, op) + addr);
        }
        if (op.isArray()) {
            int addr = Math.abs(((ArrayEntry) op).getAddress());
            return (getPrefix(tviCode, op) + addr);
        }
        if (op.isConstant()) {
            /* if hasn't already been saved? */
            SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, op.getType());
            tempCount++;

            /* move value (e.g: 1.902) into $$TEMP */
            generate("move", op.getName(), $$TEMP);
            int addr = Math.abs(((VariableEntry) $$TEMP).getAddress());
            return (getPrefix(tviCode, $$TEMP) + addr);
        }
        // throw semantic error?
        return "ERROR";
    }

    /**
     * getPrefix: gets the address prefix, if any.
     * @param op:
     * @param tviCode:
     * @return a String representation of the address in memory.
     */
    private String getPrefix(String tviCode, SymbolTableEntry op){
        String pointerOps = "", memloc = "";
        if(tviCode.equals("param")){
            /* get the absolute memory address */
            if(!op.isParameter()){ pointerOps = "@"; }
        }
        else{
            /* dereference the address */
            if(op.isParameter()){ pointerOps = "^"; }
        }
        /* get absolute mem-location or from stack frame */
        if(global){
            if(this.globalTable.lookup(op.getName()) != null){ memloc = "_"; }
        }
        /* otherwise, is on the stack frame */
        else{
            if(this.localTable.lookup(op.getName()) != null){ memloc = "%"; }
            /* safety check */
            else if(this.globalTable.lookup(op.getName()) != null){ memloc = "_"; }
        }
        return pointerOps + memloc;
    }

    /**
     * opToString: operator to String
     * @param op: operator
     * @return operator as a String
     */
    private String opToString(Token op){
        switch (op.getOpType().toString()){
            case "ADD":
                return "add";

            case "DIVIDE":
                return "div";

            case "MULTIPLY":
                return "mul";

            case "SUBTRACT":
                return "sub";

            case "LESS_THAN":
                return "blt";

            case "LEQ":
                return "ble";

            case "GREATER_THAN":
                return "bgt";

            case "GEQ":
                return "bge";

            case "EQ":
                return "beq";

            case "NEQ":
                return "bne";

            default:
                return null;
        }
    }

    /**
     * typeCheck:
     * @param id1:
     * @param id2:
     * @return :
     */
    private int typeCheck(SymbolTableEntry id1, SymbolTableEntry id2){
        /*
         * Checks the types of id1 and id2, and returns the following :
         * 0   if id1 and id2 are both integers
         * 1   if id1 and id2 are both reals
         * 2   if id1 is real and id2 is integer
         * 3   if id1 is integer and id2 is real
         */
        if(id1.getType() == TokenType.INTEGER && id2.getType() == TokenType.INTEGER){ return 0; }
        if(id1.getType() == TokenType.REAL && id2.getType() == TokenType.REAL){ return 1; }
        if(id1.getType() == TokenType.REAL && id2.getType() == TokenType.INTEGER){ return 2; }
        if(id1.getType() == TokenType.INTEGER && id2.getType() == TokenType.REAL){ return 3; }
        else{ throw SemanticError.UnrecognizedTypes(id1.getType(), id2.getType()); }
    }

    /**
     * backPatch: inserts i in the second field of the statement at position p
     * @param p: the position
     * @param i: the element being inserted
     */
    private void backPatch(int p, int i) {
        /* ::: Inserts i in the second field of the statement at position p. ::: */
        String[] quadrpl = quads.getQuad(p);
        for (int k = 0; k < quadrpl.length; k++) {
            if (quadrpl[k].equals("_")) { quads.setEntry(p, k, Integer.toString(i)); }
        }
    }

    /**
     * semanticStackDump: routine to dump the semantic stack.
     */
    private void semanticStackDump() {
        if (!semanticsStack.isEmpty()) {
            System.out.print("\t ++ Semantic Stack Before Call: ");
            /* make a copy of the stack */
            Stack<Object> stackCopy = new Stack<>();
            stackCopy.addAll(semanticsStack);

            /* print the first element of the stack */
            System.out.print("[ " + stackCopy.pop());
            while (!stackCopy.isEmpty()) {
                /* print the remaining elements */
                System.out.print(", " + stackCopy.pop());
            }
            System.out.println(" ]");
        }
        /* otherwise, report an empty stack */
        else { System.out.println("\t ++ Semantic Stack Before Call: [ ]"); }
    }

} /* end of SemanticActions class */