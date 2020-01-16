package com.cloud.ying.longcc.generator;


import java.util.*;

public class DFAModel {
    public DFAModel(){
        states = new ArrayList<>();
    }

    public List<DFAState> getStates() {
        return states;
    }

    private List<DFAState> states;

    public void AddDFAState(DFAState state){
        state.setIndex(states.size());
        states.add(state);
    }

}
