package interfaceUtilisateur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import functions.cursors.Cursor;
import functions.cursors.CursorManager;
import interpreteur.Evaluator;
import interpreteur.Variable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The Window class represents the main application window.
 * It extends the JavaFX Application class and provides methods for initializing
 * and setting up the user interface components.
 */
public class Window extends Application {
	private Canvas mainCanvas;
	private Canvas cursorCanvas;
	private TextArea instruction;
	private Slider speed;
	private Button nextStep;
	private Button validate;
	private GraphicsContext gc;
	private int currentLine = 0;
	private Evaluator interpreter;
	private CursorManager cursorManager;
	private TextField commandInput;
	private Button executeButton;
	private Label currentFileLabel = new Label("Current File: None");
	private boolean errorStop = true;
	private Console console;
	private ListView<String> cursorList;
	private ListView<String> variableList;
	private boolean modified=false;

	public Canvas getMainCanvas() {
		return mainCanvas;
	}

	public void setMainCanvas(Canvas mainCanvas) {
		this.mainCanvas = mainCanvas;
	}

	public ListView<String> getCursorList() {
		return cursorList;
	}

	public void setCursorList(ListView<String> cursorList) {
		this.cursorList = cursorList;
	}

	public ListView<String> getVariableList() {
		return variableList;
	}

	public void setVariableList(ListView<String> variableList) {
		this.variableList = variableList;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public void setCursorCanvas(Canvas cursorCanvas) {
		this.cursorCanvas = cursorCanvas;
	}

	public Canvas getCanvas() {
		return mainCanvas;
	}

	public void setCanvas(Canvas canvas) {
		this.mainCanvas = canvas;
	}

	public TextArea getInstruction() {
		return instruction;
	}

	public void setInstruction(TextArea instruction) {
		this.instruction = instruction;
	}

	public Slider getSpeed() {
		return speed;
	}

	public Canvas getCursorCanvas() {
		return cursorCanvas;
	}

	public void setSpeed(Slider speed) {
		this.speed = speed;
	}

	public Button getNextStep() {
		return nextStep;
	}

	public void setNextStep(Button nextStep) {
		this.nextStep = nextStep;
	}

	public Button getValidate() {
		return validate;
	}

	public void setValidate(Button validate) {
		this.validate = validate;
	}

	public GraphicsContext getGc() {
		return gc;
	}

	public void setGc(GraphicsContext gc) {
		this.gc = gc;
	}

	public Evaluator getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(Evaluator interpreter) {
		this.interpreter = interpreter;
	}

	public CursorManager getCursorManager() {
		return cursorManager;
	}

	public void setCursorManager(CursorManager cursorManager) {
		this.cursorManager = cursorManager;
	}

	public TextField getCommandInput() {
		return commandInput;
	}

	public void setCommandInput(TextField commandInput) {
		this.commandInput = commandInput;
	}

	public Button getExecuteButton() {
		return executeButton;
	}

	public void setExecuteButton(Button executeButton) {
		this.executeButton = executeButton;
	}

	public Label getCurrentFileLabel() {
		return currentFileLabel;
	}

	public void setCurrentFileLabel(Label currentFileLabel) {
		this.currentFileLabel = currentFileLabel;
	}

	public Console getConsole() {
		return console;
	}

	public void setConsole(Console console) {
		this.console = console;
	}

	public boolean getErrorStop() {
		return this.errorStop;
	}

	public void setErrorStop(boolean errorStop) {
		this.errorStop = errorStop;
	}

    /**
     * Starts the application by initializing the main stage.
     * 
     * @param primaryStage The primary stage of the application.
     */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Chromat-Ynk");
		BorderPane root = new BorderPane();
		primaryStage.setMaximized(true);
		initialize(root);
		Scene scene = new Scene(root, 1920, 1000);
		primaryStage.setScene(scene);
		cursorList = new ListView<>();
		variableList = new ListView<>();
		HashMap<Integer, Cursor> activeCursor = new HashMap<Integer, Cursor>();
		VBox rightColumn = new VBox();
		rightColumn.getChildren().addAll(new Label("Cursors"), cursorList, new Label("Variables"), variableList);
		root.setRight(rightColumn);
		Evaluator evaluator = new Evaluator(this, activeCursor);
		this.interpreter = evaluator;
		this.cursorManager = new CursorManager();
		primaryStage.show();
	}

