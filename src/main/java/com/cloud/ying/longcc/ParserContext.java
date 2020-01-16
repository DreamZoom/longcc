package com.cloud.ying.longcc;

import java.util.ArrayList;
import java.util.List;

public class ParserContext implements Cloneable{
    public ParserContext(List<Token> tokens,int index) {
        this.tokens = tokens;
        this.index=index;
        this.args=new ArrayList<>();
    }

    List<Token> tokens;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int index;

    public ParserContext fork(){
        return new ParserContext(tokens,index);
    }


    List<Object> args;
}
