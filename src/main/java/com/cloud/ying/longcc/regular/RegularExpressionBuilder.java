package com.cloud.ying.longcc.regular;
import java.util.HashSet;

public class RegularExpressionBuilder {

    private RegularExpression regularExpression;

    public  RegularExpressionBuilder Symbol(char c)
    {
        regularExpression = new RegularSymbolExpression(c);
        return this;
    }

    public RegularExpressionBuilder Many()
    {
        if (!(regularExpression instanceof RegularKleeneStarExpression))
        {
            regularExpression = new RegularKleeneStarExpression(regularExpression);
        }

        return this;
    }

    public RegularExpressionBuilder Concat(RegularExpression follow)
    {
        regularExpression = new RegularConcatenationExpression(regularExpression, follow);
        return this;
    }

    public RegularExpressionBuilder Union(RegularExpression other)
    {
        if (regularExpression.equals(other))
        {
            return this;
        }
        regularExpression = new RegularAlternationExpression(regularExpression, other);
        return this;
    }

    public  RegularExpressionBuilder Literal(String literal)
    {
        regularExpression = new RegularStringLiteralExpression(literal);
        return this;
    }

    public  RegularExpressionBuilder CharSet(String charSet)
    {
        HashSet<Character> characters =new HashSet<>();
        for (char c : charSet.toCharArray()){
            characters.add(c);
        }
        regularExpression = new RegularAlternationCharSetExpression(characters);
        return this;
    }



    public  RegularExpressionBuilder Empty()
    {
        regularExpression =new RegularEmptyExpression();
        return this;
    }

    //extended operations

    public RegularExpressionBuilder Many1()
    {
        this.Concat(regularExpression);
        this.Many();
        return this;
    }

    public RegularExpressionBuilder Optional()
    {
        this.Empty();
        this.Union(regularExpression);
        return this;
    }

    public RegularExpressionBuilder Range(char min, char max)
    {
        HashSet<Character> characters =new HashSet<>();
        for (char c = min; c <= max; c++)
        {
            characters.add(c);
        }
        regularExpression = new RegularAlternationCharSetExpression(characters);
        return  this;
    }

    public RegularExpression build(){
        return  regularExpression;
    }



    public  RegularExpression or(RegularExpression... expressions){
        RegularExpression  regularExpression = null;
        for (RegularExpression expression:expressions) {
            if(regularExpression==null){
                regularExpression = expression;
            }
            else{
                regularExpression = new RegularAlternationExpression(regularExpression,expression);
            }
        }
        return regularExpression;
    }


    public  RegularExpression and(RegularExpression... expressions){
        RegularExpression  regularExpression = null;
        for (RegularExpression expression:expressions) {
            if(regularExpression==null){
                regularExpression = expression;
            }
            else{
                regularExpression = new RegularConcatenationExpression(regularExpression,expression);
            }
        }
        return regularExpression;
    }

    public  RegularExpression star(RegularExpression expression){
        return  new RegularKleeneStarExpression(expression);
    }

    public  RegularExpression s(Character character){
        return  new RegularSymbolExpression(character);
    }
    public  RegularExpression l(String literal){
        return  new RegularStringLiteralExpression(literal);
    }

    public RegularExpression r(char min, char max){
        HashSet<Character> characters =new HashSet<>();
        for (char c = min; c <= max; c++)
        {
            characters.add(c);
        }
        return new RegularAlternationCharSetExpression(characters);
    }
    public RegularExpression r(String characters){
        HashSet<Character> characterSets =new HashSet<>();
        for (char c : characters.toCharArray()){
            characterSets.add(c);
        }
        return new RegularAlternationCharSetExpression(characterSets);
    }

}
