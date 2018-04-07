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
    private static ArrayEntry arrayEn = new ArrayEntry("arry", 24, TokenType.REAL, 100, 0);
    private static ConstantEntry constEn = new ConstantEntry("1.0901", TokenType.REAL);
    private static VariableEntry funcRes = new VariableEntry("result", 123, TokenType.REALCONSTANT, false, true);
    private static FunctionEntry funcEn = new FunctionEntry("func", 2, new LinkedList<ParameterInfo>(), funcRes);
    private static IODeviceEntry ioEn = new IODeviceEntry("write");
    private static ProcedureEntry procEn = new ProcedureEntry("foo", 0, new LinkedList<ParameterInfo>());
    private static VariableEntry varEn = new VariableEntry("bar", 45, TokenType.INTCONSTANT);


    private static String DRIVER_MSG = "\nInvalid Input! \nThis driver must be run at the command-line as: " +
            "./TableDriver [initial-table-size] (optional)  [error-code] (optional)" +
            "\n 1. 'initial-table-size' is the initial size of the Symbol Table. This parameter is optional and is set to 250 by default. " +
            "\n 2. 'error' is an optional parameter. The accepted error codes are: '-1', '-2', and '-3'."+
            "\n\t * Using '-1' attempts to insert a 'WRITE' IO that was already installed using the 'installBuiltins' routine." +
            "\n\t * Using '-2' attempts to insert a variable that was already inserted in the Symbol Table." +
            "\n\t * Using '-3' attempts to insert a *new* variable with the same name as one already in the Symbol Table.";


    /**
     * main: the point of execution.
     */
    public static void main(String[] args) {
        /* Symbol Table To Test */
        SymbolTable symbolTable;

        if (args.length > 0) {
            if (args.length == 1) {
                if (args[0].matches("\\d+")) {
                    System.out.println("Creating Symbol Table of initial size: " + args[0] + " ...");
                    symbolTable = new SymbolTable(Integer.parseInt(args[0]));
                    bp(symbolTable);
                }
                else if(args[0].equals("-1") || args[0].equals("-2") || args[0].equals("-3")){
                    System.out.println("Creating Symbol Table of initial size: 250 ...");
                    symbolTable = new SymbolTable(250);
                    bp(symbolTable);

                    switch(args[0]){
                        case "-1":
                            symbolTable.insert(ioEn);
                            break;

                        case "-2":
                            VariableEntry varWithSameName = new VariableEntry("bar", 80, TokenType.REALCONSTANT);
                            symbolTable.insert(varWithSameName);
                            break;

                        case "-3":
                            symbolTable.insert(funcEn);
                            break;
                    }
                }
                else {
                    System.out.println(DRIVER_MSG);
                }
            }
            else if (args.length == 2) {
                if (args[0].matches("\\d+") && (args[1].equals("-1") || args[1].equals("-2") || args[1].equals("-3"))){
                    System.out.println("Creating Symbol Table of initial size: " + args[0] + " ...");
                    symbolTable = new SymbolTable(Integer.parseInt(args[0]));
                    bp(symbolTable);

                    switch(args[1]){
                        case "-1":
                            symbolTable.insert(ioEn);
                            break;

                        case "-2":
                            symbolTable.insert(funcEn);
                            break;

                        case "-3":
                            VariableEntry varWithSameName = new VariableEntry("bar", 80, TokenType.REALCONSTANT);
                            symbolTable.insert(varWithSameName);
                            break;
                    }
                }
                else { System.out.println(DRIVER_MSG); }
            }
            else { System.out.println(DRIVER_MSG); }
        }
        else {
            System.out.println("Creating Symbol Table of initial size: 250 ...");
            symbolTable = new SymbolTable(250);
            bp(symbolTable);
        }
    }

    private static void bp(SymbolTable symbolTable) {

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
        System.out.println("+ Array     - NAME: " + arrayEn.getName() + "    ADDRESS: " + arrayEn.getAddress() + "   TYPE: " + arrayEn.getType() + "   UB: " + arrayEn.getUpperBound() + "   LB: " + arrayEn.getLowerBound());
        System.out.println("+ Constant  - NAME: " + constEn.getName() + "  TYPE: " + constEn.getType());
        System.out.println("+ Function  - NAME: " + funcEn.getName() + "    NUMBER-OF-PARAMETERS: " + funcEn.getNumOfParam() + "   RETURN TYPE: " + funcEn.getResult().getType());
        System.out.println("+ Procedure - NAME: " + procEn.getName() + "     NUMBER-OF-PARAMETERS: " + procEn.getNumOfParam());
        System.out.println("+ Variable  - NAME: " + varEn.getName() + "     ADDRESS: " + varEn.getAddress() + "   TYPE: " + varEn.getType());

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


        System.out.println();
        System.out.println();
        symbolTable.dumpTable();


        /* ERRORS */
        System.out.println();
        System.out.println();
        System.out.println("NOTE: ");
        System.out.println("-----------------------------------------------------------");
        System.out.println("Attempting to insert a duplicate will throw an error. \n" +
                "To throw an error, you may use, the optional 'error' parameter. " +
                "\nThe accepted error codes are: 1, 2, and 3. " +
                "\n  * Using '-1' attempts to insert a 'WRITE' IO that was already installed using the 'installBuiltins' routine." +
                "\n  * Using '-2' attempts to insert a variable that was already inserted in the Symbol Table." +
                "\n  * Using '-3' attempts to insert a *new* variable with the same name as one already in the Symbol Table.\n");
    }


    private static String sucessful(String name, SymbolTableEntry entry, SymbolTable table) {
        String sucessStr = "Unsuccessful";
        SymbolTableEntry realEntry = table.lookup(name);
        if (realEntry != null && realEntry.equals(entry)) {
            sucessStr = "Success! Stored Value ==> "+realEntry.toString();
        }
        return sucessStr;
    }

}

