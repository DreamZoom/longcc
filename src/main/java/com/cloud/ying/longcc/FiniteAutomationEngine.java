package com.cloud.ying.longcc;
import com.cloud.ying.longcc.generator.CharMapping;
import com.cloud.ying.longcc.generator.DFAState;

import java.util.HashMap;
import java.util.List;

public class FiniteAutomationEngine {

    private List<HashMap<Integer,Integer>> transitionTable;
    private CharMapping charMapping;
    private  List<DFAState> states;

    public FiniteAutomationEngine(List<HashMap<Integer,Integer>> transitionTable, CharMapping charMapping, List<DFAState> states){
        this.transitionTable = transitionTable;
        this.charMapping = charMapping;
        this.states=states;
    }

    private  int state = 1;
    private  int last_state = 1;

    private StringBuffer buffer =new StringBuffer();

    public String getWords() {
        return words;
    }
    private String words=null;

    public Token getToken() {
        return token;
    }

    private Token token = null;

    public boolean accecpt(char c){
        HashMap<Integer,Integer> map = transitionTable.get(state);
        int k =charMapping.getCharIndex(c);
        last_state = state;
        state = map.get(k);
        if(state==0){
            words = buffer.toString();
            buffer.delete(0,buffer.length());
            state=1;

            DFAState dfaState = states.get(last_state);
            if(dfaState!=null){
                token = new Token(words,dfaState.getTag());
            }
            else{
                token = new Token(words,null);
            }

            return true;
        }
        else{
            buffer.append(c);
            return false;
        }
    }

}
