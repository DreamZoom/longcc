package com.cloud.ying.longcc.regular;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    Stack<Character> stack;
    Stack<RegularExpression> stack_expression;

    public RegularExpression parser(String pattern) throws Exception{
        characters = pattern.toCharArray();
        index=0;
        stack = new Stack<>();
        stack_expression =new Stack<>();
        return E();
    }
    public RegularExpression parse(String pattern) throws Exception{
        characters = pattern.toCharArray();
        index=0;
        return AND();
    }


    private RegularExpression E() throws Exception{

        List<RegularExpression> list=new ArrayList<>();
        while (index<characters.length){
            char c = characters[index];
            if(c=='('||c=='['){
                index++;
                stack.push(c);
                RegularExpression expression =E();
                list.add(expression);
                index++;
            }
            else if(c==')'||c==']'){
                Character start = stack.pop();
                if(start=='('){
                    return new RegularConcatenationExpression(list);
                }
                else if(start==']'){
                    return new RegularAlternationExpression(list);
                }
                else{
                    throw new Exception("缺少开始符");
                }
            }
            else if(c=='*'){
                if(list.size()==0) throw new Exception("*运算必须要有前置表达式");
                RegularExpression prev= list.get(list.size()-1);
                RegularExpression expression = new RegularKleeneStarExpression(prev);
                list.set(list.size()-1,expression);
                index++;
            }
            else if(c=='-'){
                if(stack.peek()!='[') throw new Exception("char - must be show in []");
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

    private RegularExpression E2() throws Exception{

        RegularExpression regularExpression;

        List<RegularExpression> list=new ArrayList<>();
        while (index<characters.length){
            char c = characters[index];
            char n = characters[index+1];

        }
        return new RegularConcatenationExpression(list);
    }


    private RegularExpression AND() throws Exception{
        List<RegularExpression> list=new ArrayList<>();
        while (index<characters.length){
            char c = characters[index];
            if(c=='('){
                index++;
                list.add(AND());
            }
            else if(c==')'){
                index++;
            }
            else if(c=='['){
                index++;
                list.add(OR());
                index++;
            }
            else{
                list.add(new RegularCharExpression(c));
                index++;
            }
        }
        return new RegularConcatenationExpression(list);
    }
    private RegularExpression OR() throws Exception{
        List<RegularExpression> list=new ArrayList<>();
        while (index<characters.length){
            list.add(AND());
        }
        return new RegularConcatenationExpression(list);
    }



}
