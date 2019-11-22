package com.cloud.ying.longcc.parser;

public class UnionParser extends Parser {
    private Parser[] parsers;
    public  UnionParser(Parser[] parsers){
        this.parsers= parsers;
    }

    @Override
    public ParseContext parse(ParseContext context) {

        for (Parser parser: parsers) {
            ParseContext context_i=context.fork();
            ParseContext result1= parser.parse(context_i);
            if(result1!=null){
                return result1;
            }
        }
        return null;
    }
}
