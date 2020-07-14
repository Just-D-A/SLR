package main.org.volgatech.table;

import main.org.volgatech.Globals.Globals;


import java.util.ArrayList;

public class Converter {
    private ArrayList<ArrayList<String>> grammar;

        private static  boolean ifCornerExist(ArrayList<ArrayList<String>> grammarMap, String checkCorn){
        for (ArrayList<String> row : grammarMap) {
            if(checkCorn.equals(row.get(0))){
                return true;
            }
        }
        return false;
    }


    private static  boolean ifRowExist(ArrayList<ArrayList<String>> grammarMap, String checkCorn){
        for (String row : grammarMap.get(0)) {
            if(checkCorn.equals(row)){
                return true;
            }
        }
        return false;
    }

    private static  boolean ifTerminal(String checkCorn){
        return !checkCorn.contains("<") || !checkCorn.contains(">");
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

    private static void printMap(ArrayList<ArrayList<String>> grammarMap){
        for (ArrayList<String> row : grammarMap) {
            for (String corn : row) {
                System.out.format("%-25s",corn);
            }
            System.out.println();
        }
    }



    private static String getWord(String word){
        return word.substring(0, word.length() - 2);
    }

    private static boolean ifWordExist(ArrayList<String> resultSearches, String searchWord){
        for(String word : resultSearches){
            if(getWord(word).equals(searchWord)){
                return  true;
            }
        }
        return false;
    }

    private static ArrayList<String> resultSearches = new ArrayList<>();
    private static boolean allExistCols(ArrayList<ArrayList<String>> grammar, String searchWord){
        for (int i = 0; i < grammar.size() ; i++) {
            ArrayList<String> tableRow = grammar.get(i);
            for (Integer id : indexOfAll(searchWord, tableRow)) {
                if(id == 0) {
                    String word = tableRow.get(id + 1) + (i + 1) + "" + (id + 1);
                    if (!ifWordExist(resultSearches, word)) {
                        resultSearches.add(word);
                        if (!searchWord.equals(getWord(word)) && !ifTerminal(tableRow.get(id + 1))) {
                            allExistCols(grammar, tableRow.get(id + 1));
                        }
                    }
                }
            }
        }
        return  true;
    }

    private static boolean leftSide(ArrayList<ArrayList<String>> grammar, String searchWord){
        for (int i = 0; i < grammar.size() ; i++) {
            ArrayList<String> tableRow = grammar.get(i);
            for (Integer id : indexOfAll(searchWord, tableRow)) {
                if(id > 0) {
                    if(tableRow.size() - 1 == id){

                        if(tableRow.size() - 1 > 1){
                            String word = tableRow.get(id - 1) + (i + 1) + "" + (id - 1);
                            if (!ifWordExist(resultSearches, word)) {
                                resultSearches.add(word);
                            }
                        } else if(ifTerminal(searchWord) && (tableRow.size() - 1 == 1)) {
                            leftSide(grammar, tableRow.get(id-1));

                        }
                    }
                }  else if((id == 0) && !ifTerminal(searchWord) && (searchWord.equals(tableRow.get(0)))) {

                    String word = tableRow.get(id+1) + (i + 1) + "" + (id + 1);
                    if (!ifWordExist(resultSearches, word)) {
                        resultSearches.add(word);
                       // leftSide(grammar, tableRow.get(id+1));

                    }
                }
            }
        }
        return  true;
    }

    private static Integer tablePosition = 0;

    private static boolean searchByRow(ArrayList<ArrayList<String>> grammar, ArrayList<ArrayList<String>> grammarMap, String gramWord, Integer curRow, Integer curCol, Integer mapPos, boolean addCol){
            String checkCorn = gramWord;
            if(!ifCornerExist(grammarMap, checkCorn) && addCol){
                ArrayList<String> sRow = new ArrayList<>();
                sRow.add(checkCorn);
                grammarMap.add(sRow);

            }
            if(!ifTerminal(checkCorn) && checkCorn.contains("|")){
                String[] array = checkCorn.split("\\|", -1);
                for(String word : array){
                    checkCorn = word;
                }

            }
            if ((grammar.get(curRow).size()-1 > curCol)){

                String nextWord = grammar.get(curRow).get(curCol+1);
                resultSearches.add(nextWord+(curRow+1)+""+(curCol+1));
                allExistCols(grammar, nextWord);

                for(String col : resultSearches){
                    if(!ifRowExist(grammarMap, getWord(col))){
                        grammarMap.get(0).add(getWord(col));
                        fillTable(grammarMap);
                        String word = getWord(col);
                    }
                }

                for (int i = 0; i < resultSearches.size() - 1 ; i++) {
                    String fWord = getWord(resultSearches.get(i));
                    String sWord = getWord(resultSearches.get(i+1));
                    if(sWord.equals(fWord)){
                        resultSearches.set(i, resultSearches.get(i) + "|" + resultSearches.get(i+1));
                        resultSearches.remove(i+1);
                    }

                }


                for(String col : resultSearches){
                    fillTable(grammarMap);
                    if(!ifTerminal(col)){
                        if(col.contains("|")){
                            String[] array = col.split("\\|", -1);
                            grammarMap.get(mapPos).set(grammarMap.get(0).indexOf(getWord(array[0])), col);
                        } else {
                            grammarMap.get(mapPos).set(grammarMap.get(0).indexOf(getWord(col)), col);
                        }
                    } else {
                        grammarMap.get(mapPos).set(grammarMap.get(0).indexOf(getWord(col)), col);
                    }
                }
                // Collections.sort(resultSearches);

                for(String col : resultSearches){
                    if(!ifCornerExist(grammarMap,col)){
                        ArrayList<String> newRow = new ArrayList<>();
                        newRow.add(col);
                        grammarMap.add(newRow);
                    }
                }


            } else {

                fillTable(grammarMap);
                Integer endRow = Integer.parseInt(checkCorn.substring(checkCorn.length()-2, checkCorn.length()-1));
                if(!ifRowExist(grammarMap, "@")) {
                    grammarMap.get(0).add("@");
                    fillTable(grammarMap);
                }
                leftSide(grammar, getWord(gramWord));
                for(String col : resultSearches){
                    if(!ifRowExist(grammarMap, getWord(col))){
                        grammarMap.get(0).add(getWord(col));
                        fillTable(grammarMap);
                        String word = getWord(col);
                    }
                }
                for(String col : resultSearches){
                    grammarMap.get(mapPos).set(grammarMap.get(0).indexOf(getWord(col)), "R" + endRow);
                }

                fillTable(grammarMap);
                grammarMap.get(mapPos).set(grammarMap.get(0).indexOf("@"), "R" + endRow);
            }
        resultSearches.clear();
        return false;
    }

    public Converter(ArrayList<ArrayList<String>> grammar) {
        this.grammar = grammar;
    }

    public ArrayList<ArrayList<String>> convertGrammar() {

        //grammar - входящая грамматика
        for(ArrayList<String> grammarStr: grammar) {
            for(String grammarStrEl: grammarStr) {
                System.out.print(grammarStrEl + " ");
            }
            System.out.println();
        }
        System.out.println("_________________________");
        
    //Create table
        ArrayList<ArrayList<String>> grammarMap = new ArrayList<ArrayList<String>>();
        ArrayList<String> upPartOfTable = new ArrayList<>();
        upPartOfTable.add(" ");
        upPartOfTable.add(grammar.get(0).get(0));
        grammarMap.add(upPartOfTable);
        ArrayList<String> leftPartOfTable = new ArrayList<>();
        leftPartOfTable.add(grammar.get(0).get(0));
        leftPartOfTable.add("ok");
        grammarMap.add(leftPartOfTable);



        grammarMap.get(0).add(grammar.get(0).get(1));
        grammarMap.get(1).add(grammar.get(0).get(1)+1+""+1);
        if(ifTerminal(grammar.get(0).get(1))){
            searchByRow(grammar, grammarMap, grammar.get(0).get(1)+1+""+1, 0, 1, 2, true);
        }
        for(int i = 3; i < grammarMap.size(); i++){
            Integer curRow = Integer.parseInt(grammarMap.get(i).get(0).substring(grammarMap.get(i).get(0).length()-2, grammarMap.get(i).get(0).length()-1)) -1;
            Integer curСol = Integer.parseInt(grammarMap.get(i).get(0).substring(grammarMap.get(i).get(0).length()-1));
            if(!ifTerminal(grammarMap.get(i).get(0)) && (grammarMap.get(i).get(0).contains("|"))){
                String[] array = grammarMap.get(i).get(0).split("\\|", -1);
                for(String word : array){
                    Integer curRow1 = Integer.parseInt(word.substring(word.length()-2, word.length()-1)) -1;
                    Integer curСol1 = Integer.parseInt(word.substring(word.length()-1));
                    searchByRow(grammar, grammarMap, word, curRow1, curСol1, i, false);
                }
            } else {
                searchByRow(grammar, grammarMap, grammarMap.get(i).get(0), curRow, curСol, i, true);
            }
        }
        int j = 0;

        if(grammarMap.get(2).get(0).equals("11")){
            for(int i = 1; i < grammarMap.get(0).size(); i++){
                String newBlock = (grammarMap.get(1).get(i) + "|" + grammarMap.get(2).get(i)).replace("|;", "");
                grammarMap.get(1).set(i, newBlock.replace(";|", ""));
            }
            grammarMap.remove(2);

        }
        return grammarMap;
    }
}
