package com.cloud.ying.longcc.generator;

import com.cloud.ying.longcc.TokenDefinition;

import java.util.ArrayList;
import java.util.List;

public class NFAState {

    private static int max = 0;
    public NFAState(){
        this.index= max++;
        this.edges =new ArrayList<>();
    }
    public List<NFAEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<NFAEdge> edges) {
        this.edges = edges;
    }

    private List<NFAEdge> edges;


    public  void AddEdge(NFAEdge edge){
        edges.add(edge);
    }

    public  void AddEmptyEdgeTo(NFAState state){
        edges.add(new NFAEdge(state));
    }


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    private Integer index;


    public TokenDefinition getTokenDefinition() {
        return tokenDefinition;
    }

    public void setTokenDefinition(TokenDefinition tokenDefinition) {
        this.tokenDefinition = tokenDefinition;
    }

    private TokenDefinition tokenDefinition;


    @Override
    public String toString() {
        return "nfa#"+index;
    }
}
