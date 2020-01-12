package com.cloud.ying.longcc;

import com.cloud.ying.longcc.parser.Parser;
import com.cloud.ying.longcc.parser.ParseContext;
import com.cloud.ying.longcc.parser.WapperParser;
import com.cloud.ying.longcc.regular.RegularExpression;
import com.cloud.ying.longcc.regular.RegularExpressionBuilder;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

public class LexiconTest {

    @Test
    public void createEngine() {

        long startTime=System.currentTimeMillis();  //获取开始时间

        Lexicon lexicon =new Lexicon();
        RegularExpressionBuilder rb =new RegularExpressionBuilder();

        TokenDefinition options=lexicon.DefineToken(rb.or(rb.s('>'),rb.s('<'),rb.s('='),rb.l(">="),rb.l("<="),rb.l("like")),"option");
        TokenDefinition link =lexicon.DefineToken(rb.or(rb.l("and"),rb.l("or")),"link");


        TokenDefinition field=lexicon.DefineToken(rb.and(rb.or(rb.r('a','z'),rb.r('A','Z')),rb.star(rb.or(rb.r('a','z'),rb.r('A','Z'),rb.r('0','9')))),"field");
        //lexicon.DefineToken(rb.and(rb.r('a','b'),rb.star(rb.r('a','b'))),"field");
        //lexicon.DefineToken(rb.star(rb.r('a','b')),"field");
        RegularExpression integer =  rb.or(rb.and(rb.s('-'),rb.and(rb.r('0','9'),rb.star(rb.r('0','9')))),rb.and(rb.r('0','9'),rb.star(rb.r('0','9'))));

        RegularExpression zh = rb.r((char)0x4E00,(char)0x9FA5);

        TokenDefinition value=lexicon.DefineToken(rb.or(integer,rb.and(rb.s('\''), rb.star(rb.or(rb.r('a','z'),rb.r('A','Z'),rb.r('0','9'),zh)),rb.s('\''))),"value");


        TokenDefinition l=lexicon.DefineToken(rb.s('('),"(");
        TokenDefinition r=lexicon.DefineToken(rb.s(')'),")");
        TokenDefinition skip=lexicon.DefineToken(rb.Literal(" ").build(),"skip");

        FiniteAutomationEngine engine = lexicon.createEngine();



        SourceScanner sourceScanner =lexicon.CreateScanner();



        String source = "(name like '哈哈哈') and (age > 19) ";
        List<Token> tokens = sourceScanner.scan(source);
        tokens.removeIf((item)->{  return  item.getTokenDefinition().getTag().equals("skip");});
        ForkableScanner scanner = new ForkableScanner(tokens);


        ParserCreater parser=new ParserCreater();
        //
        /*
        //F->EG
        //G-> and F
        //G-> or F
        //G->
        //E->NAME OP VALUE;
        //E-(F)
         *
         */


        Parser F;
        WapperParser E,G;

        E=new WapperParser();
        G=new WapperParser();
        F=parser.Concat(E,G).bind(node->{
            if(node.childs.size()==2){
                Node g = node.childs.get(1);
                Node p = g.childs.get(0);
                p.childs.add(node.childs.get(0));
                p.childs.add(g.childs.get(1));
                return p;
            }
            return node.childs.get(0);
        });

        Parser e1,e2;
        e1=parser.Concat(l.asPaser(),F,r.asPaser()).bind((n)->{
            return n.childs.get(1);
        });
        e2 = parser.Concat(field.asPaser(),options.asPaser(),value.asPaser()).bind((node)->{
            Node p = node.childs.get(1);
            p.addNode(node.childs.get(0));
            p.addNode(node.childs.get(2));
            return p;
        });
        E.setParser(parser.Union(e1,e2));


        Parser g1 = parser.Concat(link.asPaser(),F);
        G.setParser(parser.Union(g1,parser.Success()));
        //(age>19 or png=1) and (name like '哈哈哈')

        System.out.println(tokens);
        ParseContext context =new ParseContext(scanner);

        ParseContext result =F.parse(context);

        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
    }

    @Test
    public void testTime(){
        Pattern.matches("","");

        long startTime=System.currentTimeMillis();  //获取开始时间
        int count = 0;
        for (int i = 0; i <1000000 ; i++) {
            count*=1;
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
    }
}