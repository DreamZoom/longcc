package com.cloud.ying.longcc;

import com.cloud.ying.longcc.generator.*;
import com.cloud.ying.longcc.regular.RegularExpression;
import com.cloud.ying.longcc.regular.RegularParser;

import java.util.*;
import java.util.function.Predicate;

public class LexiconEngine {

    HashMap<String, RegularExpression> map;
    RegularParser parser;
    List<HashMap<Integer,Integer>> stateTransitionTable;
    CharClassTable charClassTable;
    DFAModel dfaModel;
    //终结符
    String terminator;
    HashMap<String,String[][]> grammars;
    public LexiconEngine(){
        parser=new RegularParser();
        map=new HashMap<>();
        terminator="\r\n";
        grammars =new HashMap<>();
    }

    public void defineToken(String tag,String regex) throws Exception{
        map.put(tag,parser.parser(regex));
    }
    public void defineTerminator(String regex){
        terminator = regex;
    }


    /**
     * 定义文法
     * like E=E+i
     * @param tag 非终结符
     * @param expressions 文法表达式
     */
    public void defineGrammar(String tag,String[] ...expressions){
        grammars.put(tag,expressions);
    }

    public void initialize() throws Exception{

        //定义终结符
        this.defineToken("end",terminator);
        //压缩字符集
        charClassTable = CompressCharSet();
        //解析NFA
        NFAModel nfaModel = this.ConvertToNFA(charClassTable);
        dfaModel = this.ConvertNFAToDFA(nfaModel,charClassTable);
        //build
        stateTransitionTable =createTransitionTable(dfaModel,charClassTable.maxClass);
    }

    public List<Token> scan(String expression) throws Exception{
        //默认添加终结符
        expression+=terminator;

        List<Token> tokens =new ArrayList<>();
        char[] chars = expression.toCharArray();
        int index=0,state=1,last_state=1;
        StringBuffer word =new StringBuffer();
        while (index<chars.length){
             Map<Integer,Integer> map =stateTransitionTable.get(state);
             Character character = chars[index];
             Integer charClass = charClassTable.GetClass(character);
             last_state=state;
             state=map.get(charClass);
             if(state==0){
                 if(word.toString().equals("")){
                     throw new Exception("Invalid Token:"+character);
                 }
                 DFAState last = dfaModel.getStates().get(last_state);
                 tokens.add(new Token(word.toString(),last.getTag()));
                 word.setLength(0);
                 state=1;
             }
             else{
                 word.append(character);
                 index++;
             }
        }
        return  tokens;
    }


    private int matchedSegment(List<Token> tokens,int index,String[] segment){
        boolean matched=true;
        int i=0;
        while (matched && i<segment.length){
            matched =tokens.get(index++).getTag().equals(segment[i]);
            i++;
        }
        if(matched){
            return i;
        }
        return -1;
    }
    public void parse(String expression) throws Exception{
        List<Token> tokens =this.scan(expression);
        ParserContext context =new ParserContext(tokens,0);

        System.out.println(this.parse(context,"G"));
    }

    /**
     * 或解析
     * @param context
     * @param tag
     * @return
     * @throws Exception
     */
    private boolean parse(ParserContext context, String tag) throws Exception{
        String[][] segments = grammars.get(tag);
        for (int i = 0; i <segments.length ; i++) {
            ParserContext temp =context.fork();
            if(parse(temp,segments[i])){
                //反向传播index
                context.setIndex(temp.getIndex());
                return true;
            }
        }
        return false;
    }

    /**
     * 与解析
     * @param context
     * @param segment
     * @return
     * @throws Exception
     */
    private boolean parse(ParserContext context,String[] segment) throws Exception{
        boolean matched=true;
        int i=0;
        while (matched && i<segment.length){
            String tag = segment[i];
            if(grammars.containsKey(tag)){
                matched = parse(context,tag);
            }
            else{
                if(tag.indexOf('*')>=0){
                    boolean end =false;
                    String real_tag =tag.replace("*","");
                    while (context.tokens.get(context.index).getTag().equals(real_tag)){
                        context.index++;
                    }
                }
                else{
                    matched =context.tokens.get(context.index).getTag().equals(tag);
                    context.index++;
                }
            }
            i++;
        }
        return  matched;
    }

    /**
     * 求两个集合的交集
     * @param s1
     * @param s2
     * @return
     */
    public Set<Character> Intersection(Set<Character> s1,Set<Character> s2){
        Iterator iterator_char = s1.iterator();
        Set<Character> s = new HashSet<>();
        while (iterator_char.hasNext()){
            Character character = (Character)iterator_char.next();
            if(s2.contains(character)){
                s.add(character);
            }
        }
        return s;
    }

