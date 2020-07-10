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
        int x = 1;
        ArrayList<MethodList> methodsArr = new ArrayList<>();

        for (ArrayList<String> grammarStr : grammar) {

            ArrayList<Method> methods = new ArrayList<>();
            String firstMethod = grammarStr.get(0);

            MethodList methodList = new MethodList();
            methodList.addFirstMethod(new Method(firstMethod, x, false, true, false, false));
            x++;

            for (int i = 2; i < grammarStr.size(); i++) {
                String grammarEl = grammarStr.get(i);

                if (grammarEl.equals("|")) {
                    methodList.addRightPartArray(methods);
                    methodsArr.add(methodList);

                    methods = new ArrayList<>();
                    methodList = new MethodList();

                    methodList.addFirstMethod(new Method(firstMethod, x, false, true, false, false));
                    x++;

                } else {
                    Method method = new Method(grammarEl, x);
                    methods.add(method);
                    x++;
                }

            }
            methodList.addRightPartArray(methods);
            methodsArr.add(methodList);
        }
        return methodsArr;
    }
}