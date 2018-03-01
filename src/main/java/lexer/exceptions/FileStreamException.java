/**
 * File: FileStreamException.java
 *
 * Desc: For file exceptions
 *
 * @author Luis Serazo
 *
 */

package main.java.lexer.exceptions;

public class FileStreamException extends Exception {

    public FileStreamException(String msg){ super(msg); }

    public static FileStreamException ClosingUninitializedReader(){
        return new FileStreamException("Attempting To Close An Uninitialized File Reader");
    }
}
