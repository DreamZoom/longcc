package com.cloud.ying.longcc.ql;

import com.cloud.ying.longcc.Segment;
import com.cloud.ying.longcc.Token;

public class LinkSegment extends Segment {
    Segment left;
    Token option;

    public LinkSegment(Segment left, Token option, Segment right) {
        this.left = left;
        this.option = option;
        this.right = right;
    }

    Segment right;
}
