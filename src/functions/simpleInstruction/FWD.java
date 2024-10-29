package functions.simpleInstruction;

import functions.Function;
import functions.cursors.Cursor;
import interfaceUtilisateur.Window;
import interpreteur.TokenType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * The FWD class implements the forward movement function for a cursor on a canvas.
 */
public class FWD implements Function {
	
    /**
     * Processes the arguments and calls the appropriate drawing method based on the token type.
     *
     * @param window     the Window object for the application
     * @param cursor     the Cursor to move
     * @param tokenType  the type of the token (INT or PERCENT)
     * @param args       the arguments for the function
     * 
     * @throws IllegalArgumentException if the canvas is null for percentage calculation, if the token type is invalid, or if the arguments are not of the correct types
     */
    @Override
    public void argument(Window window, Cursor cursor, String tokenType, Object... args) {
        if (args.length == 1 && (args[0] instanceof Integer || args[0] instanceof Double)) {
            if (tokenType == TokenType.INT.toString()) {
                int pixel = (Integer) args[0];
                drawForward(window.getGc(), cursor, window.getCanvas(), pixel);
            } else if (tokenType == TokenType.DOUBLE.toString()){
            	double tmp = (double) args[0];
	            int pixel = (int) tmp;
	            drawForward(window.getGc(), cursor, window.getCanvas(), pixel);
            }
            else if (tokenType == TokenType.PERCENT.toString()) {
                if (window.getCanvas() != null) {
                    double percentage = (Double) args[0];
                    int pixel = calculatePixelsFromPercentageWidth(window.getCanvas(), percentage);
                    drawForward(window.getGc(), cursor, window.getCanvas(), pixel);
                } else {
                    throw new IllegalArgumentException("Canvas cannot be null for percentage calculation");
                }
            } else {
                throw new IllegalArgumentException("Invalid token type");
            }
        } else {
            throw new IllegalArgumentException("Invalid arguments for FWD function");
        }
    }


    /**
     * Draws the cursor forward by a specified number of pixels.
     *
     * @param gc     the GraphicsContext to draw on
     * @param cursor the Cursor to move
     * @param canvas the Canvas where the drawing occurs
     * @param pixel  the number of pixels to move forward
     */
    private void drawForward(GraphicsContext gc, Cursor cursor, Canvas canvas, int pixel) {
        System.out.println("Drawing forward: " + pixel);
        double angle = cursor.getAngle();
        double angleRadians = Math.toRadians(angle);

        double endX = cursor.getPosX() + pixel * Math.cos(angleRadians);
        double endY = cursor.getPosY() + pixel * Math.sin(angleRadians);
        
        int endXInt = (int) Math.round(endX);
        int endYInt = (int) Math.round(endY);

        if (endXInt < 0) {
            endXInt = 0; // Check if end coordinates are within canvas bounds
        }
        else if (endXInt > canvas.getWidth()) {
        	endXInt = (int) Math.round(canvas.getWidth());
        }
        
        if (endYInt < 0) {
        	endYInt = 0;
        }
        else if (endYInt > canvas.getHeight()) {
        	endYInt = (int) Math.round(canvas.getHeight());
        }

	   gc.setStroke(cursor.getColor());
	   gc.strokeLine(cursor.getPosX(), cursor.getPosY(), endXInt, endYInt);

        cursor.setPosX(endXInt);
        cursor.setPosY(endYInt);
    }
    
    /**
     * Calculates the number of pixels corresponding to a percentage of the canvas size.
     *
     * @param canvas     the Canvas where the drawing occurs
     * @param percentage the percentage of the canvas size
     * @return the number of pixels corresponding to the percentage
     */
    public static int calculatePixelsFromPercentageWidth(Canvas canvas, double percentage) {
        // Assuming percentage represents a fraction of the canvas size
        double canvasWidth = canvas.getWidth();
        // Calculate the smaller dimension (either width or height) to use as a reference for percentage calculation
        double reference = canvasWidth;
        // Calculate pixels based on the percentage of the reference dimension
        int roundedInt = (int) Math.round((percentage / 100.0) * reference);
        return roundedInt;
    }
    
    /**
     * Returns the string representation of the FWD function.
     *
     * @return the string representation of the FWD function
     */
    public String toString() {
    	return "FWD";
    }

}
