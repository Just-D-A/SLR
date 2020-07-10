package main.org.volgatech.table;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.table.domain.Method;
import main.org.volgatech.table.domain.MethodList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        return сheckNumericSystem(methodsArr);
    }

    private boolean isDiscribed(ArrayList<MethodList> methodsArr, Method method) {
        if (!method.getIsTerminale()) {
            for (MethodList methodList : methodsArr) {
                String firstVal = methodList.getFirstVal();
                if (firstVal.equals(method.getVal())) {
                    return false;
                }
            }
        }
        return false;
    }

    private ArrayList<MethodList> сheckNumericSystem(ArrayList<MethodList> methodsArr) {
      /*  int countOfRepeat;
        ArrayList<MethodList> repeatedMethods = new ArrayList();
        for (int i = 1; i < methodsArr.size() - 1; i++) {
            countOfRepeat = 0;
            if (methodsArr.get(i + 1).getFirstVal().equals(methodsArr.get(i).getFirstVal())) {//если названия левый частей грамматик совпадают
                ArrayList<MethodList> methodListsToChange = new ArrayList<>();
                methodListsToChange.add(methodsArr.get(i));
                while ((i < methodsArr.size() - 1) && methodsArr.get(i).getFirstVal().equals(methodsArr.get(i + 1).getFirstVal())) {//ищем все одинаковые левые части
                    methodListsToChange.add(methodsArr.get(i + 1));
                    i++;
                }
                changeRepeats(methodListsToChange);
            }
        }
        //add next
        for (MethodList methodListToChange : methodsArr) {
            methodListToChange.setFirstNext();
            for (Method method : methodListToChange.getMethodsRightPart()) {
                if (method.getIsTerminale()) {
                    method.setNext(method.getNum() + 1);
                } else {
                    method.setNext(findNoterminaleNum(methodsArr, method.getVal()));
                }
            }
        }
        //change last next
        //если грамматика заканчивается нетерминалом тогда его значение NEXT = -1
        for (MethodList methodListToChange : methodsArr) {
            methodListToChange.changeLastNonterminalNext();
        }

        //add guideSets
        //добавляем направляющие множества

        for (MethodList methodListToChange : methodsArr) {
            methodListToChange.setFirstGuideSet(methodListToChange.getMethodsRightPart().get(0).getVal());
            for (Method method : methodListToChange.getMethodsRightPart()) {
                if (!method.getIsTerminale()) {
                    method.setGuideSet(findNoterminaleGuideSet(methodsArr, method.getVal()));

                } else {
                    if (!method.getVal().equals("@"))
                        method.setGuideSet(method.getVal());
                }
            }
        }

        //FOR @ SYM

        //for (int i = methodsArr.size() - 1; i >= 0; i--) {
        for(MethodList methodListToChange: methodsArr)
        {
            methodListToChange.setFirstGuideSet(methodListToChange.getMethodsRightPart().get(0).getVal());
            for (Method method : methodListToChange.getMethodsRightPart()) {
                if (method.getVal().equals("@")) {
                    ArrayList<String> guideSets = newGuideSetsForExitSym(methodsArr, methodListToChange.getFirstVal(), new ArrayList<>());
                    method.addGuideSets(guideSets);
                }
            }
        }

        for (
                MethodList methodListToChange : methodsArr) {
            Method firstRightMethod = methodListToChange.getFirstRightMethod();
            if (firstRightMethod.getVal().equals("@")) {
                Method firstMethod = methodListToChange.getFirstMethod();
                firstMethod.addGuideSets(firstRightMethod.getGuideSets());
                for(String str: firstRightMethod.getGuideSets()) {
                    System.out.println(str);
                }
            }
        }
        // END FOR @ SYM

        for (
                MethodList methodListToChange : methodsArr) {
            for (Method method : methodListToChange.getMethodsRightPart()) {
                if (!method.getIsTerminale()) {
                    //if(method.getGuideSets().isEmpty()) {
                        method.getGuideSets().clear();
                        method.addGuideSets(findNoterminalAllGuideSets(methodsArr, method.getVal()));
                 //   }
                }
            }
        }

        for (MethodList methodListToChange : methodsArr) {

            Method firstRightMethod = methodListToChange.getFirstRightMethod();
            Method firstMethod = methodListToChange.getFirstMethod();
            if (firstRightMethod.getIsTerminale()) {

                if(!firstRightMethod.getVal().equals("@")) {
                    firstMethod.addGuideSets(firstRightMethod.getGuideSets());
                } else {
                firstMethod.setGuideSet(firstRightMethod.getGuideSet());
                }
            } else {
                firstMethod.addGuideSets(firstRightMethod.getGuideSets());
            }
        }

   /*     for(MethodList methodList: methodsArr) {
            methodList.writeOut();
            System.out.println("_____");
        }

        //add table params
        //заполняем параметры таблицы в соответствии с тз
        for (
                MethodList methodListToChange : methodsArr) {
            ArrayList<Method> methodsRightPart = methodListToChange.getMethodsRightPart();
            for (int i = 0; i < methodsRightPart.size(); i++) {
                Method methodRightPart = methodsRightPart.get(i);
                if (methodRightPart.getIsTerminale()) {
                    methodRightPart.setParams(!methodRightPart.getVal().equals("@"), true, false, false);
                } else {
                    methodRightPart.setParams(false, true, (i + 1) < methodsRightPart.size(), false);
                }
            }
        }

        for (int i = 0; i < methodsArr.size() - 1; i++) {
            MethodList methodListToChange = methodsArr.get(i);

            MethodList methodListToChangeNext = methodsArr.get(i + 1);
            if (methodListToChange.getFirstVal().equals(methodListToChangeNext.getFirstVal())) {
                Method firstMethod = methodListToChange.getFirstMethod();
                firstMethod.setError(false);
            }
        }*/

        return methodsArr;
    }

}