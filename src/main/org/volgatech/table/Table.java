package main.org.volgatech.table;

import javafx.scene.control.Cell;
import main.org.volgatech.Globals.Globals;
import main.org.volgatech.convector.domain.GrammarElement;
import main.org.volgatech.table.domain.CellElement;

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
        for (ArrayList<GrammarElement> grammarElementString : grammar) {
            for (GrammarElement el : grammarElementString) {
                tryToAdd(el.getVal());
               // System.out.print(el.getVal() + " ");
            }
        }
        upString.add("@");
        table.add(upString);
        ArrayList<ArrayList<CellElement>> tableCells = new ArrayList<>();
        ArrayList<CellElement> firstCellStr = new ArrayList<>();
        ArrayList<GrammarElement> firstLeft = new ArrayList<>();
        firstLeft.add(grammar.get(0).get(0));
        firstCellStr.add(new CellElement(firstLeft));
        tableCells.add(firstCellStr);
        int i = 0;
        while(i < tableCells.size()) {
            //get left grammar element arr
            ArrayList<CellElement> currString = tableCells.get(i);
            CellElement leftCell = currString.get(0);
            ArrayList<GrammarElement> leftElArr = leftCell.getCell();

            for(int j= 1; j < upString.size(); j++) {
                String upVal = upString.get(j);
                ArrayList<GrammarElement> cellElements = new ArrayList<>();
                for(GrammarElement leftEl: leftElArr) {
                    ArrayList<GrammarElement> leftElRightPart = leftEl.getNextElements();
                    for(GrammarElement rightGrammarElement: leftElRightPart) {
                        if(upVal.equals(rightGrammarElement.getVal())) {
                            cellElements.add(rightGrammarElement);
                        }
                    }
                }
                currString.add(new CellElement(cellElements));
            }

            for(CellElement cellElement: currString) {
                boolean isInTable = false;
                for(ArrayList<CellElement> cellElementsTable: tableCells) {
                    if(cellElement.isEqual(cellElementsTable.get(0))) {
                        isInTable = true;
                    }
                }
                if(!isInTable) {
                    ArrayList<CellElement> newCellString = new ArrayList<>();
                    newCellString.add(cellElement);
                    tableCells.add(newCellString);
                }
            }
            i++;
        }

        for (ArrayList<CellElement> celElString : tableCells) {
            ArrayList<String> newStr = new ArrayList<>();
            CellElement firstEl = celElString.get(0);
            ArrayList<GrammarElement> firstGramEl = firstEl.getCell();
            for (CellElement cellElement : celElString) {
                String result = "";
                ArrayList<GrammarElement> gramArr = cellElement.getCell();
                boolean isFirst = true;
                for (GrammarElement grammarElement : gramArr) {
                    if (!isFirst) {
                        result += "|";
                    } else {
                        isFirst = false;
                    }
                    result += (grammarElement.getVal() + grammarElement.getStringPosition() + grammarElement.getColomPosition());
                }
                if (result.equals("")) {
                    result = ".";
                }
                newStr.add(result);
            }
            for (GrammarElement grammarElement : firstGramEl) {
                if (grammarElement.isLast()) {
                    ArrayList<GrammarElement> nextGramElements = grammarElement.getNextElements();
                    for (GrammarElement grammarElementsToR : nextGramElements) {
                        int index = getIndexByVal(grammarElementsToR.getVal());
                        newStr.set(index, "R" + grammarElement.getStringPosition());
                    }
                }
            }
            table.add(newStr);
        }

        table.get(1).set(1, "ok");
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

}
