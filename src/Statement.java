import java.util.ArrayList;

public abstract class Statement {
	protected boolean value;
	protected boolean known;

	public Statement(boolean value){
		this.value = value;
		this.known = true;
	}
	
	public Statement(boolean value, boolean known){
		this.value = value;
		this.known = known;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	public boolean isKnown() {
		return known;
	}

	public void setKnown(boolean known) {
		this.known = known;
	}

	public boolean equals(Statement other){
		if(getLabel().equals(other.getLabel()))
			return true;
		return false;
	}
	
	public abstract Statement getSwitchedValue();
	
	public abstract String getLabel();
}
