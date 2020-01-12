package com.cloud.ying.longcc;

import com.cloud.ying.longcc.regular2.RegularExpression;
import com.cloud.ying.longcc.regular2.RegularParser;
import org.junit.Test;

public class RegularTest {

    @Test
    public void test(){
        RegularParser parser =new RegularParser();
        try {
            RegularExpression expression = parser.parser("aaa");
            expression = parser.parser("aaa*[ab\u4e00-\u9fa5]");
            System.out.println("test");
        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }
    }
}
