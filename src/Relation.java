
public class Relation extends Statement{
	public enum RelationType{
		NEGATION, CONJUNCTION, DISJUNCTION, IMPLIES, EQUIVALENCE
	}
	
	private RelationType type;
	private Statement s1;
	private Statement s2;

	public Relation(RelationType type, Statement s1, boolean value) {
		super(value);
		if(type != RelationType.NEGATION)
			System.err.println("ERROR: A non atomic relation was created with only one variable");
		this.type = type;
		this.s1 = s1;
		this.s2 = null;
	}
	
	public Relation(RelationType type, Statement s1, Statement s2, boolean value) {
		super(value);
		this.type = type;
		this.s1 = s1;
		this.s2 = s2;
	}

	public Relation(RelationType type, Statement s1, boolean value, boolean known) {
		super(value, known);
		this.type = type;
		this.s1 = s1;
		this.s2 = null;
	}
	
	public Relation(RelationType type, Statement s1, Statement s2, boolean value, boolean known) {
		super(value, known);
		this.type = type;
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public boolean calcValue(boolean v1){
		if(type != RelationType.NEGATION){
			System.err.println("ERROR: A non atomic relation, \"" + getLabel() + "\" was tested with one variable");
			return false;
		}
		return v1 == true ? false : true;
	}
	
	public boolean calcValue(boolean v1, boolean v2){
		if(type == RelationType.NEGATION){
			System.err.println("ERROR: An atomic relation was tested with two variables");
			return false;
		}else if(type == RelationType.CONJUNCTION)
			return v1 && v2;
		else if(type == RelationType.DISJUNCTION)
			return v1 || v2;
		else if(type == RelationType.IMPLIES)
			return !(v1 && !v2);
		else if(type == RelationType.EQUIVALENCE)
			return v1 == v2;

		System.err.println("ERROR: RelationType cannot be interpreted");
		return false;
	}
	
	public RelationType getType() {
		return type;
	}
	public void setType(RelationType type) {
		this.type = type;
	}
	public Statement getS1() {
		return s1;
	}
	public void setV1(Variable s1) {
		this.s1 = s1;
	}
	public Statement getS2() {
		return s2;
	}
	public void setV2(Variable s2) {
		this.s2 = s2;
	}
	public boolean isAtomic(){
		return s2 == null ? true : false;
	}
	
	public String getLabel(){
		String symbol = null;
		if(type == RelationType.NEGATION)
			symbol = "¬";
		else if(type == RelationType.CONJUNCTION)
			symbol = "^";
		else if(type == RelationType.DISJUNCTION)
			symbol = "v";
		else if(type == RelationType.IMPLIES)
			symbol = "=>";
		else if(type == RelationType.EQUIVALENCE)
			symbol = "<=>";
		if(isAtomic())
			return symbol + s1.getLabel();
		return s1.getLabel() +  " " + symbol + " " + s2.getLabel();
	}
}
