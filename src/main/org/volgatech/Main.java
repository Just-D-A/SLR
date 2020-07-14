package main.org.volgatech;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.lexer.domain.Token;
import main.org.volgatech.lexer.io.LexerReader;
import main.org.volgatech.runner.Runner;
import main.org.volgatech.table.Converter;
import main.org.volgatech.table.GrammarReader;


import java.util.ArrayList;

public class Main {



    public static void main(String[] args) throws Exception {
        LexerReader lexerReader = new LexerReader();
        ArrayList<Token> tokenList = lexerReader.start(Globals.PROGRAM_FILE_NAME);
        System.out.println("*** Lexer complited ***");

        GrammarReader grammarReader = new GrammarReader(Globals.GRAMMAR_FILE_NAME);
        ArrayList<ArrayList<String>> grammar = grammarReader.readGrammar();
        System.out.println("*** Grammar success ***");

        Converter converter = new Converter(grammar);
        ArrayList<ArrayList<String>> grammarMap = converter.convertGrammar();
        //System.out.println("*** Table complited ***");
        ArrayList<ArrayList<String>> testTables = new ArrayList<>();
////Create line 0
        ArrayList<String> testString0 = new ArrayList<>();
        testString0.add(";");
        testString0.add("<S>");
        testString0.add("<idlist>");
        testString0.add("<id>");
        testString0.add("real");
        testString0.add(",");
        testString0.add("A|B|C");
        testString0.add("@");
        testTables.add(testString0);
//Create line 1
        ArrayList<String> testString1 = new ArrayList<>();
        testString1.add("<S>");
        testString1.add("ok");
        testString1.add(";");
        testString1.add(";");
        testString1.add("real11");
        testString1.add(";");
        testString1.add(";");
        testString1.add(";");
        testTables.add(testString1);
//Create line 2
        ArrayList<String> testString2 = new ArrayList<>();
        testString2.add("real11");
        testString2.add(";");
        testString2.add("<idlist>21|<idlist>12");
        testString2.add("<id>31");
        testString2.add(";");
        testString2.add(";");
        testString2.add("A|B|C41");
        testString2.add(";");
        testTables.add(testString2);
//Create line 3
        ArrayList<String> testString3 = new ArrayList<>();
        testString3.add("<idlist>21|<idlist>12");
        testString3.add(";");
        testString3.add(";");
        testString3.add(";");
        testString3.add(";");
        testString3.add(",22");
        testString3.add(";");
        testString3.add("R1");
        testTables.add(testString3);
//Create line 4
        ArrayList<String> testString4 = new ArrayList<>();
        testString4.add("<id>31");
        testString4.add(";");
        testString4.add(";");
        testString4.add(";");
        testString4.add(";");
        testString4.add("R3");
        testString4.add(";");
        testString4.add("R3");
        testTables.add(testString4);
//Create line 5
        ArrayList<String> testString5 = new ArrayList<>();
        testString5.add("A|B|C41");
        testString5.add(";");
        testString5.add(";");
        testString5.add(";");
        testString5.add(";");
        testString5.add("R4");
        testString5.add(";");
        testString5.add("R4");
        testTables.add(testString5);
//Create line 6
        ArrayList<String> testString6 = new ArrayList<>();
        testString6.add(",22");
        testString6.add(";");
        testString6.add(";");
        testString6.add("<id>23");
        testString6.add(";");
        testString6.add(";");
        testString6.add("A|B|C41");
        testString6.add(";");
        testTables.add(testString6);
//Create line 7
        ArrayList<String> testString7 = new ArrayList<>();
        testString7.add("<id>23");
        testString7.add(";");
        testString7.add(";");
        testString7.add(";");
        testString7.add(";");
        testString7.add("R2");
        testString7.add(";");
        testString7.add("R2");
        testTables.add(testString7);


        Runner runner = new Runner(testTables, tokenList, grammar);
        Token errorToken = runner.run();
        if (errorToken == null) {
            System.out.println("Complited!");
        } else {
            System.out.println("ERROR");
            errorToken.writeToken();
        }

     /*  System.out.println("*** Table complited ***");
        Runner runner = new Runner(testTables, tokenList, grammar);
        Token errorToken = runner.run();
        if (errorToken == null) {
            System.out.println("Complited!");
        } else {
            System.out.println("ERROR");
            errorToken.writeToken();
        }*/
    }
}