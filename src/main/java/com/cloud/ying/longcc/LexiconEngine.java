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

    public NFAModel ConvertToNFA(){
        NFAConverter converter = new NFAConverter();
        NFAState entryState = new NFAState();
        NFAModel lexerNFA = new NFAModel();
        lexerNFA.AddState(entryState);
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, RegularExpression> item =  (Map.Entry<String, RegularExpression>)iterator.next();
            NFAModel model=converter.Convert(item.getValue());
            entryState.AddEdge(model.getEntryEdge());
            lexerNFA.AddStates(model.getStates());
        }

        lexerNFA.setEntryEdge(new NFAEdge(entryState));
        return  lexerNFA;
    }

    private List<NFAState> GetClosure(List<NFAState> states){
        List<NFAState> list=new ArrayList<>();
        int size = states.size();
        for (int i = 0; i < size; i++) {
            for (NFAEdge edge:states.get(i).getEdges()) {
                if(edge.IsEmpty()){
                    list.add(edge.getTargetState());
                }
            }
        }
        List<NFAState> next=  GetClosure(list);
        list.addAll(next);
        return list;
    }
    public DFAModel ConvertNFAToDFA(NFAModel nfaModel){
        NFAState root = nfaModel.getEntryEdge().getTargetState();
        List<NFAState> list =new ArrayList<>();
        list.add(root);

        DFAState state1 =new DFAState();
        state1.setNfaStates(GetClosure(list));
        ;
        return null;
    }




}
