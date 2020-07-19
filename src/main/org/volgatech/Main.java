package main.org.volgatech;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.lexer.domain.Token;
import main.org.volgatech.lexer.io.LexerReader;
import main.org.volgatech.convector.Converter;
import main.org.volgatech.convector.GrammarReader;
import main.org.volgatech.convector.domain.GrammarElement;
import main.org.volgatech.table.Table;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static void printMap(ArrayList<ArrayList<String>> grammarMap){
        for (ArrayList<String> row : grammarMap) {
            for (String corn : row) {
                System.out.format("%-20s",corn);
                //System.out.print(corn + ";");
            }
            System.out.println();
        }
    }


    private static  boolean ifRowExist(ArrayList<ArrayList<String>> grammarMap, String checkCorn){
        for (String row : grammarMap.get(0)) {
            if(checkCorn.equals(row)){
                return true;
            }
        }
        return false;
    }

    private static  boolean ifCornerExist(ArrayList<ArrayList<String>> grammarMap, String checkCorn){
        for (ArrayList<String> row : grammarMap) {
            if(checkCorn.contains(row.get(0))){
                return true;
            }
        }
        return false;
    }
    private static  Integer cornerId(ArrayList<ArrayList<String>> grammarMap, String checkCorn){
        int i = 0;
        for (ArrayList<String> row : grammarMap) {
            if(row.get(0).contains(checkCorn)){
                return i;
            }
            i++;
        }
        return -1;
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

    private static  boolean ifTerminal(String checkCorn){
        return !checkCorn.contains("<") || !checkCorn.contains(">");
    }
    private static  void fillTable(ArrayList<ArrayList<String>> grammarMap){
        for (ArrayList<String> row : grammarMap) {
            if(row.size() < grammarMap.get(0).size()){
                while(row.size() < grammarMap.get(0).size()){
                    row.add(".");
                }
            }
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

        ArrayList<ArrayList<String>> map = new ArrayList<>();
        ArrayList<String> upPartOfTable = new ArrayList<>();
        upPartOfTable.add(" ");
        upPartOfTable.add(convertedGrammar.get(0).get(0).getVal());
        map.add(upPartOfTable);
        ArrayList<String> leftPartOfTable = new ArrayList<>();
        leftPartOfTable.add(convertedGrammar.get(0).get(0).getVal());
        leftPartOfTable.add("ok");
        map.add(leftPartOfTable);



        map.get(0).add(grammar.get(0).get(1));
        fillTable(map);

        for(int i = 0; i < convertedGrammar.size(); i++){
            ArrayList<GrammarElement> grammarElem = convertedGrammar.get(i);
            for(GrammarElement elem : grammarElem){
                if(elem.getStringPosition() == 0 && elem.getColomPosition() == 0){
                    GrammarElement prevEl = null;
                    for (GrammarElement el : elem.getNextElements()) {
                        if(!map.get(0).contains(el.getVal())){
                            map.get(0).add(el.getVal());
                        }
                        if(( prevEl != null) && !el.getVal().equals("@") && (prevEl.getVal().equals(el.getVal()))) {
                            Integer index = cornerId(map,prevEl.getVal()+prevEl.getStringPosition()+""+prevEl.getColomPosition());
                            map.get(index).set(0, map.get(index).get(0) + "|" + el.getVal()+el.getStringPosition()+""+el.getColomPosition());
                        } else if(cornerId(map,el.getVal()+el.getStringPosition()+""+el.getColomPosition()) == -1 && !el.getVal().contains(map.get(0).get(1))){
                            ArrayList<String> newRow = new ArrayList<>();
                            newRow.add(el.getVal()+el.getStringPosition()+""+el.getColomPosition());
                            map.add(newRow);
                        }

                        prevEl = el;
                    }
                    fillTable(map);
                }
                if(elem.getStringPosition() != 0){
                    GrammarElement prevEl = null;
                    for (GrammarElement el : elem.getNextElements()) {
                        if(!map.get(0).contains(el.getVal())){
                            map.get(0).add(el.getVal());
                        }
                        // если
                        if((prevEl != null) && !el.getVal().equals("@") && (prevEl.getVal().equals(el.getVal()))  && (cornerId(map, el.getVal()+ el.getStringPosition()+""+ el.getColomPosition()) == -1)){
                            Integer index = cornerId(map,prevEl.getVal()+prevEl.getStringPosition()+""+prevEl.getColomPosition());
                            map.get(index).set(0, map.get(index).get(0) + "|" + el.getVal()+el.getStringPosition()+""+el.getColomPosition());
                            printMap(map);
                            if(index + 1 < map.size() && map.get(index+1).get(0).equals(el.getVal()+el.getStringPosition()+""+el.getColomPosition())){
                                map.remove(index+1);
                            }
                        } else  if(cornerId(map,el.getVal()+el.getStringPosition()+""+el.getColomPosition()) == -1 && !el.getVal().contains("@")){
                            ArrayList<String> newRow = new ArrayList<>();
                            newRow.add(el.getVal()+el.getStringPosition()+""+el.getColomPosition());
                            map.add(newRow);
                            fillTable(map);
                        }
                       // map.get(cornerId(map,el.getVal()+el.getStringPosition()+""+el.getColomPosition())).set(map.get(0).indexOf(el.getVal()), el.getVal()+el.getStringPosition()+""+el.getColomPosition());
                        prevEl = el;
                    }
                    fillTable(map);
                }
//                grammarMap.get(mapPos).set(grammarMap.get(0).indexOf(getWord(col)), col);

            }
            fillTable(map);
        }

        for(int i = 0; i < convertedGrammar.size(); i++){
            ArrayList<GrammarElement> grammarElem = convertedGrammar.get(i);
            for(GrammarElement elem : grammarElem){
                if(elem.getStringPosition() != 0) {

                    for (GrammarElement el : elem.getNextElements()) {
                        Integer index = 0;

                        if (elem.getStringPosition() == 1 && elem.getColomPosition() == 0){
                            index = cornerId(map, elem.getVal());
                        }else {
                            index = cornerId(map, elem.getVal() + elem.getStringPosition() + "" + elem.getColomPosition());
                        }
                        if(index != -1){
                            if(elem.isLast()){
                                map.get(index).set(map.get(0).indexOf(el.getVal()), "R" + elem.getStringPosition());
                                map.get(index).set(map.get(0).indexOf(el.getVal()), map.get(index).get(map.get(0).indexOf(el.getVal())).replace(".|", ""));
                            } else{
                                String newVal = map.get(index).get(map.get(0).indexOf(el.getVal())) + "|" + el.getVal() + el.getStringPosition() + "" + el.getColomPosition();
                                newVal = newVal.replace(".|", "");
                                if(cornerId(map, newVal) != -1){
                                    map.get(index).set(map.get(0).indexOf(el.getVal()), newVal);
                                }
                            }

                        }
                    }
                } else  {
                    if(!elem.getVal().contains("<S>")){
                        Integer index = cornerId(map, elem.getVal());
                        map.get(1).set(map.get(0).indexOf(elem.getVal()), map.get(index).get(0));
                        map.get(2).set(map.get(0).indexOf("@"), "R" + elem.getStringPosition());
                    }
                }
            }
        }
        printMap(map);

        System.out.println("*** Table complited ***");
     /*   Runner runner = new Runner(tokenList, methods);
        Token errorToken = runner.run();
        if (errorToken == null) {
            System.out.println("Complited!");
        } else {
            System.out.println("ERROR");
            errorToken.writeToken();
        } */


    }
}