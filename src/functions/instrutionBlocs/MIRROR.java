package functions.instrutionBlocs;

import java.util.HashMap;
import java.util.Random;

import functions.cursors.Cursor;
import functions.simpleInstruction.FWD;
import functions.simpleInstruction.POS;
import interfaceUtilisateur.Window;

/**
 * The MIRROR class is an InstructionBlock that provides functionality to
 * mirror cursors around a specified point or line.
 */
public class MIRROR extends InstructionBlock {

    /**
     * Constructs a MIRROR object with the specified window.
     *
     * @param window The window object associated with this MIRROR instance.
     */
    public MIRROR(Window window) {
        super(window);
    }

    /**
     * Mirrors the cursors around a specified point.
     * The point can be specified using either integer pixel values or double percentage values.
     *
     * @param x1 The x-coordinate of the point to mirror around, in pixels or percentage.
     * @param x2 The y-coordinate of the point to mirror around, in pixels or percentage.
     * @throws Exception if an error occurs during mirroring.
     */
    public void mirrorFunction(Object x1, Object x2) throws Exception {
        int posx;
        int posy;
        if (x1 instanceof Integer && x2 instanceof Integer) {
            posx = (int) x1;
            posy = (int) x2;
        } else if (x1 instanceof Double && x2 instanceof Double) {
            posx = (int) FWD.calculatePixelsFromPercentageWidth(window.getCanvas(), (double) x1);
            posy = (int) POS.calculatePixelsFromPercentageHeight(window.getCanvas(), (double) x2);
        } else {
            throw new IllegalArgumentException("Invalid arguments for MIRROR function");
        }

        HashMap<Integer, Cursor> originalCursors = new HashMap<>(window.getCursorManager().getActiveCursor());
        System.out.println("Original cursors: " + originalCursors);

        for (int originalCursorId : originalCursors.keySet()) {
            Cursor originalCursor = originalCursors.get(originalCursorId);
            int tempID = findUniqueKey(window.getCursorManager().getCursors());
            Cursor tempCursor = new Cursor(tempID);

            tempCursor.setPosX(2 * posx - originalCursor.getPosX());
            tempCursor.setPosY(2 * posy - originalCursor.getPosY());
            tempCursor.setSelected(true);
            tempCursor.setAngle((originalCursor.getAngle() + 180) % 360); // Correct angle calculation
            tempCursor.setColor(window.getCursorManager().getCursors().get(originalCursorId).getColor());
            tempCursor.setThickness(window.getCursorManager().getCursors().get(originalCursorId).getThickness());
            tempCursor.setOpacity(window.getCursorManager().getCursors().get(originalCursorId).getOpacity());
            window.getCursorManager().setNewCursor(tempCursor, tempID);
            window.getCursorManager().setActiveCursor(window.getCursorManager().getCursors());
            
            try {
                doLines();
            } catch (Exception e) {
                throw e;
            } finally {
                window.getCursorManager().removeCursor(tempID);
                window.getCursorManager().setActiveCursor(originalCursors);
            }
            window.getCursorManager().displayActiveCursors();
            System.out.println("Cursor with tempID " + tempID + " removed");
            super.getWindow().setCurrentLine(getBeginningIndex());
        }
        skip();
    }

