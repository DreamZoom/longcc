package com.cloud.ying.longcc;

import java.util.ArrayList;
import java.util.List;

public class SourceScanner {
    private  FiniteAutomationEngine engine;
    public SourceScanner(FiniteAutomationEngine engine){
        this.engine =engine;
    }

    public List<Token> scan(String source){
//        List<String> tokens =new ArrayList<>();
        List<Token> tokens =new ArrayList<>();
        int next = 0;
        char[] chars = source.toCharArray();
        while(next>-1 && next<chars.length){
            char c= chars[next];
            if(engine.accecpt(c)){
                tokens.add(engine.getToken());
            }
            else{
                next++;
            }
        }
        return tokens;
    }
}