    /**
     * Initializes the components of the application window.
     * 
     * @param root The root BorderPane of the application window.
     */
	private void initialize(BorderPane root) {
		VBox topContainer = new VBox();
		topContainer.getChildren().addAll(setupMenu(), currentFileLabel);
		root.setTop(topContainer);
		setupInstructionPanel(root);
		setupCanvas(root);
		setupCommandPanel(root);
		console = new Console(root.getWidth(), 100);
		Button nextConsole = new Button("Execute");
		nextConsole.setOnAction(arg0 -> {
			try {
				console.executeConsole(this);
			} catch (Throwable e1) {
				if (errorStop) {
					showAlert(Alert.AlertType.ERROR, "Error", e1.getMessage(),getErrorStop(),console);
				}
			}
		});
		console.getConsole().getChildren().add(nextConsole);
		root.setBottom(console.getConsole());
	}

    /**
     * Sets up the canvas for drawing on the main stage.
     * 
     * @param root The root BorderPane of the application window.
     */
	private void setupCanvas(BorderPane root) {
		mainCanvas = new Canvas(1000, 561);
		cursorCanvas = new Canvas(1000, 561);

		gc = mainCanvas.getGraphicsContext2D();

		StackPane canvasContainer = new StackPane(mainCanvas, cursorCanvas);
		canvasContainer.setStyle("-fx-background-color: white; -fx-border-color: grey; -fx-border-width: 1;");
		root.setCenter(canvasContainer);
	}

    /**
     * Updates the list of cursors displayed in the user interface.
     * 
     * @param cursors A HashMap containing the active cursors.
     */
	public void updateCursors(HashMap<Integer, Cursor> cursors) {
		cursorList.getItems().clear();
		List<String> cursorKeys = cursors.keySet().stream().map(Object::toString).collect(Collectors.toList());
		for (String string : cursorKeys) {
			cursorList.getItems().add("Cursor n°" + string);
		}
	}

    /**
     * Updates the list of variables displayed in the user interface.
     * 
     * @param variables An ArrayList containing the variables.
     */
	public void updateVariables(ArrayList<Variable> variables) {
		variableList.getItems().clear();
		for (Variable variable : variables) {
			variableList.getItems().add(variable.getType() + " " + variable.getName() + "=" + variable.getValue().toString());
		}
	}

    /**
     * Sets up the menu bar for the application window.
     * 
     * @return The configured MenuBar object.
     */
	private MenuBar setupMenu() {
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		fileMenu.getItems().addAll(
				createMenuItem("New Text File", e -> FileManager.createNewTextFile(currentFileLabel, instruction, getErrorStop(), console)),
				createMenuItem("Load File", e -> onLoad()),
				createMenuItem("Save Text File", e -> FileManager.saveTextFile(instruction, errorStop, console)),
				createMenuItem("Export Drawing", e -> {
					try {
						FileManager.onSave(this);
					} catch (Exception e1) {
						if (errorStop) {
							showAlert(Alert.AlertType.ERROR, "Error", e1.getMessage(),getErrorStop(),console);
						}
					}
				}),
				createMenuItem("Exit", e -> System.exit(0))
				);
		Menu errorMenu = new Menu("Error");

		ToggleGroup errorGroup = new ToggleGroup();
		RadioMenuItem skipErrorMenuItem = new RadioMenuItem("Skip Error");
		skipErrorMenuItem.setOnAction(e -> {
			errorStop = false;
			System.out.println("Variable errorStop set to false");
		});
		skipErrorMenuItem.setToggleGroup(errorGroup);
		skipErrorMenuItem.setSelected(false);

		RadioMenuItem stopErrorMenuItem = new RadioMenuItem("Stop Error");
		stopErrorMenuItem.setOnAction(e -> {
			errorStop = true;
			System.out.println("Variable errorStop set to true");
		});
		stopErrorMenuItem.setSelected(true);
		stopErrorMenuItem.setToggleGroup(errorGroup);

		errorMenu.getItems().addAll(skipErrorMenuItem, stopErrorMenuItem);

		menuBar.getMenus().addAll(fileMenu, errorMenu);
		return menuBar;
	}


