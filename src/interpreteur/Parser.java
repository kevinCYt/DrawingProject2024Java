package interpreteur;

import java.util.ArrayList;

/**
 * The Parser class provides methods to parse and validate a list of tokens.
 */
public class Parser {

    /**
     * Parses a list of tokens to check if all expressions are valid.
     *
     * @param tokens the list of tokens to parse
     * @return true if all expressions are valid, false otherwise
     */
    public static boolean parse(ArrayList<TokenType> tokens) {
    		if (!isValidExpression(tokens)) {
            	System.out.println("Invalid expression detected");
                return false;
            }
        System.out.println("All expressions are valid!");
        return true;
    }

    /**
     * Checks if the given list of tokens represents a valid expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidExpression(ArrayList<TokenType> tokens) {
        if (tokens.isEmpty()) {
            System.out.println("isValidExpression: tokens are empty");
            return false;
        } 
        
        TokenType firstToken = tokens.get(0);
        switch (firstToken) {
            case FWD:
            	return isValidForwardOrBackward(tokens);
            case BWD:
                return isValidForwardOrBackward(tokens);
            case TURN:
                return isValidTurn(tokens);
            case MOV:
                return isValidMove(tokens);
            case POS:
                return isValidPosition(tokens);
            case HIDE:
            case SHOW:
                return isValidHideOrShow(tokens);
            case PRESS:
                return isValidPress(tokens);
            case COLOR:
                return isValidColor(tokens);
            case THICK:
                return isValidThickness(tokens);
            case LOOKAT:
                return isValidLookAt(tokens);
            case CURSOR:
            case SELECT:
            case REMOVE:
                return isValidCursor(tokens);
            case NUM:
            case STR:
            case BOOL:
                return isValidVariableDeclaration(tokens);
            case DEL:
                return isValidDelete(tokens);
            case VAR_NAME:
            	return isValidVarAffectation(tokens);
            case IF:
            	return isValidIF(tokens);
            case WHILE:
            	return isValidIF(tokens);
            case BEGIN:
            	return true;
            case END:
            	return true;
            case FOR:
            	return isValidFOR(tokens);
            case MIMIC:
                return isValidMIMIC(tokens);
            case MIRROR:
            	return isValidMIRROR(tokens);
            default:
                System.out.println("isValidExpression: unknown first token");
                return false;
        }
    }

    /**
     * Checks if the given list of tokens represents a valid MIMIC expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the MIMIC expression is valid, false otherwise
     */
    private static boolean isValidMIMIC(ArrayList<TokenType> tokens) {
		if(tokens.size() == 5) {
			if(tokens.get(1) == TokenType.INT) {
				if(tokens.get(2) == TokenType.INT && tokens.get(3) == TokenType.INT || tokens.get(2) == TokenType.PERCENT && tokens.get(3) == TokenType.PERCENT) {
					if(tokens.get(4) == TokenType.BEGIN) {
						return true;
					}
				}

			}
		}return false;
	}
    
    /**
     * Checks if the given list of tokens represents a valid MIRROR expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the MIRROR expression is valid, false otherwise
     */
	private static boolean isValidMIRROR(ArrayList<TokenType> tokens) {
		if(tokens.size() == 4) {
			if(tokens.get(1) == TokenType.INT && tokens.get(2) == TokenType.INT || tokens.get(1) == TokenType.PERCENT && tokens.get(2) == TokenType.PERCENT) {
				if(tokens.get(3) == TokenType.BEGIN) {
					return true;
				}
			}
		}else if(tokens.size() == 6) {
			if(tokens.get(1) == TokenType.INT && tokens.get(2) == TokenType.INT && tokens.get(3) == TokenType.INT && tokens.get(4) == TokenType.INT || tokens.get(1) == TokenType.PERCENT && tokens.get(2) == TokenType.PERCENT && tokens.get(3) == TokenType.PERCENT && tokens.get(4) == TokenType.PERCENT) {
				if(tokens.get(5) == TokenType.BEGIN) {
					return true;
				}
			}
		}
		return false;
	}
    
