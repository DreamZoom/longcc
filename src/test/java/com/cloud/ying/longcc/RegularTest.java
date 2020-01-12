package com.cloud.ying.longcc;

import com.cloud.ying.longcc.generator.NFAModel;
import com.cloud.ying.longcc.regular.RegularExpression;
import com.cloud.ying.longcc.regular.RegularParser;
import org.junit.Test;

public class RegularTest {

    @Test
    public void test(){

        try {

            LexiconEngine engine =new LexiconEngine();
            engine.defineToken("field","[a-zA-Z][a-zA-Z0-9]*");
            engine.defineToken("value","[a-zA-Z0-9]*");
            NFAModel lexerNFA =engine.ConvertToNFA();
            System.out.println("test");
        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }
    }
}
