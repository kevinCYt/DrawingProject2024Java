package functions.simpleInstruction;

import java.awt.geom.Point2D;

import functions.Function;
import functions.cursors.Cursor;
import interfaceUtilisateur.Window;
import interpreteur.TokenType;


/**
 * The LOOKAT class implements the function to orient the cursor towards a specific point or another cursor.
 */
public class LOOKAT implements Function {
	
    /**
     * Processes the arguments and adjusts the cursor orientation based on the provided arguments.
     *
     * @param window        the Window object for the application
     * @param currentCursor the current Cursor to orient
     * @param tokenType     the type of the token ("coordinates" or "cursor")
     * @param args          the arguments for the function
     * 
     * @throws IllegalArgumentException if the argument size is invalid or if the arguments are not of the correct types
     */
	@Override
	public void argument(Window window, Cursor currentCursor, String tokenType, Object... args) {
		if (args.length == 2) {
			int X = 0;
			int Y = 0;
			if (tokenType == TokenType.PERCENT.toString()) {
				if (window.getCanvas() != null) {
                    double percentage1 = (Double) args[0];
                    double percentage2 = (Double) args[1];
                    X = FWD.calculatePixelsFromPercentageWidth(window.getCanvas(), percentage1);
                    Y = POS.calculatePixelsFromPercentageHeight(window.getCanvas(), percentage2);
				}
			}
			if (args[0] instanceof Integer && args[1] instanceof Integer) {
            	X = (int) args[0];
            	Y = (int) args[1];
            }
            else if (args[0] instanceof Double && args[1] instanceof Double) {
            	double tmp1 = (double) args[0];
            	double tmp2 = (double) args[1];
            	X = (int) tmp1;
            	Y = (int) tmp2;
            }
            else if (args[0] instanceof Integer && args[1] instanceof Double) {
            	X = (int) args[0];
            	double tmp2 = (double) args[1];
            	Y = (int) tmp2;
            }
            else if (args[0] instanceof Double && args[1] instanceof Integer) {
            	double tmp1 = (double) args[0];
            	X = (int) tmp1;
            	Y = (int) args[1];
            }
            else {
            	throw new IllegalArgumentException("Invalid arguments for LOOKAT :" + args[0] + args[1]);
            }
			
			// Angle formed by the line defined by the two start and end points and the x-axis in the trigonometric circle (in degrees)
	        Point2D.Double point1 = new Point2D.Double(currentCursor.getPosX(), currentCursor.getPosY());
	        Point2D.Double point2 = new Point2D.Double(X, Y);
	        double angleFwdDegree = MOV.calculateAngle(point1, point2);
	        
	        // Difference between this angle and the cursor orientation
	        int angleFwdDegreeInt = (int) Math.round(angleFwdDegree);
	        int angleCursorInt = (int) currentCursor.getAngle();
	        int complementAngle = Math.abs(angleFwdDegreeInt - angleCursorInt);
	        
	        // Compensate the angle
	        if (currentCursor.getAngle() < angleFwdDegreeInt) {
	        	currentCursor.setAngle(currentCursor.getAngle() + complementAngle);
	        }
	        else {
	        	currentCursor.setAngle(currentCursor.getAngle() - complementAngle);
	        }
			
			} else if(args.length == 1 && args[0] instanceof Integer) {
				if (args[0] instanceof Integer) {
					Cursor targetCursor = window.getCursorManager().getCursors().get(args[0]);
					argument(window, currentCursor, "", targetCursor.getPosX(), targetCursor.getPosY());
				}
				else if (args[0] instanceof Double) {
					double tmp3 = (double) args[0];
					int cursorId = (int) tmp3;
					Cursor targetCursor = window.getCursorManager().getCursors().get(cursorId);
					argument(window, currentCursor, "", targetCursor.getPosX(), targetCursor.getPosY());
				}
			} else {
				throw new IllegalArgumentException("Invalid argument size");
			}
	}
	
    /**
     * Returns the string representation of the LOOKAT function.
     *
     * @return the string representation of the LOOKAT function
     */
    public String toString() {
    	return "LOOKAT";
    }

}
