package main.org.volgatech.convector;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.convector.domain.GrammarElement;

import java.util.ArrayList;

public class Converter {
    private ArrayList<ArrayList<String>> grammar;
    private ArrayList<ArrayList<GrammarElement>> newGrammar;

    public Converter(ArrayList<ArrayList<String>> grammar) {
        this.grammar = grammar;
        newGrammar = new ArrayList<>();
    }

    public ArrayList<ArrayList<GrammarElement>> convertGrammar() {
        //add axiom
     //
        addAxiom();
        //convert to cell element and set numbers
        convertToCellElement();


        //set next elements for table
        for (int i = 0; i < newGrammar.size(); i++) {
            ArrayList<GrammarElement> grammarString = newGrammar.get(i);
            for (int j = 0; j < grammarString.size(); j++) {
                GrammarElement el = grammarString.get(j);
                el.setNextElements(setNextElements(grammarString.get(j), new ArrayList<>()));
                el.setLast(j == (grammarString.size() - 1));
            }
        }
        ArrayList<GrammarElement>  axStr = new ArrayList<>();
        axStr.add((new GrammarElement("@", 0, 2)));
        newGrammar.get(0).get(1).setNextElements(axStr);
        for (ArrayList<GrammarElement> grammarString : newGrammar) {
            for (GrammarElement el : grammarString) {
                System.out.println("***");
                el.writeOutGrammarElement();
            }
        }



        return newGrammar;
    }


    private void convertToCellElement() {
        for (int i = 0; i < grammar.size(); i++) {
            ArrayList<String> grammarString = grammar.get(i);
            ArrayList<GrammarElement> newGrammarString = new ArrayList<>();
            for (int j = 0; j < grammarString.size(); j++) {
                newGrammarString.add(new GrammarElement(grammarString.get(j), i, j));
            }
            newGrammar.add(newGrammarString);
        }
    }

    private void addAxiom() {
        ArrayList<ArrayList<String>> grammarWithAxiom = new ArrayList<>();
        ArrayList<String> firstGrammarString = grammar.get(0);
        String firstEl = firstGrammarString.get(0);
        ArrayList<String> axiom = new ArrayList<>();

        axiom.add(Globals.AXIOM_VAL);
        axiom.add(firstEl);
        grammarWithAxiom.add(axiom);

        grammarWithAxiom.addAll(grammar);
        grammar = grammarWithAxiom;
    }

    private ArrayList<GrammarElement> setNextElements(GrammarElement curr, ArrayList<GrammarElement> storage) {
        ArrayList<GrammarElement> result = new ArrayList<>();
        for (GrammarElement elInStorage : storage) {
            if (curr.isEqual(elInStorage)) {
                return result;
            }
        }
        storage.add(curr);
        GrammarElement nextEl = getElByIndex(curr.getStringPosition(), curr.getColomPosition() + 1); //get next
        if (nextEl == null) {
            //функция взврощающая терминалы
         /*   System.out.println(" _________");
            curr.writeOutGrammarElement();
            getElByIndex(curr.getStringPosition(), 0).writeOutGrammarElement();
            System.out.println(" _________");*/
          /*  System.out.print("WITH LAST EL ");
            curr.writeOutGrammarElement();*/
            result.addAll(findForLast(getElByIndex(curr.getStringPosition(), 0), new ArrayList<>()));
          /*  if (result.isEmpty()) {
                result.add(new GrammarElement("@", curr.getStringPosition(), curr.getColomPosition() + 1));
            }*/
        } else {
            result.add(nextEl);
            if (!nextEl.isTerminale()) {
                for (ArrayList<GrammarElement> grammarElementString : newGrammar) {
                    GrammarElement el = grammarElementString.get(0);
                    String elVal = el.getVal();
                    if (elVal.equals(nextEl.getVal())) {
                       // System.out.println("_________________________");
                     //   System.out.println("LEFT: " + elVal + " FOR NEXT " + nextEl.getVal() + " " + nextEl.getStringPosition() + " " + nextEl.getColomPosition() );
                        result.addAll(setNextElements(el, storage));
                    }
                }
            }
        }
        return result;
    }

    private ArrayList<GrammarElement> findForLast(GrammarElement curr, ArrayList<GrammarElement> storageLast) {
        ArrayList<GrammarElement> result = new ArrayList<>();
        for (GrammarElement elInStorage : storageLast) {
            if (curr.isEqual(elInStorage)) {
                return result;
            }
        }
        storageLast.add(curr);
        for (int i = 0; i < newGrammar.size(); i++) {
            ArrayList<GrammarElement> grammarString = newGrammar.get(i);
            for (int j = 1; j < grammarString.size(); j++) {
                GrammarElement el = grammarString.get(j);
                String elVal = el.getVal();
                if (elVal.equals(curr.getVal())) {
                  /*  System.out.print( "INSIDE WITH ");
                    el.writeOutGrammarElement();*/
                    GrammarElement nextEl = getElByIndex(el.getStringPosition(), el.getColomPosition() + 1);//get next
                    if (nextEl == null) {
                     // System.out.print("AGAIN With " + curr.getStringPosition());
                        GrammarElement grEl = getElByIndex(el.getStringPosition(), 0);
                   //     grEl.writeOutGrammarElement();
                        if (grEl.getVal().equals(Globals.AXIOM_VAL)) {
                            result.add(new GrammarElement("@", 0, el.getColomPosition() + 1));
                            //return result;
                        }
                        result.addAll(findForLast(grEl, storageLast));
                    } else {
                       // nextEl.writeOutGrammarElement();

                        if (!nextEl.isTerminale()) {
                            for (ArrayList<GrammarElement> grammarElementString : newGrammar) {
                                GrammarElement element = grammarElementString.get(0);
                                String elementVal = element.getVal();
                                if (elementVal.equals(nextEl.getVal())) {
                                    result.addAll(setNextElements(el, new ArrayList<>()));
                                }
                            }
                        } else {
                            result.add(nextEl);
                        }
                    }
                }
            }
        }
      //  System.out.println("OUT");
        return result;
    }

    private GrammarElement getElByIndex(int x, int y) {
        if (x < newGrammar.size()) {
            ArrayList<GrammarElement> grammarElementString = newGrammar.get(x);
            return y < grammarElementString.size() ? grammarElementString.get(y) : null;
        }
        System.out.println("ERROR IN CONVECTOR");
        return null;
    }


    public ArrayList<ArrayList<String>> getGrammar() {
        return grammar;
    }
}