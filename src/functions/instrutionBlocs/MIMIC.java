package functions.instrutionBlocs;

import java.util.ArrayList;

import functions.cursors.Cursor;
import functions.simpleInstruction.FWD;
import functions.simpleInstruction.POS;
import interfaceUtilisateur.Window;
import interpreteur.Lexer;
import interpreteur.TokenType;

/**
 * The MIMIC class is an InstructionBlock that provides functionality to mimic the movement
 * and actions of a cursor at specified coordinates.
 */
public class MIMIC extends InstructionBlock{
	
	private int cursorID;
	private int tempID;

	/**
	 * Constructs a MIMIC object with the specified window.
	 *
	 * @param window The window object associated with this MIMIC instance.
	 */
	public MIMIC(Window window) {
		super(window);
	}
	
	/**
	 * Mimics the cursor's movement to the specified coordinates.
	 * The coordinates can be specified using either integer pixel values or double percentage values.
	 *
	 * @param id  The ID of the cursor to mimic.
	 * @param x1  The x-coordinate of the position to move to, in pixels or percentage.
	 * @param x2  The y-coordinate of the position to move to, in pixels or percentage. 
	 * @throws Exception if an error occurs during mimicking.
	 */
	public void mimicfunction(int id, Object x1, Object x2) throws Throwable {
		int posX = 0;
		int posY = 0;
		if(x1 instanceof Double && x2 instanceof Double) {
			posX = FWD.calculatePixelsFromPercentageWidth(window.getCanvas(),(double) x1);
			posY = POS.calculatePixelsFromPercentageHeight(window.getCanvas(),(double) x2);
		}
		else if(x1 instanceof Integer && x2 instanceof Integer){
			posX = (int) x1;
			posY = (int) x2;
		}
		else {
			throw new IllegalArgumentException("Invalid arguments for MIMIC function");
		}
		int tempID = MIRROR.findUniqueKey(window.getCursorManager().getCursors());
		Cursor tempCursor = new Cursor(tempID);
		tempCursor.setPosX(posX);
		tempCursor.setPosY(posY);
        window.getCursorManager().setNewCursor(tempCursor, tempID);
		window.getCursorManager().getCursors().put(tempID, tempCursor);
		this.cursorID = id;
		this.tempID = tempID;
        try {
            doLines();
        } catch (Throwable e) {
			skip();
            throw e;
        } finally {
        	window.getCursorManager().displayCursors();
        	window.getCursorManager().removeCursor(tempID);
            window.getCursorManager().setActiveCursor(window.getCursorManager().getCursors());
        }
        skip();
        System.out.println("Cursor with tempID " + tempID + " removed");
	}
	
	/**
	 * Executes the instructions, handling nested BEGIN and END tokens.
	 * This method processes the instructions line by line and handles cursor actions accordingly.
	 * @throws Exception if an error occurs during instruction execution.
	 */
	@Override
	public void doLines() throws Exception {
	    String[] lines = window.getInstruction().getText().split("\\r?\\n");
	    int nestedIfCount = 0;

	    System.out.println("Starting doLines execution");

	    for (int i = window.getCurrentLine() + 1; i < lines.length; i++) {
	        ArrayList<TokenType> tokens = Lexer.tokenize(lines[i]);
	        window.setCurrentLine(i);
	        
	        // Debugging output for each line
	        System.out.println("Processing line " + i + ": " + lines[i]);
		window.getCursorManager().getCursors().get(tempID).setSelected(window.getCursorManager().getCursors().get(cursorID).isSelected()); // setting the selection of the new cursor to be the same as the Mimic one
	        window.getCursorManager().setActiveCursor(window.getCursorManager().getCursors());
		if (tokens.contains(TokenType.BEGIN)) {
	            nestedIfCount++;
	            try {
	            	window.nextStep();
	            }catch(Throwable e) {
					skip();
					throw e;
	            }
	        	
	        } else if (tokens.contains(TokenType.END) && nestedIfCount == 0) {
	            nestedIfCount--;
	            setEndIndex(i);
	            break;
	        } else if (nestedIfCount == 0) {
	        	window.getCursorManager().displayCursors();
	            try {
	            	window.nextStep();
	            }catch(Throwable e) {
					skip();
					throw e;
	            }
	        } 
	    }
	}

}
