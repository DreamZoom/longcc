package com.cloud.ying.longcc.regular2;

import java.util.ArrayList;
import java.util.List;

/**
 * 正则与运算表达式
 */
public class RegularConcatenationExpression  extends  RegularExpression{
    private List<RegularExpression> expressions;
    public RegularConcatenationExpression(RegularExpression ...expressions){
        this.expressions=new ArrayList<>();
        for (int i = 0; i <expressions.length ; i++) {
            this.expressions.add(expressions[i]);
        }
    }


    public RegularConcatenationExpression(List<RegularExpression> expressions){
        this.expressions=new ArrayList<>();
        this.expressions.addAll(expressions);
    }

}
