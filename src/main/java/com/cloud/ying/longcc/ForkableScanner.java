package com.cloud.ying.longcc;

import java.util.List;

public class ForkableScanner implements Cloneable {
    List<Token> tokens ;
    int index = 0;
    public ForkableScanner(List<Token> tokens){
        this.tokens = tokens;
    }

    public  Token read(){
        if(index>=this.tokens.size()){
            return null;
        }
        return  this.tokens.get(index++);
    }


    public ForkableScanner fork(){
        ForkableScanner o = null;
        try {
            o = (ForkableScanner) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }
}
