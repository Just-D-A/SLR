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
    Stack<Integer> stack;
    int currStringsIndex;
    int currTokenIndex;
    Token currToken;
    Token nextToken;

    public Runner(ArrayList<ArrayList<String>> table, ArrayList<Token> tokens, ArrayList<ArrayList<String>> grammar) {
        this.table = table;
        this.tokens = tokens;
        this.grammar = grammar;
        stack = new Stack<>();
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
        shift();
        System.out.println("__TOKENS END__");
        stack.push(1);
        System.out.println("***RUNNER START***");
        String currVal = "";

        while (!stack.empty()) {
            int currIndexString = stack.pop();
            System.out.println("WITH TOKEN " + currToken.getValue() + " TO STACK " + currIndexString);
            //System.out.println(currIndexString);
            System.out.println(stack);
            int upIndex;
            ArrayList<String> currString = table.get(currIndexString);
    //        System.out.println("Curr VAl " + currVal);
            if (currVal.equals("")) {
                if (isTerminal(currString.get(0))) {
                    shift();
         //           System.out.println(currToken.getValue() + " Change to this");
           //         System.out.println(currString.get(0) + " Change to this");
                }
                //     System.out.println("WITH TOKEN " + currToken.getValue() + " TO STACK " + currIndexString);
                upIndex = getColomIndexByToken(currToken);

        //        System.out.println("Upndex " + upIndex + " TOKEN VAL " + currToken.getValue());
            } else {
         //       System.out.println(currVal + " _________-");
                upIndex = getUpIndexByVal(currVal);
                currVal = "";
            }
   //         System.out.println("UP INDEX " + upIndex);
            if (upIndex == -1) {
                System.out.println("ERROR = " + upIndex);
                return currToken;
            }
            String valOfCell = currString.get(upIndex);
            if (valOfCell.equals("ok")) {
                return null;
            }
      //      System.out.println("VAL " + valOfCell);
            char[] charArr = valOfCell.toCharArray();
            if ((charArr[0] == 'R')) {
                String substring = valOfCell.substring(1);
                valOfCell = getGrammarStringFirstVal(Integer.parseInt(substring));
                int index = getUpIndexByVal(valOfCell);
                currVal = valOfCell;
          //      System.out.println(currVal);
                int countOfRight = getRightGrammarStringPart(Integer.parseInt(substring));
            //    System.out.println(Integer.parseInt(substring) + " is rigth " + countOfRight);
                for(int i = 0; i < countOfRight - 1; i++) {
                    System.out.println("MORE 3");
                    stack.pop();
                }
                //stack.push(currIndexString);
                //stack.push(index);
            } else if (!valOfCell.equals(".")) {
          //      System.out.println("GO TO " + getStringIndexByCellEl(valOfCell));
                stack.push(currIndexString);
                stack.push(getStringIndexByCellEl(valOfCell));
        //        System.out.println("From  " + currIndexString);
       //         System.out.println(getStringIndexByCellEl(valOfCell) + " TO " + valOfCell);
            } else {
                return currToken;
            }
        }

        return currToken;
    }

    private int getRightGrammarStringPart(int i) {
        ArrayList<String> rightPart = grammar.get(i);
        return rightPart.size() - 1;
    }

    private String getGrammarStringFirstVal(int i) {
      /*  for (ArrayList<String> strArr : grammar) {
            for (String str : strArr) {
                System.out.print(str + " ");
            }
            System.out.println();
        }*/
      //  System.out.println(i + " LOL " + grammar.get(i).get(0));
        return grammar.get(i).get(0);
    }

    private int getGrammarStringSize(int i) {
        return grammar.get(i).size();
    }

    private Token getNextTokern(int tokenIndex) {
        if (tokenIndex < tokens.size()) {
            tokenIndex++;
            return tokens.get(tokenIndex);
        }
        return null;
    }

    private void shift() {
        if (currTokenIndex < tokens.size()) {
            currToken = tokens.get(currTokenIndex);
            currTokenIndex++;
        }
    }

    private int getUpIndexByVal(String val) {
        ArrayList<String> upString = table.get(0);
        int x = -1;
        for (int i = 0; i < upString.size(); i++) {
            // System.out.print(upString.get(i) + " ");
            if (val.equals(upString.get(i))) {
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
