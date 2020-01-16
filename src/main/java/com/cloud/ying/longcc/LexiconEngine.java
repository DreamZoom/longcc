package com.cloud.ying.longcc;

import com.cloud.ying.longcc.generator.*;
import com.cloud.ying.longcc.regular.RegularExpression;
import com.cloud.ying.longcc.regular.RegularParser;

import java.util.*;
import java.util.function.Predicate;

public class LexiconEngine {

    List<TokenDefinition> tokenDefinitions;
    RegularParser parser;
    List<HashMap<Integer,Integer>> stateTransitionTable;
    CharClassTable charClassTable;
    DFAModel dfaModel;
    //终结符
    String terminator;
    HashMap<String,Grammar> grammars;
    public LexiconEngine(){
        parser=new RegularParser();
        tokenDefinitions=new ArrayList<>();
        terminator="\r\n";
        grammars =new HashMap<>();
    }

    public void defineToken(String tag,String regex) throws Exception{
        tokenDefinitions.add(new TokenDefinition(parser.parser(regex),tag));
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
    public Grammar defineGrammar(String tag,String[] ...expressions){
        Grammar grammar=new Grammar(expressions);
        grammars.put(tag,grammar);
        return grammar;
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


    public Segment parse(String expression) throws Exception{
        List<Token> tokens =this.scan(expression);
        ParserContext context =new ParserContext(tokens,0);
        return this.parse(context,"G");
    }

    /**
     * 或解析
     * @param context
     * @param tag
     * @return
     * @throws Exception
     */
    private Segment parse(ParserContext context, String tag) throws Exception{
        Grammar grammar = grammars.get(tag);
        Segment segment=null;
        for (int i = 0; i <grammar.segments.length ; i++) {
            ParserContext temp =context.fork();
            if(parse(temp,grammar.segments[i])){
                //反向传播index
                context.setIndex(temp.getIndex());
                //System.out.println(tag+"="+context.tokens.subList(context.index,temp.index));
                if(grammar.parser!=null){
                    segment=grammar.parser.parse(temp.args.toArray());
                }
                return segment;
            }
        }
        return null;
    }

    /**
     * 匹配一个句子 并返回匹配的数据
     * @param context 上下文
     * @param expression 表达式
     * @return true 与expression相匹配
     * @throws Exception
     */
    private boolean parse(ParserContext context,String[] expression) throws Exception{
        boolean matched=true;
        int i=0;
        while (matched && i<expression.length){
            String tag = expression[i];
            if(grammars.containsKey(tag)){
                Segment segment = parse(context,tag);
                if(segment!=null){
                    matched=true;
                    context.args.add(segment);
                }
            }
            else{
                if(tag.indexOf('*')>=0){
                    boolean m =false;
                    String real_tag =tag.replace("*","");
                    while (context.tokens.get(context.index).getTag().equals(real_tag)){
                        context.index++;
                        m=true;
                    }
                    matched=m;
                }
                else{
                    matched =context.tokens.get(context.index).getTag().equals(tag);
                    context.index++;
                }
                if(matched){
                    context.args.add(context.tokens.get(context.index-1));
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
        Iterator iterator = tokenDefinitions.iterator();
        while (iterator.hasNext()){
            TokenDefinition definition =  (TokenDefinition)iterator.next();
            list.addAll(definition.getExpression().GetListCharSet());
        }

        //找公共区间
        List<Set<Character>> array=new ArrayList<>();
        array.addAll(list);
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
        Iterator iterator = tokenDefinitions.iterator();
        while (iterator.hasNext()){
            TokenDefinition definition =  (TokenDefinition)iterator.next();
            NFAModel model=converter.Convert(definition.getExpression());
            model.getTailState().setTag(definition.getTag());
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

        DFAState state1 = NFAUtils.GetClosure(preState1);
        dfaModel.AddDFAState(state1);

        DFAState[] newStates = new DFAState[maxCharIndex+1];

        List<DFAState> states = dfaModel.getStates();

        int p = 1, j = 0;
        while (j <= p)
        {
            DFAState sourceState =states.get(j);

            for (int i = 1; i <= maxCharIndex; i++) {
                newStates[i] = NFAUtils.GetDFAState(sourceState,i);
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
