package com.cloud.ying.longcc.parser;

import com.cloud.ying.longcc.Node;
import com.cloud.ying.longcc.Token;
import com.cloud.ying.longcc.TokenDefinition;

public class TokenParser extends Parser {

    private TokenDefinition tokenDefinition;

    public TokenParser(TokenDefinition token){
        this.tokenDefinition = token;
    }

    @Override
    public ParseContext parse(ParseContext context) {
        Token lexeme = context.getScanner().read();
        if(lexeme==null){
            return null;
        }
        if(lexeme.getTag()==null|| lexeme.getTag()!=tokenDefinition.getTag()) return null;

        context.setNode(new Node(lexeme));
        return context;
    }
}
