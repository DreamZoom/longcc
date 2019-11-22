package com.cloud.ying.longcc;

import java.util.ArrayList;
import java.util.List;

public class LexemeResult {

    public LexemeResult(List<Token> tokens, ForkableScanner scanner) {
        this.tokens = tokens;
        this.scanner = scanner;
    }

    public LexemeResult(Token token, ForkableScanner scanner) {
        if(this.tokens==null) this.tokens =new ArrayList<>();
        this.tokens.add(token);
        this.scanner = scanner;
    }

    public LexemeResult(ForkableScanner scanner) {
        if(this.tokens==null) this.tokens =new ArrayList<>();
        this.scanner = scanner;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    private List<Token> tokens;

    public ForkableScanner getScanner() {
        return scanner;
    }

    private ForkableScanner scanner;
}
