/*
 * File: SemanticActions.java
 *
 * Desc: contains the semantic actions of the compiler.
 *
 * - Semantic Actions 1 [x]
 * - Semantic Actions 2 [ ]
 * - Semantic Actions 3 [ ]
 *
 * Sem 1: actions 1, 2, 3, 4, 6, 7, 9, 13.
 *
 */
package main.java.semantics;

import main.java.lexer.Tokenizer;
import main.java.semantics.errors.SemanticError;
import main.java.table.*;
import main.java.table.errors.SymbolTableError;
import main.java.token.Token;
import main.java.token.TokenType;

import java.util.LinkedList;
import java.util.Stack;

/**
 * SemanticActions
 * @author Luis Serazo
 */
public class SemanticActions {

    /* flags (SEM 1) */
    private boolean insert; /* INSERT / SEARCH */
    private boolean global; /* GLOBAL / LOCAL */
    private boolean array;  /* ARRAY / SIMPLE */

    /* global memory and local memory sizes */
    private int globalMem;
    private int localMem;

    /* local and global Symbol Tables */
    private final int TABLE_SIZE = 1000;
    private SymbolTable localTable;
    private SymbolTable globalTable;

    // TODO: Find a better way of doing this.?
    // TODO: Fix this, not good practice to use object (determine correct parents).
    /* Stack used for controlling semantic action routines */
    private Stack<Object> semanticsStack;
    private Tokenizer tokenizer;

