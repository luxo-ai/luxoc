package main.java;

import main.java.lexer.Tokenizer;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {
        /* String for reading in user input */
      //  String in;
        /* able to read from the Sys.in */
        Scanner reader = new Scanner(System.in);
        /* first thing the user inputs */
     //   in = reader.nextLine();

        /* Tokenizer for testing purposes (use user input) */
        Tokenizer tkzr = new Tokenizer("examples/comments.pas");

        /* always ask for input until Sys exception */
        try {
            while (true) {
                /* who cares what this is */
                reader.nextLine();
                /* print the token */
                System.out.println(tkzr.getNextToken());
            }
        } catch (Exception ex) {
            /* close the reader */
            reader.close();
            System.out.println("Reader Closed");
        }

       // System.out.println(tkzr.getNextToken());
     //  System.out.println(tkzr.getNextToken());


    }
}

