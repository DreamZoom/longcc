package com.cloud.ying.longcc.regular;

import java.util.List;
import java.util.Set;

public abstract class RegularExpression {
    /**
     * 可压缩的字符集
     * @return
     */
    public abstract Set<Character> GetCompressibleCharSet();

    /**
     * 不可压缩的字符集
     * @return
     */
    public abstract Set<Character> GetIncompressibleCharSet();

    public abstract Set<Set<Character>> GetListCharSet();

    public boolean IsSymbol(){
        return false;
    }

    /**
     * 获取符号 must used if is symbol
     * @return
     */
    public Character GetSymbol(){
        return null;
    }
}
