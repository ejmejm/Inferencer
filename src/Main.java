public class Main {
	public static void main(String args[]){
		//Examples.hornClauses();
		Model model = new Model();

		model.variables.add(new Variable("P", true));
		model.variables.add(new Variable("Q", false, false));
		
		model.relations.add(new Relation(Relation.RelationType.IMPLIES, model.getStatementByLabel("P"), 
				model.getStatementByLabel("Q"), true));
		
		System.out.println(model.dpllEntails(model.getStatementByLabel("¬Q")));
	}
}