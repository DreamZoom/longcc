package com.cloud.ying.longcc.regular;

/**
 * 克林星运算表达式
 */
public class RegularKleeneStarExpression extends RegularExpression {
    public RegularExpression getExpression() {
        return expression;
    }

    private RegularExpression expression;
    public RegularKleeneStarExpression(RegularExpression expression){
        this.expression=expression;
    }
}
