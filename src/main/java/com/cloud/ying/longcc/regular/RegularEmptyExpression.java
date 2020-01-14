package com.cloud.ying.longcc.regular;

import java.util.HashSet;
import java.util.Set;

public class RegularEmptyExpression extends  RegularExpression {
    @Override
    public Set<Character> GetCompressibleCharSet() {
        Set<Character> list = new HashSet<>();
        return list;
    }

    @Override
    public Set<Character> GetIncompressibleCharSet() {
        Set<Character> list = new HashSet<>();
        return list;
    }

    @Override
    public Set<Set<Character>> GetListCharSet() {
        return null;
    }
}
