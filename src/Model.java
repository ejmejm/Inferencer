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
	
	public void printTT(){
		tt.print();
	}
	
	public boolean entails(Statement s){
		return tt.entails(s);
	}
	
	public void printEntails(Statement s){
		tt.printEntails(s);
	}
}
