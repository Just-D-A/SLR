package main.org.volgatech.table;

import main.org.volgatech.Globals.Globals;


import java.util.ArrayList;

public class Converter {
    private ArrayList<ArrayList<String>> grammar;

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
        
        //КОД ЗДЕСЬ

        ArrayList<ArrayList<String>> grammarMap = new ArrayList<>();

        return grammarMap;
    }
}
