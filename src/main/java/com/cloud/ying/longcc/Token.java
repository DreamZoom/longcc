package com.cloud.ying.longcc;
public class Token {

    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public TokenDefinition getTokenDefinition() {
        return tokenDefinition;
    }

    public void setTokenDefinition(TokenDefinition tokenDefinition) {
        this.tokenDefinition = tokenDefinition;
    }

    private TokenDefinition tokenDefinition;


    public Token(String word,TokenDefinition tag){
        this.word=word;
        this.tokenDefinition = tag;
    }

    @Override
    public String toString() {
        if(tokenDefinition!=null)
            return this.word+"==>"+this.tokenDefinition.getTag();
        return this.word+"==>Unkown";
    }

    public  String getTag(){
        if(tokenDefinition!=null)
            return this.tokenDefinition.getTag();
        return "";
    }
}
