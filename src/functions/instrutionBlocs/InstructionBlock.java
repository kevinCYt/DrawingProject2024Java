package functions.instrutionBlocs;

import java.util.ArrayList;
import interfaceUtilisateur.Window;
import interpreteur.Lexer;
import interpreteur.TokenType;

/**
 * The Class InstructionBlock.
 */
public class InstructionBlock {
	
	/** The beginning index. */
	private int beginningIndex;
	
	/** The end index. */
	private int endIndex;
	
	/** The window. */
	protected Window window;

	/**
	 * Gets the window.
	 *
	 * @return the window
	 */
	public Window getWindow() {
		return window;
	}

	/**
	 * Sets the window.
	 *
	 * @param window the new window
	 */
	public void setWindow(Window window) {
		this.window = window;
	}

	/**
	 * Gets the beginning index.
	 *
	 * @return the beginning index
	 */
	public int getBeginningIndex() {
		return beginningIndex;
	}

	/**
	 * Sets the beginning index.
	 *
	 * @param beginningIndex the new beginning index
	 */
	public void setBeginningIndex(int beginningIndex) {
		this.beginningIndex = beginningIndex;
	}

	/**
	 * Gets the end index.
	 *
	 * @return the end index
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * Sets the end index.
	 *
	 * @param endIndex the new end index
	 */
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * Instantiates a new instruction block.
	 *
	 * @param window the window
	 */
	public InstructionBlock(Window window) {
		this.window=window;
		this.beginningIndex=window.getCurrentLine();
	}
	
	/**
	 * Skip.
	 */
	public void skip() {
		String[] lines = window.getInstruction().getText().split("\\r?\\n");
		int nestedIfCount = 0;

		for (int i = window.getCurrentLine(); i < lines.length; i++) {
			ArrayList<TokenType> tokens = Lexer.tokenize(lines[i]);
			if (tokens.contains(TokenType.BEGIN)) {
				nestedIfCount++;
			} else if (tokens.contains(TokenType.END)) {
				nestedIfCount--;
				if (nestedIfCount == 0) {
					window.setCurrentLine(i);
					setEndIndex(i);
					break;
				}
			}
		}
	}


	/**
	 * Do lines.
	 *
	 * @throws Exception the exception
	 */
	public void doLines() throws Exception {
		String[] lines = window.getInstruction().getText().split("\\r?\\n");
		int nestedIfCount = 0;

		for (int i = window.getCurrentLine()+1; i < lines.length; i++) {
			ArrayList<TokenType> tokens = Lexer.tokenize(lines[i]);
			window.setCurrentLine(i);
			if (tokens.contains(TokenType.BEGIN)) {
				nestedIfCount++;
				try {
					window.nextStep();
				}catch(Throwable e) {
					throw e;
				}
				window.updateCursors(window.getCursorManager().getCursors());
				window.updateVariables(window.getInterpreter().getVariableList());
			} else if (tokens.contains(TokenType.END)) {
				nestedIfCount--;
				if(nestedIfCount==0) {
					try {
						window.nextStep();
					}catch(Throwable e) {
						throw e;
					}
					window.updateCursors(window.getCursorManager().getCursors());
					window.updateVariables(window.getInterpreter().getVariableList());
					setEndIndex(i);
				}

				break;
			}else if (nestedIfCount == 0) {
				try {
					window.nextStep();
				}catch(Throwable e) {
					throw e;
				}
				window.updateCursors(window.getCursorManager().getCursors());
				window.updateVariables(window.getInterpreter().getVariableList());
			} 
		}
	}
}
