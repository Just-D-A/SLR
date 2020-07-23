package main.org.volgatech.table.domain;

import main.org.volgatech.convector.domain.GrammarElement;

import java.util.ArrayList;

public class CellElementStr {
    private ArrayList<GrammarElement> leftEl;
    private ArrayList<GrammarElement> rightEl;
    public CellElementStr(ArrayList<GrammarElement> leftEl, ArrayList<GrammarElement> rightEl) {
        this.leftEl = leftEl;
        this.rightEl = rightEl;
    }

    public ArrayList<GrammarElement> getLeftEl() {
        return leftEl;
    }

    public ArrayList<GrammarElement> getRightEl() {
        return rightEl;
    }
}
