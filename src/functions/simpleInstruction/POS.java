
package functions.simpleInstruction;

import functions.Function;
import functions.cursors.Cursor;
import interfaceUtilisateur.Window;
import interpreteur.TokenType;
import javafx.scene.canvas.Canvas;

/**
 * The POS class implements the function to set the position of the cursor on the canvas.
 */
public class POS implements Function {
	
    /**
     * Processes the arguments and sets the cursor position based on the provided coordinates.
     *
     * @param window    the Window object for the application
     * @param cursor    the Cursor to move
     * @param tokenType the type of the token (not used in this function)
     * @param args      the arguments for the function
     * 
     * @throws IllegalArgumentException if the argument size is invalid or if the arguments are not of the correct types
     */
	@Override
	public void argument(Window window, Cursor cursor, String tokenType, Object... args) {
		if (args.length == 2) {
			int posX = 0;
            int posY = 0;
			if (tokenType == TokenType.PERCENT.toString()) {
				if (window.getCanvas() != null) {
                    double percentage1 = (Double) args[0];
                    double percentage2 = (Double) args[1];
                    posX = FWD.calculatePixelsFromPercentageWidth(window.getCanvas(), percentage1);
                    posY = calculatePixelsFromPercentageHeight(window.getCanvas(), percentage2);
				}
			}
			
			else if (args[0] instanceof Integer && args[1] instanceof Integer) {
            	posX = (int) args[0];
            	posY = (int) args[1];
            }
            else if (args[0] instanceof Double && args[1] instanceof Double) {
            	double tmp1 = (double) args[0];
            	double tmp2 = (double) args[1];
            	posX = (int) tmp1;
            	posY = (int) tmp2;
            }
            else if (args[0] instanceof Integer && args[1] instanceof Double) {
            	posX = (int) args[0];
            	double tmp2 = (double) args[1];
            	posY = (int) tmp2;
            }
            else if (args[0] instanceof Double && args[1] instanceof Integer) {
            	double tmp1 = (double) args[0];
            	posX = (int) tmp1;
            	posY = (int) args[1];
            }
            else {
            	throw new IllegalArgumentException("Invalid arguments for POS :" + args[0] + " " + args[1]);
            }
			if (posX < 0) {
				posX = 0;
			}
			else if (posX > window.getCanvas().getWidth()) {
				posX = (int) Math.round(window.getCanvas().getWidth());
			}
			
			
			if (posY < 0) {
				posY = 0;
			}
			else if (posY > window.getCanvas().getHeight()) {
				posY = (int) Math.round(window.getCanvas().getHeight());
			}
			
			cursor.setPosX(posX);
			cursor.setPosY(posY);
		}
		else {
			throw new IllegalArgumentException("Invalid argument size");
		}
	}
	
    /**
     * Returns the string representation of the POS function.
     *
     * @return the string representation of the POS function
     */
    public String toString() {
    	return "POS";
    }
    
    /**
     * Calculates the number of pixels from a percentage of the canvas height.
     *
     * @param canvas     the Canvas object representing the drawing area
     * @param percentage the percentage of the canvas height
     * @return the number of pixels calculated from the percentage of the canvas height
     */
    public static int calculatePixelsFromPercentageHeight(Canvas canvas, double percentage) {
        // Assuming percentage represents a fraction of the canvas size
        double canvasHeight = canvas.getHeight();
        // Calculate the smaller dimension (either width or height) to use as a reference for percentage calculation
        double reference = canvasHeight;
        // Calculate pixels based on the percentage of the reference dimension
        int roundedInt = (int) Math.round((percentage / 100.0) * reference);
        return roundedInt;
    }
}
