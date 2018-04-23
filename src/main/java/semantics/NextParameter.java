package main.java.semantics;

import main.java.table.ParameterInfo;

import java.util.LinkedList;
import java.util.Stack;


public class NextParameter{

    private Stack<LinkedList<ParameterInfo>> paramLists;
    private Stack<Integer> paramPointers;

    NextParameter(){
        paramLists = new Stack<>();
        paramPointers = new Stack<>();
    }

    public LinkedList<ParameterInfo> pop(){
        paramPointers.pop();
        return paramLists.pop();
    }

    public void push(LinkedList<ParameterInfo> paramList){
        paramLists.push(paramList);
        paramPointers.push(0);
    }

    public ParameterInfo nextParam(){
        return paramLists.peek().get(paramPointers.peek());
    }

    public void increment(){
        paramPointers.push((paramPointers.pop()) + 1);
    }

}
