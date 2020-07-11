package main.org.volgatech;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.lexer.domain.Token;
import main.org.volgatech.lexer.io.LexerReader;
import main.org.volgatech.table.Converter;
import main.org.volgatech.table.GrammarReader;
import main.org.volgatech.table.domain.Method;
import main.org.volgatech.table.domain.MethodList;
import main.org.volgatech.table.domain.Table;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static  boolean ifCornerExist(ArrayList<ArrayList<String>> grammarMap, String checkCorn){
//                        System.out.println(checkCorn);
        boolean newGram = true;
        for (ArrayList<String> row : grammarMap) {
            if(checkCorn.equals(row.get(0))){
                return true;
            }
        }
        return false;
    }

    private static void searchNewObj(ArrayList<ArrayList<String>> grammarMap, ArrayList<ArrayList<String>> grammar, String cornId, String corn, boolean is){
        int k = 0;
        for(ArrayList<String> strArr: grammar) {
            int j = 0;
            for(String str: strArr) {
                if (j > 0){
                    String checkCorn = str + (k+1) + (j);

                    if(!ifCornerExist(grammarMap, checkCorn)){
                        ArrayList<String> newRow = new ArrayList<>();
                        newRow.add(checkCorn);
                        grammarMap.get(0).add(str);
                        grammarMap.get(k+1).add(checkCorn);
                        grammarMap.add(newRow);
                    }
                }
                j++;
            }
            k++;
        }
    }

    public static void main(String[] args) throws Exception {
        LexerReader lexerReader = new LexerReader();
        ArrayList<Token> tokenList = lexerReader.start(Globals.PROGRAM_FILE_NAME);
        System.out.println("*** Lexer complited ***");

        GrammarReader grammarReader = new GrammarReader(Globals.GRAMMAR_FILE_NAME);
        ArrayList<ArrayList<String>> grammar = grammarReader.readGrammar();
        System.out.println("*** Grammar success ***");

        for(ArrayList<String> strArr: grammar) {
            for(String str: strArr) {
                System.out.println(str);
            }
            System.out.println("_____________");
        }


        ArrayList<ArrayList<String>> grammarMap = new ArrayList<ArrayList<String>>();
        ArrayList<String> upPartOfTable = new ArrayList<>();
        upPartOfTable.add(" ");
        upPartOfTable.add("S");
        grammarMap.add(upPartOfTable);
        ArrayList<String> leftPartOfTable = new ArrayList<>();
        leftPartOfTable.add("S");
        leftPartOfTable.add("ok");
        grammarMap.add(leftPartOfTable);
        int i = 0;



        while(i < grammarMap.size()) {
            int k = 0;
            for(ArrayList<String> strArr: grammar) {
                int j = 0;
                for(String str: strArr) {
                    if (j > 0){
                        String checkCorn = str + (k+1) + (j);

                        if(!ifCornerExist(grammarMap, checkCorn)){
                            ArrayList<String> newRow = new ArrayList<>();
                            newRow.add(checkCorn);
                            grammarMap.get(0).add(str);
                            grammarMap.get(k+1).add(checkCorn);
                            grammarMap.add(newRow);

                            if(strArr.size() - 1 > j){
                                searchNewObj(grammarMap, grammar, checkCorn, str, true);
                            } else {
                                searchNewObj(grammarMap, grammar, checkCorn, str, true);
                            }





                        }
                    }
                    j++;
                }
                k++;
            }
            i++;
        }
        for (ArrayList<String> row : grammarMap) {
            for (String corn : row) {
                System.out.print(corn + '\t');
            }
            System.out.println();
        }
     /*   Converter converter = new Converter(grammar);
        ArrayList<MethodList> methodList = converter.convertGrammar();


       Table table = new Table(methodList);
        ArrayList<Method> methods = table.createMethodsArr();

        table.writeTible();
*/

   /*     System.out.println("*** Table complited ***");
        Runner runner = new Runner(tokenList, methods);
        Token errorToken = runner.run();
        if (errorToken == null) {
            System.out.println("Complited!");
        } else {
            System.out.println("ERROR");
            errorToken.writeToken();
        }*/


    }
}