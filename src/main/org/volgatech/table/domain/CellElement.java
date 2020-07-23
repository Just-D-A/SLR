package main.org.volgatech.table.domain;

import main.org.volgatech.convector.domain.GrammarElement;

import java.util.ArrayList;

public class CellElement {
    private ArrayList<GrammarElement>  cell;
    public CellElement(ArrayList<GrammarElement> cell) {
        this.cell = cell;
    }

    public ArrayList<GrammarElement> getCell() {
        return cell;
    }

    public boolean isEqual(CellElement cellElement) {
        ArrayList<GrammarElement> grammarElements = cellElement.getCell();
        for(int i = 0; i < grammarElements.size() & i < cell.size(); i++) {
            if(!grammarElements.get(i).isEqual(cell.get(i))) {
                return false;
            }
        }
        return true;
    }
}