    /**
     * 压缩字符集
     */
    public CharClassTable CompressCharSet(){
        Set<Set<Character>> list=new HashSet<>();
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, RegularExpression> item =  (Map.Entry<String, RegularExpression>)iterator.next();
            list.addAll(item.getValue().GetListCharSet());
        }

        //找公共区间

        List<Set<Character>> array=new ArrayList<>();
        array.addAll(list);
//        Collections.sort(array,(a,b)->a.size()-b.size());

        boolean changed=true;
        while (changed){
            changed=false;

            Iterator iterator_char1 = array.iterator();
            while (iterator_char1.hasNext()){
                Set<Character> s1 =(Set<Character>)iterator_char1.next();
                Iterator iterator_char2 = array.iterator();
                while (iterator_char2.hasNext()){
                    Set<Character> s2 =(Set<Character>)iterator_char2.next();
                    if(s2==s1) {
                        //引用相等即为同一个
                        continue;
                    }

                    Set<Character> s3 =Intersection(s1,s2);
                    if(s3.size()>0){
                        changed=true;
                        array.add(s3);
                        s1.removeAll(s3);
                        s2.removeAll(s3);
                        if(s1.size()==0){
                            array.remove(s1);
                        }
                        if(s2.size()==0){
                            array.remove(s2);
                        }
                        //Collections.sort(array,(a,b)->a.size()-b.size());
                        break;
                    }
                }
                if(changed){
                    break;
                }
            }

        }


        Map<Integer,Set<Character>> map= new HashMap<>();

        Iterator iterator_char6 = array.iterator();
        int index=1;
        while (iterator_char6.hasNext()){
            Set<Character> s6 =(Set<Character>)iterator_char6.next();
            map.put(index++,s6);
        }

        CharClassTable charClassTable =new CharClassTable(map,index-1);
        return charClassTable;
    }

    public NFAModel ConvertToNFA(CharClassTable classTable){
        NFAConverter converter = new NFAConverter(classTable);
        NFAState entryState = new NFAState();
        NFAModel lexerNFA = new NFAModel();
        lexerNFA.AddState(entryState);
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, RegularExpression> item =  (Map.Entry<String, RegularExpression>)iterator.next();
            NFAModel model=converter.Convert(item.getValue());
            model.getTailState().setTag(item.getKey());
            entryState.AddEdge(model.getEntryEdge());
            lexerNFA.AddStates(model.getStates());
        }

        lexerNFA.setEntryEdge(new NFAEdge(entryState));
        return  lexerNFA;
    }


    public DFAModel ConvertNFAToDFA(NFAModel nfaModel,CharClassTable charClassTable){

        Integer maxCharIndex = charClassTable.maxClass;
        DFAModel dfaModel =new DFAModel();

        DFAState state0 = new DFAState();
        dfaModel.AddDFAState(state0);


        DFAState preState1 = new DFAState();
        preState1.getNfaStates().add(nfaModel.getEntryEdge().getTargetState());

        DFAState state1 = dfaModel.GetClosure(preState1);
        dfaModel.AddDFAState(state1);

        DFAState[] newStates = new DFAState[maxCharIndex+1];

        List<DFAState> states = dfaModel.getStates();

        int p = 1, j = 0;
        while (j <= p)
        {
            DFAState sourceState =states.get(j);

            for (int i = 1; i <= maxCharIndex; i++) {
                newStates[i] = dfaModel.GetDFAState(sourceState,i);
            }
            for (int c = 1; c <= maxCharIndex; c++){
                DFAState e = newStates[c];

                boolean isSetExist = false;
                for (int i = 0; i <= p; i++)
                {
                    if (e.getNfaStates().equals(states.get(i).getNfaStates()))
                    {
                        DFAEdge newEdge = new DFAEdge(c, states.get(i));
                        sourceState.addEdge(newEdge);
                        isSetExist = true;
                    }
                }
                if(!isSetExist){
                    p += 1;
                    dfaModel.AddDFAState(e);
                    DFAEdge newEdge = new DFAEdge(c, e);
                    sourceState.addEdge(newEdge);
                }
            }
            j+=1;
        }
        return  dfaModel;
    }

    public List<HashMap<Integer,Integer>> createTransitionTable(DFAModel dfaModel,int maxCharIndex){
        List<HashMap<Integer,Integer>> transitionTable=new ArrayList<>();
        List<DFAState> states = dfaModel.getStates();
        for (DFAState state : states){

            HashMap<Integer,Integer> map = new HashMap<>();
            for (int c = 1; c <= maxCharIndex; c++){
                int stateIndex = 0;
                for(DFAEdge edge: state.getEdges()){
                    if(edge.getSymbol()==c){
                        stateIndex = edge.getTargetState().getIndex();
                    }
                }
                map.put(c,stateIndex);
            }
            transitionTable.add(map);
        }

        return transitionTable;
    }


}
