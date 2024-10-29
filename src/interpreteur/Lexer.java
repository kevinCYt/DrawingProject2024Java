package interpreteur;

import java.util.ArrayList;

/**
 * The Lexer class is responsible for tokenizing input text into a list of tokens.
 */
public class Lexer {

    /**
     * Tokenizes the input text into a list of tokens.
     *
     * @param inputText the input text to tokenize
     * @return a list of TokenType representing the tokens in the input text
     */
    // Method to tokenize the input text into a list of tokens
    public static ArrayList<TokenType> tokenize(String inputText) {
        ArrayList<TokenType> tokens = new ArrayList<>();
            String[] parts = inputText.split("\\s+|\\s*,\\s*");  // Split line into parts by spaces or commas
            for (String part : parts) {
                TokenType type = determineTokenType(part);  // Determine the token type of the part
                if (type != null) {
                	tokens.add(type);
                } else {
                	tokens.add(TokenType.UNKNOWN_EXPRESSION);  // Default to UNKNOWN_EXPRESSION if type is unknown
                }
            }
        return tokens;
    }

    /**
     * Determines the token type based on the given part of the text.
     *
     * @param param the part of the text to determine the token type for
     * @return the TokenType of the given part, or null if the token type is unknown
     */
    private static TokenType determineTokenType(String param) {
        if (param.matches("\\d+(\\.\\d+)?%")) {
            return TokenType.PERCENT;
        } else if (param.matches("#[0-9A-Fa-f]{6}")) {
            return TokenType.COLOR_HEX;
        } else if (param.matches("\\d+")) {
            return TokenType.INT;
        } else if (param.matches("\\d+(\\.\\d+)?")) {
            return TokenType.DOUBLE;
        } else if (param.matches("=")) {
            return TokenType.EQUAL;
        } else if (param.matches("\\*")) {
            return TokenType.TIMES;
        } else if (param.matches("\\+")) {
            return TokenType.PLUS;
        } else if (param.matches("-")) {
            return TokenType.MINUS;
        } else if (param.matches("/")) {
            return TokenType.DIVIDE;
        } else if (param.matches("(true|false)")) {
            return TokenType.BOOLEAN;
        } else if (param.matches("FWD")) {
            return TokenType.FWD;
        } else if (param.matches("BWD")) {
            return TokenType.BWD;
        } else if (param.matches("TURN")) {
            return TokenType.TURN;
        } else if (param.matches("MOV")) {
            return TokenType.MOV;
        } else if (param.matches("POS")) {
            return TokenType.POS;
        } else if (param.matches("HIDE")) {
            return TokenType.HIDE;
        } else if (param.matches("SHOW")) {
            return TokenType.SHOW;
        } else if (param.matches("PRESS")) {
            return TokenType.PRESS;
        } else if (param.matches("COLOR")) {
            return TokenType.COLOR;
        } else if (param.matches("THICK")) {
            return TokenType.THICK;
        } else if (param.matches("LOOKAT")) {
            return TokenType.LOOKAT;
        } else if (param.matches("CURSOR")) {
            return TokenType.CURSOR;
        } else if (param.matches("SELECT")) {
            return TokenType.SELECT;
        } else if (param.matches("REMOVE")) {
            return TokenType.REMOVE;
        } else if (param.matches("NUM")) {
            return TokenType.NUM;
        } else if (param.matches("STR")) {
            return TokenType.STR;
        } else if (param.matches("BOOL")) {
            return TokenType.BOOL;
        } else if (param.matches("DEL")) {
            return TokenType.DEL;
        } else if (param.matches("IF")) {
            return TokenType.IF;
        } else if (param.matches("FOR")) {
            return TokenType.FOR;
        } else if (param.matches("WHILE")) {
            return TokenType.WHILE;
        } else if (param.matches("MIMIC")) {
            return TokenType.MIMIC;
        } else if (param.matches("MIRROR")) {
            return TokenType.MIRROR;
        }else if(param.matches("TO")){
        	return TokenType.TO;
        }else if (param.matches("==")) {
            return TokenType.EQUALEQUAL;
        } else if (param.matches("<")) {
            return TokenType.INF;
        } else if (param.matches(">")) {
            return TokenType.SUP;
        } else if (param.matches("!")) {
            return TokenType.NOT;
        } else if (param.matches("!=")) {
            return TokenType.NOTEQUAL;
        } else if (param.matches("&&")) {
            return TokenType.AND;
        } else if (param.matches("\\|\\|")) {
            return TokenType.OR;
        } else if (param.matches(">=")) {
            return TokenType.SUPOREQUAL;
        } else if (param.matches("<=")) {
            return TokenType.INFOREQUAL;
        } else if (param.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return TokenType.VAR_NAME;
        } else if (param.matches("\".*\"")) {
            return TokenType.STRING;
        } else if (param.matches("\\{")) {
            return TokenType.BEGIN;
        } else if (param.matches("\\}")) {
            return TokenType.END;
        } else {
            return null;
        }
    }
}
