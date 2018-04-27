/*
 * File: SemanticActions.java
 *
 * Desc: contains the semantic actions of the compiler.
 *
 */
package main.java.semantics;

import main.java.lexer.Tokenizer;
import main.java.semantics.errors.SemanticError;
import main.java.table.*;
import main.java.table.errors.SymbolTableError;
import main.java.token.Token;
import main.java.token.TokenType;

import java.util.*;

/**
 * SemanticActions
 * @author Luis Serazo
 */
public class SemanticActions {

    /* debug mode flag */
    private boolean debug = false;

    /**
     * Actions class to represent a specific action.
     * Run method: overridden when needed.
     */
    class Action{ void run(Token token){ } }

    /**
     * ETYPE: enum flags for representing either
     * Relational or Arithmetic expressions
     */
    enum ETYPE{ ARITHMETIC, RELATIONAL }

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
     * TVI Controls
     */
    private Quadruples quads;
    private int localStore;
    private int globalStore;
    private int tempCount;

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
    private Tokenizer lexer;


    /*
     * Misc
     */
    private SymbolTableEntry currentFunc = null;
    private Stack<Integer> paramCount = new Stack<>();
    private ArrayList<Integer> skipElse;
    private Integer beginLoop;
    private NextParameter nextParam = new NextParameter();
   // private Stack<LinkedList<ParameterInfo>> nextParam;


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
        localStore = 0;

        /* pass control of the lexer */
        this.lexer = lexer;

        /* number of temp variables is zero */
        this.tempCount = 0;

        /* populate the actions */
        actions = new Action[NUM_ACTIONS]; // Note: there are 58 actions.
        init();

