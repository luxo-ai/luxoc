package main.java;

import main.java.lexer.TokenType;
import main.java.lexer.Tokenizer;

public class LexDriver {

    public static void main(String[] args) {

        if(args.length == 1){
            System.out.println("Tokenizing code in: "+args[0]);

            Tokenizer tknz = new Tokenizer(args[0]);
            
            while (tknz.getPrevTokenType() != TokenType.ENDOFFILE){
                System.out.println(tknz.getNextToken());
            }

        }
        else if(args.length < 1){
            System.out.println("Too Few Arguments Given To: java LexDriver");
        }
        else{
            System.out.println("Too Many Arguments Given To: java LexDriver");
        }

    }
}
