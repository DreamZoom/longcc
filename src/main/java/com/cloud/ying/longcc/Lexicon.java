package com.cloud.ying.longcc;

import com.cloud.ying.longcc.generator.*;
import com.cloud.ying.longcc.regular.RegularExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Lexicon {

    public List<TokenDefinition> getTokenDefinitionList() {
        return tokenDefinitionList;
    }

    private  List<TokenDefinition> tokenDefinitionList;

    public  Lexicon(){
        tokenDefinitionList =new ArrayList<>();
    }

    public  TokenDefinition DefineToken(RegularExpression regex, String tag){
        TokenDefinition tokenDefinition =new TokenDefinition(regex,tag);
        tokenDefinition.setIndex(tokenDefinitionList.size());
        tokenDefinitionList.add(tokenDefinition);
        return  tokenDefinition;
    }

//
//    public CharMapping compressCharSet(){
//
//        HashMap<Character,Integer> compactClassTable =new HashMap<>();
//        List<HashSet<Character>> compactable =new ArrayList<>();
//        HashSet<Character> uncompactable =new HashSet<>();
//        for (TokenDefinition tokenDefinition:tokenDefinitionList) {
//            compactable.addAll(tokenDefinition.getExpression().GetCompactableCharSets());
//            uncompactable.addAll(tokenDefinition.getExpression().GetUncompactableCharSet());
//        }
//        HashSet<Character> compactableSets =new HashSet<>();
//
//        for (HashSet<Character> sets:compactable) {
//            compactableSets.addAll(sets);
//        }
//        compactableSets.removeAll(uncompactable);
//        int charIndex = 1;
//
//        for (char c: uncompactable) {
//            compactClassTable.put(c,charIndex++);
//        }
//
//        HashMap<HashSet<Integer>, Integer> compactClassDict = new HashMap<>();
//        for (char c: compactableSets) {
//            HashSet<Integer> keys = new HashSet<>();
//            for (int i = 0; i < compactable.size(); i++)
//            {
//                HashSet<Character> sets = compactable.get(i);
//                if (sets.contains(c)) {keys.add(i);}
//            }
//
//            if(compactClassDict.containsKey(keys)){
//                Integer index = compactClassDict.get(keys);
//                compactClassTable.put(c,index);
//            }
//            else{
//                int index = charIndex++;
//                compactClassDict.put(keys,index);
//                compactClassTable.put(c,index);
//            }
//
//        }
//        return new CharMapping(compactClassTable,charIndex-1);
//    }


    public NFAModel convertToNFA(CharMapping charMapping){
        NFAConverter converter = new NFAConverter();
        NFAState entryState = new NFAState();
        NFAModel lexerNFA = new NFAModel();
        lexerNFA.AddState(entryState);
        for (TokenDefinition token : this.getTokenDefinitionList())
        {
            NFAModel tokenNFA = token.CreateFiniteAutomatonModel(converter);
            entryState.AddEdge(tokenNFA.getEntryEdge());
            lexerNFA.AddStates(tokenNFA.getStates());
        }
        lexerNFA.setEntryEdge(new NFAEdge(entryState));
        return  lexerNFA;
    }


    public DFAModel ConvertNFAToDFA(NFAModel nfaModel,int maxCharIndex){
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


    /*
    创建扫描引擎
     */
    public FiniteAutomationEngine createEngine(){


        CharMapping charMapping = null;//compressCharSet();

        NFAModel nfaModel =  this.convertToNFA(charMapping);
        DFAModel model =this.ConvertNFAToDFA(nfaModel,charMapping.maxIndex);
        List<HashMap<Integer,Integer>> transitionTable = this.createTransitionTable(model,charMapping.maxIndex);

        for (int i = 0; i < transitionTable.size(); i++) {
            System.out.println("DFA#"+(i)+"@"+transitionTable.get(i));
        }

        return new FiniteAutomationEngine(transitionTable,charMapping,model.getStates());
    }

    public SourceScanner CreateScanner(){
        FiniteAutomationEngine engine = this.createEngine();
        SourceScanner sourceScanner =new SourceScanner(engine);
        return  sourceScanner;
    }
}
