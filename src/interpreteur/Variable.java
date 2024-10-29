package interpreteur;

import java.util.Objects;

public class Variable {
    private String name;
    private Object value;
    private TokenType type;

    public Variable(String name, Object value, TokenType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }
    
    public Variable() {
    	this.name = "";
    	this.value = 0;
    	this.type = TokenType.UNKNOWN_EXPRESSION;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }
    
    public String toString() {
    	return "" + this.name + " = " + this.value + "| Type : " + this.type;
    }
   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name);
    }
    
}