    /**
     * Tests if the given tokens form a valid comparison operation.
     *
     * @param token1 the first token
     * @param token2 the operator token
     * @param token3 the second token
     * @return true if the tokens form a valid comparison operation, false otherwise
     */
    public static boolean testOp(TokenType token1, TokenType token2, TokenType token3) {
    		if (token1 == TokenType.INT && token3 == TokenType.INT) {
    			if(token2 == TokenType.INF || token2 == TokenType.SUP || token2 == TokenType.EQUALEQUAL) {
    				return true;
    			}
    		}
    		return false;
    	}
    
    /**
     * Checks if the given list of tokens represents a valid IF expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the IF expression is valid, false otherwise
     */
    private static boolean isValidIF(ArrayList<TokenType> tokens) {
    	if (tokens.size() ==3) {
    		if(tokens.get(1)==TokenType.BOOLEAN && tokens.get(2)==TokenType.BEGIN) {
    			return true;
    		}
    	}return false;
    }
    
    /**
     * Checks if the given list of tokens represents a valid FOR expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the FOR expression is valid, false otherwise
     */
    private static boolean isValidFOR(ArrayList<TokenType> tokens) {
    	if (tokens.size() ==7) {
    		if(tokens.get(1)==TokenType.VAR_NAME && (tokens.get(2)==TokenType.INT ||tokens.get(2)==TokenType.DOUBLE)&& tokens.get(3)==TokenType.TO && (tokens.get(4)==TokenType.INT ||tokens.get(4)==TokenType.DOUBLE)&&(tokens.get(5)==TokenType.INT ||tokens.get(5)==TokenType.DOUBLE) ){
    			return true;
    		}	
    	}else if (tokens.size() ==6) {
    		if(tokens.get(1)==TokenType.VAR_NAME && (tokens.get(2)==TokenType.INT ||tokens.get(2)==TokenType.DOUBLE)&& tokens.get(3)==TokenType.TO && (tokens.get(4)==TokenType.INT ||tokens.get(4)==TokenType.DOUBLE) ){
    			return true;
    		}else if(tokens.get(1)==TokenType.VAR_NAME && tokens.get(2)==TokenType.TO && (tokens.get(3)==TokenType.INT ||tokens.get(3)==TokenType.DOUBLE) ){
    			return true;
    		}
    	}else if (tokens.size() ==5) {
    		if(tokens.get(1)==TokenType.VAR_NAME && tokens.get(2)==TokenType.TO && (tokens.get(3)==TokenType.INT ||tokens.get(3)==TokenType.DOUBLE) ){
    			return true;
    		}
    	}return false;
    }

