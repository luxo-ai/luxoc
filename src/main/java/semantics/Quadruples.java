/*
 * File: Quadruple.java
 *
 * Desc: stores the intermediate code as it is generated.
 *
 */
package main.java.semantics;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Quadruples class
 * @author Luis Serazo
 */
public class Quadruples {

    /* Quadruples have a vector of string arrays */
    private Vector<String[]> quadruples;
    /* Keep track of the next quadruple */
    private int nextQuad;

    /**
     * Quadruples constructor
     * Initialize the quadruple vector and use dummy
     * variables as padding for the first line.
     * Package-private.
     */
    Quadruples(){
        quadruples = new Vector<>();
        /* set up dummy quadruple for padding line 0 */
        String[] dummyQuadruple = new String[4];
        dummyQuadruple[0] = dummyQuadruple[1] = dummyQuadruple[2] = dummyQuadruple[3] = null;
        /* add dummy to the quadruple */
        quadruples.add(nextQuad, dummyQuadruple);
        /* Start on line 1 */
        nextQuad = 1;
    }

    /**
     * getEntry: getter method for quadruple entry
     * @param line: line of the quadruple
     * @param offset: offset into TVI line.
     * @return the entry in the quadruple
     */
    public String getEntry(int line, int offset){ return quadruples.elementAt(line)[offset]; }

    /**
     * setEntry: setter method for quadruple entry
     * @param line: line of the quadruple
     * @param offset: offset into quadruple line
     * @param entry: the entry to replace the current entry.
     */
    public void setEntry(int line, int offset, String entry){ quadruples.elementAt(line)[offset] = entry; }

    /**
     * nextQuadIndex: gives the next quadruple index.
     * @return the index of the next quadruple.
     * Package-private
     */
    int nextQuadIndex(){ return nextQuad; }

    /**
     * nextQuad: increment the next quadruple index.
     * Package-private
     */
    void nextQuad(){ this.nextQuad++; }

    /**
     * getQuad: retrieves a quadruple on a specific line.
     * @param line: the line where the quadruple is located.
     * @return the quadruple at location, line.
     */
    public String[] getQuad(int line){ return quadruples.elementAt(line); }

    /**
     * addQuad: adds a quadruple to the collection of quadruples.
     * @param quad: the quadruple being added.
     * Package-private
     */
    void addQuad(String[] quad){
        quadruples.add(nextQuad, quad);
        /* increment to the next quadruple */
        this.nextQuad++;
    }

    /**
     * print: routine for printing the quadruple.
     */
    public void print(){
        int lineNumber = 1;
        String separator;
        System.out.println("::: TVI CODE ::: ");
        Enumeration<String[]> en = this.quadruples.elements();
        /* skip line one */
        en.nextElement();
        while(en.hasMoreElements()){
            String[] quad = en.nextElement();
            separator = " ";
            System.out.print(lineNumber + ": " + quad[0]);
            if(quad[1] != null){ System.out.print(separator + quad[1]); }
            if(quad[2] != null){
                separator = ", ";
                System.out.print(separator + quad[2]);
            }
            if(quad[3] != null){ System.out.print(separator + quad[3]); }
            System.out.println();
            lineNumber++;
        }
    }


    /**
     * saveQuadruples: save the quadruples to a file
     * @param fileName: the name of the file.
     * @param encoding: the encoding of the file.
     * @throws FileNotFoundException: in case the file cannot be created.
     * @throws UnsupportedEncodingException: in case the encoding is not supported.
     */
    public void saveQuadruples(String fileName, String encoding) throws FileNotFoundException, UnsupportedEncodingException{
        /* PrintWriter allows for writing of formatted text to a file */
        PrintWriter writer = new PrintWriter(fileName + ".tvi", encoding);

        /* starting line number */
        int lineNumber = 1;
        /* separator for TVI code */
        String separator;

        /* save the header */
        writer.println("::: TVI Code :::");

        Enumeration<String[]> en = this.quadruples.elements();
        en.nextElement();
        en.nextElement();

        while(en.hasMoreElements()){
            String[] quad = en.nextElement();
            separator = " ";
            writer.print(lineNumber + ": " + quad[0]);
            if(quad[1] != null){ writer.print(separator + quad[1]); }
            if(quad[2] != null){
                separator = ", ";
                writer.print(separator + quad[3]);
            }
            writer.println();
            lineNumber ++;
        }
        writer.close();
    }

    /**
     * saveQuadruples: save the quadruples to a file
     * @param fileName: the name of the file.
     * @throws FileNotFoundException: in case the file cannot be created.
     * @throws UnsupportedEncodingException: in case the encoding is not supported.
     */
    public void saveQuadruples(String fileName) throws FileNotFoundException, UnsupportedEncodingException{
        /* PrintWriter allows for writing of formatted text to a file */
        PrintWriter writer = new PrintWriter(fileName + ".tvi", "UTF-8");

        /* starting line number */
        int lineNumber = 1;
        /* separator for TVI code */
        String separator;

        /* save the header (comment with ; ) */
        writer.println("; ::: TVI Code :::");

        Enumeration<String[]> en = this.quadruples.elements();
        en.nextElement();
        en.nextElement();

        while(en.hasMoreElements()){
            String[] quad = en.nextElement();
            separator = " ";
            writer.print(lineNumber + ": " + quad[0]);
            if(quad[1] != null){ writer.print(separator + quad[1]); }
            if(quad[2] != null){
                separator = ", ";
                writer.print(separator + quad[3]);
            }
            writer.println();
            lineNumber ++;
        }
        writer.close();
    }

} /* end of Quadruples class */
