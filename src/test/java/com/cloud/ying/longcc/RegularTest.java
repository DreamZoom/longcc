package com.cloud.ying.longcc;

import com.cloud.ying.longcc.generator.NFAModel;
import com.cloud.ying.longcc.regular.RegularExpression;
import com.cloud.ying.longcc.regular.RegularParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegularTest {

    @Test
    public void test(){

        try {
            LexiconEngine engine =new LexiconEngine();
            engine.defineToken("and","and");
            engine.defineToken("field","[a-zA-Z][a-zA-Z0-9]*");
            engine.defineToken("option","=");
            engine.defineToken("value","[\u4e00-\u9fa50-9]*");
            engine.defineToken("space"," ");

            engine.defineGrammar("E",new String[]{"field","option","value"});
            engine.defineGrammar("G",new String[]{"E","space*","and","space*","E"});
            long start = System.currentTimeMillis();
            engine.initialize();
            engine.parse("name=王小龙 and age=19");
            System.out.println("it consumes " +(System.currentTimeMillis() - start) + "ms");

        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }
    }
}