        /* install builtins here and ignore semantic action 9 -> just pop off stack */
        SymbolTable.installBuiltins(globalTable);
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
        this.actions[4] = new Action(){ @Override public void run(Token token) { action_5(token); } };
        // 6
        this.actions[5] = new Action(){ @Override public void run(Token token) { action_6(token); } };
        // 7
        this.actions[6] = new Action(){ @Override public void run(Token token) { action_7(token); } };
        // 8
        this.actions[7] = new Action(){ @Override public void run(Token token) { throw SemanticError.NoSuchAction(8); } };
        // 9
        this.actions[8] = new Action(){ @Override public void run(Token token) { action_9(token); } };
        // 10
        this.actions[9] = new Action(){ @Override public void run(Token token) { throw SemanticError.NoSuchAction(10); } };
        // 11
        this.actions[10] = new Action(){ @Override public void run(Token token) { action_11(token); } };
        // 12
        this.actions[11] = new Action(){ @Override public void run(Token token) { throw SemanticError.NoSuchAction(12); } };
        // 13
        this.actions[12] = new Action(){ @Override public void run(Token token) { action_13(token); } };
        // 14
        this.actions[13] = new Action(){ @Override public void run(Token token) { throw SemanticError.NoSuchAction(14); } };
        // 15
        this.actions[14] = new Action(){ @Override public void run(Token token) { action_15(token); } };
        // 16
        this.actions[15] = new Action(){ @Override public void run(Token token) { action_16(token); } };
        // 17
        this.actions[16] = new Action(){ @Override public void run(Token token) { action_17(token); } };
        // 18
        this.actions[17] = new Action(){ @Override public void run(Token token) { throw SemanticError.NoSuchAction(18); } };
        // 19
        this.actions[18] = new Action(){ @Override public void run(Token token) { action_19(token); } };
        // 20
        this.actions[19] = new Action(){ @Override public void run(Token token) { action_20(token); } };
        // 21
        this.actions[20] = new Action(){ @Override public void run(Token token) { action_21(token); } };
        // 22
        this.actions[21] = new Action(){ @Override public void run(Token token){ action_22(token); } };
        // 23
        this.actions[22] = new Action(){ @Override public void run(Token token) { throw SemanticError.NoSuchAction(23); } };
        // 24
        this.actions[23] = new Action(){ @Override public void run(Token token) { action_24(token); } };
        // 25
        this.actions[24] = new Action(){ @Override public void run(Token token) { action_25(token); } };
        // 26
        this.actions[25] = new Action(){ @Override public void run(Token token) { action_26(token); } };
        // 27
        this.actions[26] = new Action(){ @Override public void run(Token token) { action_27(token); } };
        // 28
        this.actions[27] = new Action(){ @Override public void run(Token token) { action_28(token); } };
        // 29
        this.actions[28] = new Action(){ @Override public void run(Token token) { action_29(token); } };
        // 30
        this.actions[29] = new Action(){ @Override public void run(Token token) { action_30(token); } };
        // 31
        this.actions[30] = new Action(){ @Override public void run(Token token) { action_31(token); } };
        // 32
        this.actions[31] = new Action(){ @Override public void run(Token token) { action_32(token); } };
        // 33
        this.actions[32] = new Action(){ @Override public void run(Token token) { action_33(token); } };
        // 34
        this.actions[33] = new Action(){ @Override public void run(Token token) { action_34(token); } };
        // 35
        this.actions[34] = new Action(){ @Override public void run(Token token) { action_35(token); } };
        // 36
        this.actions[35] = new Action(){ @Override public void run(Token token) { action_36(token); } };
        // 37
        this.actions[36] = new Action(){ @Override public void run(Token token) { action_37(token); } };
        // 38
        this.actions[37] = new Action(){ @Override public void run(Token token) { action_38(token); } };
        // 39
        this.actions[38] = new Action(){ @Override public void run(Token token) { action_39(token); } };
        // 40
        this.actions[39] = new Action(){ @Override public void run(Token token) { action_40(token); } };
        // 41
        this.actions[40] = new Action(){ @Override public void run(Token token) { action_41(token); } };
        // 42
        this.actions[41] = new Action(){ @Override public void run(Token token) { action_42(token); } };
        // 43
        this.actions[42] = new Action(){ @Override public void run(Token token) { action_43(token); } };
        // 44
        this.actions[43] = new Action(){ @Override public void run(Token token) { action_44(token); } };
        // 45
        this.actions[44] = new Action(){ @Override public void run(Token token) { action_45(token); } };
        // 46
        this.actions[45] = new Action(){ @Override public void run(Token token) { action_46(token); } };
        // 47
        this.actions[46] = new Action(){ @Override public void run(Token token) { action_47(token);} };
        // 48
        this.actions[47] = new Action(){ @Override public void run(Token token) { action_48(token); } };
        // 49
        this.actions[48] = new Action(){ @Override public void run(Token token) { action_49(token); } };
        // 50
        this.actions[49] = new Action(){ @Override public void run(Token token) { action_50(token); } };
        // 51
        this.actions[50] = new Action(){ @Override public void run(Token token) { action_51(token); } };
        // 52
        this.actions[51] = new Action(){ @Override public void run(Token token) { action_52(token); } };
        // 53
        this.actions[52] = new Action(){ @Override public void run(Token token) { action_53(token); } };
        // 54
        this.actions[53] = new Action(){ @Override public void run(Token token) { action_54(token); } };
        // 55
        this.actions[54] = new Action(){ @Override public void run(Token token) { action_55(token); } };
        // 56
        this.actions[55] = new Action(){ @Override public void run(Token token) { action_56(token); } };
        // 57
        this.actions[56] = new Action(){ @Override public void run(Token token) { throw SemanticError.NoSuchAction(57); } };
        // 58
        this.actions[57] = new Action(){ @Override public void run(Token token) { throw SemanticError.NoSuchAction(58); } };
    } // end of INIT

    /**
     * execute: runs the semantic analyzer routine.
     * @param actionID: the action number passed by the parser.
     * @param token: the current Token being considered.
     * @throws SemanticError when a semantic error occurs in one of the IDs.
     * @throws SymbolTableError when the Symbol Table encounters a problem.
     *
     */
    public void execute(int actionID, Token token) throws SemanticError, SymbolTableError{
        try{
            this.actions[actionID - 1].run(token);
            if(debug){ semanticStackDump(); }
        }
        catch (ArrayIndexOutOfBoundsException ex){ throw SemanticError.ActionDoesNotExist(actionID); }
    }


    /* -------------------- SEMANTIC ACTIONS ------------------- */

    /**
     * action_1
     * @param token: the Token in question.
     * Inserting into symbol table
     *
     */
    private void action_1(Token token){
        /* INSERT/SEARCH = INSERT */
        this.insert = true;
    }

    /**
     * action_2
     * @param token: the Token in question.
     * No longer inserting into symbol table
     */
    private void action_2(Token token){
        /* INSERT/SEARCH = SEARCH */
        this.insert = false;
    }

    /**
     * action_3
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
     * action_4
     * @param token: the Token in question.
     * Push declarations of same type. i.e: var i,x,y: integer
     */
    private void action_4(Token token){
        /* push TYPE through the full Token */
        semanticsStack.push(token);
    }

    /**
     * action_5
     */
    private void action_5(Token token){
        this.insert = false;
        SymbolTableEntry id = (SymbolTableEntry) semanticsStack.pop();
        generate("PROCBEGIN", id);
        this.localStore = quads.nextQuadIndex();
        generate("alloc", "_");
    }

    /**
     * action_6
     * @param token: the Token in question.
     */
    private void action_6(Token token){
        /* ARRAY/SIMPLE = ARRAY */
        this.array = true;
    }

    /**
     * action_7
     * @param token: the Token in question.
     */
    private void action_7(Token token){
        /* push constant (real or int constant) */
        ConstantEntry constant = new ConstantEntry(token.getValue(), token.getTokenType());
        semanticsStack.push(constant);
    }

    /**
     * action_9
     * @param token: the Token in question.
     */
    private void action_9(Token token){
        /* if there are any program parameters, there should at least be input output */
        try{
            // MAIN installed using builtins routine
            // READ installed using builtins routine
            // WRITE installed using builtins routine
            // INPUT installed using builtins routine
            // OUTPUT installed using builtins routine
            semanticsStack.pop(); // input
            semanticsStack.pop(); // output
            /* name of the program -> e.g: myProgram (third on stack, so bottom most) */
            insertProcedure((Token) semanticsStack.pop());
            /* INSERT/SEARCH = SEARCH */
            this.insert = false;
            generate("call", (globalTable.lookup("MAIN")).getName().toLowerCase(), 0);
            generate("exit");
        }
        catch (EmptyStackException ex){ throw SemanticError.InputOutputNotSpecified(); }
    }

    /**
     * action_11
     */
    private void action_11(Token token){
        this.global = true;
        localTable.delete();
        currentFunc = null;
        backPatch(localStore, localMem);
        generate("free", localMem);
        generate("PROCEND");
    }

    /**
     * action_13
     * @param token: the Token in question.
     */
    private void action_13(Token token){
        /* push id (identifier) */
        semanticsStack.push(token);
    }

    /**
     * action_15
     */
    private void action_15(Token token){
        VariableEntry funResult = createFunResult("$$" + token.getValue(), TokenType.INTEGER); // use tempCount and increment?
        FunctionEntry func = new FunctionEntry(token.getValue(), 0, new LinkedList<ParameterInfo>(), funResult);
        if(global){ insertToGlobal(func, token.getLineNum()); }
        else{ insertToLocal(func, token.getLineNum()); }

        semanticsStack.push(func);
        this.global = false;
        this.localMem = 0;
    }

    /**
     * action_16
     */
    private void action_16(Token token){
        Token tok = (Token) semanticsStack.pop();
        TokenType tType = tok.getTokenType();
        SymbolTableEntry id = (SymbolTableEntry) semanticsStack.peek();
        id.setType(tType);
        currentFunc = id;
        /* set the type in the symbol table too */
        lookupEntry("$$" + id.getName()).setType(tType);
    }

    /**
     * action_17
     */
    private void action_17(Token token){
        ProcedureEntry proc = new ProcedureEntry(token.getValue(), 0, new LinkedList<ParameterInfo>());
        if(global){ insertToGlobal(proc, token.getLineNum()); }
        else{ insertToLocal(proc, token.getLineNum()); }

        semanticsStack.push(proc);
        this.global = false;
        localMem = 0;
    }

    /**
     * action_19
     */
    private void action_19(Token token){ paramCount.push(0); }

    /**
     * action_20
     */
    private void action_20(Token token){
        Integer numberOfParam = paramCount.pop();
        /* obtain the procedure or function through base class */
        RoutineEntry rout = (RoutineEntry) semanticsStack.peek();
        rout.setNumOfParam(numberOfParam);
    }

    /**
     * action_21
     * For same type
     */
    private void action_21(Token token){
        int lower = -1,upper = -1;
        int numOfParam = paramCount.pop();

        Token tok = (Token) semanticsStack.pop();
        TokenType type = tok.getTokenType();

        if(this.array){
            upper = intValue(((ConstantEntry) semanticsStack.pop()));
            lower = intValue(((ConstantEntry) semanticsStack.pop()));
        }

        LinkedList<ParameterInfo> paramList = new LinkedList<>();
            while(semanticsStack.peek() instanceof Token && (((Token) semanticsStack.peek()).getTokenType() == TokenType.IDENTIFIER)){
                Token id = (Token) semanticsStack.pop();
                ParameterInfo paramInfo = new ParameterInfo(id.getValue(), type);

                if(this.array){ storeArraySA21(id.getValue(), lower, upper, localMem, type, token.getLineNum()); }
                else{ storeVarSA21(id.getValue(), type, localMem, token.getLineNum()); }

                numOfParam++;
                paramList.add(paramInfo);
            }
            this.array = false;

        if(semanticsStack.peek() instanceof RoutineEntry){
            RoutineEntry rout = (RoutineEntry) semanticsStack.peek();
            rout.appendParamInfo(paramList);
        }
        paramCount.push(numOfParam);

    }

    /**
     * action_22
     */
    private void action_22(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();

        isRelational(eTYPE, token.getLineNum());

        ArrayList<Integer> eFALSE = (ArrayList<Integer>) semanticsStack.pop();
        ArrayList<Integer> eTRUE = (ArrayList<Integer>) semanticsStack.peek();

        semanticsStack.push(eFALSE);
        backPatch(eTRUE, quads.nextQuadIndex());
    }

    /**
     * action_24
     */
    private void action_24(Token token){
        beginLoop = quads.nextQuadIndex();
        semanticsStack.push(beginLoop);
    }

    /**
     * action_25
     */
    private void action_25(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isRelational(eTYPE, token.getLineNum());

        ArrayList<Integer> eFALSE = (ArrayList<Integer>) semanticsStack.pop();
        ArrayList<Integer> eTRUE = (ArrayList<Integer>) semanticsStack.peek();
        semanticsStack.push(eFALSE);
        backPatch(eTRUE, quads.nextQuadIndex());
    }

    /**
     * action_26
     */
    private void action_26(Token token){
        ArrayList<Integer> eFALSE = (ArrayList<Integer>) semanticsStack.pop();
        /* pop ETRUE */
        semanticsStack.pop();
        Integer begin = (Integer) semanticsStack.pop();
        generate("goto", begin.toString());
        backPatch(eFALSE, quads.nextQuadIndex());
    }

    /**
     * action_27
     */
    private void action_27(Token token){
        skipElse = makeList(quads.nextQuadIndex());
        generate("goto", "_");
        ArrayList<Integer> eFALSE = (ArrayList<Integer>) semanticsStack.peek();
        backPatch(eFALSE, quads.nextQuadIndex());
        semanticsStack.push(skipElse);
    }

    /**
     * action_28
     */
    private void action_28(Token token){
        skipElse = (ArrayList<Integer>) semanticsStack.pop();
        /* pop EFALSE and ETRUE */
        semanticsStack.pop();
        semanticsStack.pop();
        backPatch(skipElse, quads.nextQuadIndex());
    }

    /**
     * action_29
     */
    private void action_29(Token token){
        ArrayList<Integer> eFALSE = (ArrayList<Integer>) semanticsStack.pop();
        /* pop ETRUE */
        semanticsStack.pop();
        backPatch(eFALSE, quads.nextQuadIndex());
    }

    /**
     * action_30
     * @param token: the Token in question.
     */
    private void action_30(Token token){
        /* lookup id in symbol table */
        SymbolTableEntry id = lookupEntry(token.getValue());
        /* if not found, ERROR (undeclared variable) */
        if(id == null){ throw SemanticError.UndeclaredVariable(token.getValue(), token.getLineNum()); }
        semanticsStack.push(id);
        /* added after semantic actions 3 */
        semanticsStack.push(ETYPE.ARITHMETIC);
    }

    /**
     * action_31
     * @param token: the Token in question.
     */
    private void action_31(Token token) {
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isArithmetic(eTYPE, token.getLineNum());

        /* Grab the operands and offset off the stack */
        SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();
        SymbolTableEntry offset = (SymbolTableEntry) semanticsStack.pop(); // simply make null.
        SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();
        // handle errors?
        generationRoutineSA31(id1, offset, id2, token.getLineNum());
    }

    /**
     * action_32
     */
    private void action_32(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isArithmetic(eTYPE, token.getLineNum());
        SymbolTableEntry id = (SymbolTableEntry) semanticsStack.peek();
        if(!id.isArray()){ throw SemanticError.ExpectedArray(token.getLineNum()); }
    }

    /**
     * action_33
     */
    private void action_33(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isArithmetic(eTYPE, token.getLineNum());
        SymbolTableEntry id = (SymbolTableEntry) semanticsStack.pop();
        ArrayEntry array = (ArrayEntry) semanticsStack.peek();
        /* check if bounds make sense */
        if(id.getType() != TokenType.INTEGER){ throw SemanticError.InvalidArrayBounds(token.getLineNum()); }
        else{
            SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, TokenType.INTEGER);
            generate("sub", id, array.getLowerBound(), $$TEMP);
            semanticsStack.push($$TEMP);
        }
    }

    /**
     * action_34
     */
    private void action_34(Token token){
        if(semanticsStack.peek() instanceof ETYPE){
            semanticsStack.pop();
        }
        if(!semanticsStack.isEmpty()){
            SymbolTableEntry id = (SymbolTableEntry) semanticsStack.peek();
            if(id.isFunction()){ action_52(token); }
            else{ semanticsStack.push(null); }
        }
        else{ semanticsStack.push(null); }
    }

    /**
     * action_35
     */
    private void action_35(Token token){
        paramCount.push(0);
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        ProcedureEntry proc = (ProcedureEntry) semanticsStack.peek();
        semanticsStack.push(eTYPE);
        nextParam.push(proc.getParamInfo());
    }

    /**
     * action_36
     */
    private void action_36(Token token){
        if(semanticsStack.peek() instanceof ETYPE){ semanticsStack.pop(); }
        ProcedureEntry proc = (ProcedureEntry) semanticsStack.pop();

        if(proc.getNumOfParam() != 0){ throw SemanticError.WrongNumberOfParameters(proc.getName(), token.getLineNum()); }
        generate("call", proc, 0);
    }


    /**
     * action_37
     */
    private void action_37(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isArithmetic(eTYPE, token.getLineNum());
        if(semanticsStack.peek() instanceof ETYPE){ semanticsStack.pop(); }

        SymbolTableEntry id = (SymbolTableEntry) semanticsStack.peek();
        if(!(id.isVariable() || id.isConstant() || id.isFunctionResult() || id.isArray())){
            throw SemanticError.InvalidParameter(id.getName(), token.getLineNum());
        }
        paramCount.push((paramCount.pop()) + 1);
        RoutineEntry rout = getRoutine();
        if(rout == null){ throw SemanticError.RoutineNotFound(token.getLineNum()); }

        if(rout.getName() != null && !(rout.getName().equals("READ") || rout.getName().equals("WRITE"))){
            if(paramCount.peek() > rout.getNumOfParam()){ throw SemanticError.WrongNumberOfParameters(rout.getName(), token.getLineNum()); }
            ParameterInfo paramInfo = nextParam.nextParam();
            ensureParamType(id.getType(), paramInfo.getType(), token.getLineNum());
            if(paramInfo.isArray()){ ensureArrayBounds(paramInfo, token.getLineNum()); }
            // nextParam.increment();
        }
    }

    /**
     * for action 37
     */
    private void ensureParamType(TokenType type1, TokenType type2, int lineNumber){
        if(type1 == TokenType.INTCONSTANT){ type1 = TokenType.INTEGER; }
        else if(type1 == TokenType.REALCONSTANT){ type1 = TokenType.REAL; }
        /* adjust type 2 */
        if(type2 == TokenType.INTCONSTANT){ type2 = TokenType.INTEGER; }
        else if(type2 == TokenType.REALCONSTANT){ type2 = TokenType.REAL; }

        /* check if the same type */
        if(!(type1 == type2)){ throw SemanticError.ParameterTypeMismatch(type1, type2, lineNumber); }
    }

    /**
     * for action 37
     */
    private void ensureArrayBounds(ParameterInfo info, int lineNumber){
        ArrayEntry arry = getArray();
        if(arry == null){ throw SemanticError.RoutineNotFound(lineNumber); }

        if(arry.getLowerBound() != info.getLB()){ throw SemanticError.InvalidArrayBound(false, lineNumber); }
        if(arry.getUpperBound() != info.getUB()){ throw SemanticError.InvalidArrayBound(true, lineNumber); }
    }


    /**
     * for action 37
     */
    private ArrayEntry getArray(){
        Object[] semanticArray = semanticsStack.toArray();
        ArrayEntry entry = null;
        for(Object obj : semanticArray){
            if(obj instanceof ArrayEntry){
                entry = (ArrayEntry) obj;
                break;
            }
        }
        return entry;
    }

    /**
     * action_38
     */
    private void action_38(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isArithmetic(eTYPE, token.getLineNum());
        semanticsStack.push(token); /* operators are left as Tokens */
    }

    /**
     * action_39
     */
    private void action_39(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isArithmetic(eTYPE, token.getLineNum());

        SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();
        Token operator = (Token) semanticsStack.pop();
        SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();

        SymbolTableEntry $$TEMP;
        switch(typeCheck(id1, id2)){
            case 0:
            case 1:
                generate(opToString(operator), id1, id2, "_");
                break;
            case 2:
                $$TEMP = create("$$TEMP" + tempCount, TokenType.REAL);
                generate("ltof", id2, $$TEMP);
                generate(opToString(operator), id1, $$TEMP, "_");
                break;
            case 3:
                $$TEMP = create("$$TEMP" + tempCount, TokenType.REAL);
                generate("ltof", id1, $$TEMP);
                generate(opToString(operator), $$TEMP, id2, "_");
        }
        generate("goto", "_");
        ArrayList<Integer> eTRUE = makeList(quads.nextQuadIndex() - 2);
        ArrayList<Integer> eFALSE = makeList(quads.nextQuadIndex() - 1);
        semanticsStack.push(eTRUE);
        semanticsStack.push(eFALSE);
        semanticsStack.push(ETYPE.RELATIONAL);
    }

    /**
     * action_40
     */
    private void action_40(Token token){
        /* push sign on the stack */
        semanticsStack.push(token);
    }

    /**
     * action_41
     */
    private void action_41(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isArithmetic(eTYPE, token.getLineNum());

        /* pop off the id and sign from the stack */
        SymbolTableEntry id = (SymbolTableEntry) semanticsStack.pop();
        Token sign = (Token) semanticsStack.pop();
        /* check if the sign is a unary minus */
        if(sign.getTokenType() == TokenType.UNARYMINUS){
            SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, id.getType());
            if(id.getType() == TokenType.INTEGER){
                generate("uminus", id, $$TEMP);
            }
            else{
                generate("fuminus", id, $$TEMP);
            }
            semanticsStack.push($$TEMP);
        }
        else{ semanticsStack.push(id); }
        semanticsStack.push(ETYPE.ARITHMETIC);
    }

    /**
     * action_42
     */
    private void action_42(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        if(token.getOpType() == Token.OperatorType.OR){
            isRelational(eTYPE, token.getLineNum());
            ArrayList<Integer> eFALSE = (ArrayList<Integer>) semanticsStack.peek(); /* cast to ArrayList<Integer>? */
            backPatch(eFALSE, quads.nextQuadIndex()); // TODO: pop => peek
        }
        else{ isArithmetic(eTYPE, token.getLineNum()); }
        /* push operator as a Token */
        semanticsStack.push(token);
    }

    /**
     * action_43:
     * Note: always favor converting to real, if a real present. Think scope.
     */
    private void action_43(Token token){
        /* obtain ids and the operator between them */
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        if(eTYPE == ETYPE.RELATIONAL){
            ArrayList<Integer> eFALSE2 = (ArrayList<Integer>) semanticsStack.pop();
            ArrayList<Integer> eTRUE2 = (ArrayList<Integer>) semanticsStack.pop();
            Token operator = (Token) semanticsStack.pop();

            if(operator.getOpType() == Token.OperatorType.OR){
                /* pop E(1) FALSE */
                semanticsStack.pop();
                ArrayList<Integer> eTRUE = (ArrayList<Integer>) semanticsStack.pop();
                ArrayList<Integer> eTRUE3 = merge(eTRUE, eTRUE2);

                semanticsStack.push(eTRUE3);
                semanticsStack.push(eFALSE2); /* eFALSE3 = eFALSE2 */
                semanticsStack.push(ETYPE.RELATIONAL);
            }
            else{
                semanticsStack.push(operator);
                semanticsStack.push(eTRUE2);
                semanticsStack.push(eFALSE2);
            }
        }
        else{
            /* from before */
            SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();
            Token operator = (Token) semanticsStack.pop();
            SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();
            isArithmetic(eTYPE, token.getLineNum());
            generationRoutineSA43(id1, operator, id2);
            semanticsStack.push(ETYPE.ARITHMETIC);
        }
    }

    /**
     * action_44:
     */
    private void action_44(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        if(eTYPE == ETYPE.RELATIONAL){
            if(token.getOpType() == Token.OperatorType.AND){
                ArrayList<Integer> eFALSE = (ArrayList<Integer>) semanticsStack.pop();
                ArrayList<Integer> eTRUE = (ArrayList<Integer>) semanticsStack.peek();
                semanticsStack.push(eFALSE);
                backPatch(eTRUE, quads.nextQuadIndex());
            }
        }
        /* push operator --> a Token */
        semanticsStack.push(token);
    }

    /**
     * action_45
     */
    private void action_45(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();

        if(eTYPE == ETYPE.RELATIONAL){
            ArrayList<Integer> eFALSE2 = (ArrayList<Integer>) semanticsStack.pop();
            ArrayList<Integer> eTRUE2 = (ArrayList<Integer>) semanticsStack.pop();
            Token operator = (Token) semanticsStack.pop();
            if(operator.getOpType() == Token.OperatorType.AND){
                ArrayList<Integer> eFALSE = (ArrayList<Integer>) semanticsStack.pop();
                /* pop E(1) TRUE */
                semanticsStack.pop();
                semanticsStack.push(eTRUE2);
                semanticsStack.push(merge(eFALSE, eFALSE2));
                semanticsStack.push(ETYPE.RELATIONAL);
            }
        }
        /* otherwise same routine as before */
        else{
            isArithmetic(eTYPE, token.getLineNum());

            /* obtain ids and the operator between them */
            SymbolTableEntry id2 = (SymbolTableEntry) semanticsStack.pop();
            Token operator = (Token) semanticsStack.pop();
            SymbolTableEntry id1 = (SymbolTableEntry) semanticsStack.pop();

            /* check if a bad mod (id1 and id2) are not ints */
            if(typeCheck(id1, id2) != 0 && operator.getOpType() == Token.OperatorType.MOD){
                throw SemanticError.BadMod(operator.getLineNum());
            }
            if (typeCheck(id1, id2) != 0 && operator.getOpType() == Token.OperatorType.DIV) {
                throw SemanticError.BadDiv(operator.getLineNum());
            }
            generationRoutineSA45(id1, operator, id2);
            semanticsStack.push(ETYPE.ARITHMETIC);
        }
    }

    /**
     * action_46
     */
    private void action_46(Token token){
        if(token.getTokenType() == TokenType.IDENTIFIER){ findAndPushID(token); }
        else if(isConstant(token)){ findAndPushConst(token); }
        semanticsStack.push(ETYPE.ARITHMETIC);
    }

    /**
     * action_47
     */
    private void action_47(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isRelational(eTYPE, token.getLineNum());

        ArrayList<Integer> eTRUE = (ArrayList<Integer>) semanticsStack.pop();
        ArrayList<Integer> eFALSE = (ArrayList<Integer>) semanticsStack.pop();
        /* push on stack in opposite order */
        semanticsStack.push(eTRUE);
        semanticsStack.push(eFALSE);
        semanticsStack.push(ETYPE.RELATIONAL);
    }

    /**
     * action_48
     */
    private void action_48(Token token){
        /* pop ETYPE if possible */
        if(semanticsStack.peek() instanceof ETYPE){ semanticsStack.pop(); }
        /* obtain the offset */
        SymbolTableEntry offset = (SymbolTableEntry) semanticsStack.pop();
        /* if the offset is not null, load src + offset into dest -> push dest. */
        if(offset != null){
            if(offset.isFunction()){ action_52(token); }
            else{
                SymbolTableEntry id = (SymbolTableEntry) semanticsStack.pop();
                SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, id.getType());
                generate("load", id, offset, $$TEMP);
                semanticsStack.push($$TEMP);
            }
        }
        semanticsStack.push(ETYPE.ARITHMETIC);
    }

    /**
     * action_49
     */
    private void action_49(Token token){
        ETYPE eTYPE = (ETYPE) semanticsStack.pop();
        isArithmetic(eTYPE, token.getLineNum());
        SymbolTableEntry id = (SymbolTableEntry) semanticsStack.peek();
        semanticsStack.push(eTYPE);

        if(!(id.isFunction())){ throw SemanticError.TypeMismatch(token.getLineNum()); }

        paramCount.push(0);
        FunctionEntry func = (FunctionEntry) id;
        nextParam.push(func.getParamInfo());
    }

    /**
     * action_50
     */
    private void action_50(Token token) {
        if(semanticsStack.peek() instanceof SymbolTableEntry) {
            paramGenerationRoutine();
           // Stack<SymbolTableEntry> inverseStack = eachEntryFromBottomToTop();

           // while (!(inverseStack.isEmpty())) {
           //     SymbolTableEntry entry = inverseStack.pop();
           //     generate("param", entry);
           //     localMem++;
           //  }

            int numOfParam = paramCount.pop();
            /* pop ETYPE */
            semanticsStack.pop();
            SymbolTableEntry entry = (SymbolTableEntry) semanticsStack.pop();


            if (numOfParam > ((FunctionEntry) entry).getNumOfParam()) {
                throw SemanticError.WrongNumberOfParameters(entry.getName(), token.getLineNum());
            }
            generate("call", entry, numOfParam);
            nextParam.pop();
            SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, entry.getType());

            generate("move", ((FunctionEntry) entry).getResult(), $$TEMP);
            semanticsStack.push($$TEMP);
            semanticsStack.push(ETYPE.ARITHMETIC);
        }
    }

    /**
     * for action 51
     */
    private void paramGenerationRoutine() {
        SymbolTableEntry id;
        while ((!semanticsStack.isEmpty()) && semanticsStack.peek() instanceof SymbolTableEntry) {
            id = (SymbolTableEntry) semanticsStack.pop();
            generate("param", id);
            localMem++;
        }
    }





    /**
     * action_51
     */
    private void action_51(Token token){
        RoutineEntry rout = getRoutine();
        if(rout == null){ throw SemanticError.RoutineNotFound(token.getLineNum()); }

        if(rout.getName() != null && rout.getName().equals("READ")){ readSA51(token); }
        else if(rout.getName() != null && rout.getName().equals("WRITE")){ writeSA51(token); }
        else{
            if(paramCount.peek() != rout.getParamInfo().size()){
                throw SemanticError.WrongNumberOfParameters(rout.getName(), token.getLineNum());
            }

           // Stack<SymbolTableEntry> container = eachEntryFromBottomToTop();
           // while(!container.isEmpty()){
           //     SymbolTableEntry param = container.pop();
           //     generate("param", param);
           //     localMem++;
          //  }
            paramGenerationRoutine();
            generate("call", rout, paramCount.pop());
            nextParam.pop();
            if(semanticsStack.peek() instanceof ETYPE){ semanticsStack.pop(); }
            semanticsStack.pop();
        }
    }

    /**
     * for action 51
     */
    private Stack<SymbolTableEntry> eachEntryFromBottomToTop() {
        Stack<SymbolTableEntry> container = new Stack<>();
        SymbolTableEntry id;
        while ((!semanticsStack.isEmpty()) && semanticsStack.peek() instanceof SymbolTableEntry) {
            id = (SymbolTableEntry) semanticsStack.peek();
            container.push(id);
            semanticsStack.pop();
        }
        return container;
    }

    /**
     * for action 51
     */
    private RoutineEntry getRoutine(){
        Object[] semanticArray = semanticsStack.toArray();
        RoutineEntry rout = null;

        for(Object obj : semanticArray){
            if(obj instanceof RoutineEntry){
                rout = (RoutineEntry) obj;
                break;
            }
        }
        return rout;
    }

    /**
     * for action 51
     */
    private void generate(String tviCode, SymbolTableEntry operand1, int operand2){
        String op1 = toAddress(tviCode, operand1);
        String[] quadrpl = {tviCode, op1, String.valueOf(operand2), null};
        quads.addQuad(quadrpl);
    }

    /**
     * for action 51
     */
    private void writeSA51(Token token){
        Stack<SymbolTableEntry> entryStack = eachEntryFromBottomToTop();
        while(!entryStack.isEmpty()){
            SymbolTableEntry entry = entryStack.pop();
            generate("print", "\"" + entry.getName() + " = " + "\"");
            if(entry.getType() == TokenType.REAL){ generate("foutp", lookupEntry(entry.getName())); }
            else{ generate("outp", entry); }

            generate("newl");
        }
        paramCount.pop();
        if(semanticsStack.peek() instanceof ETYPE){ semanticsStack.pop(); }
        semanticsStack.pop();

    }

    /**
     * for action 51
     */
    private void readSA51(Token token){
        Stack<SymbolTableEntry> entryStack = eachEntryFromBottomToTop();
        while(!entryStack.isEmpty()){
            SymbolTableEntry entry = entryStack.pop();
            generate("print", "\"Enter Value: \"");
            if(entry.getType() == TokenType.REAL){ generate("finp", lookupEntry(entry.getName())); }
            else{ generate("inp", entry); }
        }
        paramCount.pop();
        if(semanticsStack.peek() instanceof ETYPE){ semanticsStack.pop(); }
        semanticsStack.pop();

    }

    /**
     * action_52
     */
    private void action_52(Token token){
        /* pop ETYPE */
        semanticsStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) semanticsStack.pop();

        if(!id.isFunction()){ throw SemanticError.TypeMismatch(token.getLineNum()); }

        FunctionEntry func = (FunctionEntry) id;
        if(func.getNumOfParam() > 0){ throw SemanticError.WrongNumberOfParameters(func.getName(), token.getLineNum()); }

        generate("call", func, 0);
        SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, id.getType());
        generate("move", func.getResult(), $$TEMP);
        semanticsStack.push($$TEMP);
        semanticsStack.push(ETYPE.ARITHMETIC);
    }

    /**
     * action_53
     */
    private void action_53(Token token){
        /* pop ETYPE */
        semanticsStack.pop();
        /* pop id */
        SymbolTableEntry id = (SymbolTableEntry) semanticsStack.pop();

        if(id.isFunction()){
            if(id != currentFunc){ throw SemanticError.BadFunction(id.getName(), token.getLineNum()); }
            semanticsStack.push(((FunctionEntry) id).getResult());
        }
        else{
            semanticsStack.push(id);
            semanticsStack.push(ETYPE.ARITHMETIC); // eTYPE
        }
    }

    /**
     * action_54
     */
    private void action_54(Token token){
        SymbolTableEntry id;
        if(semanticsStack.peek() instanceof ETYPE){
            ETYPE eTYPE = (ETYPE) semanticsStack.pop();
            id = (SymbolTableEntry) semanticsStack.peek();
            semanticsStack.push(eTYPE);
        }
        else{
            /* pop id */
            id = (SymbolTableEntry) semanticsStack.pop();
        }
        if(!(id.isProcedure())){ throw SemanticError.BadProcedure(id.getName(), token.getLineNum()); }
    }

    /**
     * action_55
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
     * action_56
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

    /* ----------------- END OF SEMANTIC ACTIONS ----------------- */


    /* ------------------ GENERATION ROUTINES ------------------ */

    /**
     * generationRoutineSA31: for semantic action 31
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
     * generationRoutineSA43: for semantic action 43
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
                /* generate the tvi target code */
                generate(opToString(operator), id1, id2, $$TEMP);
                /* push result */
                semanticsStack.push($$TEMP);
                break;

            /* id1 = REAL , id2 = REAL */
            case 1:
                /* RESULT := */
                $$TEMP = create("$$TEMP" + tempCount, TokenType.REAL);
                generate("f" + opToString(operator), id1, id2, $$TEMP);
                /* push result */
                semanticsStack.push($$TEMP);
                break;

            /* id1 = REAL , id2 = INTEGER */
            case 2:
                /* TEMP1 for casting id2 to REAL */
                $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
                /* RESULT := */
                $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);

                generate("ltof", id2, $$TEMP1);
                generate("f" + opToString(operator), id1, $$TEMP1, $$TEMP2); // float operator
                /* push result */
                semanticsStack.push($$TEMP2);
                break;

            /* id1 = INTEGER , id2 = REAL */
            default:
                /* TEMP1 for casting id1 to REAL */
                $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
                /* RESULT := */
                $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);

                generate("ltof", id1, $$TEMP1);
                generate("f" + opToString(operator), $$TEMP1, id2, $$TEMP2); // float operator
                /* push result */
                semanticsStack.push($$TEMP2);
                break;
        }
    }

    /**
     * generationRoutineSA45: for semantic action 45
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
     * generationRoutineSA45a: for semantic action 45
     */
    private void generationRoutineSA45a(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        if (operator.getOpType() == Token.OperatorType.MOD) {
            /* */
            SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.INTEGER);
            SymbolTableEntry $$TEMP3 = create("$$TEMP" + tempCount, TokenType.INTEGER);

            generate("div", id1, id2, $$TEMP1);
            generate("mul", id2, $$TEMP1, $$TEMP2);
            generate("sub", id1, $$TEMP2, $$TEMP3);
            /* push result */
            semanticsStack.push($$TEMP3);

        } else if (operator.getOpType() == Token.OperatorType.DIVIDE) {
            SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
            SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);
            SymbolTableEntry $$TEMP3 = create("$$TEMP" + tempCount, TokenType.REAL);

            generate("ltof", id1, $$TEMP1);
            generate("ltof", id2, $$TEMP2);
            generate("fdiv", $$TEMP1, $$TEMP2, $$TEMP3);

            /* push result */
            semanticsStack.push($$TEMP3);

        } else {
            SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, TokenType.INTEGER);
            generate(opToString(operator), id1, id2, $$TEMP);
            semanticsStack.push($$TEMP);
        }
    }

    /**
     * generationRoutineSA45b: for semantic action 45
     */
    private void generationRoutineSA45b(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, TokenType.REAL);
        generate("f" + opToString(operator), id1, id2, $$TEMP);
        semanticsStack.push($$TEMP);
    }

    /**
     * generationRoutineSA45c: for semantic action 45
     */
    private void generationRoutineSA45c(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
        SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);

        generate("ltof", id2, $$TEMP1);
        generate("f" + opToString(operator), id1, $$TEMP1, $$TEMP2);
        semanticsStack.push($$TEMP2);
    }

    /**
     * generationRoutineSA45d: for semantic action 45
     */
    private void generationRoutineSA45d(SymbolTableEntry id1, Token operator, SymbolTableEntry id2){
        SymbolTableEntry $$TEMP1 = create("$$TEMP" + tempCount, TokenType.REAL);
        SymbolTableEntry $$TEMP2 = create("$$TEMP" + tempCount, TokenType.REAL);

        generate("ltof", id1, $$TEMP1);
        generate("f" + opToString(operator), $$TEMP1, id2, $$TEMP2);
        semanticsStack.push($$TEMP2);
    }

    /* ------------ END OF GENERATION ROUTINES ------------------ */


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
        while((!semanticsStack.isEmpty()) && (semanticsStack.peek() instanceof Token)){ // TODO: maybe use the new pseudo code? is identifier?
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
        while((!semanticsStack.isEmpty()) && (semanticsStack.peek() instanceof Token)){ // TODO: maybe use the new pseudo code? is identifier?
            Token ID = (Token) semanticsStack.pop();

            /* if GLOBAL/LOCAL = GLOBAL (store in global memory) */
            VariableEntry var;
            if(global){
                /* insert id in global symbol table (Variable_entry) */
                var = new VariableEntry(ID.getValue(), globalMem, TYP);
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
        SymbolTableEntry entry = localTable.lookup(name.toUpperCase());
        if(entry == null){ return globalTable.lookup(name.toUpperCase()); }
        else{ return entry; }
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
     * storeArraySA21
     */
    private void storeArraySA21(String name, int lower, int upper, int address, TokenType type, int lineNumber){
        ArrayEntry arry = new ArrayEntry(name, address, type, upper, lower, true);
        if(global){ insertToGlobal(arry, lineNumber); }
        else{ insertToLocal(arry, lineNumber); }
        this.localMem++; // ??
    }

    /**
     * storeVarSA21
     */
    private void storeVarSA21(String name, TokenType type, int address, int lineNumber){
        VariableEntry var = new VariableEntry(name, address, type, true);
        if(global){ insertToGlobal(var, lineNumber);}
        else{ insertToLocal(var, lineNumber); }
        this.localMem++; // ??
    }

    /**
     * findAndPushID: used in semantic action 45
     */
    private void findAndPushID(Token token){
        SymbolTableEntry id = lookupEntry(token.getValue());
        /* Throw error if id is not in Symbol Table */
        if(id == null){ throw SemanticError.UndeclaredVariable(token.getValue(), token.getLineNum()); }
        semanticsStack.push(id);
    }

    /**
     * isConstant: checks if the given Token is a constant (used in semantic action 45)
     */
    private boolean isConstant(Token token){
        return (token.getTokenType() == TokenType.INTCONSTANT ||
                token.getTokenType() == TokenType.REALCONSTANT);
    }

    /**
     * findAndPushConst: used in semantic action 45
     */
    private void findAndPushConst(Token token){
        /* look up the constant in the constant Symbol Table */
        ConstantEntry constnt = (ConstantEntry) constTable.lookup(token.getValue().toUpperCase());

        /* if constant not found, create it and insert it */
        if(constnt == null){
            if(token.getTokenType() == TokenType.INTCONSTANT){
                constnt = new ConstantEntry(token.getValue(), TokenType.INTEGER, global);
            }
            else{ constnt = new ConstantEntry(token.getValue(), TokenType.REAL, global); }
            insertToConst(constnt);
        }
        /* push the constant */
        semanticsStack.push(constnt);
    }

    /* ------------ END OF HELPERS ------------ */


    /*  ---------------  GEN  -------------- */

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

    /**
     *
     */
    private void generate(String tviCode, String operand1, int mem){
        String[] quadrpl = {tviCode, operand1, String.valueOf(mem), null};
        quads.addQuad(quadrpl);
    }

    /**
     * generate for 33
     */
    private void generate(String tviCode, SymbolTableEntry operand, int bound, SymbolTableEntry operand2){
        String op = toAddress(tviCode, operand);
        String op2 = String.valueOf(bound);
        String op3 = toAddress(tviCode, operand2);
        String[] quadrpl = {tviCode, op, op2, op3};
        quads.addQuad(quadrpl);
    }

    /**
     * generate for 39
     */
    private void generate(String tviCode, SymbolTableEntry operand1, SymbolTableEntry operand2, String operand3){
        String op1 = toAddress(tviCode, operand1);
        String op2 = toAddress(tviCode, operand2);
        String[] quadrpl = {tviCode, op1, op2, operand3};
        quads.addQuad(quadrpl);
    }

    /**
     * generate for 5
     */
    private void generate(String tviCode, SymbolTableEntry operand){
        String op = toAddress(tviCode, operand);
        String[] quadrpl = {tviCode, op, null, null};
        quads.addQuad(quadrpl);
    }

    /*  -----------  End of GEN  ------------ */


    /* --------------  HELPERS FOR GENERATION ----------------- */

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
        if(global){
            VariableEntry $$NAME = new VariableEntry(name, -globalMem, type);
            insertToGlobal($$NAME);
            globalMem++;
            tempCount++;
            return $$NAME;
        }
        else{
            VariableEntry $$NAME = new VariableEntry(name, -localMem, type);
            insertToLocal($$NAME);
            localMem++;
            tempCount++;
            return $$NAME;
        }
    }

    /**
     * create: creates a new memory location.
     * @param name:
     * @param type:
     */
    private VariableEntry createToGlobal(String name, TokenType type) throws SymbolTableError, SemanticError{
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
     * createFunResult: creates a function result
     * @param name:
     * @param type:
     */
    private VariableEntry createFunResult(String name, TokenType type) throws SymbolTableError, SemanticError{
        VariableEntry $$FRESULT = new VariableEntry(name, -globalMem, type);
        insertToGlobal($$FRESULT);
        globalMem++;
        return $$FRESULT;
    }

    /**
     * toAddress: converts an entry to a address.
     * @param tviCode: tviCode as a String
     * @param op: operand in question
     * @return mem address as String.
     */
    private String toAddress(String tviCode, SymbolTableEntry op) {
        if (op.isProcedure() || op.isFunction()) {
            /* functions and procedures begin like: PROCBEGIN with function [name] */
            return (op.getName().toLowerCase());
        }
        if (op.isVariable()) {
            int addr = Math.abs(((VariableEntry) op).getAddress());
            return (getTag(tviCode, op) + addr);
        }
        if (op.isArray()) {
            int addr = Math.abs(((ArrayEntry) op).getAddress());
            return (getTag(tviCode, op) + addr);
        }
        /* else create constant entry. */
        if (op.isConstant()) {
            /* if hasn't already been saved? */
            SymbolTableEntry $$TEMP = create("$$TEMP" + tempCount, op.getType());

            /* move value (e.g: 1.902) into $$TEMP */
            // HERE MOVE
            generate("move", op.getName(), $$TEMP);
            int addr = Math.abs(((VariableEntry) $$TEMP).getAddress());
            return (getTag(tviCode, $$TEMP) + addr);
        }
        // todo: throw semantic error?
        return "ERROR";
    }

    /**
     * getTag: gets the address prefix, if any.
     * @param op:
     * @param tviCode:
     * @return a String representation of the address in memory.
     */
    private String getTag(String tviCode, SymbolTableEntry op){
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

            case "DIV":
                return "div";

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
                throw SemanticError.OperandStringUnknown(op.getOpType().toString());
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
        else{ throw SemanticError.UnrecognizedTypes(id1.getType(), id2.getType(), lexer.getLineNum()); }
    }

    /* -------------- END OF GENERATION HELPERS ----------------- */

    /**
     * backPatch: inserts i in the second field of the statement at position p
     * @param p: the position
     * @param i: the element being inserted
     */
    private void backPatch(int p, int i) {
        /* ::: Inserts i in the second field of the statement at position p. ::: */
        String[] quadrpl = quads.getQuad(p);
        for (int k = 0; k < quadrpl.length; k++) {
            if (quadrpl[k] != null && quadrpl[k].equals("_")) {
                quads.setEntry(p, k, Integer.toString(i));
            }
        }
    }

    /**
     * backpatch
     */
    private void backPatch(ArrayList<Integer> list, int i){
        for(Integer pointer: list){
            String[] quadrpl = quads.getQuad(pointer);
            for(int k = 0; k < quadrpl.length; k++){
                if(quadrpl[k] != null && quadrpl[k].equals("_")){
                    quads.setEntry(pointer, k, String.valueOf(i));
                }
            }
        }
    }

    /* --------------- EFALSE, ETRUE HELPERS ------------------ */

    /**
     * makeList
     */
    private ArrayList<Integer> makeList(int i){
        ArrayList<Integer> listy = new ArrayList<>();
        listy.add(i);
        return listy;
    }

    /**
     * merge
     */
    private ArrayList<Integer> merge(ArrayList<Integer> l1, ArrayList<Integer> l2){
        ArrayList<Integer> merged = new ArrayList<>(l1);
        merged.addAll(l2);
        return merged;
    }

    /* ---------------- ETYPE HELPERS ------------------- */

    /**
     * isArithmetic
     */
    private void isArithmetic(ETYPE eTYPE, int lineNumber) throws SemanticError {
        if(eTYPE != ETYPE.ARITHMETIC){ throw SemanticError.BadETYPE(lineNumber); }
    }

    /**
     * isRelational
     */
    private void isRelational(ETYPE eTYPE, int lineNumber){
        if(eTYPE != ETYPE.RELATIONAL){ throw SemanticError.InvalidRelational(lexer.getLineNum()); }
    }

    /* ------------------ SEMANTIC ACTION FEATURES ---------------------- */

    /**
     * semanticStackDump: routine to dump the semantic stack.
     */
    private void semanticStackDump() {
        if (!semanticsStack.isEmpty()) {
            System.out.println("\t\t |");
            System.out.print("\t\t ---> Semantic Stack: ");
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
            System.out.println("\n");
        }
        /* otherwise, report an empty stack */
        else {
            System.out.println("\t\t |");
            System.out.println("\t\t ---> Semantic Stack: [ ]");
            System.out.println("\n");
        }
    }

    /**
     * semanticStackDump: routine to dump the semantic stack.
     */
    private void semanticStackDump(String id) {
        System.out.println("Semantic Action: "+id);
        if (!semanticsStack.isEmpty()) {
            System.out.print("Stack: ");
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
        else {
            System.out.println("Stack: [ ]");

        }
        quads.print();
        System.out.println("\n");
    }

    /**
     * debugMode: sets the debug mode to true
     */
    public void debugMode(){
        this.debug = true;
    }

    /**
     * printQ: prints the quadruples.
     */
    public void printQ(){
        this.quads.print();
    }

} /* end of SemanticActions class */