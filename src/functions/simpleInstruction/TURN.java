package functions.simpleInstruction;

import functions.Function;
import functions.cursors.Cursor;
import interfaceUtilisateur.Window;

// Trigonometric order, in degree

/**
 * The TURN class implements the function to rotate the cursor by a specified degree.
 */
public class TURN implements Function {
	
    /**
     * Processes the arguments and rotates the cursor by the specified degree.
     *
     * @param window    the Window object for the application
     * @param cursor    the Cursor to rotate
     * @param tokenType the type of the token (not used in this function)
     * @param args      the arguments for the function
     * 
     * @throws IllegalArgumentException if the degree argument is not a valid number
     */
	@Override
	public void argument(Window window, Cursor cursor, String tokenType, Object... args) throws IllegalArgumentException {
		if (args.length == 1 && (args[0] instanceof Double || args[0] instanceof Integer)) {
			double degree;
			if (args[0] instanceof Double) {
				degree = (double) args[0];
			} else {
				degree = (int) args[0];
			}
			double tempAngle = cursor.getAngle() + degree;
			double finalAngle = tempAngle % 360;
			if (finalAngle < 0) {
				finalAngle += 360;
	        }
			
			cursor.setAngle(finalAngle);
		} else {
			throw new IllegalArgumentException("Degree argument must be a number.");
		}
	}
	
    /**
     * Returns the string representation of the TURN function.
     *
     * @return the string representation of the TURN function
     */
    @Override
    public String toString() {
    	return "TURN";
    }
}
