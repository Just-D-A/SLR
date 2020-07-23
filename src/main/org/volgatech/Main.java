package main.org.volgatech;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.lexer.domain.Token;
import main.org.volgatech.lexer.io.LexerReader;
import main.org.volgatech.convector.Converter;
import main.org.volgatech.convector.GrammarReader;
import main.org.volgatech.convector.domain.GrammarElement;
import main.org.volgatech.runner.Runner;
import main.org.volgatech.table.Table;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static void printMap(ArrayList<ArrayList<String>> grammarMap){
        for (ArrayList<String> row : grammarMap) {
            for (String corn : row) {
                System.out.format("%-25s",corn);
                //System.out.print(corn + ";");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        LexerReader lexerReader = new LexerReader();
        ArrayList<Token> tokenList = lexerReader.start(Globals.PROGRAM_FILE_NAME);
        System.out.println("*** Lexer complited ***");

        GrammarReader grammarReader = new GrammarReader(Globals.GRAMMAR_FILE_NAME);
        ArrayList<ArrayList<String>> grammar = grammarReader.readGrammar();
        System.out.println("*** Grammar success ***");

        Converter converter = new Converter(grammar);
        ArrayList<ArrayList<GrammarElement>> convertedGrammar = converter.convertGrammar();
        System.out.println("*** Converter complited ***");

        for(ArrayList<GrammarElement> arr: convertedGrammar) {
            for(GrammarElement el: arr ) {
                System.out.print(el.getVal() + " ");
            }
            System.out.println();
        }

        Table table = new Table(convertedGrammar);
        ArrayList<ArrayList<String>> tableMap = table.create();
        printMap(tableMap);

        ArrayList<ArrayList<String>> grammmarConvert = converter.getGrammar();

        System.out.println("*** Table complited ***");
        Runner runner = new Runner(tableMap, tokenList, grammmarConvert);
        Token errorToken = runner.run();
        System.out.println("*** Runner complited ***");
        if (errorToken != null) {
            System.out.println("ERROR");
            errorToken.writeToken();
        }
    }
}