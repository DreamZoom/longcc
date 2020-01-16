package com.cloud.ying.longcc;

public class Grammar {

    String[][] segments;
    public Grammar(String[] ...expressions){
        this.segments = expressions;
    }

    Parser parser;

    public void defineParser(Parser parser){
        this.parser=parser;
    }
}
