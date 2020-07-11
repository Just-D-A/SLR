package main.org.volgatech.table;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.table.domain.Method;
import main.org.volgatech.table.domain.MethodList;

import java.util.ArrayList;

public class Converter {
    private ArrayList<ArrayList<String>> grammar;

    public Converter(ArrayList<ArrayList<String>> grammar) {
        this.grammar = grammar;
    }

    public ArrayList<MethodList> convertGrammar() {
        return makeMethodList();
    }

    private ArrayList<MethodList> makeMethodList() {
        String[][] table = new String[Globals.MAX_GRAMMAR_EL_COUNT][Globals.MAX_GRAMMAR_EL_COUNT];
        return null;
    }
}