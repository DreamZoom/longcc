package com.cloud.ying.longcc.generator;

import com.cloud.ying.longcc.TokenDefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class NFAState {

    private static int max = 0;
    public NFAState(){
        this.index= max++;
        this.edges =new HashSet<>();
    }
    public HashSet<NFAEdge> getEdges() {
        return edges;
    }

    public void setEdges(HashSet<NFAEdge> edges) {
        this.edges = edges;
    }

    private HashSet<NFAEdge> edges;


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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;
    public boolean OnlyEmptyEdge(){
        if(this.edges.size()!=1) return  false;
        Iterator iterator = this.edges.iterator();
        while (iterator.hasNext()){
            NFAEdge edge =(NFAEdge)iterator.next();
            if(edge.IsEmpty()) return true;
        }
        return false;
    }

    public NFAState OnlyEmptyEdgeNextState(){
        if(this.edges.size()!=1) return  null;
        Iterator iterator = this.edges.iterator();
        while (iterator.hasNext()){
            NFAEdge edge =(NFAEdge)iterator.next();
            if(edge.IsEmpty()) {
                return edge.getTargetState();
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "nfa#"+index+"#"+tag;
    }
}
