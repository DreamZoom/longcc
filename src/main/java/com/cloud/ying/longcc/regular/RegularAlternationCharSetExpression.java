package com.cloud.ying.longcc.regular;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RegularAlternationCharSetExpression extends  RegularExpression {
    public RegularAlternationCharSetExpression(HashSet<Character> charSet){
        this.setCharSet(charSet);
    }
    public HashSet<Character> getCharSet() {

        return characters;
    }

    public void setCharSet(String charSet) {
        characters =new HashSet<>();
        for (char c : charSet.toCharArray()) {
            this.characters.add(c);
        }

    }
    public void setCharSet(HashSet<Character> characters) {

        this.characters = characters;
    }

    private HashSet<Character> characters;


    @Override
    public List<HashSet<Character>> GetCompactableCharSets() {
        List<HashSet<Character>> list =new ArrayList<>();
        list.add((HashSet<Character>)characters.clone());
        return list;
    }
}
