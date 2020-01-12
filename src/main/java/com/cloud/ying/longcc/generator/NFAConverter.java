package com.cloud.ying.longcc.generator;

import com.cloud.ying.longcc.regular.*;

public class NFAConverter {

    CharMapping charMapping;
    public  NFAConverter(CharMapping charMapping){
        this.charMapping = charMapping;
    }

    public NFAModel Convert(RegularExpression expression)
    {
        if(expression instanceof RegularAlternationExpression){
            return  ConvertAlternation((RegularAlternationExpression)expression);
        }
        if(expression instanceof RegularSymbolExpression){
            return  ConvertSymbol((RegularSymbolExpression)expression);
        }
        if(expression instanceof RegularEmptyExpression){
            return  ConvertEmpty((RegularEmptyExpression)expression);
        }
        if(expression instanceof RegularConcatenationExpression){
            return  ConvertConcatenation((RegularConcatenationExpression)expression);
        }
        if(expression instanceof RegularAlternationCharSetExpression){
            return  ConvertAlternationCharSet((RegularAlternationCharSetExpression)expression);
        }
        if(expression instanceof RegularStringLiteralExpression){
            return  ConvertStringLiteral((RegularStringLiteralExpression)expression);
        }
        if(expression instanceof RegularKleeneStarExpression){
            return  ConvertKleeneStar((RegularKleeneStarExpression)expression);
        }
        return ConvertEmpty((RegularEmptyExpression)expression);
    }

    public  NFAModel ConvertAlternation(RegularAlternationExpression expression){
        RegularExpression[] regularExpressions = expression.getExpressions();

        NFAState head = new NFAState();
        NFAState tail = new NFAState();
        //build edges
        NFAModel alternationNfa = new NFAModel();
        alternationNfa.AddState(head);
        alternationNfa.AddState(tail);

        for (RegularExpression exp:regularExpressions) {
            NFAModel model = Convert(exp);
            head.AddEdge(model.getEntryEdge());
            model.getTailState().AddEmptyEdgeTo(tail);
            alternationNfa.AddStates(model.getStates());
        }
        //add an empty entry edge
        alternationNfa.setEntryEdge(new NFAEdge(head));
        alternationNfa.setTailState(tail);

        return alternationNfa;
    }

    public  NFAModel ConvertSymbol(RegularSymbolExpression expression){
        NFAState tail = new NFAState();

        NFAEdge entryEdge = new NFAEdge(charMapping.getCharIndex(expression.getSymbol()), tail);
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

        RegularExpression[] regularExpressions = expression.getExpressions();
        NFAModel head = null;
        NFAModel last = null;
        NFAModel concatenationNfa = new NFAModel();
        //首尾相连
        for (RegularExpression exp:regularExpressions) {
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

    public  NFAModel ConvertAlternationCharSet(RegularAlternationCharSetExpression expression){
        NFAState head = new NFAState();
        NFAState tail = new NFAState();
        //build edges

        NFAModel charSetNfa = new NFAModel();

        charSetNfa.AddState(head);

        for (char c : expression.getCharSet()) {
            NFAEdge symbolEdge = new NFAEdge(charMapping.getCharIndex(c), tail);
            head.AddEdge(symbolEdge);
        }
        charSetNfa.AddState(tail);

        //add an empty entry edge
        charSetNfa.setEntryEdge(new NFAEdge(head));
        charSetNfa.setTailState(tail);

        return charSetNfa;
    }

    public  NFAModel ConvertStringLiteral(RegularStringLiteralExpression expression){
        NFAModel literalNfa = new NFAModel();

        NFAState lastState = null;

        for(char symbol : expression.getLiteral().toCharArray())
        {
            NFAState symbolState = new NFAState();

            NFAEdge symbolEdge = new NFAEdge(charMapping.getCharIndex(symbol), symbolState);

            if (lastState != null)
            {
                lastState.AddEdge(symbolEdge);
            }
            else
            {
                literalNfa.setEntryEdge(symbolEdge);
            }

            lastState = symbolState;

            literalNfa.AddState(symbolState);
        }

        literalNfa.setTailState(lastState);
        return literalNfa;
    }

    public  NFAModel ConvertKleeneStar(RegularKleeneStarExpression expression){
        NFAModel innerNFA = Convert(expression.getInnerExpression());

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