    /**
     * Loads a file into the application window.
     */
	public void onLoad() {
		FileManager.onLoad(mainCanvas, instruction, currentFileLabel);
		setCurrentLine(0);
	}

    /**
     * Clears the drawings from the canvas.
     * 
     * @param window The Window object representing the application window.
     */
	public static void eraseDrawing(Window window) {
		window.getGc().clearRect(0, 0, window.getCanvas().getWidth(), window.getCanvas().getHeight());
		window.cursorCanvas.getGraphicsContext2D().clearRect(0, 0, window.cursorCanvas.getWidth(), window.cursorCanvas.getHeight());
		window.getInterpreter().getVariableList().clear();
		window.getCursorManager().getActiveCursor().clear();
		window.getCursorManager().getCursors().clear();
		window.updateCursors(window.getCursorManager().getCursors());
		window.updateVariables(window.getInterpreter().getVariableList());
		window.setCurrentLine(0);
		window.getInterpreter().getWhileList().clear();
		window.setModified(false);
		System.out.println("Canva Cleared");
	}

	
    /**
     * Creates a MenuItem object for the menu.
     * 
     * @param text   The text to display on the menu item.
     * @param action The action to perform when the menu item is clicked.
     * @return The created MenuItem object.
     */
	private MenuItem createMenuItem(String text, EventHandler<ActionEvent> action) {
		MenuItem menuItem = new MenuItem(text);
		menuItem.setOnAction(action);
		return menuItem;
	}

