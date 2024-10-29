package functions.simpleInstruction;

import functions.Function;
import functions.cursors.Cursor;
import interfaceUtilisateur.Window;

/**
 * The THICK class implements the function to set the thickness of the cursor's drawing line.
 */
public class THICK implements Function {
	
    /**
     * Processes the arguments and sets the thickness of the cursor's drawing line.
     *
     * @param window    the Window object for the application
     * @param cursor    the Cursor to set the thickness
     * @param tokenType the type of the token (not used in this function)
     * @param args      the arguments for the function
     * 
     * @throws IllegalArgumentException if the argument is not a valid number
     */
	@Override
	public void argument(Window window, Cursor cursor, String tokenType, Object... args) throws IllegalArgumentException {
		if (args.length == 1 && args[0] instanceof Double) {
			double thickness = (double) args[0];
			
			cursor.setThickness(thickness);
			window.getGc().setLineWidth(thickness);
		} else {
			throw new IllegalArgumentException("Invalid argument type, expected double but found: " + args[0]);
		}
	}
	
    /**
     * Returns the string representation of the THICK function.
     *
     * @return the string representation of the THICK function
     */
    @Override
    public String toString() {
    	return "THICK";
    }
}
