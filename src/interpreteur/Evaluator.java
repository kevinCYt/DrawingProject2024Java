package interpreteur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import functions.Function;
import functions.cursors.Cursor;
import functions.cursors.CursorManager;
import functions.instrutionBlocs.FOR;
import functions.instrutionBlocs.IF;
import functions.instrutionBlocs.WHILE;
import functions.simpleInstruction.BWD;
import functions.simpleInstruction.COLOR;
import functions.simpleInstruction.FWD;
import functions.simpleInstruction.LOOKAT;
import functions.simpleInstruction.MOV;
import functions.simpleInstruction.POS;
import functions.simpleInstruction.TURN;
import functions.simpleInstruction.THICK;
import functions.instrutionBlocs.MIMIC;
import functions.instrutionBlocs.MIRROR;
import interfaceUtilisateur.Window;
import javafx.scene.paint.Color;

/**
 * The Evaluator class is responsible for evaluating and executing commands based on the input.
 * It supports variable management, parsing, and executing different types of commands such as
 * moving cursors, drawing, and controlling flow with loops and conditionals.
 */

public class Evaluator {
	private Window window;
	private ArrayList<Variable> variableList = new ArrayList<>();
	private HashMap<Integer,WHILE> whileList=new HashMap<Integer, WHILE>();
	
	/**
     * Constructs an Evaluator with the specified window and active cursors.
     * 
     * @param window the window for output and updates
     * @param activeCursor a map of active cursors
     */
	
	public Evaluator(Window window, HashMap<Integer, Cursor> activeCursor) {
		this.window=window;
	}
	
	/**
     * Executes a single line of input.
     * 
     * @param input the input string to be executed
     * @param cursorManager the cursor manager to handle cursor operations
	 * @throws Throwable 
     */
	
