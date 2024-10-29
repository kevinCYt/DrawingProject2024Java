package functions;

import functions.cursors.Cursor;
import interfaceUtilisateur.Window;


public interface Function {
     /**
     * Performs the operation based on the provided arguments.
     *
     * @param window     the application window
     * @param cursor     the cursor used in the application
     * @param tokenType  the type of token associated with the function
     * @param args       the arguments passed to the function
     */
    void argument(Window window, Cursor cursor,String tokenType, Object... args);
}
