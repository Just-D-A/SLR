package main.org.volgatech.runner;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.lexer.domain.Token;
import main.org.volgatech.lexer.domain.TokenType;

import java.util.ArrayList;
import java.util.Stack;

public class Runner {
    private ArrayList<ArrayList<String>> table;
    ArrayList<ArrayList<String>> grammar;
    ArrayList<Token> tokens;
    Stack<String> stack;
    int currStringsIndex;
    int currTokenIndex;
    Token currToken;
    Token nextToken;

    public Runner(ArrayList<ArrayList<String>> table, ArrayList<Token> tokens, ArrayList<ArrayList<String>> grammar) {
        this.table = table;
        this.tokens = tokens;
        this.grammar = grammar;
        Stack<String> stack = new Stack<>();
        currStringsIndex = 1;
        currTokenIndex = 0;
        fixTokenList();
    }

    private void fixTokenList() {
        ArrayList<Token> fixedTokenList = new ArrayList<>();
        for (Token token : tokens) {
            if (token != null) {
                fixedTokenList.add(token);
            }
        }
        tokens = fixedTokenList;
    }

    public Token run() {
        tokens.add(new Token(0, "@", 0, 0));
        for(Token token: tokens) {
            System.out.println(token.getValue() + " ");
        }
        System.out.println("__TOKENS END__");
        shift();
        String result = goToNext();
        //writeOutStack();
        return result.equals("ok") ? null : currToken;
      //  return null;
    }

    private String goToNext() {
        System.out.println("WITH TOKEN " + currToken.getValue());
        String valOfCell = "";
        String upValOfCell = "";


        ArrayList<String> localCurrentStr = table.get(currStringsIndex);
        if(isTerminal(localCurrentStr.get(0))) {
            shift();
            System.out.println("CHANGE TOKEN " + currToken.getValue());
        }
        //ArrayList<String> currString = table.get(currStringsIndex);
        int upIndex = getColomIndexByToken(currToken);
        if(upIndex == -1) {
            System.out.println("ERROR");
            return "ERROR_1";
        }

        while(!valOfCell.equals(";")) {
        //    System.out.println(upIndex);
            valOfCell = localCurrentStr.get(upIndex);
            if(valOfCell.equals("ok")) {
                return "ok";
            }
            System.out.println(valOfCell);
            char[] charArr = valOfCell.toCharArray();
            if((charArr[0] == 'R') | ((valOfCell.equals(";")))) {
                break;
            }
            currStringsIndex = getStringIndexByCellEl(valOfCell);
            if (currStringsIndex == -1) {
                System.out.println("CAN NOT FIND LEFT ELEMENT OF " + valOfCell + " CURRENT STRING INDEX " + currStringsIndex);
                return "ERROR_2";
            }

            upValOfCell = goToNext();
            if(upValOfCell.contains("ERROR")) {
                return upValOfCell;
            }
            System.out.println(upValOfCell + " UP");
            char[] charArrs = upValOfCell.toCharArray();
            if(charArrs[0] == 'R') {
                break;
            }
            upIndex = getUpIndexByVal(upValOfCell);
        }
        if((valOfCell.equals(";")) & upValOfCell.equals("")) {
            return "ERROR_3";
        }
        System.out.println("IM HERE " + upValOfCell);
      //  stack.push(token.getValue());
        char[] charArr = valOfCell.toCharArray();
        if(charArr[0] == 'R') {
            String substring = valOfCell.substring(1);
           /* int grammarSrtSize = getGrammarStringSize(Integer.parseInt(substring));
            //     writeOutStack();
            for(int i = 0; i < grammarSrtSize -1; i++) {
                System.out.println("GET FROM STACK " + stack.pop());
            }
            //       writeOutStack();*/
            valOfCell = getGrammarStringFirstVal(Integer.parseInt(substring));
            //stack.push(valOfCell);
            return valOfCell;
        }
        System.out.println("RETURN " + upValOfCell);
        return upValOfCell;
    }

    private String getGrammarStringFirstVal(int i) {
        return grammar.get(i-1).get(0);
    }

    private int getGrammarStringSize(int i) {
        return grammar.get(i-1).size();
    }

    private Token getNextTokern(int tokenIndex) {
        if(tokenIndex < tokens.size()) {
            tokenIndex++;
            return tokens.get(tokenIndex);
        }
        return null;
    }

    private void shift() {
        if(currTokenIndex < tokens.size()) {
            currToken = tokens.get(currTokenIndex);
            currTokenIndex++;
        }
    }

    private int getUpIndexByVal(String val) {
        ArrayList<String> upString = table.get(0);
        int x = -1;
        for(int i = 0; i< upString.size();i++) {
           // System.out.print(upString.get(i) + " ");
            if(val.equals(upString.get(i))) {
                x = i;
                break;
            }
        }
    //    System.out.println();
        return x;
    }

    private int getStringIndexByCellEl(String valOfCell) {
        int x = -1;
        for (int i = 0; i < table.size(); i++) {
            String leftPart = table.get(i).get(0);
            if (leftPart.equals(valOfCell)) {
                System.out.println();
                x = i;
                break;
            }
        }
        return x;
    }


    private int getColomIndexByToken(Token token) {
        ArrayList<String> firstStrings = table.get(0);
     //   System.out.println("Token val " + token.getValue());
        int x = -1;

        for (int i = 0; i < firstStrings.size(); i++) {
            String upCellEl = firstStrings.get(i);
            if (upCellEl.contains("|")) {
                String[] args = parseArguments(upCellEl);
                for (int j = 0; j < args.length; j++) {
                    if (equlesTerminaleOrType(token, args[j])) {
                        x = i;
                        break;
                    }
                }
            } else {
                if (equlesTerminaleOrType(token, firstStrings.get(i))) {
                    x = i;
                    break;
                }

            }
        }
        return x;
    }

    private String[] parseArguments(String line) {
        return line.split("\\|");
    }

    private boolean equlesTerminaleOrType(Token token, String guideSet) {
        String tokenVal = token.getValue();
        int tokenTypeIndex = token.getTokenType();
        TokenType type = new TokenType();
        String valueOfType = type.getTokenType(tokenTypeIndex);
        return (tokenVal.equals(guideSet)) ^ (guideSet.equals(valueOfType)) ^
                ((tokenTypeIndex >= Globals.INTEGER_KEY) & (tokenTypeIndex <= Globals.FLOAT_KEY) & (guideSet.equals(Globals.GRAMMAR_AND_TOKEN_NUMBER_SYMBOL)));
    }

    private boolean isTerminal(String val) {
        if (val.length() > 1) {
            char[] valCharArr = val.toCharArray();
            return (valCharArr[0] != '<');
        } else {
            return true;
        }
    }
}
