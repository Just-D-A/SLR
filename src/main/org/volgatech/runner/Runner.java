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


        return null;
    }

    private boolean goToNext(Token token, int index) {
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

    private boolean checkTerminal(String val) {
        if (val.length() > 1) {
            char[] valCharArr = val.toCharArray();
            return (valCharArr[0] != '<');
        } else {
            return true;
        }
    }
}
