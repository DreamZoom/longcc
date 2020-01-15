package com.cloud.ying.longcc;

import com.cloud.ying.longcc.parser.Parser;
import com.cloud.ying.longcc.parser.TokenParser;
import com.cloud.ying.longcc.generator.NFAConverter;
import com.cloud.ying.longcc.generator.NFAModel;
import com.cloud.ying.longcc.regular.RegularExpression;

import java.util.function.Function;

public class TokenDefinition {


    public RegularExpression getExpression() {
        return expression;
    }

    public void setExpression(RegularExpression expression) {
        this.expression = expression;
    }

    private RegularExpression expression;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    private Integer index;

    public TokenDefinition(RegularExpression expression,String tag){
        setExpression(expression);
        setTag(tag);
    }


    public NFAModel CreateFiniteAutomatonModel(NFAConverter converter)
    {
        NFAModel nfa = converter.Convert(expression);
        nfa.getTailState().setTokenDefinition(this);
        return nfa;
    }

    @Override
    public String toString() {
        return "#"+index+"@"+tag;
    }

    public Parser asPaser(){
        TokenDefinition definition = this;
        Function<ForkableScanner,LexemeResult> func = scanner -> {
            Token lexeme = scanner.read();
            if(lexeme!=null && lexeme.getTag()==definition.getTag()){
                return  new LexemeResult(lexeme,scanner);
            }
            else{
                return null;
            }
        };
        return new TokenParser(definition);
    }
}
