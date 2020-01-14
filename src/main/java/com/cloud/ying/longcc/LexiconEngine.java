package com.cloud.ying.longcc;

import com.cloud.ying.longcc.generator.*;
import com.cloud.ying.longcc.regular.RegularExpression;
import com.cloud.ying.longcc.regular.RegularParser;

import java.util.*;

public class LexiconEngine {

    HashMap<String, RegularExpression> map;
    RegularParser parser;
    public LexiconEngine(){
        parser=new RegularParser();
        map=new HashMap<>();
    }

    public void defineToken(String tag,String regex) throws Exception{
        map.put(tag,parser.parser(regex));
    }

    public void initialize(){
        //压缩字符集
        CharClassTable charClassTable = CompressCharSet();
        //解析NFA
        NFAModel nfaModel = this.ConvertToNFA(charClassTable);
        DFAModel dfaModel = this.ConvertNFAToDFA(nfaModel,charClassTable);
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
        Collections.sort(array,(a,b)->a.size()-b.size());

        boolean changed=true;
        while (changed){
            changed=false;

            Iterator iterator_char1 = array.iterator();
            while (iterator_char1.hasNext()){
                Set<Character> s1 =(Set<Character>)iterator_char1.next();
                Iterator iterator_char2 = array.iterator();
                while (iterator_char2.hasNext()){
                    Set<Character> s2 =(Set<Character>)iterator_char2.next();
                    if(s2.equals(s1)) continue;

                    Set<Character> s3 =Intersection(s1,s2);
                    if(s3.size()>0){
                        changed=true;
                        array.add(s3);
                        s1.removeAll(s3);
                        s2.removeAll(s3);
                        array.remove(new HashSet<>());
                        Collections.sort(array,(a,b)->a.size()-b.size());
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
            model.getEntryEdge().getTargetState().setTag(item.getKey());
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




}
