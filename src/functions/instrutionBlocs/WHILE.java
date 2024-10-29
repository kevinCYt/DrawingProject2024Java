package functions.instrutionBlocs;

import interfaceUtilisateur.Window;

/**
 * The Class WHILE.
 */
public class WHILE extends InstructionBlock{
	
	/** The count. */
	private int count=0;
	
	/**
	 * Instantiates a new while.
	 *
	 * @param window the window
	 */
	public WHILE(Window window) {
		super(window);
	}
	
	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * While funct.
	 *
	 * @param value the value
	 * @throws Exception 
	 */
	public void while_funct(boolean value) throws Exception {
		System.out.println(value);
		if(this.getCount()<1500) {
			this.setCount(this.getCount()+1);
			if (value) {
				try {
					doLines();
					super.getWindow().setCurrentLine(getBeginningIndex() - 1);
				} catch (Throwable e) {
					skip();
					window.setCurrentLine(window.getCurrentLine()-1);
					throw e;
				}
			} else {
				skip();
			}
		}else {
			skip();
			StackOverflowError e = new StackOverflowError("The WHILE loop had more than 1500 occurrences and had been stopped.");
			throw e;
		}
		
		
		
	}
}
