package com.cloud.ying.longcc.parser;

public class WapperParser extends Parser {
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    private Parser parser;
    public WapperParser(Parser parser){
        this.parser = parser;
    }
    public WapperParser(){

    }

    @Override
    public ParseContext parse(ParseContext context) {
        return parser.parse(context);
    }
}
