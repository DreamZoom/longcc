package com.cloud.ying.longcc.ql;

import com.cloud.ying.longcc.Segment;
import com.cloud.ying.longcc.Token;

public class CompareSegment extends Segment {
    Token field;
    Token option;
    Token value;

    public CompareSegment(Token field, Token option, Token value) {
        this.field = field;
        this.option = option;
        this.value = value;
    }
}
