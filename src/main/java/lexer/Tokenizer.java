/**
 * FILE: Tokenizer
 *
 * DESC: contains the tokenizer class.
 *
 * @author Luis Serazo
 *
 */
package main.java.lexer;
import java.io.IOException;
import java.util.HashMap;

public class Tokenizer {

    private FileStream fStream;
    private HashMap symbolTable;


    public Tokenizer(String filename) throws IOException {

        this.fStream = new FileStream(filename);
        this.symbolTable = new HashMap();

    }

    /**
     * test if numeric value
     */
    private boolean isNumeric(char chr) {
        if (chr >= '0' && chr <= '9') return true;
        return false;
    }

    /**
     * test if alphabetical
     */
    public boolean isLetter(char chr) {
        if ((chr >= 'a' && chr <= 'z' || chr >= 'A' && chr <= 'Z')) return true;
        return false;
    }

    /**
     *
     */
    public Token getNextToken() {
        String buffer = "";

        return null;
    }
}


