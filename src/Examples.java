//¬

public class Examples {
	public static void modusPonens(){
		Model model = new Model();
		
		model.variables.add(new Variable("P", true));
		model.variables.add(new Variable("Q", false, false));
		
		model.relations.add(new Relation(Relation.RelationType.IMPLIES, model.getStatementByLabel("P"), 
				model.getStatementByLabel("Q"), true));
		
		model.rebuildTT();
		
		//model.printTT(); //Uncomment to display entire truth table
		
		model.printEntails(model.getStatementByLabel("Q"));
	}
	
	public static void WumpusWorldS(){
		Model model = new Model();
		
		model.variables.add(new Variable("P11", false, false));
		model.variables.add(new Variable("B11", false, false));
		model.variables.add(new Variable("P12", false, false));
		model.variables.add(new Variable("P21", false, false));
		model.variables.add(new Variable("P22", false, false));
		model.variables.add(new Variable("P31", false, false));
		model.variables.add(new Variable("B21", true, true));

		model.relations.add(new Relation(Relation.RelationType.NEGATION, model.getStatementByLabel("P12"), false, false));
		model.relations.add(new Relation(Relation.RelationType.NEGATION, model.getStatementByLabel("P11"), true, true));
		model.relations.add(new Relation(Relation.RelationType.NEGATION, model.getStatementByLabel("B11"), true, true));
		model.relations.add(new Relation(Relation.RelationType.DISJUNCTION, model.getStatementByLabel("P12"), 
				model.getStatementByLabel("P21"), false, false));
		model.relations.add(new Relation(Relation.RelationType.EQUIVALENCE, model.getStatementByLabel("B11"), 
				model.getStatementByLabel("P12 v P21"), true, true));
		model.relations.add(new Relation(Relation.RelationType.DISJUNCTION, model.getStatementByLabel("P11"), 
				model.getStatementByLabel("P22"), false, false));
		model.relations.add(new Relation(Relation.RelationType.DISJUNCTION, model.getStatementByLabel("P11 v P22"), 
				model.getStatementByLabel("P31"), false, false));
		model.relations.add(new Relation(Relation.RelationType.EQUIVALENCE, model.getStatementByLabel("B21"), 
				model.getStatementByLabel("P11 v P22 v P31"), true, true));
		
		model.rebuildTT();
		
		//model.printTT(); //Uncomment to display entire truth table
		
		model.printEntails(model.getStatementByLabel("P12"));
		model.printEntails(model.getStatementByLabel("¬P12"));
	}
	
	public static void hornClauses(){
		Model model = new Model();
		
		model.variables.add(new Variable("Mythical", false, false));
		model.variables.add(new Variable("Immortal", false, false));
		model.variables.add(new Variable("Mammal", false, false));
		model.variables.add(new Variable("Horned", false, false));
		model.variables.add(new Variable("Mortal", false, false));
		model.variables.add(new Variable("Magical", false, false));

		model.relations.add(new Relation(Relation.RelationType.NEGATION, model.getStatementByLabel("Mythical"), false, false));
		model.relations.add(new Relation(Relation.RelationType.CONJUNCTION, model.getStatementByLabel("Mortal"), 
				model.getStatementByLabel("Mammal"), false, false));
		model.relations.add(new Relation(Relation.RelationType.DISJUNCTION, model.getStatementByLabel("Immortal"), 
				model.getStatementByLabel("Mammal"), false, false));
		model.relations.add(new Relation(Relation.RelationType.IMPLIES, model.getStatementByLabel("Mythical"), 
				model.getStatementByLabel("Immortal"), true, true));
		model.relations.add(new Relation(Relation.RelationType.IMPLIES, model.getStatementByLabel("¬Mythical"), 
				model.getStatementByLabel("Mortal ^ Mammal"), true, true));
		model.relations.add(new Relation(Relation.RelationType.IMPLIES, model.getStatementByLabel("Immortal v Mammal"), 
				model.getStatementByLabel("Horned"), true, true));
		model.relations.add(new Relation(Relation.RelationType.IMPLIES, model.getStatementByLabel("Horned"), 
				model.getStatementByLabel("Magical"), true, true));
		
		model.rebuildTT();
		

		//model.printTT(); //Uncomment to display entire truth table
		
		model.printEntails(model.getStatementByLabel("Mythical"));
		model.printEntails(model.getStatementByLabel("Magical"));
		model.printEntails(model.getStatementByLabel("Horned"));
	}
}