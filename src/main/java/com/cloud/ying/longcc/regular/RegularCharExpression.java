package com.cloud.ying.longcc.regular;

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
}
