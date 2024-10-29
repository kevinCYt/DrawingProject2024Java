package functions.cursors;

import javafx.scene.paint.Color;

/**
 * The Cursor class represents a drawing cursor with properties like position, angle, color, and thickness.
 * The initial position and dimensions of the canvas are hardcoded.
 */
public class Cursor {
    private int posX;
    private int posY;
    private int id;
    private double angle; // in degrees
    private boolean showed = false;
    private Color color;
    private double thickness;
    private double opacity;
    private int dimCanvaX;
    private int dimCanvaY;
    private boolean selected = false;

    /**
     * Constructor to initialize a cursor with a specific ID.
     *
     * @param id the unique identifier for the cursor
     */
    public Cursor(int id) {
        this.setPosX(0);
        this.setPosY(0);
        this.setDimCanvaX(getDimCanvaX());
        this.setDimCanvaY(getDimCanvaY());
        this.setId(id);
        this.setColor(Color.BLUE);
        this.setOpacity(1);
    }

    /**
     * Checks if the cursor is displayed.
     *
     * @return true if the cursor is displayed, false otherwise
     */
    public boolean isShowed() {
        return showed;
    }

    /**
     * Sets the visibility of the cursor.
     *
     * @param showed true to show the cursor, false to hide it
     */
    public void setShowed(boolean showed) {
        this.showed = showed;
    }

    /**
     * Gets the ID of the cursor.
     *
     * @return the cursor ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the cursor.
     *
     * @param id the new cursor ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the Y position of the cursor.
     *
     * @return the Y position of the cursor
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Sets the Y position of the cursor.
     *
     * @param posY the new Y position of the cursor
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Gets the X position of the cursor.
     *
     * @return the X position of the cursor
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Sets the X position of the cursor.
     *
     * @param posX the new X position of the cursor
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Moves the cursor to the specified coordinates.
     *
     * @param x the new X position
     * @param y the new Y position
     */
    public void move(int x, int y) {
        this.setPosX(x);
        this.setPosY(y);
    }

    /**
     * Gets the thickness of the cursor's drawing line.
     *
     * @return the thickness of the drawing line
     */
    public double getThickness() {
        return thickness;
    }

    /**
     * Sets the thickness of the cursor's drawing line.
     *
     * @param thickness the new thickness
     */
    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    /**
     * Gets the color of the cursor.
     *
     * @return the color of the cursor
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the cursor.
     *
     * @param color the new color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the canvas width dimension.
     *
     * @return the canvas width dimension
     */
    public int getDimCanvaX() {
        return dimCanvaX;
    }

    /**
     * Sets the canvas width dimension.
     *
     * @param dimCanvaX the new canvas width dimension
     */
    public void setDimCanvaX(int dimCanvaX) {
        this.dimCanvaX = dimCanvaX;
    }

    /**
     * Gets the opacity of the cursor.
     *
     * @return the opacity of the cursor
     */
    public double getOpacity() {
        return opacity;
    }

    /**
     * Sets the opacity of the cursor.
     *
     * @param opacity the new opacity
     */
    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    /**
     * Gets the canvas height dimension.
     *
     * @return the canvas height dimension
     */
    public int getDimCanvaY() {
        return dimCanvaY;
    }

    /**
     * Sets the canvas height dimension.
     *
     * @param dimCanvaY the new canvas height dimension
     */
    public void setDimCanvaY(int dimCanvaY) {
        this.dimCanvaY = dimCanvaY;
    }

    /**
     * Gets the angle of the cursor in degrees.
     *
     * @return the angle of the cursor
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Sets the angle of the cursor in degrees.
     *
     * @param angle the new angle
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Checks if the cursor is selected.
     *
     * @return true if the cursor is selected, false otherwise
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selection state of the cursor.
     *
     * @param selected true to select the cursor, false to deselect it
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