	public void executeLine(String input, CursorManager cursorManager) throws Throwable {

		ArrayList<TokenType> tokens = Lexer.tokenize(input);
		System.out.println("Tokens size: " + tokens.size());
		System.out.println("Tokens content: " + tokens);
		String[] words = input.split("\\s+");
		ArrayList<String> wordList = new ArrayList<>(Arrays.asList(words));

		ArrayList<Object> parameterList = new ArrayList<>();

		System.out.println(input);

		// Iterate over each token in the tokens list
		for (TokenType token : tokens) {
			// Check if the token is of type VAR_NAME
			if (token == TokenType.VAR_NAME) {
				System.out.println("TOKENTYPE = VAR_NAME");

				// List to store all the indexes of the token of type VAR_NAME
				List<Integer> indexList = new ArrayList<>();

				// Collect all the indexes where the token is of type VAR_NAME
				for (int i = 0; i < tokens.size(); i++) {
					if (tokens.get(i) == TokenType.VAR_NAME) {
						indexList.add(i);
					}
				}

				// Iterate over each variable in the variable list
				for (Variable var : variableList) {
					System.out.println(wordList.toString());

					// Iterate over all the collected indexes
					for (int indexOfVar : indexList) {
						System.out.println(indexOfVar);

						// Check if the variable name matches the word in wordList at the same index as the token
						if (var.getName().equals(wordList.get(indexOfVar)) && indexOfVar != 0 && tokens.get(0) != TokenType.DEL) {
							System.out.println("Changing TokenType at index " + indexOfVar);

							// Update the token type at the found index with the variable type
							tokens.set(indexOfVar, var.getType());

							System.out.println("tokens: " + tokens + " wordList: " + wordList + " Index: " + indexOfVar);

							// Update the word value at the found index with the variable value
							wordList.set(indexOfVar, var.getValue().toString());

							System.out.println(tokens);
						}
					}
				}
			}
		}


		// Parsing and execution logic for the tokens and commands
		
		if (Parser.parse(tokens)) {
			String currentTokenInString = "";
			for (int i = 1; i < tokens.size(); i++) {
				currentTokenInString = tokens.get(i).toString();
				System.out.println("Current token: " + currentTokenInString);
				if (currentTokenInString.equalsIgnoreCase("ID") || currentTokenInString.equalsIgnoreCase("INT")) {
					parameterList.add(Integer.parseInt(wordList.get(i)));
				} else if (currentTokenInString.equalsIgnoreCase("DOUBLE") || currentTokenInString.equalsIgnoreCase("PERCENT")) {
					// Regular expression to match a double value with optional decimal part
					Pattern pattern = Pattern.compile("(\\d*\\.?\\d+)(%)?");
					Matcher matcher = pattern.matcher(wordList.get(i));
					if (matcher.find()) {
						String match = matcher.group(1);
						System.out.println("Match found: " + match);
						parameterList.add(Double.parseDouble(match));
					} else {
						System.out.println("No match found");
					}
				}else if (currentTokenInString.equalsIgnoreCase("COLOR_HEX") ||
						currentTokenInString.equalsIgnoreCase("VAR_NAME") ||
						currentTokenInString.equalsIgnoreCase("EQUAL") ||
						currentTokenInString.equalsIgnoreCase("TIMES") ||
						currentTokenInString.equalsIgnoreCase("PLUS") ||
						currentTokenInString.equalsIgnoreCase("MINUS") ||
						currentTokenInString.equalsIgnoreCase("DIVIDE") ||
						currentTokenInString.equalsIgnoreCase("STRING") ||
						currentTokenInString.equalsIgnoreCase("BOOLEAN") ||
						currentTokenInString.equalsIgnoreCase("EQUALEQUAL") ||
						currentTokenInString.equalsIgnoreCase("NOTEQUAL") ||
						currentTokenInString.equalsIgnoreCase("NOT") ||
						currentTokenInString.equalsIgnoreCase("OR") ||
						currentTokenInString.equalsIgnoreCase("AND") ||
						currentTokenInString.equalsIgnoreCase("SUP") ||
						currentTokenInString.equalsIgnoreCase("INF") ||
						currentTokenInString.equalsIgnoreCase("SUPOREQUAL") ||
						currentTokenInString.equalsIgnoreCase("INFOREQUAL")||
						currentTokenInString.equalsIgnoreCase("TO") ||
						currentTokenInString.equalsIgnoreCase("NOT")) {

					parameterList.add(wordList.get(i));
				}
			}

			switch (tokens.get(0)) {             // switch case for the first token of the line
			case FWD:
				if (parameterList.get(0) instanceof Integer || parameterList.get(0) instanceof Double) {
					handleSimpleInstruction(new FWD(), parameterList, cursorManager,currentTokenInString);
				} else {
					throw new IllegalArgumentException("Invalid argument type for FWD");
				}
				break;

			case BWD:
				if (parameterList.get(0) instanceof Integer || parameterList.get(0) instanceof Double) {
					handleSimpleInstruction(new BWD(), parameterList, cursorManager,currentTokenInString);
				} else {
					throw new IllegalArgumentException("Invalid argument type for BWD");  
				}
				break;

			case LOOKAT:
				if ((parameterList.size() == 2 && (parameterList.get(0) instanceof Integer || parameterList.get(0) instanceof Double) && (parameterList.get(1) instanceof Integer || parameterList.get(1) instanceof Double)) || (parameterList.size() == 1 && (parameterList.get(0) instanceof Integer || parameterList.get(0) instanceof Double))) {
					handleSimpleInstruction(new LOOKAT(), parameterList, cursorManager,currentTokenInString);
				} else {
					throw new IllegalArgumentException("Invalid argument type for LOOKAT");
				}
				break;

			case NUM:
				if (parameterList.size() == 3) {
					if ( parameterList.get(0) instanceof String && parameterList.get(1) instanceof String && (parameterList.get(2) instanceof Double || parameterList.get(2) instanceof Integer)) {
						Variable doubleVar = new Variable(parameterList.get(0).toString(),parameterList.get(2),TokenType.DOUBLE);
						for (Variable var: variableList) {
							if (var.equals(doubleVar)) {
								throw new IllegalStateException("This variable already exists");    
							}
						}
						variableList.add(doubleVar);
						window.updateVariables(variableList);
						window.sysout("NUM " + doubleVar.getName()+" = "+doubleVar.getValue()+" added");
					}
				}
				else if (parameterList.size() == 1) {
					if ( parameterList.get(0) instanceof String) {
						Variable doubleVar = new Variable(parameterList.get(0).toString(),0,TokenType.DOUBLE);
						for (Variable var: variableList) {
							if (var.equals(doubleVar)) {
								throw new IllegalStateException("This variable already exists");    
							}
						}
						variableList.add(doubleVar);
						window.updateVariables(variableList);
						window.sysout("NUM " + doubleVar.getName()+" = "+doubleVar.getValue()+" added");
					}
				}

				break;
			case STR:
				if (parameterList.size() == 3) {
					if ( parameterList.get(0) instanceof String && parameterList.get(1) instanceof String && parameterList.get(2) instanceof String) {
						Variable strVar = new Variable(parameterList.get(0).toString(),parameterList.get(2).toString(),TokenType.STRING);
						for (Variable var: variableList) {
							if (var.equals(strVar)) {
								throw new IllegalStateException("Variable already exists");    
							}
						}
						variableList.add(strVar);
						window.sysout("STRING " + strVar.getName()+" = "+strVar.getValue()+" added");
						window.updateVariables(variableList);
					}
				}
				else if (parameterList.size() == 1) {
					if ( parameterList.get(0) instanceof String) {
						Variable strVar = new Variable(parameterList.get(0).toString(),"",TokenType.STRING);
						for (Variable var: variableList) {
							if (var.equals(strVar)) {
								throw new IllegalStateException("This variable already exists");    
							}
						}
						variableList.add(strVar);
						window.sysout("STRING " + strVar.getName()+" = "+strVar.getValue()+" added");
						window.updateVariables(variableList);
					}
				}
				break;

			case BOOL:
				if (parameterList.size() == 3) {
					if ( parameterList.get(0) instanceof String && parameterList.get(1) instanceof String && parameterList.get(2) instanceof String) {
						Variable boolVar = new Variable(parameterList.get(0).toString(),parameterList.get(2),TokenType.BOOLEAN);
						for (Variable var: variableList) {
							if (var.equals(boolVar)) {
								throw new IllegalStateException("Variable already exists");    
							}
						}
						variableList.add(boolVar);
						window.sysout("BOOL " + boolVar.getName()+" = "+boolVar.getValue()+" added");
						window.updateVariables(variableList);
					}
				}
				else if (parameterList.size() == 1) {
					if ( parameterList.get(0) instanceof String) {
						Variable boolVar = new Variable(parameterList.get(0).toString(),false,TokenType.BOOLEAN);
						for (Variable var: variableList) {
							if (var.equals(boolVar)) {
								throw new IllegalStateException("Variable already exists");    
							}
						}
						variableList.add(boolVar);
						window.sysout("BOOL " + boolVar.getName()+" = "+boolVar.getValue()+" added");
						window.updateVariables(variableList);
					}
				}
				break;

			case VAR_NAME:
				if (variableList.isEmpty()) {
					throw new IllegalStateException("This variable doesn't exist");
				}
				boolean variableFound1 = false;
				for (Variable var : variableList) {
					if(var.getName().equals(wordList.get(0))) {
						variableFound1 = true;
						switch(var.getType()) {
						case INT:
						case DOUBLE:
							boolean expectValue = true;
							parameterList.remove(0);                // remove the equal sign so we can only have the arithmetic expression
							for (Object obj: parameterList) {
								if (expectValue) {
									// Expecting a value (INT, DOUBLE, or VAR_NAME)
									if (!(obj instanceof Double || obj instanceof Integer)) {
										throw new IllegalArgumentException("Expected int or double but found : " + obj);
									}
								} else {
									// Expecting an operator (PLUS, MINUS, TIMES, DIVIDE)
									if (!(obj.toString().equals("+") || obj.toString().equals("-") || obj.toString().equals("*") || obj.toString().equals("/"))) {
										throw new IllegalArgumentException("Expected operator but found : " + obj);
									}
								}
								expectValue = !expectValue;
							}
							double result = evaluateExpression(parameterList);      // return the result of the expression
							System.out.println("Résultat de l'expression 1 : " + result);
							var.setValue(result);
							window.updateVariables(variableList);
							break;
						case STRING:
							boolean expectString = true;
							parameterList.remove(0);
							for (Object obj: parameterList) {
								if (expectString) {
									// Expecting a value (INT, DOUBLE, or VAR_NAME)
									if (!(obj instanceof String)) {
										throw new IllegalArgumentException("Expected int or double but found : " + obj);
									}
								} else {
									// Expecting an operator (PLUS, MINUS, TIMES, DIVIDE)
									if (!(obj.toString().equals("+"))) {
										throw new IllegalArgumentException("Expected operator but found : " + obj);
									}
								}
								expectString = !expectString;
							}
							String concatenateString = concatenateString(parameterList);
							System.out.println("Resultat de la concaténation : " + concatenateString);
							var.setValue(concatenateString);
							window.updateVariables(variableList);
							break;
						case BOOLEAN:
							parameterList.remove(0);
							System.out.println("ETAPE 0 " + parameterList);
							boolean booleanResult = evaluateBooleanExpression(parameterList);
							var.setValue(booleanResult);
							window.updateVariables(variableList);
							break;
						default:
							throw new IllegalStateException("Unknown variable type"); 
						}
					}
				}
				if (!(variableFound1)) {
					throw new IllegalStateException("Can't find variable " + wordList.get(0));
				} 
				break;

			case DEL:

				if (variableList.isEmpty()) {
					throw new IllegalStateException("You can't delete a variable that doesn't exist");
				}
				if (!(parameterList.get(0) instanceof String)) {
					throw new IllegalArgumentException("Expected var_name but found : " + parameterList.get(0));
				}
				boolean variableFound2 = false; // Flag to track if the variable was found
				for (Variable var : variableList) {
					if(var.getName().equals(parameterList.get(0))) {
						variableList.remove(var);
						System.out.println("removed " + var.getName());
						window.updateCursors(cursorManager.getCursors());
						window.sysout("\"removed \" + var.getName()");
						variableFound2 = true; // Set flag to true when variable is found and removed
						window.updateVariables(variableList);
						break;
					} 
				}
				if (!variableFound2) {
					throw new IllegalStateException("Variable" + parameterList.get(0) + " not found.");
				}

				break;

			case CURSOR:
				handleCursorInstruction(parameterList, cursorManager);
				break;
			case SELECT:
				handleSelectInstruction(parameterList, cursorManager);
				break;
			case REMOVE:
				handleRemoveInstruction(parameterList, cursorManager);
				break;
			case HIDE:
				handleHideInstruction(parameterList, cursorManager);
				break;
			case SHOW:
				handleShowInstruction(parameterList, cursorManager);
				break;
			case MOV:
				if ((parameterList.get(0) instanceof Integer || parameterList.get(0) instanceof Double) && (parameterList.get(1) instanceof Integer || parameterList.get(1) instanceof Double)) {
					handleSimpleInstruction(new MOV(), parameterList, cursorManager,currentTokenInString);
				} else {
					throw new IllegalArgumentException("Invalid argument type for MOV");
				}
				break;
			case TURN:
				if(parameterList.size() == 1) {
					if(parameterList.get(0) instanceof Double || parameterList.get(0) instanceof Integer) {
						handleSimpleInstruction(new TURN(), parameterList, cursorManager,currentTokenInString);
					}
				}
				else {
					throw new IllegalArgumentException("Invalid argument type for TURN");
				}
				break;
			case COLOR:
				handleColorInstruction(parameterList, cursorManager,currentTokenInString);
				window.sysout(tokens.get(0).toString()+" done.");
				break;
			case THICK:
				if (parameterList.get(0) instanceof Integer || parameterList.get(0) instanceof Double) {
					handleSimpleInstruction(new THICK(), parameterList, cursorManager,currentTokenInString);
				} else {
					throw new IllegalArgumentException("Invalid argument type for THICK");  
				}
				break;		    
			case POS:
				if ((parameterList.get(0) instanceof Integer || parameterList.get(0) instanceof Double) && (parameterList.get(1) instanceof Integer || parameterList.get(1) instanceof Double)) {
					handleSimpleInstruction(new POS(), parameterList, cursorManager, currentTokenInString);
					window.sysout(tokens.get(0).toString()+" done.");
				}
				else {
					throw new IllegalArgumentException("Invalid arguments for pos : " + parameterList.get(0) + parameterList.get(0));
				}
				break;
			case PRESS:
				handlePressInstruction(parameterList,tokens.get(1), cursorManager);
				window.sysout(tokens.get(0).toString()+" done.");
				break;		
			case IF:
				IF temp=new IF(window);
				if(parameterList.size() == 1) {
					temp.if_funct(Boolean.parseBoolean((String)parameterList.get(0)));
				}
				break;
			case WHILE:
				WHILE tempWhile=null;
				if(!whileList.containsKey( window.getCurrentLine() )) {
					tempWhile=new WHILE(window);
					whileList.put(window.getCurrentLine(), tempWhile);
				}else {
					tempWhile=whileList.get(window.getCurrentLine());
				}
				if(parameterList.size() == 1) {
					try{
						tempWhile.while_funct(Boolean.parseBoolean((String)parameterList.get(0)));
					}catch (Throwable e) {
						throw e;
					}
				}
				break;
			case FOR:
			    System.out.println(parameterList.toString());
			    if (parameterList.size() == 5) {
			        if (parameterList.get(0) instanceof String) {
			            Variable doubleVar = new Variable(parameterList.get(0).toString(), parameterList.get(1), TokenType.DOUBLE);
			            for (Variable var : variableList) {
			                if (var.equals(doubleVar)) {
			                    throw new IllegalStateException("This variable already exists");
			                }
			            }
			            variableList.add(doubleVar);
			            window.updateVariables(variableList);
			            window.sysout("NUM " + doubleVar.getName() + " = " + doubleVar.getValue() + " added");
			        }
			        int from = parameterList.get(1) instanceof Double ? ((Double) parameterList.get(1)).intValue() : (Integer) parameterList.get(1);
			        int to = parameterList.get(3) instanceof Double ? ((Double) parameterList.get(3)).intValue() : (Integer) parameterList.get(3);
			        int step = parameterList.get(4) instanceof Double ? ((Double) parameterList.get(4)).intValue() : (Integer) parameterList.get(4);
			        FOR tempFOR = new FOR(window, (String) parameterList.get(0), from, to, step);
			        tempFOR.funct_FOR();
			    } else if (parameterList.size() == 4 && ((String) parameterList.get(1)).equals("TO")) {
			        if (parameterList.get(0) instanceof String) {
			            Variable doubleVar = new Variable(parameterList.get(0).toString(), 0, TokenType.DOUBLE);
			            for (Variable var : variableList) {
			                if (var.equals(doubleVar)) {
			                    throw new IllegalStateException("This variable already exists");
			                }
			            }
			            variableList.add(doubleVar);
			            window.updateVariables(variableList);
			            window.sysout("NUM " + doubleVar.getName() + " = " + doubleVar.getValue() + " added");
			        }
			        int to = parameterList.get(2) instanceof Double ? ((Double) parameterList.get(2)).intValue() : (Integer) parameterList.get(2);
			        int step = parameterList.get(3) instanceof Double ? ((Double) parameterList.get(3)).intValue() : (Integer) parameterList.get(3);
			        FOR tempFOR = new FOR(window, (String) parameterList.get(0), -1, to, step);
			        tempFOR.funct_FOR();
			    } else if (parameterList.size() == 4 && ((String) parameterList.get(2)).equals("TO")) {
			        if (parameterList.get(0) instanceof String) {
			            Variable doubleVar = new Variable(parameterList.get(0).toString(), parameterList.get(1), TokenType.DOUBLE);
			            for (Variable var : variableList) {
			                if (var.equals(doubleVar)) {
			                    throw new IllegalStateException("This variable already exists");
			                }
			            }
			            variableList.add(doubleVar);
			            window.updateVariables(variableList);
			            window.sysout("NUM " + doubleVar.getName() + " = " + doubleVar.getValue() + " added");
			        }
			        int from = parameterList.get(1) instanceof Double ? ((Double) parameterList.get(1)).intValue() : (Integer) parameterList.get(1);
			        int to = parameterList.get(3) instanceof Double ? ((Double) parameterList.get(3)).intValue() : (Integer) parameterList.get(3);
			        FOR tempFOR = new FOR(window, (String) parameterList.get(0), from, to, -1);
			        tempFOR.funct_FOR();
			    } else if (parameterList.size() == 3) {
			        if (parameterList.get(0) instanceof String) {
			            Variable doubleVar = new Variable(parameterList.get(0).toString(), 0, TokenType.DOUBLE);
			            for (Variable var : variableList) {
			                if (var.equals(doubleVar)) {
			                    throw new IllegalStateException("This variable already exists");
			                }
			            }
			            variableList.add(doubleVar);
			            window.updateVariables(variableList);
			            window.sysout("NUM " + doubleVar.getName() + " = " + doubleVar.getValue() + " added");
			        }
			        int to = parameterList.get(2) instanceof Double ? ((Double) parameterList.get(2)).intValue() : (Integer) parameterList.get(2);
			        FOR tempFOR = new FOR(window, (String) parameterList.get(0), -1, to, -1);
			        tempFOR.funct_FOR();
			    }
			    window.sysout("command FOR done.");
			    break;

			case END:
				window.sysout("END seen");
				break;
			case MIMIC:
				MIMIC tempMimic = new MIMIC(window);
				if(parameterList.size() == 3 && parameterList.get(0) instanceof Integer) {
					if(parameterList.get(1) instanceof Integer && parameterList.get(2) instanceof Integer || parameterList.get(1) instanceof Double && parameterList.get(2) instanceof Double) {
						tempMimic.mimicfunction((int) parameterList.get(0), parameterList.get(1), parameterList.get(2));
					}
				}
				break;
			case MIRROR:
				MIRROR tempMirror = new MIRROR(window);
				if(parameterList.size() == 2) {
					tempMirror.mirrorFunction(parameterList.get(0), parameterList.get(1));
				}else if(parameterList.size() == 4) {
					tempMirror.mirrorFunction(parameterList.get(0), parameterList.get(1),parameterList.get(2), parameterList.get(3));
				}
				break;
			default:
				// Handle unknown function type
				throw new IllegalArgumentException("Unknown function type");
			}


		} else {
			throw new IllegalArgumentException("Syntax error [Parser]: Invalid expression");
		}
		System.out.println("VARIABLES ACTIVES:");
		for (Variable i: variableList) {
			System.out.println(i);
		}
	}

