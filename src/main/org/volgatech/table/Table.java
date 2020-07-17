package main.org.volgatech.table;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.convector.domain.GrammarElement;

import java.util.ArrayList;

public class Table {
    private ArrayList<ArrayList<GrammarElement>> grammar;
    ArrayList<ArrayList<String>> table;
    ArrayList<String> upString;
    private String val;
    private ArrayList<ArrayList<ArrayList<GrammarElement>>> elTable;

    public Table(ArrayList<ArrayList<GrammarElement>> grammar) {
        this.grammar = grammar;
        table = new ArrayList<>();
        upString = new ArrayList<>();
        elTable = new ArrayList<>();

    }

    public ArrayList<ArrayList<String>> create() {
        //create UP STRING
        upString.add(" ");
        upString.add("@");
        for (ArrayList<GrammarElement> grammarElementString : grammar) {
            for (GrammarElement el : grammarElementString) {
                tryToAdd(el.getVal());
                //   System.out.print(el.getVal() + " ");
            }
        }
        table.add(upString);
        //  ArrayList<String> secondString = new ArrayList<>();
        for (String val : upString) {
            System.out.print(val + " ");
        }
        //init
        ArrayList<ArrayList<GrammarElement>> elTableString = new ArrayList<>();
        ArrayList<GrammarElement> cellArr = new ArrayList<>();

        cellArr.add(grammar.get(0).get(0));
        elTableString.add(cellArr);
        elTable.add(elTableString);

        for (int i = 0; i < elTable.size(); i++) {
            ArrayList<ArrayList<GrammarElement>> currString = elTable.get(i);
            for (int j = 1; j < upString.size(); j++) {
                currString.add(new ArrayList<>());
            }
            //поехали заполним
            GrammarElement leftEl = currString.get(0).get(0); // get <S>
            ArrayList<GrammarElement> rightPart = leftEl.getNextElements();
            for (GrammarElement rightPartEl : rightPart) {
                int upIndex = getIndexByVal(rightPartEl.getVal());
                ArrayList<GrammarElement> cell = currString.get(upIndex);

                if (rightPartEl.isLast()) {
                    GrammarElement elR = new GrammarElement("R" + rightPartEl.getStringPosition(), 0, 0);
                    cell.add(elR);
                } else {
                    cell.add(rightPartEl);

                }
            }
            for(ArrayList<GrammarElement> cell: currString) {
                tryAddLeftCell(cell);
            }
            for (int j = 1; j < currString.size(); j++) {
                ArrayList<GrammarElement> cell = currString.get(j);
                for (GrammarElement cellEl : cell) {
                    System.out.print(cellEl.getVal() + " ");
                }
                System.out.println();
            }
        }


        System.out.println();
        //FILL LEFT AND INSIDE
        upString.add(Globals.AXIOM_VAL);

        return table;
    }

    private int getIndexByVal(String val) {
        for (int i = 0; i < upString.size(); i++) {
            if (val.equals(upString.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private void tryToAdd(String vals) {

        for (String upEl : upString) {
            if (vals.equals(upEl)) {
                return;
            }
        }
        upString.add(vals);
    }

    private GrammarElement getElByIndex(int x, int y) {
        if (x < grammar.size()) {
            ArrayList<GrammarElement> grammarElementString = grammar.get(x);
            return y < grammarElementString.size() ? grammarElementString.get(y) : null;
        }
        System.out.println("ERROR IN CONVECTOR");
        return null;
    }

    private void tryAddLeftCell(ArrayList<GrammarElement> cell) {
        for (ArrayList<ArrayList<GrammarElement>> elTableString : elTable) {
            if (!isNew(elTableString.get(0), cell)) {
                return;
            }
        }
        ArrayList<ArrayList<GrammarElement>> newString = new ArrayList<>();
        newString.add(cell);
    }

    private boolean isNew(ArrayList<GrammarElement> grammarElements, ArrayList<GrammarElement> cell) {
        for (int i = 0; i < cell.size(); i++) {
            if (!grammarElements.get(i).isEqual(cell.get(i))) {
                return false;
            }
        }
        return true;
    }

}
