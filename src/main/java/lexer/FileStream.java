/**
 * FILE: FileStream.java
 *
 * DESC: contains the FileStream class
 *
 * @author Luis Serazo
 *
 */
package main.java.lexer;
import main.java.lexer.errors.LexerError;
import main.java.lexer.exceptions.FileStreamException;
import java.io.*;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;


/* begin FileStream class */
public class FileStream {

    /**
     * Constants:
     */
    /* end of a line */
    private static final char EOF = (char)-1;
    private static final String PascalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
                                                   "0123456789" +
                                                   "[]{}()<>" +
                                                   "+=-*/" +
                                                   ".,;:" +
                                                   "&|" +
                                                   "\t\n ";

    /**
     * File handling structures:
     */
    /* a buffer for reading the file */
    private BufferedReader bReader = null;
    /* push back stack */
    private Stack<Character> pbStack = new Stack<>();
    /* log stuff */
    private static final Logger LOGGER = Logger.getLogger(FileStream.class.getName());
    
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
     * popStack
     */
    public void popStack(int times){
        while(times>0){
            if(pbStack.empty()){ break; }
            pbStack.pop();
            times--;
        }
    }

    /**
     * openFile: wrapper routine to open the file.
     */
    private void openFile(String filePath){
        try{
            filePath = new File(filePath).getAbsolutePath();
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
            System.out.println(e1.toString());
        }
        catch (IOException e2){
            System.out.println(e2.toString());
        }
    }

    /**
     * closeFile: wrapper routine for closing the file.
     */
    public void closeFile(){
        try{
            /* check if the reader was even used (i,e: a file was even opened) */
            if(this.bReader == null) throw FileStreamException.ClosingUninitializedReader();

            /* otherwise: reset any metadata */
            this.line = null;
            this.lineNum = 0;
            this.fileChar = EOF;
            this.charOffset = 0;
            /* close the buffered reader */
            this.bReader.close();
        }
        catch (FileStreamException ex1){
            System.out.print(ex1.toString());
          //  LOGGER.log(Level.WARNING, ex1.toString(), ex1);
        }
        catch (IOException ex2){
            System.out.println(ex2.toString());
            ex2.printStackTrace(System.out);
           // LOGGER.log(Level.WARNING, ex2.toString(), ex2);
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
            } catch (IOException ex) {
                System.out.println(ex.toString());
                ex.printStackTrace(System.out);
              //  LOGGER.log(Level.WARNING, ex.toString(), ex);
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

        /* check the push back stack first and return the character at the top of the stack */
        if(!pbStack.empty()){
            char pb = this.pbStack.pop();
            if(pb=='{'){
                skip();
                return this.fileChar;
            }
            return pb;
        }

        /* skip over whitespace */
        if(Character.isWhitespace(this.fileChar) || this.fileChar == '\t'){
            boolean test = this.fileChar == '\t';
          //  System.out.println("The char is: "+this.fileChar+test);
            skip();
            return this.fileChar;
            //return ' ';
        }

        if(this.fileChar == '{'){
            skip();
            return this.fileChar;
        }
        /*
         * otherwise
         */
        char old = this.fileChar;
        char newChar = mvFilePointer();
        this.fileChar = newChar;

        // we must ensure that the new character is valid
        if(newChar != EOF && !PascalChars.contains(""+newChar)){
            throw LexerError.InvalidCharacter(lineNum, newChar);
        }

       if(this.fileChar == '{') {
           pushBack(this.fileChar);
           return ' ';
       }
       return this.fileChar;
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
    private char jumpComment() throws LexerError{
        /* save the start of the comment */
        int commentStart = this.lineNum;

        /* grab the next character */

        char runner = mvFilePointer();

        try {
            /* skip over comments */
            /* only ever breaking out of this loop if EOF (error) or } or { (error) */
            while (runner != EOF) {
                /* keep skipping until we get to a closed bracket */
                if (runner == '}'){
                    runner = mvFilePointer();
                    if(runner == '}'){ throw LexerError.IllegalComment(lineNum, "{ ... }}"); }
                    break;
                }

                if (runner == '{'){ throw LexerError.IllegalComment(lineNum, "{ ... { ... }"); }

                runner = mvFilePointer();
            }
            /* if the runner is EOF comment never ended */
            if (runner == EOF) throw LexerError.UnclosedComment(lineNum);
            
        } catch (LexerError error) {
           // LOGGER.log(Level.SEVERE, error.toString(), error);
            throw error;
        }
         return runner;
    }
} /* end of FileStream class */
