package functions.simpleInstruction;

import java.awt.geom.Point2D;

import functions.Function;
import functions.cursors.Cursor;
import interfaceUtilisateur.Window;
import interpreteur.TokenType;

/**
 * The MOV class implements the function to move the cursor to a new position on the canvas.
 */
public class MOV implements Function {

    /**
     * Calculates the angle between two points in degrees.
     *
     * @param point1 the first point
     * @param point2 the second point
     * @return the angle between the points in degrees
     */
    public static double calculateAngle(Point2D.Double point1, Point2D.Double point2) {
        // Calculate vector coordinates
        double dx = point2.x - point1.x;
        double dy = point2.y - point1.y;

        // Calculate angle in radians
        double angleRadians = Math.atan2(dy, dx);

        // Convert angle to degrees
        double angleDegrees = Math.toDegrees(angleRadians);

        // Adjust angle to be positive in the trigonometric circle
        if (angleDegrees < 0) {
            angleDegrees += 360;
        }

        return angleDegrees;
    }

    /**
     * Processes the arguments and moves the cursor to the specified position.
     *
     * @param window    the Window object for the application
     * @param cursor    the Cursor to move
     * @param tokenType the type of the token (not used in this function)
     * @param args      the arguments for the function
     * 
     * @throws IllegalArgumentException if the cursor is null, if the argument size is invalid, or if the arguments are not of the correct types
     */
    @Override
    public void argument(Window window, Cursor cursor, String tokenType, Object... args) {
        if (cursor == null) {
            throw new IllegalArgumentException("No cursor selected");
        }

        if (args.length == 2) {
            int pixelX = 0;
            int pixelY = 0;
            if (tokenType == TokenType.PERCENT.toString()) {
				if (window.getCanvas() != null) {
                    double percentage1 = (Double) args[0];
                    double percentage2 = (Double) args[1];
                    pixelX = FWD.calculatePixelsFromPercentageWidth(window.getCanvas(), percentage1);
                    pixelY = POS.calculatePixelsFromPercentageHeight(window.getCanvas(), percentage2);
				}
            }
			else if (args[0] instanceof Integer && args[1] instanceof Integer) {
            	pixelX = (int) args[0];
            	pixelY = (int) args[1];
            }
            else if (args[0] instanceof Double && args[1] instanceof Double) {
            	double tmp1 = (double) args[0];
            	double tmp2 = (double) args[1];
            	pixelX = (int) tmp1;
            	pixelY = (int) tmp2;
            }
            else if (args[0] instanceof Integer && args[1] instanceof Double) {
            	pixelX = (int) args[0];
            	double tmp2 = (double) args[1];
            	pixelY = (int) tmp2;
            }
            else if (args[0] instanceof Double && args[1] instanceof Integer) {
            	double tmp1 = (double) args[0];
            	pixelX = (int) tmp1;
            	pixelY = (int) args[1];
            }
            else {
            	throw new IllegalArgumentException("Invalid arguments for MOV :" + args[0] + args[1]);
            }

            int endX = cursor.getPosX() + pixelX;
            int endY = cursor.getPosY() + pixelY;

            if (endX < 0) {
                endX = 0;
            } else if (endX > window.getCanvas().getWidth()) {
                endX = (int) window.getCanvas().getWidth();
            }

            if (endY < 0) {
                endY = 0;
            } else if (endY > window.getCanvas().getHeight()) {
                endY = (int) window.getCanvas().getHeight();
            }

            // Angle formed by the line defined by the two start and end points and the x-axis in the trigonometric circle (in degrees)
            Point2D.Double point1 = new Point2D.Double(cursor.getPosX(), cursor.getPosY());
            Point2D.Double point2 = new Point2D.Double(endX, endY);
            double angleFwdDegree = MOV.calculateAngle(point1, point2);

            // Difference between this angle and the cursor orientation
            int angleFwdDegreeInt = (int) Math.round(angleFwdDegree);
            int angleCursorInt = (int) cursor.getAngle();
            int complementAngle = Math.abs(angleFwdDegreeInt - angleCursorInt);

            // Compensate the angle
            if (cursor.getAngle() < angleFwdDegreeInt) {
                cursor.setAngle(cursor.getAngle() + complementAngle);
            } else {
                cursor.setAngle(cursor.getAngle() - complementAngle);
            }

            // Determine the distance to travel
            double distanceX = endX - cursor.getPosX();
            double distanceY = endY - cursor.getPosY();
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            int intDistance = (int) Math.round(distance);

            FWD fwd = new FWD();
            // Use FWD
            fwd.argument(window, cursor, "INT", intDistance);
            } else {
            	throw new IllegalArgumentException("Invalid arguments size");
            }
        }
    
    /**
     * Returns the string representation of the MOV function.
     *
     * @return the string representation of the MOV function
     */
    public String toString() {
    	return "MOV";
    }
}