	/**
     * Retrieves the list of WHILE loops.
     *
     * @return The list of WHILE loops.
     */

	public HashMap<Integer, WHILE> getWhileList() {
		return whileList;
	}
	
	/**
     * Sets the list of WHILE loops.
     *
     * @param whileList The list of WHILE loops to set.
     */
	
	public void setWhileList(HashMap<Integer, WHILE> whileList) {
		this.whileList = whileList;
	}
	
	/**
     * Retrieves the window.
     *
     * @return The window.
     */
	
	public Window getWindow() {
		return window;
	}
	
	/**
     * Sets the window.
     *
     * @param window The window to set.
     */
	
	public void setWindow(Window window) {
		this.window = window;
	}

	/**
     * Retrieves the list of variables.
     *
     * @return The list of variables.
     */
	
	public ArrayList<Variable> getVariableList() {
		return variableList;
	}
	
	 /**
     * Sets the list of variables.
     *
     * @param variableList The list of variables to set.
     */
	
	public void setVariableList(ArrayList<Variable> variableList) {
		this.variableList = variableList;
	}
	
	/**
     * Handles a simple instruction with a given function, parameter list, cursor manager, and current token.
     *
     * @param function           The function to execute.
     * @param parameterList      The list of parameters for the function.
     * @param cursorManager      The cursor manager.
     * @param currentTokenInString The current token as a string.
     * @throws Exception If an error occurs while handling the instruction.
     */
	
