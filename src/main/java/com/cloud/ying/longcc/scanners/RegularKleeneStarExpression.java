package com.cloud.ying.longcc.scanners;

import java.util.HashSet;
import java.util.List;

public class RegularKleeneStarExpression extends RegularExpression {

    public  RegularKleeneStarExpression(RegularExpression regularExpression){
        innerExpression = regularExpression;
    }
    public RegularExpression getInnerExpression() {
        return innerExpression;
    }

    public void setInnerExpression(RegularExpression innerExpression) {
        this.innerExpression = innerExpression;
    }

    private RegularExpression innerExpression;


    @Override
    public HashSet<Character> GetUncompactableCharSet() {
        return innerExpression.GetUncompactableCharSet();
    }

    @Override
    public List<HashSet<Character>> GetCompactableCharSets() {
        return innerExpression.GetCompactableCharSets();
    }
}
