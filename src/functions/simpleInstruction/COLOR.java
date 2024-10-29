package functions.simpleInstruction;

import functions.Function;
import functions.cursors.Cursor;
import interfaceUtilisateur.Window;
import javafx.scene.paint.Color;

/**
 * The COLOR class implements the color change function for a cursor.
 */
public class COLOR implements Function {
	
    /**
     * Processes the arguments and sets the cursor color based on the provided arguments.
     *
     * @param window     the Window object for the application
     * @param cursor     the Cursor to change color
     * @param tokenType  the type of the token (not used in this function)
     * @param args       the arguments for the function
     * 
     * @throws IllegalArgumentException if the arguments are invalid
     */
	@Override
    public void argument(Window window, Cursor cursor, String tokenType, Object... args) {
        // Hex color argument
        if (args.length == 1 && args[0] instanceof String) {
            String hex = (String) args[0];
            if (hex.charAt(0) == '#') {
                hex = hex.substring(1);
            }

            if (hex.length() == 6) {
                try {
                    int r = Integer.parseInt(hex.substring(0, 2), 16);
                    int g = Integer.parseInt(hex.substring(2, 4), 16);
                    int b = Integer.parseInt(hex.substring(4, 6), 16);
                    System.out.println(r+""+g+""+b);
                    Color newColor = Color.rgb(r, g, b, cursor.getOpacity());
                    cursor.setColor(newColor);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid hex string: " + hex);
                }
            } else {
            	throw new IllegalArgumentException("Hex string must be 6 characters long: " + hex);
            }
        }
        // RGB integer arguments
        else if (args.length == 3 && areAllIntsOrWholeDoubles(args)) {
            int r = ((Number) args[0]).intValue();
            int g = ((Number) args[1]).intValue();
            int b = ((Number) args[2]).intValue();

            if (isValidRGB(r, g, b)) {
            	Color newColor = Color.rgb(r, g, b, cursor.getOpacity());
                cursor.setColor(newColor);
                System.out.println("New color set with RGB values: " + r + ", " + g + ", " + b);
            } else {
                throw new IllegalArgumentException("RGB values must be between 0 and 255: " + r + ", " + g + ", " + b);
            }
        }
        // RGB double arguments (normalized to [0, 1])
        else if (args.length == 3 && args[0] instanceof Double && args[1] instanceof Double && args[2] instanceof Double) {
            double r = (double) args[0];
            double g = (double) args[1];
            double b = (double) args[2];

            if (isValidNormalizedRGB(r, g, b)) {
            	Color newColor = new Color(r, g, b, cursor.getOpacity());
                cursor.setColor(newColor);
                System.out.println("New color set with normalized RGB values: " + r + ", " + g + ", " + b);
            } else {
                throw new IllegalArgumentException("Normalized RGB values must be between 0 and 1: " + r + ", " + g + ", " + b);
            }
        } else {
            throw new IllegalArgumentException("Invalid arguments");
        }
    }
	
	/**
	 * Checks if all elements in the array are either integers or whole doubles.
	 *
	 * @param args the array of arguments
	 * @return true if all elements are integers or whole doubles, false otherwise
	 */
	public static boolean areAllIntsOrWholeDoubles(Object[] args) {
        for (Object arg : args) {
            if (!(arg instanceof Integer || (arg instanceof Double && isWholeNumber((Double) arg)))) {
                return false;
            }
        }
        return true;
    }

	/**
	 * Checks if a double is a whole number.
	 *
	 * @param number the double value
	 * @return true if the double is a whole number, false otherwise
	 */
    public static boolean isWholeNumber(Double number) {
        return number % 1 == 0;
    }

    /**
     * Validates if the RGB values are within the range 0-255.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @return true if valid, false otherwise
     */
    private boolean isValidRGB(int r, int g, int b) {
        if ((r >= 0 && r <= 255) && (g >= 0 && g <= 255) && (b >= 0 && b <= 255)){
        	return true;
        }
        return false;
    }

    /**
     * Validates if the normalized RGB values are within the range 0-1.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @return true if valid, false otherwise
     */
    private boolean isValidNormalizedRGB(double r, double g, double b) {
        if ((r >= 0.0 && r <= 1.0) && (g >= 0.0 && g <= 1.0) && (b >= 0.0 && b <= 1.0)) {
        	return true;
        }
        return false;
    }
    
    /**
     * Returns the string representation of the COLOR function.
     *
     * @return the string representation of the COLOR function
     */
    public String toString() {
    	return "COLOR";
    }
}
