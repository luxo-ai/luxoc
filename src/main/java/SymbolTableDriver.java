/*
 * File: SymbolTableDriver.java
 *
 * Desc: used to test the symbol tables
 *
 */
package main.java;

import main.java.table.*;
import main.java.token.Token;
import main.java.token.TokenType;

import java.util.LinkedList;

/**
 * SymbolTableDriver class
 * @author Luis Serazo
 */
public class SymbolTableDriver {

    /* point of execution */
    public static void main(String[] args){
        /* objects for testing */
        SymbolTable symbolTable = new SymbolTable(1000);
        ArrayEntry array;
        ConstantEntry constant;
        FunctionEntry func;
        IODeviceEntry io;
        ProcedureEntry proc;
        VariableEntry var;

        /* initialize */
        array = new ArrayEntry("myArray", 100, TokenType.REAL, 100, 0);
        constant = new ConstantEntry("1.090", TokenType.REAL);
        func = new FunctionEntry("foo", 2, new LinkedList<ParameterInfo>(), new VariableEntry("res", 123, TokenType.REALCONSTANT));
       // io = new IODeviceEntry("WRITe");
        proc = new ProcedureEntry("pro", 0, new LinkedList<ParameterInfo>());
        var = new VariableEntry("x", 456, TokenType.INTCONSTANT);

        System.out.println("Created Symbol Table of length: 100 and a current size of: "+symbolTable.size());
        System.out.println("Inserting 6 different entries ... ");

        symbolTable.insert(array);
        symbolTable.insert(constant);
        symbolTable.insert(func);
      // symbolTable.insert(io);
        symbolTable.insert(proc);
        symbolTable.insert(var);

        System.out.println("After inserting, the size size of the table is: "+symbolTable.size());

        System.out.println("---------------------------------------------------");
        System.out.println("Lookup the array entry: ");
        SymbolTable.installBuiltins(symbolTable);
        System.out.println(symbolTable.lookup("WRITe").isReserved());

        int[] arry = new int[12];

        for(int i = 0; i < arry.length; i++){
            arry[i] = i;
        }

        for(int i : arry){
            System.out.println(i);
        }

        //test(new Token(TokenType.IDENTIFIER, ""));

    }

   // public static void test(Token tok){ }
}