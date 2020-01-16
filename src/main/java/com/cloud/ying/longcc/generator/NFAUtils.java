package com.cloud.ying.longcc.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public  class NFAUtils {

    public static DFAState GetClosure(DFAState state)
    {
        DFAState closure = new DFAState();

        closure.getNfaStates().addAll(state.getNfaStates());
        boolean changed = true;

        while (changed)
        {
            changed = false;
            int size = closure.getNfaStates().size();
            NFAState[] list =new NFAState[size];
            closure.getNfaStates().toArray(list);

            for(int s =0;s<list.length;s++)
            {
                NFAState nfaState = list[s];
                HashSet<NFAEdge> outEdges = nfaState.getEdges();

                Iterator iterator = outEdges.iterator();
                while (iterator.hasNext()){
                    NFAEdge edge =(NFAEdge)iterator.next();
                    if(edge.IsEmpty()) {
                        NFAState target = edge.getTargetState();
                        changed = closure.getNfaStates().add(target) || changed;
                    }
                }
            }
        }

        return closure;
    }

    public static DFAState GetDFAState(DFAState start, Integer symbol)
    {
        DFAState target = new DFAState();
        for(NFAState nfaState : start.getNfaStates())
        {
            HashSet<NFAEdge> outEdges = nfaState.getEdges();
            Iterator iterator = outEdges.iterator();
            while (iterator.hasNext()){
                NFAEdge edge =(NFAEdge)iterator.next();
                if (!edge.IsEmpty() && edge.getSymbol().equals(symbol))
                {
                    target.getNfaStates().add(edge.getTargetState());
                }
            }
        }
        return GetClosure(target);
    }
}
