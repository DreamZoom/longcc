package com.cloud.ying.longcc.regular;

import java.util.HashSet;

public class RegularStringLiteralExpression extends RegularExpression {

    public RegularStringLiteralExpression(String literal){
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    private String literal;


    @Override
    public HashSet<Character> GetUncompactableCharSet() {
        HashSet<Character> sets =new HashSet<>();
        for (char  c: literal.toCharArray()){
            sets.add(c);
        }
        return sets;
    }
}
