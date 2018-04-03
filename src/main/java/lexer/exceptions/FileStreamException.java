/*
 * File: FileStreamException.java
 *
 * Desc: Used when trying to close an uninitialized reader.
 *
 */

package main.java.lexer.exceptions;

/**
 * FileStreamException
 * @author Luis Serazo
 */
public class FileStreamException extends Exception {

    private FileStreamException(String msg){ super(msg); }

    public static FileStreamException ClosingUninitializedReader(){
        return new FileStreamException("Attempting To Close An Uninitialized File Reader");
    }
}