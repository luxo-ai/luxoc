package main.java;

import main.java.lexer.FileStream;
import main.java.lexer.Token;
import main.java.lexer.TokenType;

public class LexApp {

    public static void main(String[] args){

        FileStream fs = new FileStream("/Users/luxo/Desktop/luxoc/test_files/simple.pas");

        //   System.out.println(fs.getLine());  // x = 3

        System.out.println(fs.getFileChar()); // x

        System.out.println(fs.nextChar()); // ' '

        // System.out.println(fs.getFileChar()+"a1"); // ' '

        System.out.println(fs.nextChar()); // =

        // System.out.println(fs.getFileChar()); // =

        System.out.println(fs.nextChar()); // ' '

        //  System.out.println(fs.getFileChar()); // ' '

        System.out.println(fs.nextChar()); // 3

        //  System.out.println(fs.getFileChar()); // 3

        System.out.println(fs.nextChar()); // ' '

        //  System.out.println(fs.getFileChar()); // ' '

        System.out.println(fs.nextChar()); // 'i'

        // System.out.println(fs.getFileChar()); // 'i'

        System.out.println(fs.nextChar()); // 'f'

        //      System.out.println(fs.getFileChar()+"i"); // 'f'

        System.out.println(fs.nextChar()); // 'f'

        System.out.println(fs.nextChar()); // 'f'

        System.out.println(fs.nextChar()); // 'f'


        System.out.println(fs.nextChar()); // 'f'

        System.out.println(fs.nextChar()); // 'f'

        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'
        System.out.println(fs.nextChar()); // 'f'


        Token tk = new Token(TokenType.ADDOP, null);


        System.out.println(tk.toString());


    }
}
