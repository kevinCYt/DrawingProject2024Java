package interfaceUtilisateur;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

/**
 * FileManager handles file operations such as saving and loading canvas content,
 * creating new text files, and saving text file content.
 */
public class FileManager {
    
    /**
     * Handles the save action to save the canvas as a PNG file.
     * @param canvas the canvas to be saved as an image
     * @throws Exception 
     */
    protected static void onSave(Window window) throws Exception {
        
        if(!window.isModified()) {
        	throw new Exception("Canva is empty and cannot be saved");
        }else {
        	// Create a writable image with the canvas dimensions
            WritableImage writableImage = new WritableImage((int)window.getCanvas().getWidth(), (int)window.getCanvas().getHeight());
            window.getCanvas().snapshot(null, writableImage);
        	FileChooser fileChooser = new FileChooser();
            
            // Set extension filter for PNG files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            
            // Show save file dialog
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                saveFile(writableImage, file);
            }
        }
    }

	
    /**
     * Saves the content as an image file.
     * @param content the writable image content to be saved
     * @param file the file where the content will be saved
     */
    protected static void saveFile(WritableImage content, File file) {
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(content, null), "png", file);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Handles the load action to load instructions from a file.
     * @param canvas the canvas where the instructions will be applied
     * @param instruction the TextArea where instructions will be displayed
     * @param currentFileLabel the label displaying the current file name
     */
    protected static void onLoad(Canvas canvas, TextArea instruction, Label currentFileLabel) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        
        if (file != null) {
            InstructionFile instructionFile = new InstructionFile(file);
            instruction.setText(instructionFile.getString());
            currentFileLabel.setText("Current File: " + file.getName());
        }
    }
    
    /**
     * Handles the creation of a new text file.
     * @param currentFileLabel the label displaying the current file name
     * @param instruction the TextArea where instructions will be displayed
     * @param errorStop boolean indicating if errors should stop the process
     * @param console the console for outputting messages
     */
    protected static void createNewTextFile(Label currentFileLabel, TextArea instruction, boolean errorStop, Console console) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                if (file.createNewFile()) {
                    Window.showAlert(Alert.AlertType.INFORMATION, "File Created", "New text file created successfully.", errorStop, console);
                    // Update the current file label with the new file name
                    currentFileLabel.setText("Current File: " + file.getName());
                    
                    // Clear the current content of the text area
                    instruction.clear();
                } else {
                    Window.showAlert(Alert.AlertType.WARNING, "File Exists", "File already exists.", errorStop, console);
                }
            } catch (IOException ex) {
                Window.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while creating the file.", errorStop, console);
            }
        }
    }

    /**
     * Handles saving the edited text file.
     * @param instruction the TextArea containing the text to be saved
     * @param errorStop boolean indicating if errors should stop the process
     * @param console the console for outputting messages
     */
    protected static void saveTextFile(TextArea instruction, boolean errorStop, Console console) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(instruction.getText());
                fileWriter.close();
                Window.showAlert(Alert.AlertType.INFORMATION, "File Saved", "Text file saved successfully.", errorStop, console);
            } catch (IOException ex) {
                Window.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving the file.", errorStop, console);
            }
        }
    }
}
