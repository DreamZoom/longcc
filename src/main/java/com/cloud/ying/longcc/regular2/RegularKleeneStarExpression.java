package com.cloud.ying.longcc.regular2;

/**
 * 克林星运算表达式
 */
public class RegularKleeneStarExpression extends RegularExpression {
    private RegularExpression expression;
    public RegularKleeneStarExpression(RegularExpression expression){
        this.expression=expression;
    }
}
