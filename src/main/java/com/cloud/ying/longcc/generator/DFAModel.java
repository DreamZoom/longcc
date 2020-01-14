package com.cloud.ying.longcc.generator;


import com.cloud.ying.longcc.TokenDefinition;

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

        //.getNfaStates().
        List<TokenDefinition> tokenDefinitionList =new ArrayList<>();
        for (NFAState s:state.getNfaStates()) {
            if(s.getTokenDefinition()!=null){
                tokenDefinitionList.add(s.getTokenDefinition());
            }
        }

        tokenDefinitionList.sort(new Comparator<TokenDefinition>(){
            @Override
            public int compare(TokenDefinition o1, TokenDefinition o2) {
                return o1.getIndex()-o2.getIndex();
            }
        });

        if(tokenDefinitionList.size()>0){
            TokenDefinition tokenDefinition = tokenDefinitionList.get(0);
            state.setTokenDefinition(tokenDefinition);
        }


        if(state.getIndex()==5){
            System.out.println("#"+state.getIndex()+"="+state.getTokenDefinition());
        }

    }


    public DFAState GetClosure(DFAState state)
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

    public DFAState GetDFAState(DFAState start, Integer symbol)
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
