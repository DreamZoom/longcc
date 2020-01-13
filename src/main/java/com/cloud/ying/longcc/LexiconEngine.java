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
        //解析
        NFAModel nfaModel = this.ConvertToNFA();

        //压缩字符集
        CompressCharSet();

    }

    /**
     * 压缩字符集
     */
    public void CompressCharSet(){
        HashSet<Character> incompressibleCharSet = new HashSet<>();
        HashSet<Character> compressibleCharSet = new HashSet<>();
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, RegularExpression> item =  (Map.Entry<String, RegularExpression>)iterator.next();
            incompressibleCharSet.addAll(item.getValue().GetIncompressibleCharSet());
            compressibleCharSet.addAll(item.getValue().GetCompressibleCharSet());
        }

        compressibleCharSet.removeAll(incompressibleCharSet);


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


    public DFAModel ConvertNFAToDFA(NFAModel nfaModel){
        NFAState root = nfaModel.getEntryEdge().getTargetState();


        DFAState state1 =new DFAState();

        List<NFAState> list =new ArrayList<>();
        list.add(root);
        List<NFAState> states =NFAUtils.GetClosure(list);
        list.addAll(states);
        state1.getNfaStates().addAll(list);

        return null;
    }




}
