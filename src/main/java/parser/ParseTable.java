/*
 * File: ParseTable.java
 * Desc: Contains the ParseTable wrapper class.
 *
 */
package main.java.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * ParseTable: wrapper class for parsetable.dat data
 * @author Luis Serazo
 */
public class ParseTable {

    /* default path to the parse table data */
    private static final String DEFAULT_PATH = "src/main/resources/parse_table.dat";

    /* save the abstract file from the pathname */
    private File parseData;

    /* save the data to a 2-dim array */
    private int[][] parseMatrix;
    private static final int ROWS = 35;
    private static final int COLS = 38;

    /**
     * ParseTable constructor
     * @throws FileNotFoundException if we cannot locate the file.
     */
    ParseTable() throws FileNotFoundException{
        parseMatrix = new int[ROWS][COLS];
        loadMatrix();
    }

    /**
     * ParseTable constructor
     * @param filePath: location of a different parse file
     * @throws FileNotFoundException if we cannot locate the file.
     */
    ParseTable(String filePath) throws FileNotFoundException{
        parseMatrix = new int[ROWS][COLS];
        loadMatrix(filePath);
    }

    /**
     * loadMatrix: routine for saving the data in parsetable.dat to the table matrix.
     * @throws FileNotFoundException if file unable to be found
     */
    private void loadMatrix() throws FileNotFoundException{
        parseData = new File(DEFAULT_PATH);
        Scanner scanner = new Scanner(parseData.getAbsoluteFile());

        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++){
                parseMatrix[row][col] = scanner.nextInt();
            }
        }
        scanner.close();
    }

    /**
     * loadMatrix: routine for saving the data in parsetable.dat to the table matrix.
     * @throws FileNotFoundException if file unable to be found
     */
    private void loadMatrix(String filePath) throws FileNotFoundException{
        parseData = new File(filePath);
        Scanner scanner = new Scanner(parseData.getAbsoluteFile());

        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++){
                parseMatrix[row][col] = scanner.nextInt();
            }
        }
        scanner.close();
    }

    /**
     * toString: toString routine for the matrix table.
     * @return the parseMatrix as a String.
     */
    @Override
    public String toString(){
        String parseStr = "";
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++){
                parseStr += parseMatrix[row][col]+" ";
            }
            parseStr += "\n";
        }
        return parseStr;
    }

    /**
     * getRule: returns a rule from the parseMatrix
     * @param row: row in matrix
     * @param col: col in matrix
     * @return the entry in the parseMatrix
     */
    public int getRule(int row, int col){ return parseMatrix[row][col]; }

    /**
     * getFile: getter method for the abstract file
     * @return a copy of the abstract file, parseData
     */
    public File getFile(){ return parseData; }

} /* end of ParseTable class */