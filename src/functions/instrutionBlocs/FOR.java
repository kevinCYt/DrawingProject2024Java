package functions.instrutionBlocs;

import interfaceUtilisateur.Window;
import interpreteur.Variable;

// TODO: Auto-generated Javadoc
/**
 * The Class FOR.
 */
public class FOR extends InstructionBlock {
    
    /** The variable name. */
    private String variableName;
    
    /** The start value. */
    private double startValue;
    
    /** The end value. */
    private double endValue;
    
    /** The step. */
    private double step;
    
    /** The Constant NULL. */
    private static final double NULL=-1;
    
    /**
     * Instantiates a new for.
     *
     * @param window the window
     * @param variableName the variable name
     * @param startValue the start value
     * @param endValue the end value
     * @param step the step
     */
    public FOR(Window window, String variableName, int startValue, int endValue, int step) {
        super(window);
        this.variableName = variableName;
        if(startValue==NULL) {
        this.startValue = 0;
        }else {
        	this.startValue=startValue;
        }
        this.endValue = endValue;
        if(step==NULL) {
        	this.step = 1;
        }else {
        	this.step=step;
        }
        
    }

    /**
     * Gets the variable name.
     *
     * @return the variable name
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets the variable name.
     *
     * @param variableName the new variable name
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    /**
     * Gets the start value.
     *
     * @return the start value
     */
    public double getStartValue() {
        return startValue;
    }

    /**
     * Sets the start value.
     *
     * @param startValue the new start value
     */
    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    /**
     * Gets the end value.
     *
     * @return the end value
     */
    public double getEndValue() {
        return endValue;
    }

    /**
     * Sets the end value.
     *
     * @param endValue the new end value
     */
    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    /**
     * Gets the step.
     *
     * @return the step
     */
    public double getStep() {
        return step;
    }

    /**
     * Sets the step.
     *
     * @param step the new step
     */
    public void setStep(double step) {
        this.step = step;
    }

    /**
     * Funct FOR.
     *
     * @throws Exception the exception
     */
    public void funct_FOR() throws Exception {
        for (double i = startValue; i <= endValue; i += step) {
        	for(Variable var:window.getInterpreter().getVariableList()) {
        		if(var.getName()==this.getVariableName()) {
        			var.setValue(i);
        			break;
        		}
        	}
        	window.updateVariables(window.getInterpreter().getVariableList());
        	super.doLines();
            super.getWindow().setCurrentLine(getBeginningIndex());
        }skip();
    }
}
