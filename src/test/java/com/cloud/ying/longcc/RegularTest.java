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
            LexiconEngine engine =new LexiconEngine();
            engine.defineToken("link","[(and)(or)]");
            engine.defineToken("field","[a-zA-Z][a-zA-Z0-9]*");
            engine.defineToken("option","[(=)(!=)(>=)(<=)(>)(<)]");

            String number = "(([0-9][0-9]*.[0-9][0-9]*)|([0-9][0-9]*))";
            String string = "([\"'][\u4e00-\u9fa50-9a-zA-Z]*[\"'])";
            engine.defineToken("value",number+"|"+string);
            engine.defineToken("(","\\(");
            engine.defineToken(")","\\)");
            engine.defineToken("space"," ");

//            engine.defineGrammar("G",new String[]{"field","option","value"}).defineParser((tokens)->{
//                if(tokens.length==3) {
//                    return  new CompareSegment((Token)tokens[0],(Token)tokens[1],(Token)tokens[2]);
//                }
//                else {
//                    throw new Exception("语法错误");
//                }
//            });


            engine.defineGrammar("G",new String[]{"G","space*","link","space*","G"},new String[]{"(","G",")"},new String[]{"field","option","value"}).defineParser((tokens)->{
                if(tokens.length==1) {
                    return  (Segment)tokens[0];
                }
                else if(tokens.length==3){
                    if(!"(".equals(tokens[0])){
                        return  new CompareSegment((Token)tokens[0],(Token)tokens[1],(Token)tokens[2]);
                    }
                    return  (Segment)tokens[1];
                }
                else if(tokens.length==5){
                    return new LinkSegment((Segment)tokens[0],(Token)tokens[2],(Segment)tokens[4]);
                }
                else{
                    throw new Exception("语法错误");
                }
            });
            System.out.println("start");
            long start = System.currentTimeMillis();
            engine.initialize();
            Segment segment = engine.parse2("(name='王小龙' and grand=5) or age>19");
            System.out.println("it consumes " +(System.currentTimeMillis() - start) + "ms");



        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }
    }
}