    /**
     * SemanticAction constructor
     * @param tokenizer: the lexer.
     */
    public SemanticActions(Tokenizer tokenizer){
        semanticsStack = new Stack<>();
        localTable = new SymbolTable(TABLE_SIZE);
        globalTable = new SymbolTable(TABLE_SIZE);
        /* set the flags */
        this.insert = true; // = INSERT
        this.global = true; // = GLOBAL
        this.array = false; // = SIMPLE
        /* initialize memory sizes */
        this.globalMem = 0;
        this.localMem = 0;
        /* tokenizer for Semantic Actions */
        this.tokenizer = tokenizer;
    }

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
        switch (actionID){
            case 1:
                /* INSERT/SEARCH = INSERT */
                insert = true;
                break;

            case 2:
                /* INSERT/SEARCH = SEARCH */
                insert = false;
                break;

            case 3:
                /* ::: TYP = pop TYPE ::: */
                Token tok = (Token) semanticsStack.pop();
                /* obtain the type of the Token */
                TokenType TYP = tok.getTokenType();
                /* ::: if ARRAY/SIMPLE = ARRAY ::: */
                if(array){
                    /* ::: UB (LB) = pop CONSTANT ::: (upper bound, lower bound) */
                    ConstantEntry UB = (ConstantEntry) semanticsStack.pop();
                    ConstantEntry LB = (ConstantEntry) semanticsStack.pop();
                    /* ::: MSIZE = (UB - LB) + 1 ::: */
                    int MEM_SIZE = intDistance(UB, LB);
                    storeArray(UB, LB, MEM_SIZE, TYP);
                }
                else{ storeVariable(TYP); }
                /* ::: ARRAY/SIMPLE = SIMPLE ::: */
                array = false;
                break;

            case 4:
                /* ::: push TYPE ::: */
                semanticsStack.push(token);
                break;

            case 5:
                break;

            case 6:
                /* ::: ARRAY/SIMPLE = ARRAY ::: */
                this.array = true;
                break;

            case 7:
                /* ::: push constant (real or int constant) ::: */
                ConstantEntry constant = new ConstantEntry(token.getValue(), token.getTokenType());
                semanticsStack.push(constant);
                break;

            case 8:
                break;

            case 9:
                /* insert top two ids (identifiers) on semantic stack in the sym table mark as reserved */
                insertIO((Token) semanticsStack.pop());
                insertIO((Token) semanticsStack.pop());
                /* insert bottom most id in the sym table (Procedure entry, with num param = 0), mark as reserved */
                insertProcedure((Token) semanticsStack.pop());
                /* INSERT/SEARCH = SEARCH */
                // TODO: pop ids?
                // TODO: take procedure from bottom-most???
                this.insert = false;
                break;

            case 10:
                break;

            case 11:
                break;

            case 12:
                break;

            case 13:
                /* ::: push id (identifier) ::: */
                semanticsStack.push(token);
                break;

            case 14:
                break;

            case 15:
                break;

            case 16:
                break;

            case 17:
                break;

            case 18:
                break;

            case 19:
                break;

            case 20:
                break;

            case 21:
                break;

            case 22:
                break;

            case 23:
                break;

            case 24:
                break;

            case 25:
                break;

            case 26:
                break;

            case 27:
                break;

            case 28:
                break;

            case 29:
                break;

            case 30:
                break;

            case 31:
                break;

            case 32:
                break;

            case 33:
                break;

            case 34:
                break;

            case 35:
                break;

            case 36:
                break;

            case 37:
                break;

            case 38:
                break;

            case 39:
                break;

            case 40:
                break;

            case 41:
                break;

            case 42:
                break;

            case 43:
                break;

            case 44:
                break;

            case 45:
                break;

            case 46:
                break;

            case 47:
                break;

            case 48:
                break;

            case 49:
                break;

            case 50:
                break;

            case 51:
                break;

            case 52:
                break;

            case 53:
                break;

            case 54:
                break;

            case 55:
                break;

            case 56:
                break;

            case 57:
                break;

            case 58:
                break;

            default:
                break;

        }
    } /* end of execute method */

    /**
     * intDistance: the intefer distance of two int constants
     * @param x: a ConstantEntry
     * @param y: a ConstantEntry
     */
    private int intDistance(ConstantEntry x, ConstantEntry y){
        return (Integer.parseInt(x.getName()) - Integer.parseInt(y.getName()) + 1);
    }

    /**
     * intValue: get the integer value of a constants
     * @param x: a ConstantEntry
     */
    private int intValue(ConstantEntry x){
        return Integer.parseInt(x.getName());
    }

    /**
     * storeArray: store the array in memory.
     * @param UB: upper bound, a ConstantEntry.
     * @param LB: lower bound, a ConstantEntry.
     * @param MEM_SIZE: the memory size.
     * @param TYP: the token type
     */
    private void storeArray(ConstantEntry UB, ConstantEntry LB, int MEM_SIZE, TokenType TYP){
        /* ::: For each id on the semantic stack ::: */
        while(!semanticsStack.isEmpty()){
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
        while(!semanticsStack.isEmpty()){
            /* ::: ID = pop id ::: */
            Token ID = (Token) semanticsStack.pop();

            /* ::: if GLOBAL/LOCAL = GLOBAL ::: (store in global memory) */
            VariableEntry var;
            if(global){
                /* ::: insert id in global symbol table (Variable_entry) */
                var = new VariableEntry(ID.getValue(), globalMem, TYP);
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
     * insertLocal: insert into a the local symbol table
     * @param entry: the entry we're inserting.
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
     * lookupEntry: lookup wrapper for specified symbol tables.
     * @param globalEntry: boolean indicating if a global entry or local entry
     * @param name: the String name of the entry.
     * @return a corresponding SymbolTableEntry or null (if doesn't exist).
     */
    private SymbolTableEntry lookupEntry(boolean globalEntry, String name){
        if(globalEntry){
            return (SymbolTableEntry) globalTable.lookup(name.toUpperCase()); // maybe just get rid of interface and cast.
        }
        else{
            SymbolTableEntry value = (SymbolTableEntry) localTable.lookup(name.toUpperCase());
            if(value != null){ return value; }
            return (SymbolTableEntry) globalTable.lookup(name.toUpperCase());
        }
    }

    /**
     * insertIO: insert the reserved file IO
     * @param token: a Token
     */
    private void insertIO(Token token) throws SymbolTableError, SemanticError{
        IODeviceEntry entry = new IODeviceEntry(token.getValue());
        entry.setToReserved();
        insertToGlobal(entry, token.getLineNum());
    }

    /**
     * insertProcedure: insert any reserved procedures
     * @param token: a Token
     */
    private void insertProcedure(Token token) throws SymbolTableError, SemanticError{
        ProcedureEntry entry = new ProcedureEntry(token.getValue(), 0, new LinkedList<ParameterInfo>());
        entry.setToReserved();
        insertToGlobal(entry, token.getLineNum());
    }

    /**
     * semanticStackDump: routine to dump the semantic stack
     */
    private void semanticStackDump(){
        System.out.println("Dumping the semantic stack ...");
        for(int i = 0; i < semanticsStack.size(); i++){
            System.out.println("- "+semanticsStack.get(i));
        }
    }

} /* end of SemanticActions class */