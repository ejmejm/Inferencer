
public class Variable extends Statement{
	private String label;

	public Variable(String label, boolean value){
		super(value);
		this.label = label;
	}

	public Variable(String label, boolean value, boolean known){
		super(value, known);
		this.label = label;
	}
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
