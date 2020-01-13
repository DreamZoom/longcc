package com.cloud.ying.longcc.regular;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 正则与运算表达式
 */
public class RegularConcatenationExpression  extends  RegularExpression{
    public List<RegularExpression> getExpressions() {
        return expressions;
    }

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

    @Override
    public Set<Character> GetCompressibleCharSet() {
        Set<Character> list = new HashSet<>();
        for (int i = 0; i <expressions.size() ; i++) {
            list.addAll(expressions.get(i).GetCompressibleCharSet());
        }
        return list;
    }
    @Override
    public Set<Character> GetIncompressibleCharSet() {
        Set<Character> list = new HashSet<>();
        for (int i = 0; i <expressions.size() ; i++) {
            if(expressions.get(i) instanceof RegularCharExpression){
                list.add(((RegularCharExpression) expressions.get(i)).getCharacter());
            }
            else{
                list.addAll(expressions.get(i).GetIncompressibleCharSet());
            }
        }
        return list;

    }

}
