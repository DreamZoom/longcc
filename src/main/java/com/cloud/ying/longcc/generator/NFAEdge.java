package com.cloud.ying.longcc.generator;

public class NFAEdge {

    public NFAEdge(NFAState targetState){
        this.symbol = null;
        this.targetState = targetState;
    }
    public NFAEdge(Character symbol,NFAState targetState){
        this.symbol = symbol;
        this.targetState = targetState;
    }
    private Character symbol;

    public Character getSymbol() {
        return symbol;
    }

    public void setSymbol(Character symbol) {
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
