package com.cloud.ying.longcc;


import com.cloud.ying.longcc.regular.RegularExpression;

public class TokenDefinition {


    public RegularExpression getExpression() {
        return expression;
    }

    public void setExpression(RegularExpression expression) {
        this.expression = expression;
    }

    private RegularExpression expression;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;

    public TokenDefinition(RegularExpression expression,String tag){
        setExpression(expression);
        setTag(tag);
    }

}