    /**
     * Mirrors the cursors around a specified line defined by two points.
     * The points can be specified using either integer pixel values or double percentage values.
     *
     * @param x1 The x-coordinate of the first point of the line, in pixels or percentage.
     * @param y1 The y-coordinate of the first point of the line, in pixels or percentage.
     * @param x2 The x-coordinate of the second point of the line, in pixels or percentage.
     * @param y2 The y-coordinate of the second point of the line, in pixels or percentage.
     * @throws Exception if an error occurs during mirroring.
     */
    public void mirrorFunction(Object x1, Object y1, Object x2, Object y2) throws Exception {
        int posx1, posy1, posx2, posy2;

        if (x1 instanceof Integer && y1 instanceof Integer && x2 instanceof Integer && y2 instanceof Integer) {
        	posx1 = (int) x1;
            posy1 = (int) y1;
            posx2 = (int) x2;
            posy2 = (int) y2;
        } else if (x1 instanceof Double && y1 instanceof Double && x2 instanceof Double && y2 instanceof Double) {
            posx1 = FWD.calculatePixelsFromPercentageWidth(window.getCanvas(), (double) x1);
            posy1 = POS.calculatePixelsFromPercentageHeight(window.getCanvas(), (double) y1);
            posx2 = FWD.calculatePixelsFromPercentageWidth(window.getCanvas(), (double) x2);
            posy2 = POS.calculatePixelsFromPercentageHeight(window.getCanvas(), (double) y2);
        } else {
            throw new IllegalArgumentException("Invalid arguments for MIRROR function");
        }

        int[] mirrorPos = new int[2];
        int mirrorX, mirrorY;
        HashMap<Integer, Cursor> originalCursors = new HashMap<>(window.getCursorManager().getActiveCursor());

        for (int originalCursorId : originalCursors.keySet()) {
            Cursor originalCursor = originalCursors.get(originalCursorId);
            int tempID = findUniqueKey(window.getCursorManager().getCursors());
            Cursor tempCursor = new Cursor(tempID);

            int x = originalCursor.getPosX();
            int y = originalCursor.getPosY();

            
            mirrorPos = findSymmetricPoint(posx1,posy1,posx2,posy2,x,y,(int)window.getCanvas().getWidth(),(int)window.getCanvas().getHeight());

            mirrorX = mirrorPos[0];
            mirrorY = mirrorPos[1];
            
            System.out.println("Original Position: (" + x + ", " + y + ")");
            System.out.println("Mirrored Position: (" + mirrorX + ", " + mirrorY + ")");

            tempCursor.setPosX((int) mirrorX);
            tempCursor.setPosY((int) mirrorY);
            tempCursor.setSelected(true);
            tempCursor.setAngle((originalCursor.getAngle() + 180) % 360);
            tempCursor.setColor(window.getCursorManager().getCursors().get(originalCursorId).getColor());
            tempCursor.setThickness(window.getCursorManager().getCursors().get(originalCursorId).getThickness());
            tempCursor.setOpacity(window.getCursorManager().getCursors().get(originalCursorId).getOpacity());

            window.getCursorManager().setNewCursor(tempCursor, tempID);
            window.getCursorManager().setActiveCursor(window.getCursorManager().getCursors());
            window.getCursorManager().displayActiveCursors();
            try {
                doLines();
            } catch (Exception e) {
                throw e;
            } finally {
                window.getCursorManager().removeCursor(tempID);
                window.getCursorManager().setActiveCursor(originalCursors);
            }
            window.getCursorManager().displayActiveCursors();
            System.out.println("Cursor with tempID " + tempID + " removed");
            super.getWindow().setCurrentLine(getBeginningIndex());
        }
        skip();
    }

    /**
     * Finds the symmetric point of a given point (Px, Py) with respect to a line segment defined by (Ax, Ay) and (Bx, By).
     *
     * @param Ax           The x-coordinate of the first point of the line.
     * @param Ay           The y-coordinate of the first point of the line.
     * @param Bx           The x-coordinate of the second point of the line.
     * @param By           The y-coordinate of the second point of the line.
     * @param Px           The x-coordinate of the point to reflect.
     * @param Py           The y-coordinate of the point to reflect.
     * @param canvasWidth  The width of the canvas.
     * @param canvasHeight The height of the canvas.
     * @return An array containing the x and y coordinates of the symmetric point.
     */
    public static int[] findSymmetricPoint(int Ax, int Ay, int Bx, int By, int Px, int Py, int canvasWidth, int canvasHeight) {
        // Check if the line segment is vertical
        if (Ax == Bx) {
            // Reflect across the y-axis
            return new int[]{canvasWidth-1 - Px, Py};
        }
        
        // Check if the line segment is horizontal
        if (Ay == By) {
            // Reflect across the x-axis
            return new int[]{Px, canvasHeight-1 - Py};
        }
        
        // Calculate the slope
        double m = ((double) (By - Ay)) / (Bx - Ax);

        // Calculate the y-intercept
        double b = Ay - m * Ax;

        // Calculate the equation of the perpendicular passing through P
        double xi = (m * (Py + ((double) Px / m) - b)) / (m * m + 1);
        double yi = m * xi + b;

        // Calculate the coordinates of the symmetric point
        int xSymmetric = (int) (2 * xi - Px);
        int ySymmetric = (int) (2 * yi - Py);

        // Scale the symmetric point coordinates based on canvas dimensions
        xSymmetric = (int) ((double) xSymmetric / canvasWidth);
        ySymmetric = (int) ((double) ySymmetric / canvasHeight);

        return new int[]{xSymmetric, ySymmetric};
    }




    public static int findUniqueKey(HashMap<Integer, Cursor> map) {
        Random rand = new Random();
        int key;

        do {
            key = rand.nextInt(Integer.MAX_VALUE);
        } while (map.containsKey(key));

        return key;
    }
}