	private void handleSimpleInstruction(Function function, ArrayList<Object> parameterList,CursorManager cursorManager, String currentTokenInString) throws Exception {
		if (cursorManager.getActiveCursor().isEmpty()) {
			throw new Exception("No cursor selected.");
		}
		for (int id : cursorManager.getActiveCursor().keySet()) {
			try {
				function.argument(window, cursorManager.getActiveCursor().get(id), currentTokenInString, parameterList.toArray());
				window.sysout("command "+ function.toString() +" done.");
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
	}

	/**
	 * Handles the HIDE instruction which hides a cursor identified by an index.
	 *
	 * @param parameterList The list of parameters passed to the instruction.
	 *                       It is expected to contain a single Integer or a Double that represents an integer value.
	 * @param cursorManager The CursorManager instance that manages the cursors.
	 * @throws Exception if the cursor doesn't exist or if an invalid argument type is provided.
	 */
	
	private void handleHideInstruction(ArrayList<Object> parameterList, CursorManager cursorManager) throws Exception {
		if (parameterList.get(0) instanceof Integer) {
			try {
				if(cursorManager.getCursors().get((Integer) parameterList.get(0)) == null) {
					throw new Exception("This cursor doesn't exist");
				}
				cursorManager.getCursors().get((Integer) parameterList.get(0)).setShowed(false);
				window.sysout("command HIDE done.");
			} catch (IllegalArgumentException e) {
				throw new Exception(e.getMessage());
			}
		}
		else if(parameterList.get(0) instanceof Double && isWholeDouble(parameterList)){
			try {
				double tmp = (double) parameterList.get(0);
				int arg = (int) tmp;
				if(cursorManager.getCursors().get(arg) == null) {
					throw new Exception("This cursor doesn't exist");
				}
				cursorManager.getCursors().get(arg).setShowed(false);
				window.sysout("command HIDE done.");
			} catch (IllegalArgumentException e) {
				throw new Exception(e.getMessage());
			}
		} else {

			throw new IllegalArgumentException("Invalid argument type for HIDE : " + parameterList.get(0));
		}
	}

	/**
	 * This method handles the instruction to adjust cursor opacity.
	 * 
	 * @param parameterList   The list of parameters passed to the instruction.
	 * @param tokens          The token type indicating the unit of opacity value (DOUBLE or PERCENT).
	 * @param cursorManager   The cursor manager managing the cursors.
	 * @throws Exception if an error occurs during the operation.
	 */
	
	private void handlePressInstruction(ArrayList<Object> parameterList, TokenType tokens, CursorManager cursorManager) throws Exception {
		if (tokens == TokenType.DOUBLE) {
			if (parameterList.size() == 1 && parameterList.get(0) instanceof Double) {
				double value = (double) parameterList.get(0);
				if (value > 1) {
					throw new Exception("Value should be between 0 and 1");
				}
				if (cursorManager.getActiveCursor().isEmpty()) {
					throw new Exception("No cursor selected.");
				}
				for (Cursor cursor : cursorManager.getActiveCursor().values()) {
					try {
						cursor.setOpacity(value);
						Color currentColor = cursor.getColor();
						Color colorWithOpacity = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), value);
						cursor.setColor(colorWithOpacity);
					} catch (IllegalArgumentException e) {
						throw new IllegalArgumentException(e.getMessage());
					}
				}
				window.sysout("command PRESS done.");
			} else {
				throw new IllegalArgumentException("Invalid argument type for PRESS");
			}
		} else if (tokens == TokenType.PERCENT){
			if (parameterList.size() == 1 && parameterList.get(0) instanceof Double) {
				double value = (double) parameterList.get(0);
				if (value > 100) {
					throw new IllegalArgumentException("Value should be between 0 and 100 : " + value);
				}
				if (cursorManager.getActiveCursor().isEmpty()) {
					throw new Exception("No cursor selected.");
				}
				for (Cursor cursor : cursorManager.getActiveCursor().values()) {
					try {
						cursor.setOpacity(value / 100);
						Color currentColor = cursor.getColor();
						Color colorWithOpacity = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), value / 100);
						cursor.setColor(colorWithOpacity);
					} catch (IllegalArgumentException e) {
						throw new IllegalArgumentException(e.getMessage());
					}
				}
				window.sysout("command PRESS done.");
			} else {
				throw new IllegalArgumentException("Invalid argument type for PRESS");
			}
		}
		else {
			throw new IllegalArgumentException("Invalid argument type for PRESS : " + parameterList.get(0) );
		}
	}
	
	/**
	 * This method handles the instruction to show a cursor.
	 * 
	 * @param parameterList   The list of parameters passed to the instruction.
	 * @param cursorManager   The cursor manager managing the cursors.
	 * @throws Exception if an error occurs during the operation.
	 */
	
	private void handleShowInstruction(ArrayList<Object> parameterList, CursorManager cursorManager) throws Exception {
		if (parameterList.get(0) instanceof Integer) {
			try {
				if(cursorManager.getCursors().get((Integer) parameterList.get(0)) == null) {
					throw new Exception("This cursor doesn't exist");
				}
				cursorManager.getCursors().get((Integer) parameterList.get(0)).setShowed(true);
				window.sysout("command SHOW done.");
			} catch (IllegalArgumentException e) {
				throw new Exception(e.getMessage());
			}
		} else if(parameterList.get(0) instanceof Double && isWholeDouble(parameterList)){
			try {
				double tmp = (double) parameterList.get(0);
				int arg = (int) tmp;
				if(cursorManager.getCursors().get(arg) == null) {
					throw new Exception("This cursor doesn't exist");
				}
				cursorManager.getCursors().get(arg).setShowed(true);
				window.sysout("command HIDE done.");
			} catch (IllegalArgumentException e) {
				throw new Exception(e.getMessage());
			}
		} else {
			throw new IllegalArgumentException("Invalid argument type for SHOW : " + parameterList.get(0));
		}
	}

	/**
	 * This method handles the instruction to create a new cursor.
	 * 
	 * @param parameterList   The list of parameters passed to the instruction.
	 * @param cursorManager   The cursor manager managing the cursors.
	 * @throws Exception if an error occurs during the operation.
	 */
	
	private void handleCursorInstruction(ArrayList<Object> parameterList, CursorManager cursorManager) throws Exception {
		if (parameterList.get(0) instanceof Integer) {
			try {
				if (!cursorManager.getHashs().contains(parameterList.get(0))) {
					Cursor cursor = new Cursor((Integer) parameterList.get(0));
					cursorManager.setNewCursor(cursor, (Integer) parameterList.get(0));
					window.updateCursors(cursorManager.getCursors());
					window.sysout("command CURSOR done.");
				} else {
					throw new Exception("This Cursor Already Exist\nNo cursor were added.");
				}
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage());
			} catch (Exception e) {
				throw new Exception("This Cursor Already Exist\nNo cursor were added.");
			}
		} else if (parameterList.get(0) instanceof Double && isWholeDouble(parameterList)){
			try {
				double tmp = (double) parameterList.get(0);
				int arg = (int) tmp;
				if (!cursorManager.getHashs().contains(arg)) {
					Cursor cursor = new Cursor(arg);
					cursorManager.setNewCursor(cursor, arg);
					window.updateCursors(cursorManager.getCursors());
					window.sysout("command CURSOR done.");
				} else {
					throw new Exception("This Cursor Already Exist\nNo cursor were added.");
				}
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage());
			} catch (Exception e) {
				throw new Exception("This Cursor Already Exist\nNo cursor were added.");
			}
		}else {
			throw new IllegalArgumentException("Invalid argument type for CURSOR");
		}
	}
	
	/**
	 * This method handles the instruction to select a cursor.
	 * 
	 * @param parameterList   The list of parameters passed to the instruction.
	 * @param cursorManager   The cursor manager managing the cursors.
	 * @throws Exception if an error occurs during the operation.
	 */
	
	private void handleSelectInstruction(ArrayList<Object> parameterList, CursorManager cursorManager) throws Exception {
		if (parameterList.get(0) instanceof Integer) {
			try {
				if(cursorManager.getCursors().get((Integer) parameterList.get(0)) == null) {
					throw new Exception("This cursor doesn't exist");
				}
				cursorManager.selectCursor((Integer) parameterList.get(0));
				cursorManager.setActiveCursor(cursorManager.getCursors());
				window.sysout("command SELECT done.");
			} catch (IllegalArgumentException e) {
				throw new Exception(e.getMessage());
			}
		} else if (parameterList.get(0) instanceof Double && isWholeDouble(parameterList)){
			try {
				double tmp = (double) parameterList.get(0);
				int arg = (int) tmp;
				if(cursorManager.getCursors().get(arg) == null) {
					throw new Exception("This cursor doesn't exist");
				}
				cursorManager.selectCursor(arg);
				cursorManager.setActiveCursor(cursorManager.getCursors());
				window.sysout("command SELECT done.");
			} catch (IllegalArgumentException e) {
				throw new Exception(e.getMessage());
			}
		} else {

			throw new IllegalArgumentException("Invalid argument type for SELECT");
		}
	}

	/**
	 * This method handles the instruction to remove a cursor.
	 * 
	 * @param parameterList   The list of parameters passed to the instruction.
	 * @param cursorManager   The cursor manager managing the cursors.
	 * @throws Exception if an error occurs during the operation.
	 */
	
	private void handleRemoveInstruction(ArrayList<Object> parameterList, CursorManager cursorManager) throws Exception {
		if (parameterList.get(0) instanceof Integer) {
			try {
				if(cursorManager.getCursors().get((Integer) parameterList.get(0)) == null) {
					throw new Exception("This cursor doesn't exist");
				}
				cursorManager.removeCursor((Integer) parameterList.get(0));
				cursorManager.setActiveCursor(cursorManager.getCursors());
				window.updateCursors(cursorManager.getCursors());
				window.sysout("command REMOVE done.");
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		} else if (parameterList.get(0) instanceof Double && isWholeDouble(parameterList)) {
			try {
				double tmp = (double) parameterList.get(0);
				int arg = (int) tmp;
				if(cursorManager.getCursors().get(arg) == null) {
					throw new Exception("This cursor doesn't exist");
				}
				cursorManager.removeCursor(arg);
				cursorManager.setActiveCursor(cursorManager.getCursors());
				window.updateCursors(cursorManager.getCursors());
				window.sysout("command REMOVE done.");
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		} else {
			throw new IllegalArgumentException("Invalid argument type for REMOVE");
		}
	}

	/**
	 * This method handles the instruction to set cursor color.
	 * 
	 * @param parameterList        The list of parameters passed to the instruction.
	 * @param cursorManager        The cursor manager managing the cursors.
	 * @param currentTokenInString The current token as a string.
	 * @throws Exception if an error occurs during the operation.
	 */
	
	private void handleColorInstruction(ArrayList<Object> parameterList, CursorManager cursorManager,String currentTokenInString) throws Exception {
		if (parameterList.size() == 1 && parameterList.get(0) instanceof String) {
			try {
				handleSimpleInstruction(new COLOR(), parameterList, cursorManager,currentTokenInString);
				window.sysout("command COLOR done.");
			}
			catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		} else if (parameterList.size() == 3 &&
				((parameterList.get(0) instanceof Integer || parameterList.get(0) instanceof Double ) && (parameterList.get(1) instanceof Integer || parameterList.get(1) instanceof Double) && (parameterList.get(2) instanceof Integer || parameterList.get(1) instanceof Double) ||
						parameterList.get(0) instanceof Double && parameterList.get(1) instanceof Double && parameterList.get(2) instanceof Double)) {
			try{
				handleSimpleInstruction(new COLOR(), parameterList, cursorManager,currentTokenInString);
			}
			catch (IllegalArgumentException e ) {
				throw new IllegalArgumentException(e.getMessage());
			}
		} else {
			throw new IllegalArgumentException("Invalid argument type for COLOR");
		}
	}

	/**
	 * This method evaluates a mathematical expression represented by the given list of objects.
	 * 
	 * @param expression The list representing the mathematical expression.
	 * @return The result of the evaluation.
	 */
	
	private static double evaluateExpression(ArrayList<Object> expression) {
		if (expression.isEmpty()) {
			throw new IllegalArgumentException("Empty expression");
		}

		double result = ((Number) expression.get(0)).doubleValue();
		for (int i = 1; i < expression.size(); i += 2) {
			if (!(expression.get(i) instanceof String)) {
				throw new IllegalArgumentException("invalid operator : " + expression.get(i));
			}

			String operator = (String) expression.get(i);
			if (i + 1 >= expression.size() || !(expression.get(i + 1) instanceof Number)) {
				throw new IllegalArgumentException("Missing operator after the value : " + operator);
			}

			double operand = ((Number) expression.get(i + 1)).doubleValue();
			switch (operator) {
			case "+":
				result += operand;
				break;
			case "-":
				result -= operand;
				break;
			case "*":
				result *= operand;
				break;
			case "/":
				if (operand == 0) {
					throw new ArithmeticException("Dividing by 0");
				}
				result /= operand;
				break;
			default:
				throw new IllegalArgumentException("Unknown operator: " + operator);
			}
		}
		return result;
	}

	/**
	 * This method concatenates string expressions into a single string.
	 * 
	 * @param expression The list representing the string expressions.
	 * @return The concatenated string.
	 */
	
	private String concatenateString(ArrayList<Object> expression) {
		if (expression.isEmpty()) {
			throw new IllegalArgumentException("Empty expression");
		}
		if (expression.size() == 1) {
			String str = (String) expression.get(0);
			return str.substring(1, str.length() - 1);
		} else {
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < expression.size(); i += 2) {
				String str = (String) expression.get(i);
				result.append(str.substring(1, str.length() - 1));
			}
			return result.toString();
		}
	}

	/**
	 * This method evaluates a boolean expression represented by the given list of objects.
	 * 
	 * @param parameterList The list representing the boolean expression.
	 * @return The result of the evaluation.
	 */
	
	private boolean evaluateBooleanExpression(ArrayList<Object> parameterList) {
	    boolean result = false;
	    boolean currentResult = false;
	    String currentOp = null;

	    for (int i = 0; i < parameterList.size(); i++) {
	        Object obj = parameterList.get(i);

	        if (obj instanceof Boolean) {
	            currentResult = (Boolean) obj;
	        } else if (obj instanceof String && obj.toString().equals("!")) {
	            i++;
	            System.out.println(parameterList.get(i));
	            if (parameterList.get(i).toString().equals("false") ||parameterList.get(i).toString().equals("true")) {
	            	String tmp = parameterList.get(i).toString();
	                currentResult = Boolean.parseBoolean(tmp);
	                currentResult = !currentResult;
	            } else {
	                throw new IllegalArgumentException("Invalid expression after '!'");
	            }
	        } else if (obj instanceof Integer || obj instanceof Double) {
	            double leftOperand = ((Number) obj).doubleValue();
	            i++;
	            String operator = parameterList.get(i).toString();
	            i++;
	            double rightOperand = ((Number) parameterList.get(i)).doubleValue();

	            switch (operator) {
	                case "<":
	                    currentResult = leftOperand < rightOperand;
	                    break;
	                case ">":
	                    currentResult = leftOperand > rightOperand;
	                    break;
	                case "<=":
	                    currentResult = leftOperand <= rightOperand;
	                    break;
	                case ">=":
	                    currentResult = leftOperand >= rightOperand;
	                    break;
	                case "==":
	                    currentResult = leftOperand == rightOperand;
	                    break;
	                case "!=":
	                    currentResult = leftOperand != rightOperand;
	                    break;
	                default:
	                    throw new IllegalArgumentException("Unknown operator: " + operator);
	            }
	        } else if (obj.toString().equals("&&")) {
	            result = currentOp == null ? currentResult : (currentOp.equals("||") ? result || currentResult : result && currentResult);
	            currentOp = "&&";
	        } else if (obj.toString().equals("||")) {
	            result = currentOp == null ? currentResult : (currentOp.equals("||") ? result || currentResult : result && currentResult);
	            currentOp = "||";
	        } else if (obj.toString().equals("true")) {
	            currentResult = true;
	        } else if (obj.toString().equals("false")) {
	            currentResult = false;
	        }
	    }

	    if (currentOp != null) {
	        result = currentOp.equals("||") ? result || currentResult : result && currentResult;
	    } else {
	        result = currentResult;
	    }

	    return result;
	}

	/**
	 * Checks if all double values in the list are whole numbers.
	 * 
	 * @param argsList The list of double values to be checked.
	 * @return True if all double values are whole numbers, false otherwise.
	 */
	
	private boolean isWholeDouble(ArrayList<Object> argsList) {
		for (Object obj : argsList) {
			if (obj instanceof Double) {
				Double doubleValue = (Double) obj;
				// Check if the double value is a whole number
				if (doubleValue % 1 != 0) {
					return false; // It's a double but not a whole number
				}
			}
		}
		return true; // All doubles have a whole number value
	}
}