    /**
     * Checks if the given list of tokens represents a valid forward or backward expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidForwardOrBackward(ArrayList<TokenType> tokens) {
        if (tokens.size() != 2) {
            System.out.println("isValidForwardOrBackward: incorrect token size");
            return false;
        }
        TokenType secondToken = tokens.get(1);
        if (secondToken != TokenType.PERCENT && secondToken != TokenType.INT && secondToken != TokenType.DOUBLE) {  
            System.out.println("isValidForwardOrBackward: invalid second token");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given list of tokens represents a valid turn expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidTurn(ArrayList<TokenType> tokens) {
        if (tokens.size() != 2) {
            System.out.println("isValidTurn: incorrect token size");
            return false;
        }
        TokenType secondToken = tokens.get(1);
        if (secondToken != TokenType.DOUBLE && secondToken != TokenType.INT) { 
            System.out.println("isValidTurn: invalid second token");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given list of tokens represents a valid move expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidMove(ArrayList<TokenType> tokens) {
        if (tokens.size() != 3) {
            System.out.println("isValidMove: incorrect token size");
            return false;
        }
        TokenType secondToken = tokens.get(1);
        TokenType thirdToken = tokens.get(2);
        if (!(secondToken == TokenType.PERCENT || secondToken == TokenType.INT || secondToken == TokenType.DOUBLE) ||  
            !(thirdToken == TokenType.PERCENT || thirdToken == TokenType.INT || thirdToken == TokenType.DOUBLE)) {
            System.out.println("isValidMove: invalid second or third token");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given list of tokens represents a valid position expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidPosition(ArrayList<TokenType> tokens) {
        if (tokens.size() != 3) {
            System.out.println("isValidPosition: incorrect token size");
            return false;
        }
        TokenType secondToken = tokens.get(1);
        TokenType thirdToken = tokens.get(2);
        if (!(secondToken == TokenType.PERCENT || secondToken == TokenType.INT || secondToken == TokenType.DOUBLE) ||
            !(thirdToken == TokenType.PERCENT || thirdToken == TokenType.INT || thirdToken == TokenType.DOUBLE)) {
            System.out.println("isValidPosition: invalid second or third token");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given list of tokens represents a valid hide or show expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidHideOrShow(ArrayList<TokenType> tokens) {
        if (tokens.size() != 2 && tokens.get(1) != TokenType.INT) {
            System.out.println("isValidHideOrShow: incorrect token size");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given list of tokens represents a valid cursor expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidCursor(ArrayList<TokenType> tokens) {
        if (tokens.size() != 2 || (tokens.get(1) != TokenType.INT && tokens.get(1) != TokenType.DOUBLE)) {
            System.out.println("isValidCursor: invalid token size or second token");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given list of tokens represents a valid press expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidPress(ArrayList<TokenType> tokens) {
        if (tokens.size() != 2) {
            System.out.println("isValidPress: incorrect token size");
            return false;
        }
        TokenType secondToken = tokens.get(1);
        if (secondToken != TokenType.DOUBLE && secondToken != TokenType.PERCENT && secondToken != TokenType.VAR_NAME) {   // VAR_NAME doit être compris entre 0 et 1 (il faut donc vérifier qu'elle est du bon type et qu'elle existe)
            System.out.println("isValidPress: invalid second token");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given list of tokens represents a valid color expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidColor(ArrayList<TokenType> tokens) {
        if (tokens.size() == 2) {
            if (tokens.get(1) == TokenType.COLOR_HEX) {
                return true;
            } else {
                System.out.println("isValidColor: invalid color hex");
                return false;
            }
        }
        if (tokens.size() == 4) {
            TokenType secondToken = tokens.get(1);
            TokenType thirdToken = tokens.get(2);
            TokenType fourthToken = tokens.get(3);
            if (((secondToken == TokenType.INT || secondToken == TokenType.DOUBLE) && (thirdToken == TokenType.INT || thirdToken == TokenType.DOUBLE) && (fourthToken == TokenType.INT || fourthToken == TokenType.DOUBLE)) ||
                (secondToken == TokenType.DOUBLE && thirdToken == TokenType.DOUBLE && fourthToken == TokenType.DOUBLE)){      // Les trois VAR_NAMES doivent soit être que des entiers entre 0 et 255 ou alors que des doubles entre 0 et 1
                return true;
            } else {
                System.out.println("isValidColor: invalid RGB tokens");
                return false;
            }
        }
        System.out.println("isValidColor: incorrect token size");
        return false;
    }

    /**
     * Checks if the given list of tokens represents a valid thickness expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidThickness(ArrayList<TokenType> tokens) {
        if (tokens.size() != 2) {
            System.out.println("isValidThickness: incorrect token size");
            return false;
        }
        if (tokens.get(1) != TokenType.DOUBLE && tokens.get(1) != TokenType.VAR_NAME) {        
            System.out.println("isValidThickness: invalid second token");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given list of tokens represents a valid look at expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the expression is valid, false otherwise
     */
    private static boolean isValidLookAt(ArrayList<TokenType> tokens) {
        if (tokens.size() == 3) {
            TokenType secondToken = tokens.get(1);
            TokenType thirdToken = tokens.get(2);
            if ((secondToken == TokenType.PERCENT || secondToken == TokenType.DOUBLE || secondToken == TokenType.INT) &&
                (thirdToken == TokenType.PERCENT || thirdToken == TokenType.DOUBLE || secondToken == TokenType.INT)) {
                return true;
            } else {
                System.out.println("isValidLookAt: invalid second or third token");
                return false;
            }
        }
        if (tokens.size() == 2 && (tokens.get(1) == TokenType.INT || tokens.get(1) == TokenType.VAR_NAME)) {
            return true;
        }
        System.out.println("isValidLookAt: incorrect token size or invalid cursor token");
        return false;
    }

