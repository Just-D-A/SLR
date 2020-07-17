package main.org.volgatech.convector.domain;

import java.util.ArrayList;

public class GrammarElement {
    private String val;
    private int stringPosition;
    private int colomPosition;
    private ArrayList<GrammarElement> nextElements;
    private boolean isTerminale;
    private boolean isLast;

    public GrammarElement(String val, int stringPosition, int colomPosition) {
        this.val = val;
        this.stringPosition = stringPosition;
        this.colomPosition = colomPosition;
        nextElements = new ArrayList<>();
        isTerminale = checkTerminal();
    }

    public String getVal() {
        return val;
    }

    public int getStringPosition() {
        return stringPosition;
    }

    public int getColomPosition() {
        return colomPosition;
    }

    public ArrayList<GrammarElement> getNextElements() {
        return nextElements;
    }

    public void setNextElements(ArrayList<GrammarElement> nextElements) {
        this.nextElements = nextElements;
    }

    public boolean isEqual(GrammarElement grammarElement) {
        return ((val.equals(grammarElement.getVal())) & (stringPosition == grammarElement.stringPosition) & (colomPosition == grammarElement.colomPosition));
    }

    private boolean checkTerminal() {
        if (val.length() > 1) {
            char[] valCharArr = val.toCharArray();
            return (valCharArr[0] != '<');
        } else {
            return true;
        }
    }

    public boolean isTerminale() {
        return isTerminale;
    }

    public void writeOutGrammarElement() {
        StringBuilder result = new StringBuilder(val + " " + stringPosition + " " + colomPosition + " " + isLast + " ");
        boolean first = true;
        for (GrammarElement el : nextElements) {
            result.append(el.getVal() + el.getStringPosition() + el.getColomPosition());
            result.append(":");
        }
        System.out.println(result);
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
