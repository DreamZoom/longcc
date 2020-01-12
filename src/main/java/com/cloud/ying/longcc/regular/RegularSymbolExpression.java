package com.cloud.ying.longcc.regular;

import java.util.HashSet;

public class RegularSymbolExpression extends  RegularExpression {
    public RegularSymbolExpression(char symbol){
        this.symbol = symbol;
    }
    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    private char symbol;



    @Override
    public HashSet<Character> GetUncompactableCharSet() {
        HashSet<Character> sets =new HashSet<>();
        sets.add(symbol);
        return sets;
    }
}
