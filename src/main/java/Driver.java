/*
 * File: Driver.java
 *
 * Desc: contains the point of execution
 *
 */
package main.java;

import main.java.parser.Parser;
import java.io.FileNotFoundException;

/**
 * Driver class
 * @author Luis Serazo
 *
 */
public class Driver {

    /**
     * Main: the point of execution
     *
     * @param args: user input
     */
    public static void main(String[] args) throws FileNotFoundException {
        Parser prs;
        if (args.length == 1) {
            prs = new Parser(args[0]);
            System.out.println("Beginning to parse code in: " + args[0]);
            System.out.println();
            prs.run();
        } else if (args.length == 2) {
            prs = new Parser(args[0], args[1]);
            System.out.println("Beginning to parse code in: " + args[0]);
            System.out.println("With a parse table in: "+args[1]);
            System.out.println();
            prs.run();
        } else {
            System.out.println("Driver must take only one or two arguments");
        }
    }
}