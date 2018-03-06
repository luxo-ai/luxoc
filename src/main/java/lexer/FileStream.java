/*
 * File: FileStream.java
 *
 * Desc: contains the FileStream class.
 *
 */
package main.java.lexer;

import main.java.lexer.errors.LexerError;
import main.java.lexer.exceptions.FileStreamException;
import java.io.*;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * FileStream class
 * @author Luis Serazo
 *
 */
public class FileStream {

    /*
     * Constants:
     * - COMNT_START: the character that precedes a comment.
     * - COMNT_END: the character that ends a comment.
     * - EOF: the character found at the end of a file.
     * - SPACE: whitespace.
     * - PASCAL: the characters accepted by the language.
     */
    private static final char COMNT_START = '{';
    private static final char COMNT_END = '}';
    private static final char EOF = (char)-1;
    private static final char SPACE = ' ';
    private static final String PASCAL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
            "0123456789" +
            "[]{()<>" +
            "+=-*/" +
            ".,;:" +
            "&|" +
            "\t\n ";

    /*
     * File handling structures:
     * - file: an abstract representation of the file
     * - bReader: a BufferedReader for reading the file
     * - pbStack: push back stack
     * - logger: structure to log problems
     */
    private File file;
    private BufferedReader bReader = null;
    private Stack<Character> pbStack = new Stack<>();
    private static final Logger LOGGER = Logger.getLogger(FileStream.class.getName());

    /*
     * FileStream metadata:
     * - lineNum: the current line number being analyzed
     * - line: the actual line being analyzed
     * - charOffset: the character offset for the character being obtained
     * - fileChar: the current character in the file being analyzed
     * - filePath: the path to the file
     */
    private int lineNum;
    private String line;
    private int charOffset;
    private char fileChar;
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
    public BufferedReader getBReader() {
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
     * @return the current line being analyzed
     */
    public String getLine() {
        return this.line;
    }

    /**
     * getFilePath: getter method for the file path
     * @return the path to the file
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * pushBack: wraps around stack push (simply makes things more readable)
     * @param chr: a file character.
     */
    public void pushBack(char chr){
        pbStack.push(chr);
    }

    /**
     * popStack: pops  the stack n times
     * @param n: the number of times the stack will be popped.
     */
    public void popStack(int n){
        while(n>0 && !pbStack.isEmpty()){
            pbStack.pop();
            n--;
        }
    }

    /**
     * openFile: wrapper routine to open the file.
     */
    private void openFile(String filePath){
        try{
            /* set abstract File representation */
            this.file = new File(filePath);
            /* initialize the buffered reader */
            this.bReader = new BufferedReader(new FileReader(this.file.getAbsolutePath()));

            /* initialize the line and other metadata */
            this.line = bReader.readLine();
            this.lineNum = 1;
            /* use get char to set the offset and set the current character */
            this.fileChar = mvFilePointer();
            skip();
        }
        /* handle exceptions */
        catch (IOException | LexerError e){ /* subclass of FileNotFoundException */
            System.out.println(e.toString());
            e.printStackTrace(System.out);
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
            this.bReader = null;
        }
        /* handle exceptions */
        catch (FileStreamException | IOException e){
            System.out.print(e.toString());
            e.printStackTrace(System.out);
            //  LOGGER.log(Level.WARNING, ex1.toString(), ex1);
        }
    }


    /**
     * isEOL: end of line?
     * @return True if we've reached the end of the current line, False otherwise.
     */
    private boolean isEOL(){ return this.charOffset >= this.line.length(); }

    /**
     * isEOF: end of file?
     * @return True if the file char is EOF, False otherwise.
     */
    private boolean isEOF(){ return this.fileChar == EOF; }


    /**
     * mvFilePointer: method for obtaining the next character in the file.
     * @return the next char in the file
     */
    private char mvFilePointer() {
        /* check if the current line is EOF */
        if (this.line == null) { return EOF; }

        /* need to move to the next line */
        if (isEOL()) {
            this.charOffset = 0;
            this.lineNum++;
            /* make sure we can move to the next line */
            try {
                this.line = bReader.readLine();
                /* catch and exceptions */
            } catch (IOException ex) {
                System.out.println(ex.toString());
                ex.printStackTrace(System.out);
                //  LOGGER.log(Level.WARNING, ex.toString(), ex);
            }
            return SPACE;
        }
        /* otherwise: */
        char nxtChar = this.line.charAt(this.charOffset);
        this.charOffset++;
        /* return the next character */
        return nxtChar;
    }


    /**
     * nextChar: grab the next character.
     * @return the next character (from the file or the push back stack)
     */
    public char nextChar() {
        /* check the push back stack first and return the character at the top of the stack */
        if (!pbStack.empty()) {
            return this.pbStack.pop();
        }

        /* skip over whitespace */
        if (Character.isWhitespace(this.fileChar) || this.fileChar == '\t') {
            skip();
            return SPACE;
        }

        if (this.fileChar == COMNT_START) {
            skip();
            return SPACE;
        }
        /*
         * otherwise
         */
        char old = this.fileChar;
        int preLine = this.lineNum;
        this.fileChar = mvFilePointer();


        if (old != EOF && !PASCAL.contains("" + old)) {
            throw LexerError.InvalidCharacter(preLine, old);
        }

        return old;
    }



    /**
     * skip: sub-routine for skipping white space.
     */
    private void skip(){
        /* copy the current file character */
        char runner = this.fileChar;

        /* skip if the current character is the beginning of a comment or whitespace */
        while(runner == COMNT_START || Character.isWhitespace(runner)){
            /* if the character is the start of a comment, skip it */
            if (runner == COMNT_START){
                runner = jumpComment();
            }
            /* otherwise mv the file pointer */
            else {
                runner =  mvFilePointer();
            }
        }
        /* set the file character */
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
            while (runner != EOF) {
                /* keep skipping until we get to a closed bracket */
                if (runner == COMNT_END){
                    runner = mvFilePointer();
                    if(runner == COMNT_END){
                        throw LexerError.IllegalComment(commentStart, "{ ... }}");
                    }
                    break;
                }
                if (runner == COMNT_START){
                    throw LexerError.IllegalComment(commentStart, "{ ... { ... }");
                }
                /* continue to iterate through the file */
                runner = mvFilePointer();
            }
            /* if the runner is EOF comment never ended */
            if (runner == EOF){ throw LexerError.UnclosedComment(commentStart); }

        } catch (LexerError error) {
            // LOGGER.log(Level.SEVERE, error.toString(), error);
            throw error;
        }
        return runner;
    }
} /* end of FileStream class */