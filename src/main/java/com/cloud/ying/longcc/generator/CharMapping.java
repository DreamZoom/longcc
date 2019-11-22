package com.cloud.ying.longcc.generator;

import java.util.HashMap;

public class CharMapping {
    HashMap<Character,Integer> compressCharSets;
    public Integer maxIndex = 0;

    public CharMapping(HashMap<Character,Integer> compressCharSets, Integer max){
        this.compressCharSets =compressCharSets;
        this.maxIndex = max;
    }


    public Integer getCharIndex(char c){
        if(compressCharSets.containsKey(c))
            return  compressCharSets.get(c);
        return 0;
    }
}
