package com.cloud.ying.longcc.generator;

import com.cloud.ying.longcc.TokenDefinition;

import java.util.*;

public class DFAState {

    public  DFAState(){
        this.edges = new ArrayList<>();
        this.nfaStates =new HashSet<>();
    }

    public List<DFAEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<DFAEdge> edges) {
        this.edges = edges;
    }

    private List<DFAEdge> edges;


    public HashSet<NFAState> getNfaStates() {
        return nfaStates;
    }

    public void setNfaStates(HashSet<NFAState> nfaStates) {
        this.nfaStates = nfaStates;
    }

    private HashSet<NFAState> nfaStates;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    private Integer index;

    public void addEdge(DFAEdge edge){
        edges.add(edge);
    }


    public TokenDefinition getTokenDefinition() {
        return tokenDefinition;
    }

    public void setTokenDefinition(TokenDefinition tokenDefinition) {
        this.tokenDefinition = tokenDefinition;
    }

    private TokenDefinition tokenDefinition;


    @Override
    public String toString() {
//        Iterator iterator = nfaStates.iterator();
        StringBuffer sb=new StringBuffer();
        return "DFA#"+index+"@"+sb.toString();
    }


    public  String getTag(){

        List<NFAState> states =new ArrayList<>();
        states.addAll(nfaStates);
        states.sort((a,b)->a.getEdges().size()-b.getEdges().size());
        Iterator iterator = states.iterator();
        while (iterator.hasNext()){
            NFAState state = (NFAState)iterator.next();
            if(state.getTag()!=null && !state.getTag().equals("")){
                return state.getTag();
            }
        }
        return "";
    }
}