    /**
     * Checks if the given list of tokens represents a valid variable declaration.
     *
     * @param tokens the list of tokens to check
     * @return true if the variable declaration is valid, false otherwise
     */
    private static boolean isValidVariableDeclaration(ArrayList<TokenType> tokens) {
        if (tokens.size() == 2 && tokens.get(1) == TokenType.VAR_NAME) {
            return true;
        }
        if (tokens.size() == 4 && tokens.get(1) == TokenType.VAR_NAME && tokens.get(2) == TokenType.EQUAL) {
            TokenType valueType = tokens.get(3);
            TokenType varType = tokens.get(0);
            if (varType == TokenType.NUM && (valueType == TokenType.INT || valueType == TokenType.DOUBLE)) {
                return true;
            }
            if (varType == TokenType.STR && valueType == TokenType.STRING) {
                return true;
            }
            if (varType == TokenType.BOOL && valueType == TokenType.BOOLEAN) {
                return true;
            }
        }
        System.out.println("isValidVariableDeclaration: incorrect token size or invalid token types");
        return false;
    }

    /**
     * Checks if the given list of tokens represents a valid delete expression.
     *
     * @param tokens the list of tokens to check
     * @return true if the delete expression is valid, false otherwise
     */
    private static boolean isValidDelete(ArrayList<TokenType> tokens) {
        if (tokens.size() != 2 || tokens.get(1) != TokenType.VAR_NAME) {
            System.out.println("isValidDelete: incorrect token size or invalid second token");
            return false;
        }
        return true;
    }
    
    /**
     * Checks if the given list of tokens represents a valid variable assignment.
     *
     * @param tokens the list of tokens to check
     * @return true if the variable assignment is valid, false otherwise
     */
    private static boolean isValidVarAffectation(ArrayList<TokenType> tokens) {
        if (tokens.size() < 3 || tokens.get(1) != TokenType.EQUAL) {
            System.out.println("isValidVarAffectation: incorrect token size or missing equal sign");
            return false;
        }

        // Start checking from the token after the equal sign
        boolean expectValue = true;
        for (int i = 2; i < tokens.size(); i++) {
            TokenType token = tokens.get(i);

            if (expectValue) {
                // Expecting a value (INT, DOUBLE, or VAR_NAME)
                if (token != TokenType.INT && token != TokenType.DOUBLE && token != TokenType.VAR_NAME && token != TokenType.STRING && token != TokenType.BOOLEAN  && token != TokenType.NOT) {
                    System.out.println("isValidVarAffectation: expected value, but found " + token);
                    return false;
                }
            } else {
                // Expecting an operator (PLUS, MINUS, TIMES, DIVIDE)
                if (token != TokenType.PLUS && token != TokenType.MINUS && token != TokenType.TIMES && token != TokenType.DIVIDE && token != TokenType.INF && token != TokenType.SUP 
                		&& token != TokenType.AND && token != TokenType.OR && token != TokenType.NOTEQUAL && token != TokenType.NOT && token != TokenType.INFOREQUAL
                    	&& token != TokenType.SUPOREQUAL && token != TokenType.EQUALEQUAL && token != TokenType.BOOLEAN) {
                    System.out.println("isValidVarAffectation: expected operator, but found " + token);
                    return false;
                }
            }

            // Alternate between expecting value and operator
            expectValue = !expectValue;
        }
	    
	if (tokens.get(2) == TokenType.NOT && tokens.get(3) == TokenType.BOOLEAN) {
        	expectValue = false;
        }
        // The last token must be a value
        if (expectValue) {
            System.out.println("isValidVarAffectation: expression ends with an operator");
            return false;
        }

        return true;
    }

}
