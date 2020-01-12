package com.cloud.ying.longcc.regular;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RegularAlternationExpression extends RegularExpression {
    public RegularExpression[] getExpressions() {
        return expressions;
    }

    private RegularExpression[] expressions;
    public  RegularAlternationExpression(RegularExpression... expressions){
       this.expressions = expressions;
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
