/*
 * File: TableDriver.java
 *
 * Desc: used to test the symbol tables
 *
 */
package main.java;

import main.java.table.*;
import main.java.token.TokenType;
import java.util.LinkedList;

/**
 * TableDriver class
 * @author Luis Serazo
 */
public class TableDriver {

    /**
     * main: the point of execution.
     */
    public static void main(String[] args) {
        if (args.length == 1 && args[0].matches("-?\\d+") || args.length == 0) {
            /* Symbol Table To Test */
            SymbolTable symbolTable;
            if(args.length == 0) {
                System.out.println("Creating Symbol Table of initial size: 250 ....");
                symbolTable = new SymbolTable(250);
            }
            else{
                System.out.println("Creating Symbol Table of initial size: " + args[0] + " ....");
                symbolTable = new SymbolTable(Integer.parseInt(args[0]));
            }
            System.out.println();
            System.out.println("Symbol Table contains: " + symbolTable.size() + " elements");

            /*
             * Table Entries for Testing:
             *
             * - arrayEn: an array entry.
             * - constEn: a constant entry.
             * - funcEn:  a function entry.
             * - funcRes: the result of the function entry.
             * - ioEn:    an io device entry.
             * - procEn:  a procedure entry.
             * - varEn:   a variable entry.
             */
            ArrayEntry arrayEn;
            ConstantEntry constEn;
            FunctionEntry funcEn;
            VariableEntry funcRes;
            IODeviceEntry ioEn;
            ProcedureEntry procEn;
            VariableEntry varEn;

            /* Initialize the entries -  */
            arrayEn = new ArrayEntry("arry", 24, TokenType.REAL, 100, 0);
            constEn = new ConstantEntry("1.0901", TokenType.REAL);
            funcRes = new VariableEntry("result", 123, TokenType.REALCONSTANT, false, true);
            funcEn = new FunctionEntry("func", 2, new LinkedList<ParameterInfo>(), funcRes);
            ioEn = new IODeviceEntry("write");
            procEn = new ProcedureEntry("foo", 0, new LinkedList<ParameterInfo>());
            varEn = new VariableEntry("bar", 45, TokenType.INTCONSTANT);

            /* install builtins */
            System.out.println();
            System.out.println();
            System.out.println("Installing Builtins ... ");
            SymbolTable.installBuiltins(symbolTable);
            System.out.println();
            System.out.println(" ===> The symbol table now has: " + symbolTable.size() + " elements");

            /* insert the test entries */
            System.out.println();
            System.out.println();
            System.out.println("Inserting ...  ");
            System.out.println("+ Array     - NAME: " + arrayEn.getName() + "   ADDRESS: " + arrayEn.getAddress() + "   TYPE: " + arrayEn.getType() + "   UB: " + arrayEn.getUpperBound() + "   LB: " + arrayEn.getLowerBound());
            System.out.println("+ Constant  - NAME: " + constEn.getName() + "   TYPE: " + constEn.getType());
            System.out.println("+ Function  - NAME: " + funcEn.getName() + "   NUMBER-OF-PARAMETERS: " + funcEn.getNumOfParam() + "   RETURN TYPE: " + funcEn.getResult().getType());
            System.out.println("+ Procedure - NAME: " + procEn.getName() + "    NUMBER-OF-PARAMETERS: " + procEn.getNumOfParam());
            System.out.println("+ Variable  - NAME: " + varEn.getName() + "   ADDRESS: " + varEn.getAddress() + "   TYPE: " + varEn.getType());

            symbolTable.insert(arrayEn);
            symbolTable.insert(constEn);
            symbolTable.insert(funcEn);
            symbolTable.insert(procEn);
            symbolTable.insert(varEn);

            System.out.println();
            System.out.println(" ===> The symbol table now has: " + symbolTable.size() + " elements");


            /* look up the entries */
            System.out.println();
            System.out.println();
            System.out.println("We now look up the entries in reverse order. ");
            System.out.println("Looking up ... ");
            System.out.println("+ Variable  (bar):    " + sucessful("bar", varEn, symbolTable));
            System.out.println("+ Procedure (foo):    " + sucessful("foo", procEn, symbolTable));
            System.out.println("+ Function  (func):   " + sucessful("func", funcEn, symbolTable));
            System.out.println("+ Constant  (1.0901): " + sucessful("1.0901", constEn, symbolTable));
            System.out.println("+ Array     (arry):   " + sucessful("arry", arrayEn, symbolTable));



            /* ERRORS */
            System.out.println();
            System.out.println();
            System.out.println("NOTE: ");
            System.out.println("-----------------------------------------------------------");
            System.out.println("Attempting to insert a duplicate will throw an error. \nUncomment the last lines of the diver's " +
                    "main method to for an \nexample of a duplicate insertion. ");


            /* Duplicate Insertion */
            // symbolTable.insert(ioEn);

            /* Another Duplicate Insertion */
            VariableEntry varWithSameName = new VariableEntry("bar", 80, TokenType.REALCONSTANT);
            // symbolTable.insert(varWithSameName);

            /* Attempt Inserting The Same Thing */
            // symbolTable.insert(funcEn);

        } else {
            System.out.println(DRIVER_MSG);
        }
    }

    private static String sucessful(String name, SymbolTableEntry entry, SymbolTable table) {
        String sucessStr = "Unsuccessful";
        SymbolTableEntry realEntry = table.lookup(name);
        if (realEntry != null && realEntry.equals(entry)) {
            sucessStr = "Success";
        }
        return sucessStr;
    }


    private static String DRIVER_MSG = "\nInvalid Input! \nThis driver must be run at the command-line as: " +
            "./TableDriver [initial-table-size] (optional)" +
            "\n** initial-table-size is the initial size of the Symbol Table. This parameter is optional and is " +
            "set to 250 by default. ";
}

