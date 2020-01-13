package com.cloud.ying.longcc.regular;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 表示一个字符
 */
public class RegularCharExpression extends RegularExpression {
    public RegularCharExpression(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    private Character character;

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
}
