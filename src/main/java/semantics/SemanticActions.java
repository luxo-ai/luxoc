/*
 * File: SemanticActions.java
 *
 * Desc: contains the semantic actions of the compiler.
 *
 * - Semantic Actions 1 [x]
 * - Semantic Actions 2 [ ]
 * - Semantic Actions 3 [ ]
 *
 * actions 1, 2, 3, 4, 6, 7, 9, 13.
 * actions 55, 56, 30, 31, 40, 41, 42, 43, 44, 45, 46, and 48.
 *
 */
package main.java.semantics;

import main.java.lexer.Tokenizer;
import main.java.parser.SemanticAction;
import main.java.semantics.errors.SemanticError;
import main.java.table.*;
import main.java.table.errors.SymbolTableError;
import main.java.token.Token;
import main.java.token.TokenType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * SemanticActions
 * @author Luis Serazo
 */
public class SemanticActions {

    /* flags (SEM 1) */
    private boolean insert = true; /* INSERT / SEARCH */
    private boolean global = true; /* GLOBAL / LOCAL */
    private int globalVars = 0;    /* GLOBAL_VARS */
    private int localVars = 0;     /* LOCAL_VARS */

    /* flags internal to run (SEM 1) */
    private boolean array = true;    /* ARRAY / SIMPLE */
    private int globalMem;
    private int localMem;

    /* semantic action stack */
    private Stack<Object> semanticsStack;

    /* symbolTables */
    private SymbolTable localTable;
    private SymbolTable globalTable;

    /**
     * SemanticAction constructor
     *
     */
    public SemanticActions(Tokenizer tokenizer){
        // NOTE: Find a better way of doing this !
        semanticsStack = new Stack<>();
        this.insert = true; // = INSERT
        this.global = true; // = GLOBAL
        this.globalVars = 0;
        this.localVars = 0;
        this.array = false; // = SIMPLE
        this.globalMem = 0; // ????
        this.localMem = 0;  // ?????
    }

    /**
     * run: runs the semantic analyzer routine.
     */
    public void run(int actionID, Token token) throws SemanticError, SymbolTableError{

        switch (actionID){
            case 1:
                /* INSERT (don't search) */
                insert = true;
                break;

            case 2:
                /* SEARCH (don't insert) */
                insert = false;
                break;

            case 3:
                /* TYP = pop TYPE */
                // TODO: FIX THIS, NOT GOOD PRACTICE TO USE OBJECT (determine correct parents).
                // SHOULD BE A TOKEN (in this case) ?
                Object unkown = semanticsStack.pop();
                // Cast to a TOKEN
                Token tok = (Token) unkown;

                // Per the pseudo code
                TokenType TYP = tok.getTokenType();

                if(array){
                    /* get the upper and lower bounds of the array */
                    ConstantEntry UB = (ConstantEntry) semanticsStack.pop();
                    ConstantEntry LB = (ConstantEntry) semanticsStack.pop();
                    /* calculate the size */
                    int MSIZE = intDistance(UB, LB); /* memory size */

                    // perhaps put this in another function eventually.
                    while (!semanticsStack.isEmpty()) {
                        Token id = (Token) semanticsStack.pop();
                        ArrayEntry arryEntry;
                        if(global){
                            /* insert the ID into the global table */
                            // INSERT
                            arryEntry = new ArrayEntry(id.getValue(), globalMem, TYP, intValue(UB), intValue(LB));

                            insertGlobal(arryEntry);
                            globalMem += MSIZE;
                        }
                        else {
                            /* insert the ID into the local table */
                            // INSERT
                            arryEntry = new ArrayEntry(id.getValue(), localMem, TYP, intValue(UB), intValue(LB));

                            insertLocal(arryEntry);
                            localMem += MSIZE;
                        }

                    }
                }
                else{
                    // save VariableIDs
                    while(!semanticsStack.isEmpty()){
                        Token id = (Token) semanticsStack.pop();
                        // insert top TYP
                        VariableEntry var;
                        if(global){
                            var = new VariableEntry(id.getValue(), globalMem, TYP);
                            // insert global entry
                            insertGlobal(var);
                            globalMem += 1;
                        }
                        else{
                            var = new VariableEntry(id.getValue(), localMem, TYP);
                            // insert local entry
                            insertLocal(var);
                            localMem += 1;
                        }
                    }
                }
                array = false;
                break;

            case 4:
                semanticsStack.push(token);
                break;

            case 5:
                // TODO
                break;

            case 6:
                this.array = true;
                break;

            case 7:
                ConstantEntry constant = new ConstantEntry(token.getValue(), token.getTokenType());
                semanticsStack.push(constant);
                break;

            case 8:
                // TODO
                break;

            case 9:
                // TODO
                insertIO((Token) semanticsStack.pop());
                insertIO((Token) semanticsStack.pop());
                // insert the related procedure
                insertProcedure((Token) semanticsStack.pop());
                this.insert = false;
                break;

            case 10:
                // TODO
                break;

            case 11:
                // TODO
                break;

            case 12:
                // TODO
                break;

            case 13:
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
    }

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
     * insertGlobal: insert into a the global symbol table
     * @param entry: the entry we're inserting.
     * Wrapper for SymbolTable insert (specified).
     */
    private void insertGlobal(SymbolTableEntry entry) throws SemanticError, SymbolTableError{
        entry.nameToUpperCase();
        if(globalTable.lookup(entry.getName()) != null){
            if(lookupEntry(true, entry.getName()).isReserved()){
                throw SemanticError.ReservedName(entry.getName(), 0);
            }
            else{
                throw SemanticError.NameAlreadyDeclared(entry.getName(), 0);
            }
        }
        globalTable.insert(entry);
    }

    /**
     * insertLocal: insert into a the local symbol table
     * @param entry: the entry we're inserting.
     * Wrapper for SymbolTable insert (specified).
     */
    private void insertLocal(SymbolTableEntry entry) throws SemanticError, SymbolTableError{
        entry.nameToUpperCase();
        if(localTable.lookup(entry.getName()) != null){
            if(lookupEntry(false, entry.getName()).isReserved()){
                throw SemanticError.ReservedName(entry.getName(), 0);
            }
            else{
                throw SemanticError.NameAlreadyDeclared(entry.getName(), 0);
            }
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

            if(value != null){
                return value;
            }
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
        // mark as var?
        insertGlobal(entry);
    }

    /**
     * insertProcedure: insert any reserved procedures
     * @param token: a Token
     */
    private void insertProcedure(Token token) throws SymbolTableError, SemanticError{
        ProcedureEntry entry = new ProcedureEntry(token.getValue(), 0, new LinkedList<ParameterInfo>());
        entry.setToReserved();
        insertGlobal(entry);
    }

    /**
     * GEN: generates a new quadruple containing the instruction given in TVICODE
     * @param tviCODE: tvi instructions.
     */
    private void GEN(String tviCode){
        
    }

}
