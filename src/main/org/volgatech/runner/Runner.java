package main.org.volgatech.runner;

import main.org.volgatech.Globals.Globals;
import main.org.volgatech.lexer.domain.Token;
import main.org.volgatech.lexer.domain.TokenType;

import java.util.ArrayList;
import java.util.Stack;

public class Runner {
    private ArrayList<ArrayList<String>> table;
    ArrayList<Token> tokens;
    Stack<String> stack;
    int currStringsIndex;
    Token nextToken;

    public Runner(ArrayList<ArrayList<String>> table, ArrayList<Token> tokens) {
        this.table = table;
        this.tokens = tokens;
        Stack<String> stack = new Stack<>();
        currStringsIndex = 1;
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

        Token lastToken = new Token(0, "@", 0, 0);
        tokens.add(lastToken);
        for(Token token: tokens) {
            System.out.println("TOKEN VALUE___: " + token.getValue());
        }
        for (int i = 0; i < tokens.size(); i++) {
      //      System.out.println(tokens.get(i).getValue() + "IN FOR FOR");
            if (!goToNext(tokens.get(i), i)) {
                return tokens.get(i);
            }
        }
    //    System.out.println(currStringsIndex);
        if(currStringsIndex != 1) {
            return new Token(0, "NOT END OF PROGRAME", 0, 0);
        }
        return null;
    }

    private boolean goToNext(Token token, int index) {
      //  if(!token.getValue().equals("@")) {
            System.out.println("TOKEN VAL " + token.getValue() + " NUM: " + currStringsIndex);
        //}
        if((token.getValue().equals("@")) & (currStringsIndex == 1)) {
            return true;
        }
        ArrayList<String> currStrings = table.get(currStringsIndex);
        int upIndex = getColomIndexByToken(token);
       // System.out.println(upIndex);
        if (upIndex == -1) {
            System.out.println("UNKNOWN TOKEN VAL FOR UPSET");
            return false;
        }
        String valOfCell = currStrings.get(upIndex);
        if ((valOfCell.equals(";")) | (valOfCell.equals(""))) {
            System.out.println("EMPTY OF NEED CELL " + upIndex);
            return false;
        }
        if (valOfCell.length() <= 3) {
            char[] charArr = valOfCell.toCharArray();
            if (charArr[0] == 'R') {
                String substring = valOfCell.substring(1);
            //    System.out.println(substring + " + LOL");
                currStringsIndex = Integer.valueOf(substring);
                return goToNext(token, currStringsIndex);
            }
        }
   //     System.out.println(valOfCell);
        currStringsIndex = getStringIndexByCellEl(valOfCell);
        if (currStringsIndex == -1) {
            System.out.println("CAN NOT FIND LEFT ELEMENT OF " + valOfCell);
            return false;
        }
        return true;
    }

    private int getStringIndexByCellEl(String valOfCell) {
        int x = -1;
        for (int i = 0; i < table.size(); i++) {
            String leftPart = table.get(i).get(0);
            if (leftPart.equals(valOfCell)) {
                x = i;
                break;
            }
        }
        return x;
    }


    private int getColomIndexByToken(Token token) {
        ArrayList<String> firstStrings = table.get(0);
  /*      for (String str : firstStrings) {
            System.out.print(str + " ");
        }
        System.out.println();
        System.out.println("***END FIRST LINE**");*/
        int x = -1;

        for (int i = 0; i < firstStrings.size(); i++) {
            String upCellEl = firstStrings.get(i);
            if (upCellEl.contains("|")) {
                String[] args = parseArguments(upCellEl);
           /*     System.out.println("IN " + upCellEl);
                for(String arg: args) {
                    System.out.println("arg: " + arg);
                }*/
                for (int j = 0; j < args.length; j++) {
                    //System.out.println("IN FOR " + upCellEl);
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

    private boolean checkTerminal(String val) {
        if (val.length() > 1) {
            char[] valCharArr = val.toCharArray();
            return (valCharArr[0] != '<');
        } else {
            return true;
        }
    }
}
