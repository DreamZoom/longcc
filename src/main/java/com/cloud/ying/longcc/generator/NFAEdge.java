package com.cloud.ying.longcc.generator;

import java.util.Objects;

public class NFAEdge {

    public NFAEdge(NFAState targetState){
        this.symbol = null;
        this.targetState = targetState;
    }
    public NFAEdge(Integer symbol,NFAState targetState){
        this.symbol = symbol;
        this.targetState = targetState;
    }
    private Integer symbol;

    public Integer getSymbol() {
        return symbol;
    }



    public NFAState getTargetState() {
        return targetState;
    }

    public void setTargetState(NFAState targetState) {
        this.targetState = targetState;
    }

    private NFAState targetState;

    public boolean IsEmpty(){
        return  symbol==null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NFAEdge edge = (NFAEdge) o;
        return Objects.equals(symbol, edge.symbol) &&
                Objects.equals(targetState, edge.targetState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, targetState);
    }
}
