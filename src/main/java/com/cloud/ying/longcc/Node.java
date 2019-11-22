package com.cloud.ying.longcc;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public void setToken(Token token) {
        this.token = token;
    }

    private Token token;
    public List<Node> childs;
    public Node(){
        childs=new ArrayList<>();
    }
    public Node(Token token){
        this.token=token;
        childs=new ArrayList<>();
    }

    public void addNode(Node node){
        childs.add(node);
    }

    public boolean isEmpty(){
        return childs.isEmpty();
    }

    public String getTag(){
        if(this.token!=null){
            return token.getTag();
        }
        return null;
    }
    public String getWord(){
        if(this.token!=null){
            return token.getWord();
        }
        return null;
    }

}
