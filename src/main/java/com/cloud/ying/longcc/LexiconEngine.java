package com.cloud.ying.longcc;

import com.cloud.ying.longcc.regular2.RegularExpression;
import com.cloud.ying.longcc.regular2.RegularParser;

import java.util.HashMap;

public class LexiconEngine {

    HashMap<String, RegularExpression> map;
    RegularParser parser;
    public LexiconEngine(){
        parser=new RegularParser();
        map=new HashMap<>();
    }

    public void defineToken(String tag,String regex) throws Exception{
        map.put(tag,parser.parser(regex));
    }
}
