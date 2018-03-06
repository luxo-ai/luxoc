/*
 * File: LexDriver.java
 *
 * Desc: contains the point of execution
 *       for lexer testing.
 *
 */
package main.java.lexer;
import main.java.token.TokenType;

/**
 * LexDriver class used for testing the lexer
 * @author Luis Serazo
 */
public class LexDriver {

    public static void main(String[] args) {

        if(args.length == 1){
            System.out.println("Tokenizing code in: "+args[0]);

            Tokenizer tknz = new Tokenizer(args[0]);

            do {
                System.out.println(tknz.getNextToken());
            } while(tknz.prevTokenT() != TokenType.ENDOFFILE);

        }
        else if(args.length < 1){
            System.out.println("Too Few Arguments Given To: java LexDriver");
        }
        else{
            System.out.println("Too Many Arguments Given To: java LexDriver");
        }

    }
}