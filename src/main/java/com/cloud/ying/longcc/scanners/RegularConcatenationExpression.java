package com.cloud.ying.longcc.scanners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RegularConcatenationExpression extends RegularExpression {

    public RegularExpression[] getExpressions() {
        return expressions;
    }

    private RegularExpression[] expressions;

    public  RegularConcatenationExpression(RegularExpression... expressions){
        this.expressions=expressions;
    }

    @Override
    public List<HashSet<Character>> GetCompactableCharSets() {
        List<HashSet<Character>> list = new ArrayList<>();
        for (RegularExpression expression: expressions) {
            list.addAll(expression.GetCompactableCharSets());
        }
        return list;
    }

    @Override
    public HashSet<Character> GetUncompactableCharSet() {
        HashSet<Character> characters = new HashSet<>() ;
        for (RegularExpression expression: expressions) {
            characters.addAll(expression.GetUncompactableCharSet());
        }
        return characters;
    }
}
