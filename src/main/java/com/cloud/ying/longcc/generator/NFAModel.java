package com.cloud.ying.longcc.generator;

import java.util.ArrayList;
import java.util.List;

public class NFAModel {

    public NFAModel(){
        this.states = new ArrayList<>();
    }
    private NFAState tailState ;
    private NFAEdge entryEdge ;

    public NFAState getTailState() {
        return tailState;
    }

    public void setTailState(NFAState tailState) {
        this.tailState = tailState;
    }

    public NFAEdge getEntryEdge() {
        return entryEdge;
    }

    public void setEntryEdge(NFAEdge entryEdge) {
        this.entryEdge = entryEdge;
    }

    public List<NFAState> getStates() {
        return states;
    }

    public void setStates(List<NFAState> states) {
        this.states = states;
    }

    private List<NFAState> states ;

    public void AddState(NFAState state)
    {
        states.add(state);
    }

    public void AddStates(List<NFAState> m_states)
    {
        m_states.forEach((s)->{
            AddState(s);
        });
    }
}
