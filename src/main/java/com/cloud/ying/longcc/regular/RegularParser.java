package com.cloud.ying.longcc.regular;

import java.util.ArrayList;
import java.util.List;

public class RegularParser {

    /**
     * 递归下降算法，解析正则表达式。
     * 正则文法扫描
     * S=E
     * F=char|char-char
     * F=(E) #与运算
     * F=[E] #或运算
     * G=|E
     * G=*
     * E=FG
     */
    Integer index;
    char[] characters;
    public RegularExpression parser(String pattern) throws Exception{
        characters = pattern.toCharArray();
        index=0;
        return E(0);
    }


    private RegularExpression E(int k) throws Exception{

        RegularExpression regularExpression;

        List<RegularExpression> list=new ArrayList<>();
        while (index<characters.length){
            char c = characters[index];
            if(c=='('||c=='['){
                index++;
                RegularExpression expression =E(c=='('?1:2);
                list.add(expression);
                index++;
            }
            else if(c==')'||c==']'){
                if(k==0) throw new Exception("缺少开始符");
                else if(k==1){
                    return new RegularConcatenationExpression(list);
                }
                else if(k==2){
                    return new RegularAlternationExpression(list);
                }
            }
            else if(c=='*'){
                if(list.size()==0) throw new Exception("*运算必须要有前置表达式");
                RegularExpression prev= list.get(list.size()-1);
                RegularExpression expression = new RegularKleeneStarExpression(prev);
                list.set(list.size()-1,expression);
                index++;
            }
//            else if(c=='|'){
//                if(list.size()==0) throw new Exception("*运算必须要有前置表达式");
//                RegularExpression prev= list.get(list.size()-1);
//                RegularExpression expression = E(0);
//
//                list.set(list.size()-1,expression);
//                index++;
//            }
            else if(c=='-'){
                if(k!=2) throw new Exception("char - must be show in []");
                if(list.size()==0) throw new Exception("-运算必须要有前置表达式");
                RegularExpression prev= list.get(list.size()-1);
                if(index+1>=characters.length) new Exception("-运算必须要有后置表达式");
                char min=characters[index-1];
                char max=characters[++index];
                for (int i = min+1; i <= max; i++) {
                    RegularExpression expression =new  RegularCharExpression((char)i);
                    list.add(expression);
                }
                index++;

            }
            else{
                RegularExpression expression =new  RegularCharExpression(c);
                list.add(expression);
                index++;
            }
        }
        return new RegularConcatenationExpression(list);
    }


    /**
     * S' = E
     * C = char
     * F = C-C
     * E = CE
     * E = -C
     * E = (E)
     * E = [F*]
     * G = |E
     * G = *
     * E = GE
     */
    public RegularExpression parse(String pattern){
        characters = pattern.toCharArray();
        index=0;
        return S();
    }

    public RegularExpression S(){
        char c = characters[index];
        RegularExpression expression;
        if(c=='('){
            index++;
            expression=S();
            index++;
        }
        else if(c=='['){
            index++;
            expression=S();
            index++;
        }
        else if(c=='-')
        {
            index++;
            expression=S();
        }
        else{
            expression = C();
        }
        return expression;
    }

    public RegularExpression C(){
        char c = characters[index++];
        RegularExpression expression =new  RegularCharExpression(c);
        return expression;
    }

}
