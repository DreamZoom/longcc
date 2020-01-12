package com.cloud.ying.longcc.regular;

import java.util.ArrayList;
import java.util.List;

/**
 * 正则或运算表达式
 */
public class RegularAlternationExpression extends RegularExpression {

    public List<RegularExpression> getExpressions() {
        return expressions;
    }

    private List<RegularExpression> expressions;
    public RegularAlternationExpression(RegularExpression ...expressions){
        this.expressions=new ArrayList<>();
        for (int i = 0; i <expressions.length ; i++) {
            this.expressions.add(expressions[i]);
        }
    }
    public RegularAlternationExpression(List<RegularExpression> expressions){
        this.expressions=new ArrayList<>();
        this.expressions.addAll(expressions);
    }

}
