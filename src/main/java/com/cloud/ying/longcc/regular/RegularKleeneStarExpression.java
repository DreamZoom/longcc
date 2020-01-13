package com.cloud.ying.longcc.regular;

import java.util.HashSet;
import java.util.Set;

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

    @Override
    public Set<Character> GetCompressibleCharSet() {
        return expression.GetCompressibleCharSet();
    }
    @Override
    public Set<Character> GetIncompressibleCharSet() {
        Set<Character> list = new HashSet<>();
        if(expression instanceof RegularCharExpression){
            list.add(((RegularCharExpression) expression).getCharacter());
        }
        else{
            list.addAll(expression.GetIncompressibleCharSet());
        }
        return list;

    }
}
