package com.cloud.ying.longcc.generator;

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

    public void setSymbol(Integer symbol) {
        this.symbol = symbol;
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
}
