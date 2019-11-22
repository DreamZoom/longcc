package com.cloud.ying.longcc.parser;

import com.cloud.ying.longcc.ForkableScanner;
import com.cloud.ying.longcc.Node;

public class ParseContext implements Cloneable{
    public Node getNode() {
        return node;
    }

    public ForkableScanner getScanner() {
        return scanner;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setScanner(ForkableScanner scanner) {
        this.scanner = scanner;
    }



    private Node node;
    private ForkableScanner scanner;
    public ParseContext(Node node, ForkableScanner scanner){
        this.node=node;
        this.scanner=scanner;
    }

    public ParseContext(ForkableScanner scanner){
        this.node = new Node();
        this.scanner=scanner;
    }

    public ParseContext(){
        this.node = new Node();
    }

    public ParseContext fork(){
        ParseContext o = null;
        try {
            o = (ParseContext) super.clone();
            o.setScanner(this.scanner.fork());
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }
}
