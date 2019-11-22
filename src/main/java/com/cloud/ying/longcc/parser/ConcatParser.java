package com.cloud.ying.longcc.parser;

import com.cloud.ying.longcc.Node;

public class ConcatParser extends  Parser {
    private Parser[] parsers;
    public  ConcatParser(Parser[] parsers){
        this.parsers= parsers;
    }

    @Override
    public ParseContext parse(ParseContext context) {

        ParseContext result=context;
        Node node=new Node();
        for (Parser parser: parsers) {
            result= parser.parse(result);
            if(result==null) {
                return  null;
            }
            if(result.getNode()!=null){
                node.addNode(result.getNode());
            }
        }
        if(convert!=null){
            node = convert.apply(node);
        }
        result.setNode(node);
        return result;
    }
}
