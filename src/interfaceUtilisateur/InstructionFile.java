package interfaceUtilisateur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The InstructionFile class handles reading instructions from a specified file.
 */
public class InstructionFile {
    private File instructionFile;

    /**
     * Constructor that accepts a file object.
     *
     * @param file the file to read instructions from
     */
    public InstructionFile(File file) {
        this.instructionFile = file; // Initialize the file object

        // Check if the file exists and notify if it does not
        if (!this.instructionFile.exists()) {
            System.out.println("Error: File does not exist.");
        }
    }

    /**
     * Getter method to retrieve the file object.
     *
     * @return the file object
     */
    public File getFile() {
        return instructionFile;
    }

    /**
     * Method to read words from the file and return them as a string.
     *
     * @return the content of the file as a string
     */
    public String getString() {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(instructionFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n"); // Append each line to the string with a newline character
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return text.toString(); // Return the complete content of the file as a string
    }
}
