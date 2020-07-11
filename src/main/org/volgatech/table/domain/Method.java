package main.org.volgatech.table.domain;

import java.util.ArrayList;

public class Method {
    private String val;
    private int num;
    private boolean isTerminal;
    private ArrayList<String> guideSets;
    private boolean isRightMethod;


    public Method(String val, int num) {
        this.val = val;
        this.num = num;
        isTerminal = checkTerminal();
        guideSets = new ArrayList<>();
    }

    public boolean getIsTerminale() {
        return isTerminal;
    }

    private boolean checkTerminal() {
        if (val.length() > 1) {
            char[] valCharArr = val.toCharArray();
            return (valCharArr[0] != '<');
        } else {
            return true;
        }
    }

    public void writeMethod() {
        System.out.println(val + "; IsRight?? " + isRightMethod);
    }

    public void changeNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public String getVal() {
        return val;
    }

    public void addGuideSets(ArrayList<String> guideSets) {
        this.guideSets.addAll(guideSets);
    }

    public ArrayList<String> getGuideSets() {
        return guideSets;
    }

    public void setIsRightMethod(boolean isRightMethod) {
        this.isRightMethod = isRightMethod;
    }

    public boolean getIsRightMethod() {
        return isRightMethod;
    }

    public String getOutString() {
        return num + ";" + val + ";";
    }
}
