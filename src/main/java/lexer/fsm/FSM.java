/**
 * FILE: FSM.java
 *
 * DESC: contains FSM class which describes a finite state machine
 *
 * @author Luis Serazo
 *
 */
package main.java.lexer.fsm;
import java.util.HashMap;
import java.util.List;

public class FSM {

    /**
     * states: enum list of possible states in the FSM
     * transitions: List of transitions: List[Hash(Enum, Enum)]
     * init: initial state
     *
     */
    private HashMap<Enum, State> states;
    private HashMap<String, State>[] transitions;
    private Enum entry;



    public FSM(Enum[] states, HashMap<String, Enum>[] transitions, Enum entry){
        this.entry = entry;



        // TODO: check that there are no duplicate states.

        initStates(states);

        initTransitions(transitions);
    }
    /**
     * initializer routine
     */
    private void initStates(Enum[] states){

        // TODO: preprocessing check for duplicates

        for(Enum e : states) {
            this.states.put(e, new State());
        }
    }

    /**
     * initializer routine
     */
    private void initTransitions(HashMap<String, Enum>[] transitions){
        // TODO: make sure all such states exist.
        for(HashMap<String, Enum> hmp : transitions){
            State src = this.states.get(hmp.get("source"));
            State dest = this.states.get(hmp.get("destination"));
        }
    }




}
