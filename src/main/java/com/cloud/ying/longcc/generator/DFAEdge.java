package com.cloud.ying.longcc.generator;

public class DFAEdge {

    public  DFAEdge(Integer symbol,DFAState state){
        this.symbol = symbol;
        this.targetState =state;
    }
    private Integer symbol;

    public Integer getSymbol() {
        return symbol;
    }

    public void setSymbol(Integer symbol) {
        this.symbol = symbol;
    }

    public DFAState getTargetState() {
        return targetState;
    }

    public void setTargetState(DFAState targetState) {
        this.targetState = targetState;
    }

    private DFAState targetState;
}
