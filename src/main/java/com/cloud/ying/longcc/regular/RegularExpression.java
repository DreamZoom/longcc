package com.cloud.ying.longcc.regular;

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
}
