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
    private Stack<Object> semanticsStack; // TODO: better practice, than using Object?
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
        this.actions[29] = new Action();
        // 31
        this.actions[30] = new Action();
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
        this.actions[39] = new Action();
        // 41
        this.actions[40] = new Action();
        // 42
        this.actions[41] = new Action();
        // 43
        this.actions[42] = new Action();
        // 44
        this.actions[43] = new Action();
        // 45
        this.actions[44] = new Action();
        // 46
        this.actions[45] = new Action();
        // 47
        this.actions[46] = new Action();
        // 48
        this.actions[47] = new Action();
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
        /* ::: TYP = pop TYPE ::: */
        Token tok = (Token) semanticsStack.pop();
        /* obtain the type of the Token */
        TokenType TYP = tok.getTokenType();
        /* ::: if ARRAY/SIMPLE = ARRAY ::: */
        if(this.array){
            /* ::: UB (LB) = pop CONSTANT ::: (upper bound, lower bound) */
            ConstantEntry UB = (ConstantEntry) semanticsStack.pop();
            ConstantEntry LB = (ConstantEntry) semanticsStack.pop();
            /* ::: MSIZE = (UB - LB) + 1 ::: */
            int MEM_SIZE = intDistance(UB, LB) + 1;
            storeArray(UB, LB, MEM_SIZE, TYP);
        }
        else{ storeVariable(TYP); }
        /* ::: ARRAY/SIMPLE = SIMPLE ::: */
        this.array = false;
    }

    /**
     * action_4: semantic action 4
     * @param token: the Token in question.
     */
    private void action_4(Token token){
        /* ::: push TYPE ::: */
        semanticsStack.push(token);
    }

    /**
     * action_6: semantic action 6
     * @param token: the Token in question.
     */
    private void action_6(Token token){
        /* ::: ARRAY/SIMPLE = ARRAY ::: */
        this.array = true;
    }

    /**
     * action_7: semantic action 7
     * @param token: the Token in question.
     */
    private void action_7(Token token){
        /* ::: push constant (real or int constant) ::: */
        ConstantEntry constant = new ConstantEntry(token.getValue(), token.getTokenType());
        semanticsStack.push(constant);
    }

    /**
     * action_9: semantic action 9
     * @param token: the Token in question.
     */
    private void action_9(Token token){
        // Todo: check if error handling is the right thing to do
        try{
            /* insert top two ids (identifiers) on semantic stack in the sym table mark as reserved */
            insertIO((Token) semanticsStack.pop()); // must pop but insert io can be handled in install builtins.
            insertIO((Token) semanticsStack.pop()); // or can be handled here, depends on what you think is best.
            /* insert bottom most id in the sym table (Procedure entry, with num param = 0), mark as reserved */
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
        /* ::: lookup id in symbol table ::: */
        SymbolTableEntry id = lookupEntry(token.getValue());
        /* ::: if not found, ERROR (undeclared variable) ::: */
        if(id == null){ throw SemanticError.UndeclaredVariabe(token.getValue(), lexer.getLineNum()); }
        semanticsStack.push(id);
    }

    /**
     * action_31: semantic action 31
     * @param token: the Token in question.
     */
    private void action_31(Token token){
        /*
         * :::
         *  : if TYPECHECK(id1,id2) = 3, ERROR
         *  : if TYPECHECK(id1,id2) = 2
         *      CREATE(TEMP,REAL)
         *      GEN(ltof,id2,$$TEMP)
         *      if OFFSET = NULL,
         *          GEN(move,$$TEMP,id1)
         *      else GEN(stor $$TEMP,offset,id1)
         *  : else if OFFSET = NULL,
         *          GEN(move,id2,id1)
         *      else GEN(stor id2,offset,id1)
         *      : pop id1, offset, id2
         *
         *  :::
         */
        SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();
        SymbolTableEntry offset = (SymbolTableEntry) semanticsStack.pop();
        SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();

        if(typeCheck(id1, id2) == 3){ throw SemanticError.UnmatchedTypes(id1.getName(), id2.getName(), token.getLineNum()); }
        if(typeCheck(id1, id2) == 2){
            SymbolTableEntry $$TEMP = create("TEMP", TokenType.REAL);
            tempCount++;
            generate("ltof", id2, $$TEMP);
            if(offset == null){
                generate("move", $$TEMP, id1);
            }
            else{
                generate("stor", $$TEMP, offset, id1);
            }
        }
        else{
            if(offset == null){
                generate("move", id2, id1);
            }
            else{
                generate("stor", id2, offset, id1);
            }
        }
    }

    /**
     * action_40:
     */
    private void action_40(Token token){
        semanticsStack.push(token);
    }

    /**
     * action_42:
     */
    private void action_42(Token token){
        semanticsStack.push(token);
    }

    /**
     * action_43:
     */
    private void action_43(Token token){
        SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();
        Token operator = (Token) semanticsStack.pop();
        SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();

        if(typeCheck(id1, id2) == 0){
            SymbolTableEntry $$TEMP = create("TEMP", TokenType.INTEGER);
            generate(operatorToString(operator), id1, id2, $$TEMP);
            tempCount++;
            semanticsStack.push($$TEMP);
        }
        else if(typeCheck(id1,id2) == 1){
            SymbolTableEntry $$TEMP = create("TEMP", TokenType.REAL);
            generate("f"+operatorToString(operator), id1, id2, $$TEMP);
            tempCount++;
            semanticsStack.push($$TEMP);
        }
        else if(typeCheck(id1, id2) == 2){
            SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
            SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
            generate("ltof", id2, $$TEMP1);
            generate("f"+operatorToString(operator), id1, $$TEMP1, $$TEMP2);
            tempCount += 2;
            semanticsStack.push($$TEMP2);
        }
        // == 3
        else{
            SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
            SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
            generate("ltof", id1, $$TEMP1);
            generate("f"+operatorToString(operator), $$TEMP1, id2, $$TEMP2);
            tempCount += 2;
            semanticsStack.push($$TEMP2);
        }
    }

    /**
     * action_44
     */
    private void action_44(Token token){
        semanticsStack.push(token);
    }

    /**
     * action_45
     */
    private void action_45(Token token){
        SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();
        Token operator = (Token) semanticsStack.pop();
        SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();

        if(typeCheck(id1, id2) != 0 && (operator.getOpType() != null && operator.getOpType() == Token.OperatorType.MOD)){
            throw SemanticError.BadMod();
        }
        if(typeCheck(id1, id2) == 0){
            if(operator.getOpType() != null && operator.getOpType() == Token.OperatorType.MOD){
                SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                generate("move", id1, $$TEMP1);
                SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                generate("move", $$TEMP1, $$TEMP2);
                generate("sub", $$TEMP2, id2, $$TEMP1);
                generate("bge", $$TEMP1, id2, quads.nextQuad()-2);
                tempCount +=2;
                semanticsStack.push($$TEMP1);
            }
            else if(operator.getOpType() != null && operator.getOpType() == Token.OperatorType.DIVIDE){
                SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                generate("ltof", id1, $$TEMP1);
                SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                generate("ltof", id2, $$TEMP2);
                SymbolTableEntry $$TEMP3 = create("TEMP3", TokenType.REAL);
                generate("fdiv", $$TEMP1, $$TEMP2, $$TEMP3);
                tempCount += 3;
                semanticsStack.push($$TEMP3);
            }
            else{
                SymbolTableEntry $$TEMP = create("TEMP", TokenType.INTEGER);
                generate(operatorToString(operator), id1, id2, $$TEMP);
                tempCount++;
                semanticsStack.push($$TEMP);

            }
        }
        else if(typeCheck(id1, id2) == 1){
            if(operator.getOpType() != null && operator.getOpType() == Token.OperatorType.DIV){
                SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                generate("ftol", id1, $$TEMP1);
                SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                generate("ftol", id2, $$TEMP2);
                SymbolTableEntry $$TEMP3 = create("TEMP3", TokenType.INTEGER);
                generate("div", $$TEMP1, $$TEMP2, $$TEMP3);
                tempCount += 3;
                semanticsStack.push($$TEMP3);

            }
            else{
                SymbolTableEntry $$TEMP = create("TEMP", TokenType.REAL);
                generate("f"+operatorToString(operator), id1, id2, $$TEMP);
                tempCount++;
                semanticsStack.push($$TEMP);
            }
        }
        else if(typeCheck(id1, id2) == 2){
            if(operator.getOpType() != null && operator.getOpType() == Token.OperatorType.DIV){
                SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                generate("ftol", id1, $$TEMP1);
                SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                generate("div", $$TEMP1, id2, $$TEMP2);
                tempCount++;
                semanticsStack.push($$TEMP2);
            }
            else{
                SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                generate("ltof", id2, $$TEMP1);
                SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                generate("f"+operatorToString(operator), id1, $$TEMP1, $$TEMP2);
                tempCount++;
                semanticsStack.push($$TEMP2);
            }
        }
        else if(typeCheck(id1, id2) == 3){
            if(operator.getOpType() != null && operator.getOpType() == Token.OperatorType.DIV){
                SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.INTEGER);
                generate("ftol", id2, $$TEMP1);
                SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.INTEGER);
                generate("div", id1, $$TEMP1, $$TEMP2);
                tempCount++;
                semanticsStack.push($$TEMP2);
            }
            else{
                SymbolTableEntry $$TEMP1 = create("TEMP1", TokenType.REAL);
                generate("ltof", id1, $$TEMP1);
                SymbolTableEntry $$TEMP2 = create("TEMP2", TokenType.REAL);
                generate("f"+operatorToString(operator), $$TEMP1, id2, $$TEMP2);
                tempCount++;
                semanticsStack.push($$TEMP2);
            }
        }
    }

    /**
     * action_46:
     */
    private void action_46(Token token){
        if(token.getTokenType() == TokenType.IDENTIFIER){
            //lookupIdentifier(token);
            SymbolTableEntry id = lookupEntry(token.getValue());
            if(id == null){
                throw SemanticError.UndeclaredVariabe(token.getValue(), token.getLineNum());
            }
            semanticsStack.push(id);
        }
        else if(token.getTokenType() == TokenType.INTCONSTANT ||
                token.getTokenType() == TokenType.REALCONSTANT){
           // lookupConstant(token);
            ConstantEntry constnt = (ConstantEntry) constTable.lookup(token.getValue().toUpperCase());

            if(constnt == null){
                if(token.getTokenType() == TokenType.INTCONSTANT){
                    constnt = new ConstantEntry(token.getValue(), TokenType.INTEGER);
                }
                else{
                    constnt = new ConstantEntry(token.getValue(), TokenType.REAL);
                }
                insertToConst(constnt);
            }
            semanticsStack.push(constnt);
        }
    }

    /**
     * action_48
     */
    private void action_48(Token token){
        SymbolTableEntry offset = (SymbolTableEntry) semanticsStack.pop();
        if(offset != null){
            SymbolTableEntry id = (SymbolTableEntry) semanticsStack.pop();
            SymbolTableEntry $$TEMP = create("TEMP", token.getTokenType());
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
        /* ::: BACKPATCH(GLOBAL_STORE,GLOBAL_MEM) ::: */
        backPatch(globalStore, globalMem);
        /* ::: GEN(free GLOBAL_MEM) ::: */
        generate("free", this.globalMem);
        /* ::: GEN(PROCEND) ::: */
        generate("PROCEND");
    }

    /**
     * action_56: semantic action 56
     * @param token: the Token in question.
     */
    private void action_56(Token token){
        /* ::: GEN(PROCBEGIN main) ::: */
        generate("PROCBEGIN", "main");
        /* ::: GLOBAL_STORE = NEXTQUAD ::: */
        this.globalStore = quads.nextQuadIndex();
        /* ::: GEN(alloc,_) ::: */
        generate("alloc", "_");
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
        /* ::: For each id on the semantic stack ::: */
        while(!semanticsStack.isEmpty()){ // TODO: maybe use the new pseudo code? is identifier?
            /* ::: ID = pop id ::: */
            Token ID = (Token) semanticsStack.pop();

            /* ::: if GLOBAL/LOCAL = GLOBAL ::: (store in global memory) */
            ArrayEntry arryEntry;
            if(global){
                /* ::: insert id in global symbol table (Array_entry) ::: */
                arryEntry = new ArrayEntry(ID.getValue(), globalMem, TYP, intValue(UB), intValue(LB));
                insertToGlobal(arryEntry, ID.getLineNum());
                globalMem += MEM_SIZE;
            }
            /* ::: else insert id in local symbol table (Array_entry) ::: */
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
        /* ::: For each id on the semantic stack ::: */
        while(!semanticsStack.isEmpty()){ // TODO: maybe use the new pseudo code? is identifier?
            /* ::: ID = pop id ::: */
            Token ID = (Token) semanticsStack.pop();

            /* ::: if GLOBAL/LOCAL = GLOBAL ::: (store in global memory) */
            VariableEntry var;
            if(global){
                /* ::: insert id in global symbol table (Variable_entry) */
                var = new VariableEntry(ID.getValue(), globalMem, TYP);
                System.out.println(var.getName());
                insertToGlobal(var, ID.getLineNum());
                globalMem += 1;
            }
            /* ::: else insert id in local symbol table (Variable_entry) */
            else{
                var = new VariableEntry(ID.getValue(), localMem, TYP);
                insertToLocal(var, ID.getLineNum());
                localMem += 1;
            }
        }
    }

    /**
     * insertGlobal: insert into a the global symbol table
     * @param entry: the entry we're inserting.
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
     * insertGlobal: insert into a the global symbol table
     * @param entry: the entry we're inserting.
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
     * insertLocal: insert into a the local symbol table
     * @param entry: the entry we're inserting.
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
     * insertLocal: insert into a the local symbol table
     * @param entry: the entry we're inserting.
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
    private SymbolTableEntry lookupEntry(boolean globalEntry, String name){
        if(globalEntry){
            return globalTable.lookup(name.toUpperCase());
        }
        else{
            SymbolTableEntry value = localTable.lookup(name.toUpperCase());
            if(value != null){ return value; }
            return globalTable.lookup(name.toUpperCase());
        }
    }

    /**
     * lookupEntry: lookup wrapper for specified symbol tables.
     * @param name: the String name of the entry.
     * @return a corresponding SymbolTableEntry or null (if doesn't exist).
     */
    private SymbolTableEntry lookupEntry(String name){
        if(global){
            return globalTable.lookup(name.toUpperCase());
        }
        else{
            SymbolTableEntry value = localTable.lookup(name.toUpperCase());
            if(value != null){ return value; }
            return globalTable.lookup(name.toUpperCase());
        }
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

    /**
     * SEMANTIC ACTION #55
     * generate: generates TVI code using Quadruples.
     * @param tviCode: String representation of TVI
     */
    private void generate(String tviCode){
        String[] quadrpl = {tviCode, null, null, null};
        quads.addQuad(quadrpl);
    }

    /**
     * SEMANTIC ACTION #55
     * generate: generates TVI code using Quadruples.
     * @param tviCode: String representation of TVI
     * @param memLoc: the memory location of the operand
     */
    private void generate(String tviCode, int memLoc){
        String[] quadrpl = {tviCode, String.valueOf(memLoc), null,  null};
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
     * SEMANTIC ACTION #31
     * generate: generates TVI code using Quadruples.
     * @param tviCode: String representation of TVI
     * @param op1: operand in question
     * @param op2: operand in question
     * @throws SemanticError
     * @throws SymbolTableError
     */
    private void generate(String tviCode, SymbolTableEntry op1, SymbolTableEntry op2) throws SemanticError, SymbolTableError{
        String
    }

    /**
     *
     */
    private void generate(String tviCode1, String tviCode2, SymbolTableEntry op){

    }

    /**
     * getOpAddress:
     */
    private int getOpAddress(String tviCode, SymbolTableEntry op) throws SymbolTableError, SemanticError{
        String address = "";
        if(op.isFunction() || op.isProcedure()){
            address = address + op.getName().toLowerCase();
        }
        else{
            if(op.isConstant()){
                SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, op.getType());
                tempCount++;
                generate("move", op.getName(), $$TEMP);
            }
        }
    }

    /**
     * create: creates a new memory location.
     * @param name:
     * @param type:
     */
    private VariableEntry create(String name, TokenType type) throws SymbolTableError, SemanticError{
        /*
         * :::
         * Creates a new memory location by doing the following:
         * - insert $$NAME in symbol table (Variable_entry)
         * - $$NAME.type = type
         * - $$NAME.address = NEGATIVE value of GLOBAL_MEM
         * - increment GLOBAL_MEM
         * - return $$NAME
         * :::
         */
        VariableEntry $$NAME = new VariableEntry(name, -globalMem, type);
        insertToGlobal($$NAME);
        globalMem++;
        return $$NAME;
    }

    private int typeCheck(SymbolTableEntry id1, SymbolTableEntry id2){
        /*
         * Checks the types of id1 and id2, and returns the following :
         * 0   if id1 and id2 are both integers
         * 1   if id1 and id2 are both reals
         * 2   if id1 is real and id2 is integer
         * 3   if id1 is integer and id2 is real
         *
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
    private void semanticStackDump(){
        System.out.println("Dumping the semantic stack ...");
        for(int i = 0; i < semanticsStack.size(); i++){
            System.out.println("- "+semanticsStack.get(i));
        }
    }

} /* end of SemanticActions class */