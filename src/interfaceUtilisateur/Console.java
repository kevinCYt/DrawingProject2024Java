package interfaceUtilisateur;

import functions.cursors.Cursor;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * The Console class represents the console panel in the user interface.
 * It provides methods for displaying console messages and executing commands.
 */
public class Console {
	private VBox console;
	private TextArea consoleText;
	private TextField inputText;
	
	/**
     * Getter method for the console panel.
     *
     * @return the console panel
     */
	public VBox getConsole() {
		return console;
	}

    /**
     * Setter method for the console panel.
     *
     * @param console the console panel to set
     */
	public void setConsole(VBox console) {
		this.console = console;
	}

    /**
     * Getter method for the console text area.
     *
     * @return the console text area
     */
	public TextArea getConsoleText() {
		return consoleText;
	}

    /**
     * Setter method for the console text area.
     *
     * @param consoleText the console text area to set
     */
	public void setConsoleText(TextArea consoleText) {
		this.consoleText = consoleText;
	}

    /**
     * Getter method for the input text field.
     *
     * @return the input text field
     */
	public TextField getInputText() {
		return inputText;
	}

    /**
     * Setter method for the input text field.
     *
     * @param inputText the input text field to set
     */
	public void setInputText(TextField inputText) {
		this.inputText = inputText;
	}

    /**
     * Constructor for the Console class.
     * Initializes the console panel, console text area, and input text field.
     *
     * @param width  the width of the console panel
     * @param height the height of the console panel
     */
    public Console(double width, double height) {
        consoleText = new TextArea();
        inputText = new TextField();
        console = new VBox();
        
        consoleText.setEditable(false);
        consoleText.setStyle("-fx-text-fill: tomato;");
        consoleText.setPrefHeight(height * 0.6);
        
        inputText.setPrefHeight(height * 0.05);
        
        Label consoleLabel = new Label("Console");

        VBox.setMargin(consoleLabel, new Insets(2));
        VBox.setMargin(consoleText, new Insets(2));
        VBox.setMargin(inputText, new Insets(2));
        
        console.getChildren().addAll(consoleLabel, consoleText, inputText);
    }

    /**
     * Executes the command entered in the console input text field.
     * Displays any errors or warnings in the console text area.
     *
     * @param window the main application window
     * @throws Throwable 
     */
	public void executeConsole(Window window) throws Throwable {
		try {
			window.getCursorCanvas().getGraphicsContext2D().clearRect(0, 0, window.getCursorCanvas().getWidth(), window.getCursorCanvas().getHeight());
			window.getInterpreter().executeLine(this.getInputText().getText(),window.getCursorManager());
            GraphicsContext cursorGc = window.getCursorCanvas().getGraphicsContext2D();
            for (int id : window.getCursorManager().getActiveCursor().keySet()) {
            	Cursor cursor = window.getCursorManager().getActiveCursor().get(id);
                if (cursor.isShowed()) {
                    Window.drawImageAtCursor(cursorGc, cursor);
                }
            }
            window.updateCursors(window.getCursorManager().getCursors());
    		
		} catch (Exception e) {
			String newMessage = e.getMessage() + " for the Unit instruction ";
			Window.showAlert(AlertType.WARNING, "Be Careful", e.getMessage(), window.getErrorStop(), window.getConsole());
			consoleText.setText(getConsoleText().getText() + newMessage + "\n");
		}

    }
}
