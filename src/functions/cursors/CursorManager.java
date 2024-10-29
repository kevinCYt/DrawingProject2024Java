package functions.cursors;

import java.util.HashMap;
import java.util.Set;

/**
 * The CursorManager class manages multiple cursors, allowing selection, activation, and removal of cursors.
 */
public class CursorManager {
    private HashMap<Integer, Cursor> cursors = new HashMap<>();
    private HashMap<Integer, Cursor> activeCursor = new HashMap<>();

    /**
     * Adds a new cursor to the manager.
     *
     * @param cursor the cursor to add
     * @param id     the ID of the cursor
     */
    public void setNewCursor(Cursor cursor, int id) {
        this.cursors.put(id, cursor);
    }

    /**
     * Gets the active cursors.
     *
     * @return a HashMap of active cursors
     */
    public HashMap<Integer, Cursor> getActiveCursor() {
        return activeCursor;
    }

    /**
     * Gets all the cursors managed by this manager.
     *
     * @return a HashMap of all cursors
     */
    public HashMap<Integer, Cursor> getCursors() {
        return this.cursors;
    }

    /**
     * Gets the IDs of all the cursors managed by this manager.
     *
     * @return a Set of cursor IDs
     */
    public Set<Integer> getHashs() {
        return getCursors().keySet();
    }

    /**
     * Toggles the selection state of a cursor.
     *
     * @param id the ID of the cursor to select or deselect
     */
    public void selectCursor(int id) {
        Cursor cursor = this.cursors.get(id);
        if (cursor == null) {
            throw new IllegalArgumentException("The cursor does not exist");
        }
        cursor.setSelected(!cursor.isSelected());
    }

    /**
     * Removes a cursor from the manager.
     *
     * @param id the ID of the cursor to remove
     */
    public void removeCursor(int id) {
        this.cursors.remove(id);
    }

    /**
     * Sets the active cursors based on their selection state.
     *
     * @param cursors a HashMap of cursors to consider for activation
     */
    public void setActiveCursor(HashMap<Integer, Cursor> cursors) {
        this.activeCursor.clear(); // Clear existing active cursors
        for (int id : cursors.keySet()) {
            Cursor cursor = cursors.get(id);
            if (cursor.isSelected()) {
                this.activeCursor.put(id, cursor);
            }
        }
    }

    /**
     * Displays the list of all cursors and their selection state.
     */
    public void displayCursors() {
        System.out.println("Liste des curseurs :");
        for (int id : cursors.keySet()) {
            Cursor cursor = cursors.get(id);
            System.out.println("ID : " + id + ", Selectionné : " + cursor.isSelected());
        }
    }

    /**
     * Displays the list of active cursors and their selection state.
     */
    public void displayActiveCursors() {
        System.out.println("Liste des curseurs actifs :");
        for (int id : activeCursor.keySet()) {
            Cursor cursor = activeCursor.get(id);
            System.out.println("ID : " + id + ", Selectionné : " + cursor.isSelected());
        }
    }
}
