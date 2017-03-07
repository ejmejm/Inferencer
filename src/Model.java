import java.util.ArrayList;

public class Model {
	public ArrayList<Variable> variables;
	public ArrayList<Relation> relations;
	private TT tt;
	
	public Model(){
		variables = new ArrayList<Variable>();
		relations = new ArrayList<Relation>();
		tt = new TT(this);
	}
	
	public Model(ArrayList<Variable> variables, ArrayList<Relation> relations) {
		super();
		this.variables = variables;
		this.relations = relations;
		tt = new TT(this);
	}
	
	public void rebuildTT(){
		tt.buildTable();
	}
	
	public Statement getStatementByLabel(String label){
		for(Variable v : variables)
			if(v.getLabel().equals(label))
				return v;
		for(Relation r : relations)
			if(r.getLabel().equals(label))
				return r;
		return null;
	}
	
	public Statement getStatementByIndex(int index){
		if(index < variables.size())
			return variables.get(index);
		else
			return relations.get(index-variables.size());
	}
	
	public void printTT(){
		tt.print();
	}
	
	public boolean ttEntails(Statement s){
		return tt.entails(s);
	}
	
	public void printTTEntails(Statement s){
		tt.printEntails(s);
	}
	
	public boolean dpllEntails(Statement s){
		return DPLL.entails(this, s);
	}
}
