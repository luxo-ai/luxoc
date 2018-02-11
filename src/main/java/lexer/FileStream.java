/**
 * FILE: FileStream.java
 *
 * DESC: contains the FileStream class
 *
 * @author Luis Serazo
 *
 */
package main.java.lexer;
import main.java.lexer.errors.FileStructureError;
import main.java.lexer.exceptions.ReaderUninitializedException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;


/* begin FileStream class */
public class FileStream {

    /**
     * Constants:
     */
    /* end of a line */
    private static final char EOF = (char)-1;
    
    /**
     * File handling structures:
     */
    /* a buffer for reading the file */
    private BufferedReader bReader = null;
    /* push back stack */
    private Stack<Character> pbStack = new Stack<>();
    
    /**
     * File metadata.
     */
    /* which line is being read */
    private int lineNum;
    /* the line being currently read */
    private String line;
    /* the current character offset in the line */
    private int charOffset;
    /* the current character being read in the FILE */
    private char fileChar;

    /**
     * additional metadata.
     */
    /* the file path */
    private String filePath;
    
    
    /**
     * FileStream constructor
     * @param filePath the path to the file
     */
    public FileStream(String filePath){
        this.filePath = filePath;
        openFile(filePath);
    }

    /**
     * getReader: getter method for the BufferedReader
     * @return this.bReader
     */
    public BufferedReader getReader() {
        return this.bReader;
    }

    /**
     * getLineNum: getter method for the current line number of the file
     * @return this.lineNum
     */
    public int getLineNum() {
        return this.lineNum;
    }

    /**
     * getFileChar: getter method for the current character of the file
     * @return this.fileChar
     */
    public char getFileChar() {
        return this.fileChar;
    }

    /**
     * getLine: getter method for the current line of the file
     */
    public String getLine() {
        return this.line;
    }

    /**
     * getFilePath: getter method for the file path
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * pushBack: wraps around stack push (simply makes things more readable)
     */
    public void pushBack(char chr){
        pbStack.push(chr);
    }

    /**
     * openFile: wrapper routine to open the file.
     */
    private void openFile(String filePath){
        try{
            /* initialize the buffered reader */
            this.bReader = new BufferedReader(new FileReader(filePath));

            /* otherwise continue as usual */
            this.line = bReader.readLine();
            this.lineNum = 1;
            /* use get char to set the offset and set the current character */
            this.fileChar = mvFilePointer();
            skip();
        }
        /* handle exceptions */
        catch (FileNotFoundException e1){
            System.out.print(e1);
        }
        catch (IOException e2){
            System.out.println(e2);
        }
    }

    

    /**
     * isEOL: end of line?
     */
    private boolean isEOL(){ return this.charOffset >= this.line.length(); }


    /**
     * mvFilePointer:
     */
    private char mvFilePointer() {

        if (this.line == null) {
            return EOF;
        }

        /* need to move to the next line */
        if (isEOL()) {
            this.charOffset = 0;
            this.lineNum++;

            /* make sure we can move to the next line */
            try {
                this.line = bReader.readLine();
            } catch (IOException e1) {
                System.out.println(e1);
            }
            return ' ';
        }

        char nxtChar = this.line.charAt(this.charOffset);

        this.charOffset++;


        return nxtChar;
    }


    /**
     * nextChar: grab the next character.
     */
    public char nextChar(){

        String pascalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
                             "0123456789" +
                             "[]{}()<>" +
                             "+=-*/" +
                             ".,;:" +
                             "&|" +
                             "\t\n ";

        /* check the push back stack first and return the character at the top of the stack */
        if(!pbStack.empty()){ return this.pbStack.pop(); }

        /* skip over whitespace */
        if(Character.isWhitespace(this.fileChar) && this.fileChar != '\n'){
            skip();
            return this.fileChar;
        }

        if(this.fileChar == '{'){
            skip();
            return this.fileChar;
        }

        /*
         * otherwise: we move the file pointer once, set the fileChar, and return the character.
         */
        char newChar = mvFilePointer();

        // we must ensure that the new character is valid
        if(newChar != EOF && !pascalChars.contains(""+newChar)){ throw new FileStructureError();}

        this.fileChar = newChar;

        return newChar;

    }

    /**
     * skip: sub-routine for skipping white space.
     */
    private void skip(){
        char runner = this.fileChar;
        /* skip if the current character is the beginning of a comment or whitespace */
        while(runner == '{' || Character.isWhitespace(runner)){
            /* if the character is the start of a comment, skip it */
            if (runner == '{'){
                runner = jumpComment();
            }
            /* otherwise mv the br pointer */
            else {
                runner =  mvFilePointer();
            }
        }

        this.fileChar = runner;
    }

    /**
     * jumpComment: sub-routine for jumping over comments.
     */
    private char jumpComment() {

        /* grab the next character */

        char runner = mvFilePointer();

        try {
            /* skip over comments */
            while (runner != EOF) {
                /* keep skipping until we get to a closed bracket */
                if (runner == '}') break;

                if (runner == '{') throw new FileStructureError();
                runner = mvFilePointer();
            }
            /* if the runner is EOF comment never ended */
            if (runner == EOF) throw new FileStructureError();
            
        } catch (FileStructureError e1) {
            System.out.println("Comment Not Formatted Correctly" + e1);
            e1.printStackTrace(System.out);
        }
         return mvFilePointer();
    }

} /* end of FileStream class */