    /**
     * Sets up the instruction panel on the left side of the application window.
     * 
     * @param root The root BorderPane of the application window.
     */
	private void setupInstructionPanel(BorderPane root) {
		instruction = new TextArea();
		instruction.setPrefSize(200, 500);
		instruction.setPromptText("Enter instructions or load a file");
		instruction.setEditable(false);    
		speed = new Slider(0,1,0.5);
		Label speedLabel = new Label("Speed: 0,50");
		speed.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				double speedValue = (double) speed.getValue();
				String formattedSpeed = String.format("%.2f", speedValue);
				speedLabel.setText("Speed: " + formattedSpeed);
			}
		});
		validate = new Button("Validate");
		nextStep = new Button("Next");
		nextStep.setPrefWidth(266);
		Button erase=new Button("Clear Canva");
		erase.setOnAction(e -> eraseDrawing(this));
		HBox sliderAndValidateContainer = new HBox(5);
		sliderAndValidateContainer.getChildren().addAll(speedLabel,speed, validate);
		validate.setOnAction(arg0->{
			onValidate();
		});
		nextStep.setOnAction(arg0 -> {
			try {
				nextStep();
			}catch(Throwable e) {
				// printing the error
				if (errorStop) {
					showAlert(Alert.AlertType.ERROR, "Error", e.getMessage(),getErrorStop(),console);
				}
			}
		});
		// Set up the layout for buttons
		HBox buttonContainer = new HBox(5);
		buttonContainer.getChildren().addAll(nextStep);
		VBox instructionPanel = new VBox(5);
		instructionPanel.getChildren().addAll(instruction, sliderAndValidateContainer, buttonContainer,erase);
		root.setLeft(instructionPanel);
	}

	 /**
     * Handles the validation of instructions input by the user.
     */
	private void onValidate() {
		double speedValue = speed.getValue(); // Get the current speed value from the slider
		int numberOfLines = instruction.getText().split("\\n").length; // Calculate the number of instruction lines

		// Create a new Timeline for handling periodic execution of instructions
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(
				// Set the duration for each execution cycle based on the speed value
				Duration.seconds(4 * (speedValue + 0.01)),
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// Check if there are remaining lines to execute
						if (currentLine < numberOfLines) {
							try {
								nextStep(); // Execute the next instruction step
							} catch (Throwable ex) {
								// Display an error alert and stop the timeline on exception
								if(errorStop) {
									showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage(), getErrorStop(), console);
									timeline.stop();
								} else {
									currentLine += 1;
								}
							}
						} else {
							timeline.stop(); // Stop the timeline when all lines are processed
						}
					}
				}
				));

		// Set the timeline to run indefinitely until stopped explicitly
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play(); // Start the timeline
	}

    /**
     * Sets up the command panel at the bottom of the application window.
     * 
     * @param root The root BorderPane of the application window.
     */
	private void setupCommandPanel(BorderPane root) {
		HBox bottomContainer = new HBox();
		commandInput = new TextField();
		executeButton = new Button("Execute");
		executeButton.setOnAction(event -> {
			try {
				interpreter.executeLine(commandInput.getText(), cursorManager);
				commandInput.clear();
			} catch (Throwable e) {
				showAlert(AlertType.ERROR, "Execution Error", e.getMessage(), errorStop, console);
			}
		});
		bottomContainer.getChildren().addAll(new Label("Command:"), commandInput, executeButton);
		root.setBottom(bottomContainer);
	}

    /**
     * Gets the current line number in the instruction TextArea.
     * 
     * @return The current line number.
     */
	public int getCurrentLine() {
		return currentLine;
	}

    /**
     * Sets the current line number in the instruction TextArea.
     * 
     * @param currentLine The current line number.
     */
	public void setCurrentLine(int currentLine) {
		this.currentLine = currentLine;
	}

	
    /**
     * Executes the next step of instruction processing.
     * 
     * @throws Exception If an error occurs during instruction execution.
     */
	public void nextStep() throws Exception {
		if (currentLine == instruction.getText().split("\\n").length) {
			currentLine = 0;
			showAlert(AlertType.INFORMATION, "All done", "All instruction have been executed", true, console);
			return;
		}
		try {
			// Split the input text into individual lines
			String[] lines = instruction.getText().split("\\r?\\n");
			if (this.getCurrentLine() < 0 || this.getCurrentLine() > lines.length) {
				throw new IllegalArgumentException("Invalid line number: " + this.getCurrentLine());
			}

			cursorCanvas.getGraphicsContext2D().clearRect(0, 0, cursorCanvas.getWidth(), cursorCanvas.getHeight());

			interpreter.executeLine(lines[this.getCurrentLine()], getCursorManager());

			GraphicsContext cursorGc = cursorCanvas.getGraphicsContext2D();
			for (int id : cursorManager.getCursors().keySet()) {
				Cursor cursor = cursorManager.getCursors().get(id);
				if (cursor.isShowed()) {
					drawImageAtCursor(cursorGc, cursor);
				}
			}
		} catch (Throwable e) {
			String newMessage = e.getMessage() + " at line " + currentLine;
			this.console.getConsoleText().setText(this.console.getConsoleText().getText()+ newMessage + "\n");
			throw new Error(newMessage, e); // Rethrow the exception with the new message
		}
		finally {
			this.setCurrentLine(getCurrentLine() + 1);
		}
	}

    /**
     * Draws the cursor image on the canvas at the specified position and angle.
     * 
     * @param gc     The GraphicsContext of the canvas.
     * @param cursor The Cursor object representing the cursor to draw.
     */
	public static void drawImageAtCursor(GraphicsContext gc, Cursor cursor) {
		double posX = cursor.getPosX();
		double posY = cursor.getPosY();
		double angle = cursor.getAngle();

		Image image = new Image("curseur.png");
		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();

		gc.save();
		gc.translate(posX, posY); 
		gc.rotate(angle);
		gc.drawImage(image, -imageWidth / 2, -imageHeight / 2);
		gc.restore();
	}

    /**
     * Shows an alert dialog on the application window.
     * 
     * @param alertType The type of alert dialog (e.g., ERROR, INFORMATION).
     * @param title     The title of the alert dialog.
     * @param content   The content text of the alert dialog.
     * @param wait      Indicates whether to wait for user interaction (true) or not (false).
     * @param console   The Console object for displaying messages.
     */
	public static void showAlert(AlertType alertType, String title, String content, boolean wait, Console console) {
		// Schedule the alert to be shown on the JavaFX application thread
		Platform.runLater(() -> {
			Alert alert = new Alert(alertType);
			alert.setTitle(title);
			alert.setContentText(content);
			if (wait) {
				alert.showAndWait();
			} else {
				alert.show();
			}
		});
	}
	
    /**
     * Prints a message to the console.
     * 
     * @param message The message to print.
     */
	public void sysout(String message) {
		this.console.getConsoleText().setText(this.console.getConsoleText().getText()+message+"\n");
	}

    /**
     * The main method to launch the application.
     * 
     * @param args Command-line arguments.
     */
	public static void main(String[] args) {
		launch(args);
	}
}
