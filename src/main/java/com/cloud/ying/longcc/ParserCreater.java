package com.cloud.ying.longcc;

import com.cloud.ying.longcc.parser.ConcatParser;
import com.cloud.ying.longcc.parser.Parser;
import com.cloud.ying.longcc.parser.UnionParser;

public class ParserCreater {
    public Parser Success(){
        return  new Parser();
    }

    public Parser Concat(Parser... parsers){
        return new ConcatParser(parsers);
    }

    public Parser Union(Parser... parsers){
        return  new UnionParser(parsers);
    }
}
