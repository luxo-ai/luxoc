/*
 * File: Quadruple.java
 *
 * Desc: stores the intermediate code as it is generated
 *
 */
package main.java.semantics;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Quadruples
 * @author Luis Serazo
 */
public class Quadruples {

    // SAMPLE CODE PROVIDED???
    private Vector<String[]> Quadruple;
    private int nextQuad;

    public Quadruples(){
        Quadruple = new Vector<>();
        nextQuad = 0;
        String[] dummy_quadruple = new String[4];
        dummy_quadruple[0] = dummy_quadruple[1] = dummy_quadruple[2] = dummy_quadruple[3] = null;
        Quadruple.add(nextQuad, dummy_quadruple);
        nextQuad++;
    }

    public String getField(int quadIndex, int field){
        return Quadruple.elementAt(quadIndex)[field];
    }

    public void setField(int quadIndex, int index, String field){
        Quadruple.elementAt(quadIndex)[index] = field;
    }

    public int getNextQuad(){
        return nextQuad;
    }

    public void incrementNextQuad(){
        nextQuad++;
    }

    public String[] getQuad(int index){
        return (String[]) Quadruple.elementAt(index);
    }

    public void addQuad(String[] quad){
        Quadruple.add(nextQuad, quad);
    }

    public void print(){
        int quadLabel = 1;
        String separator;

        System.out.println("CODE");
        Enumeration<String[]> e = this.Quadruple.elements();
        e.nextElement();

        while(e.hasMoreElements()){
            String[] quad = e.nextElement();
            separator = " ";
            System.out.print(quadLabel + ": " + quad[0]);
            if(quad[1] != null){
                System.out.print(separator + quad[1]);
            }
            if(quad[2] != null){
                System.out.print(separator + quad[2]);
            }
            if(quad[3] != null){
                System.out.print(separator + quad[3]);
            }
            System.out.println();
            quadLabel++;
        }
    }

    public void storeQuadruples(String filename) throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter pw = new PrintWriter(filename + ".tvi", "UTF-8");

        int quadLabel = 1;

        String separator;

        pw.println("CODE");

        Enumeration<String[]> e = this.Quadruple.elements();

        e.nextElement();
        e.nextElement();

        while(e.hasMoreElements()){
            String[] quad = e.nextElement();
            separator = " ";
            pw.print(quadLabel + ": " + quad[0]);
            if(quad[1] != null){
                pw.print(separator + quad[1]);
            }
            if(quad[2] != null){
                separator = ", ";
                pw.print(separator + quad[3]);
            }
            pw.println();
            quadLabel++;
        }
        pw.close();
    }

} /* end of Quadruples class */
