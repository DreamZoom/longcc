package com.cloud.ying.longcc;

import com.cloud.ying.longcc.generator.NFAModel;
import com.cloud.ying.longcc.ql.CompareSegment;
import com.cloud.ying.longcc.ql.LinkSegment;
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
//            LexiconEngine engine =new LexiconEngine();
//            engine.defineToken("like","[(and)(or)]");
//            engine.defineToken("field","[a-zA-Z][a-zA-Z0-9]*");
//            engine.defineToken("option","[(=)(>=)(<=)(>)(<)]");
//            engine.defineToken("number","[([0-9][0-9]*.[0-9][0-9]*),([0-9][0-9]*)]");
//            engine.defineToken("value","[\u4e00-\u9fa50-9]*");
//            engine.defineToken("space"," ");
//
//            engine.defineGrammar("E",new String[]{"field","option","value"}).defineParser((tokens)->{
//                return  new CompareSegment((Token)tokens[0],(Token)tokens[1],(Token)tokens[2]);
//            });
//            engine.defineGrammar("G",new String[]{"E","space*","like","space*","E"}).defineParser((tokens)->{
//                return new LinkSegment((Segment)tokens[0],(Token)tokens[2],(Segment)tokens[0]);
//            });
//            long start = System.currentTimeMillis();
//            engine.initialize();
//            Segment segment = engine.parse("name=王小龙 or age=19");
//            System.out.println("it consumes " +(System.currentTimeMillis() - start) + "ms");

              RegularParser parser =new RegularParser();
              RegularExpression expression =  parser.parse("a|b|c");
              System.out.println(expression);

        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }
    }
}
