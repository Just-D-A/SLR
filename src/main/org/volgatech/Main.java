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

    private static ArrayList<Integer> rowArray = new ArrayList<>();
    private static ArrayList<Integer> colArray = new ArrayList<>();

    private static boolean isTerminal = false;
    private static String word  = "";

    private static  boolean ifCornerExist(ArrayList<ArrayList<String>> grammarMap, String checkCorn){
        for (ArrayList<String> row : grammarMap) {
            if(checkCorn.equals(row.get(0))){
                return true;
            }
        }
        return false;
    }

    private static Integer cornerId(ArrayList<ArrayList<String>> grammarMap, String checkCorn){
        int i = 0;
        for (ArrayList<String> row : grammarMap) {
            if(checkCorn.equals(row.get(0))){
                return i;
            }
            i++;
        }
        return -1;
    }

    private static  boolean ifRowExist(ArrayList<ArrayList<String>> grammarMap, String checkCorn){
        for (String row : grammarMap.get(0)) {
            if(checkCorn.equals(row)){
                return true;
            }
        }
        return false;
    }
    private static  boolean ifLeftGrammarExist(ArrayList<ArrayList<String>> grammar, String checkCorn){
        for (String row : grammar.get(0)) {
            if(checkCorn.equals(row)){
                return true;
            }
        }
        return false;
    }

    private static  boolean ifTerminal(String checkCorn){
        if(checkCorn.contains("<") && checkCorn.contains(">")){
            return  false;
        }
        return true;
    }

    static <T> List<Integer> indexOfAll(T obj, List<T> list) {
        final List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (obj.equals(list.get(i))) {
                indexList.add(i);
            }
        }
        return indexList;
    }

    private static  void fillTable(ArrayList<ArrayList<String>> grammarMap){
        for (ArrayList<String> row : grammarMap) {
            if(row.size() < grammarMap.get(0).size()){
                while(row.size() < grammarMap.get(0).size()){
                    row.add(";");
                }
            }
        }
    }
    private static ArrayList<String> findRightPlaces(ArrayList<ArrayList<String>> grammar, String searchWord, ArrayList<ArrayList<String>> grammarMap){
        int a = 0;
        ArrayList<String> resultSearches = new ArrayList<>();
        for (ArrayList<String> tableRow : grammar) {
            for(Integer id : indexOfAll(searchWord,tableRow)){
                if(id > 0){
                    String newVal = searchWord + (a+1) + (id);
                    if(!ifCornerExist(grammarMap, newVal)){
                        resultSearches.add(newVal);

                    }
                }
            }
            a++;
        }
        return resultSearches;
    }

    private static void printMap(ArrayList<ArrayList<String>> grammarMap){
        for (ArrayList<String> row : grammarMap) {
            for (String corn : row) {
                System.out.format("%-25s",corn);
            }
            System.out.println();
        }
    }


    private static boolean findNextValue(ArrayList<ArrayList<String>> grammar, ArrayList<ArrayList<String>> grammarMap, ArrayList<String> nonTerminal, String rightPlace){
        int j = 0;
        for(ArrayList<String> strArr: grammar) {
            if((strArr.size() == 2) && (!ifTerminal(strArr.get(1))) && (strArr.get(0).equals(rightPlace.substring(0, rightPlace.length()-2)))){
                nonTerminal.add(strArr.get(1)+(j+1)+""+1);
                findNextValue(grammar, grammarMap, nonTerminal, strArr.get(1));
                return true;
            }

            if((strArr.size() == 2) && (ifTerminal(strArr.get(1)))){
                nonTerminal.add(strArr.get(1)+(j+1)+""+1);
                break;
            }
            j++;
        }

        return true;
    }

    private static boolean findNonLoop(ArrayList<ArrayList<String>> grammar, ArrayList<ArrayList<String>> grammarMap, ArrayList<String> nonTerminal, String rightPlace) {
        int j = 0;
        for (ArrayList<String> strArr : grammar) {
            if ((strArr.size() == 2) && (!ifTerminal(strArr.get(1))) && (strArr.get(0).equals(rightPlace.substring(0, rightPlace.length() - 2)))) {
                nonTerminal.add(strArr.get(1) + (j + 1) + "" + 1);
                findNextValue(grammar, grammarMap, nonTerminal, strArr.get(1));
                return true;
            }
            if ((strArr.size() == 2) && (ifTerminal(strArr.get(1))) && rightPlace.equals(strArr.get(0))) {
                nonTerminal.add(strArr.get(1) + (j + 1) + "" + 1);
                break;
            }
            j++;
        }

        return true;
    }

        private static boolean findTerminal(String startVal, ArrayList<ArrayList<String>> grammar, ArrayList<ArrayList<String>> grammarMap, Integer curCol, Integer curRow){
        if(ifTerminal(grammar.get(curRow).get(curCol))){
            if((grammar.get(curRow).size() == 2) &&(curCol == 1)){
                findTerminal(grammar.get(curRow).get(curCol - 1), grammar, grammarMap, curCol, curRow - 1);
            }
        } else {
            int a = 0;
            for(ArrayList<String> strArr: grammar) {
                for(Integer id : indexOfAll(startVal,strArr)){

                    if((id > 0) && (id ==(grammar.get(a).size() - 1)) && ifTerminal(grammar.get(a).get(id -1))){
                        word = grammar.get(a).get(id -1);
                        isTerminal = true;
                        return true;
                    }
                }
                a++;
            }

        }
        return false;
    }
    private static String getWord(String word){
        return word.substring(0, word.length() - 2);
    }
    private static boolean searchByRow(ArrayList<ArrayList<String>> grammar, ArrayList<ArrayList<String>> grammarMap, String checkCorn, Integer curRow, Integer curCol){

        if( grammar.get(curRow).size() - 2 >= curCol){
            String nextWord = grammar.get(curRow).get(curCol+1);
            int k = 0;
           // findNextValue(grammar, grammarMap, nextWord, grammarMap.size()-1, nonTerminals);
            if(!ifRowExist(grammarMap, nextWord)){

                grammarMap.get(0).add(nextWord);
                ArrayList<String> rightPlaces = findRightPlaces(grammar, nextWord, grammarMap);
                fillTable(grammarMap);
                grammarMap.get(cornerId(grammarMap, checkCorn)).set(grammarMap.get(0).size()-1, String.join("|", rightPlaces));

                for (String rightVals:  rightPlaces){
                    ArrayList<String> newRow = new ArrayList<>();
                    newRow.add(rightVals);
                    grammarMap.add(newRow);
                    fillTable(grammarMap);
                }

                ArrayList<String> nonTerminals = new ArrayList<>();
                if(!ifTerminal(nextWord)){
                    findNextValue(grammar, grammarMap, nonTerminals, rightPlaces.get(0));
                }

                for(String nonTermstr: nonTerminals) {
                    ArrayList<String> newRow = new ArrayList<>();
                    newRow.add(nonTermstr);
                    if(ifRowExist(grammarMap, getWord(nonTermstr))){
                        grammarMap.get(cornerId(grammarMap, checkCorn)).set(grammarMap.get(0).indexOf(nonTermstr), nonTermstr);
                    } else {
                        grammarMap.get(0).add(getWord(nonTermstr));
                        grammarMap.get(cornerId(grammarMap, checkCorn)).add(nonTermstr);
                    }
                    fillTable(grammarMap);

                    grammarMap.add(newRow);
                }
                for (String str:  rightPlaces){
                    rowArray.add(Integer.parseInt(str.substring(str.length() - 2, str.length() - 1)) -1);
                    colArray.add(Integer.parseInt(str.substring(str.length() - 1)));
                    searchByRow(grammar, grammarMap, str, rowArray.get(rowArray.size()-1), colArray.get(colArray.size()-1));
                }
                for(String str: nonTerminals) {
                    rowArray.add(Integer.parseInt(str.substring(str.length() - 2, str.length() - 1)) - 1);
                    colArray.add(Integer.parseInt(str.substring(str.length() - 1)));
                    searchByRow(grammar, grammarMap, str, rowArray.get(rowArray.size()-1), colArray.get(colArray.size()-1));
                }

            } else {
                ArrayList<String> rightPlaces = findRightPlaces(grammar, nextWord, grammarMap);
                fillTable(grammarMap);
                grammarMap.get(cornerId(grammarMap, checkCorn)).set(grammarMap.get(0).indexOf(rightPlaces.get(0).substring(0, rightPlaces.get(0).length()-2)), String.join("|", rightPlaces));

                for (String rightVals:  rightPlaces){
                    ArrayList<String> newRow = new ArrayList<>();
                    newRow.add(rightVals);
                    grammarMap.add(newRow);
                    fillTable(grammarMap);
                }

                ArrayList<String> nonTerminals = new ArrayList<>();
                if(!ifTerminal(nextWord)){
                    findNextValue(grammar, grammarMap, nonTerminals, rightPlaces.get(0));
                }

                for(String nonTermstr: nonTerminals) {
                    ArrayList<String> newRow = new ArrayList<>();
                    newRow.add(nonTermstr);
                    fillTable(grammarMap);
                    if(ifRowExist(grammarMap, getWord(nonTermstr))){
                        grammarMap.get(cornerId(grammarMap, checkCorn)).set(grammarMap.get(0).indexOf(nonTermstr.substring(0, nonTermstr.length()-2)), nonTermstr);
                    } else {

                        grammarMap.get(0).add(getWord(nonTermstr));
                        grammarMap.get(cornerId(grammarMap, checkCorn)).add(nonTermstr);
                    }
                    fillTable(grammarMap);
                    if(!ifCornerExist(grammarMap, checkCorn)){
                        grammarMap.add(newRow);
                    }
                }
                for (String str:  rightPlaces){
                    rowArray.add(Integer.parseInt(str.substring(str.length() - 2, str.length() - 1)) -1);
                    colArray.add(Integer.parseInt(str.substring(str.length() - 1)));
                    searchByRow(grammar, grammarMap, str, rowArray.get(rowArray.size()-1), colArray.get(colArray.size()-1));
                }
                for(String str: nonTerminals) {
                    rowArray.add(Integer.parseInt(str.substring(str.length() - 2, str.length() - 1)) - 1);
                    colArray.add(Integer.parseInt(str.substring(str.length() - 1)));
                    searchByRow(grammar, grammarMap, str, rowArray.get(rowArray.size()-1), colArray.get(colArray.size()-1));
                }
            }
        } else {

            if(!ifRowExist(grammarMap, "@")){
                grammarMap.get(0).add("@");
                fillTable(grammarMap);
                grammarMap.get(cornerId(grammarMap, checkCorn)).set(grammarMap.get(0).size()-1, "R" + (curRow + 1));
            } else {
                fillTable(grammarMap);
                grammarMap.get(cornerId(grammarMap, checkCorn)).set(grammarMap.get(0).indexOf("@"), "R" + (curRow + 1));
            }
        }
        ArrayList<String> eolArray = new ArrayList<>();

        if((grammar.get(curRow).size() - 1 == curCol) ){
            fillTable(grammarMap);
            ArrayList<String> nonTerminals = new ArrayList<>();
            if(!ifTerminal(grammar.get(curRow).get(curCol))){
                findNonLoop(grammar, grammarMap, nonTerminals, grammar.get(curRow).get(curCol));
            }
            System.out.println(grammar.get(curRow).get(curCol));
            System.out.println(nonTerminals);
            if(ifTerminal(grammar.get(curRow).get(curCol-1)) && !ifTerminal(grammar.get(curRow).get(curCol)) && nonTerminals.size() > 0){
                grammarMap.get(cornerId(grammarMap, checkCorn)).set(grammarMap.get(0).indexOf(grammar.get(curRow).get(curCol-1)), "R" + (curRow + 1));
            }

            if((grammar.get(curRow).size() == 2) &&(curCol == 1)){
                findTerminal(grammar.get(curRow).get(curCol), grammar, grammarMap, curCol, curRow);
                if(isTerminal){
                    grammarMap.get(cornerId(grammarMap, checkCorn)).set(grammarMap.get(0).indexOf(word), "R" + (curRow + 1));
                    isTerminal = false;
                    String word = "";
                }
            }

        }
        return false;
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

        int j = 0;
        for(String str: grammar.get(0)) {
            if (j > 0){
                String checkCorn = str + (1) + (j);

                if(!ifCornerExist(grammarMap, checkCorn)){
                    ArrayList<String> newRow = new ArrayList<>();
                    newRow.add(checkCorn);
                    grammarMap.get(1).add(checkCorn);
                    grammarMap.add(newRow);
                    fillTable(grammarMap);
                    if(!ifRowExist(grammarMap, str)){
                        grammarMap.get(0).add(str);
                    }
                    if(grammarMap.size() == 3){
                        rowArray.add(0);
                        colArray.add(j);
                        searchByRow(grammar, grammarMap, str+1+""+j, 0, 1);
                        break;
                    }
                }
            }
            if(j == 1){
                break;
            }
            j++;
        }

        for (int k = 2; k< grammarMap.size() -1; k++){
            if(getWord(grammarMap.get(k).get(0)).equals(getWord(grammarMap.get(k+1).get(0)))){

                for (int q = 0; q < grammarMap.get(0).size() ; q++){
                    String newBlock = (grammarMap.get(k).get(q) + "|" + grammarMap.get(k+1).get(q)).replace("|;", "");
                    grammarMap.get(k).set(q, newBlock.replace(";|", ""));
                }
                grammarMap.remove(k+1);
            }
        }
        printMap(grammarMap);



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