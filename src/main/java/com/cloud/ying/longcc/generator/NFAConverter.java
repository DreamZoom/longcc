package com.cloud.ying.longcc.generator;

import com.cloud.ying.longcc.CharClassTable;
import com.cloud.ying.longcc.regular.*;

import java.util.*;

public class NFAConverter {

    CharClassTable classTable;
    public NFAConverter(CharClassTable classTable){
        assert classTable!=null;
        this.classTable=classTable;
    }

    public NFAModel Convert(RegularExpression expression)
    {
        if(expression instanceof RegularAlternationExpression){
            return  ConvertAlternation((RegularAlternationExpression)expression);
        }
        if(expression instanceof RegularCharExpression){
            return  ConvertChar((RegularCharExpression)expression);
        }
        if(expression instanceof RegularEmptyExpression){
            return  ConvertEmpty((RegularEmptyExpression)expression);
        }
        if(expression instanceof RegularConcatenationExpression){
            return  ConvertConcatenation((RegularConcatenationExpression)expression);
        }
        if(expression instanceof RegularKleeneStarExpression){
            return  ConvertKleeneStar((RegularKleeneStarExpression)expression);
        }
        return ConvertEmpty((RegularEmptyExpression)expression);
    }

    public  NFAModel ConvertAlternation(RegularAlternationExpression expression){
        List<RegularExpression> expressions = expression.getExpressions();

        NFAState head = new NFAState();
        NFAState tail = new NFAState();
        //build edges
        NFAModel alternationNfa = new NFAModel();
        alternationNfa.AddState(head);
        alternationNfa.AddState(tail);

        for (RegularExpression exp:expressions) {
            NFAModel model = Convert(exp);
            head.AddEdge(model.getEntryEdge());
            model.getTailState().AddEmptyEdgeTo(tail);
            alternationNfa.AddStates(model.getStates());
        }

        Iterator iterator = head.getEdges().iterator();

        /**
         * 压缩nfa的边，等价类划分后，nfa的边就有多余的了。
         */
        HashSet<NFAEdge> edges = new HashSet<>();
        while (iterator.hasNext()){
            NFAEdge edge =(NFAEdge)iterator.next();
            if(edge.getTargetState().OnlyEmptyEdge()){
                edge.setTargetState(edge.getTargetState().OnlyEmptyEdgeNextState());
            }
            edges.add(edge);
        }
        head.setEdges(edges);

        //add an empty entry edge
        alternationNfa.setEntryEdge(new NFAEdge(head));
        alternationNfa.setTailState(tail);

        return alternationNfa;
    }

    public  NFAModel ConvertChar(RegularCharExpression expression){
        NFAState tail = new NFAState();
        Character character =expression.getCharacter();
        Integer cls= classTable.GetClass(character);
        NFAEdge entryEdge = new NFAEdge(cls, tail);
        NFAModel symbolNfa = new NFAModel();
        symbolNfa.AddState(tail);
        symbolNfa.setTailState(tail);
        symbolNfa.setEntryEdge(entryEdge);
        return symbolNfa;
    }

    public  NFAModel ConvertEmpty(RegularEmptyExpression expression){
        NFAState tail = new NFAState();
        NFAEdge entryEdge = new NFAEdge(tail);

        NFAModel emptyNfa = new NFAModel();

        emptyNfa.AddState(tail);
        emptyNfa.setTailState(tail);
        emptyNfa.setEntryEdge(entryEdge);

        return emptyNfa;
    }

    public  NFAModel ConvertConcatenation(RegularConcatenationExpression expression){

        List<RegularExpression> expressions = expression.getExpressions();
        NFAModel head = null;
        NFAModel last = null;
        NFAModel concatenationNfa = new NFAModel();
        //首尾相连
        for (RegularExpression exp:expressions) {
            NFAModel model = Convert(exp);
            concatenationNfa.AddStates(model.getStates());
            if(head==null){
                head=model;
                last=model;
            }
            else{
                last.getTailState().AddEdge(model.getEntryEdge());
                last=model;
            }
        }
        concatenationNfa.setEntryEdge(head.getEntryEdge());
        concatenationNfa.setTailState(last.getTailState());
        return concatenationNfa;
    }



    public  NFAModel ConvertKleeneStar(RegularKleeneStarExpression expression){
        NFAModel innerNFA = Convert(expression.getExpression());

        NFAState newTail = new NFAState();
        NFAEdge entry = new NFAEdge(newTail);

        innerNFA.getTailState().AddEmptyEdgeTo(newTail);
        newTail.AddEdge(innerNFA.getEntryEdge());

        NFAModel kleenStarNFA = new NFAModel();

        kleenStarNFA.AddStates(innerNFA.getStates());
        kleenStarNFA.AddState(newTail);
        kleenStarNFA.setEntryEdge(entry);
        kleenStarNFA.setTailState(newTail);

        return kleenStarNFA;
    }
}
