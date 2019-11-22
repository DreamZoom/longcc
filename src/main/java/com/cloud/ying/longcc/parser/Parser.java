package com.cloud.ying.longcc.parser;

import com.cloud.ying.longcc.Node;

import java.util.function.Function;

public class Parser {

    protected Function<Node,Node> convert =null;
    public ParseContext parse(ParseContext context){
        context.setNode(null);
        return context;
    }

    public Parser bind(Function<Node,Node> convert){
        this.convert=convert;
        return this;
    }
}
