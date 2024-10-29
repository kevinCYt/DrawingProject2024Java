package functions.instrutionBlocs;

import interfaceUtilisateur.Window;


// TODO: Auto-generated Javadoc
/**
 * The Class IF.
 */
public class IF extends InstructionBlock {
    

	/**
	 * Instantiates a new if.
	 *
	 * @param window the window
	 */
	public IF(Window window) {
		super(window);
	}
    
    /**
     * If funct.
     *
     * @param value the value
     */
    public void if_funct(boolean value) {
    	System.out.println(value);
    	if (value) {
            try {
                doLines();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            skip();
        }
    }
}
