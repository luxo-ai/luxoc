/*
 * File: NextParameter.java
 * Desc: stack of parameter list pointers
 */

package main.java.semantics;

import main.java.table.ParameterInfo;
import java.util.LinkedList;
import java.util.Stack;


/**
 * NextParameter class
 * @author Luis Serazo
 */
public class NextParameter{

    private Stack<LinkedList<ParameterInfo>> paramLists;
    int currentParam;

    NextParameter(){
        paramLists = new Stack<>();
        currentParam = 0;
    }

    LinkedList<ParameterInfo> pop(){
        currentParam = 0;
        return paramLists.pop();
    }

    void push(LinkedList<ParameterInfo> paramList){
        paramLists.push(paramList);
    }

    ParameterInfo getParam(){
        ParameterInfo paramInfo = paramLists.peek().get(currentParam);
        currentParam++;
        return paramInfo;
    }

    ParameterInfo getParamAt(int i){
        return paramLists.peek().get(i);
    }
}
