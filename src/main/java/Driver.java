/*
 * File: Driver.java
 *
 * Desc: contains the point of execution
 *
 */
package main.java;

import main.java.parser.Parser;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Driver class for testing the parser.
 * @author Luis Serazo
 *
 */
public class Driver {

    private static final String INVALID_INPUT = "\nInvalid Input! \nThis driver must be run at the command-line as: "+
            "./Driver [file-path] [-d (optional)]" +
            "\n 1. Where file-path is the path to your source file." +
            "\n 2. Where the -d (also --debug) option indicates that you want to be in debug mode.";

    /**
     * Main: the point of execution
     *
     * @param args: user input
     */
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        Parser prs;
        if (args.length == 1) {
            prs = new Parser(args[0]);
            System.out.println("Beginning to parse code in: " + args[0]);
            System.out.println();
            prs.run();
            prs.writeToFile(getFileName(args[0]));

        } else if (args.length == 2 && (args[1].equals("-d") || args[1].equals("--debug"))) {
            prs = new Parser(args[0]);
            prs.debugMode();
            System.out.println("Beginning to parse code in: " + args[0]);
            System.out.println("Debug Mode Enabled\n");
            prs.run();
            prs.writeToFile(getFileName(args[0]));
        }
        else {
            System.out.println(INVALID_INPUT);
        }
    }

    /**
     * Used to extract file name
     */
    public static String getFileName(String path){
        String[] pathParts = path.split("/");
        String nameWithExtn = pathParts[pathParts.length - 1];
        return nameWithExtn.split("\\.")[0];
    }
}
