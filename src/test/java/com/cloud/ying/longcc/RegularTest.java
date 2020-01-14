package com.cloud.ying.longcc;

import com.cloud.ying.longcc.generator.NFAModel;
import com.cloud.ying.longcc.regular.RegularExpression;
import com.cloud.ying.longcc.regular.RegularParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RegularTest {

    @Test
    public void test(){

        try {
            LexiconEngine engine =new LexiconEngine();
            engine.defineToken("field","[a-zA-Z][a-zA-Z0-9]*");
            engine.defineToken("value","and[\u4e00-\u9fa5][a-zA-Z0-9]*");
            long start = System.currentTimeMillis();
            engine.initialize();
            System.out.println("it consumes " +(System.currentTimeMillis() - start) + "ms");
            System.out.println("test");
        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }
    }
}
