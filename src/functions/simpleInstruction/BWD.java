package functions.simpleInstruction;

import functions.Function;
import functions.cursors.Cursor;
import interfaceUtilisateur.Window;
import interpreteur.TokenType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The BWD class implements the backward movement function for a cursor on a canvas.
 */
public class BWD implements Function {
	
    /**
     * Processes the arguments and calls the appropriate drawing method based on the token type.
     *
     * @param window    the Window object for the application
     * @param cursor    the Cursor to move
     * @param tokenType the type of the token (INT or PERCENT)
     * @param args      the arguments for the function
     * 
     * @throws IllegalArgumentException if the arguments are invalid
     */
    @Override
    public void argument(Window window, Cursor cursor, String tokenType, Object... args) {
        if (args.length == 1 && (args[0] instanceof Integer || args[0] instanceof Double)) {
            if (tokenType == TokenType.INT.toString()) {
                int pixel = (Integer) args[0];
                drawBackward(window.getGc(), cursor, window.getCanvas(), pixel);
            } 
            else if (tokenType == TokenType.DOUBLE.toString()) {
            	double tmp = (double) args[0];
	            int pixel = (int) tmp;
	            drawBackward(window.getGc(), cursor, window.getCanvas(), pixel);
            }
            else if (tokenType == TokenType.PERCENT.toString()) {
                if (window.getCanvas() != null) {
                    double percentage = (Double) args[0];
                    int pixel = FWD.calculatePixelsFromPercentageWidth(window.getCanvas(), percentage);
                    drawBackward(window.getGc(), cursor, window.getCanvas(), pixel);
                } else {
                    throw new IllegalArgumentException("Canvas cannot be null for percentage calculation");
                }
            } else {
                throw new IllegalArgumentException("Invalid token type");
            }
        } else {
            throw new IllegalArgumentException("Invalid arguments for BWD function");
        }
    }
    
    /**
     * Draws the cursor backward by a specified number of pixels.
     *
     * @param gc     the GraphicsContext to draw on
     * @param cursor the Cursor to move
     * @param canvas the Canvas where the drawing occurs
     * @param pixel  the number of pixels to move backward
     */
    private void drawBackward(GraphicsContext gc, Cursor cursor, Canvas canvas, double pixel) {
        System.out.println("Drawing backward: " + pixel);
        double angle = cursor.getAngle();
        double angleRadians = Math.toRadians(angle);

        double endX = cursor.getPosX() - pixel * Math.cos(angleRadians); // Note the '-' sign for backward movement
        double endY = cursor.getPosY() - pixel * Math.sin(angleRadians); // Note the '-' sign for backward movement
        
        int endXInt = (int) Math.round(endX);
        int endYInt = (int) Math.round(endY);

        if (endXInt < 0 || endXInt > canvas.getWidth() || endYInt < 0 || endYInt > canvas.getHeight()) {
            return; // Check if end coordinates are within canvas bounds
        }

        gc.setStroke(Color.RED); // Changed color to red for backwards movement
        gc.strokeLine(cursor.getPosX(), cursor.getPosY(), endXInt, endYInt);

        cursor.setPosX(endXInt);
        cursor.setPosY(endYInt);
    }
    
    /**
     * Returns the string representation of the BWD function.
     *
     * @return the string representation of the BWD function
     */
    public String toString() {
    	return "BWD";
    }

}
