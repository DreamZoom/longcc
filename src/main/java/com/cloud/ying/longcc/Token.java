package com.cloud.ying.longcc;
public class Token {

    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;


    public Token(String word,String tag){
        this.word=word;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return this.word+"==>"+this.tag;
    }

}
