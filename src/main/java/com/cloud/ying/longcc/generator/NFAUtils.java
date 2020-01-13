package com.cloud.ying.longcc.generator;

import java.util.ArrayList;
import java.util.List;

public  class NFAUtils {

    public static List<NFAState> GetClosure(List<NFAState> states){
        List<NFAState> list=new ArrayList<>();
        int size = states.size();
        for (int i = 0; i < size; i++) {
            for (NFAEdge edge:states.get(i).getEdges()) {
                if(edge.IsEmpty()){
                    list.add(edge.getTargetState());
                }
            }
        }
        if(list.size()>0) {
            List<NFAState> next = GetClosure(list);
            list.addAll(next);
        }
        return list;
    }

    public static List<NFAState> GetDFAStates(List<NFAState> states, Integer symbol)
    {
        DFAState target = new DFAState();
        List<NFAState> newStates=new ArrayList<>();
        for(NFAState nfaState : states)
        {
            for (NFAEdge edge:nfaState.getEdges()) {
                if (!edge.IsEmpty() && edge.getSymbol().equals(symbol)){
                    newStates.add(edge.getTargetState());
                }
            }
        }
        return GetClosure(newStates);
    }
}
