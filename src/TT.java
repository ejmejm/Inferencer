import java.util.ArrayList;

public class TT {
	private Model model;
	private boolean table[][];
	
	public TT(Model model){
		this.model = model;
		buildTable();
	}
	
	//Sets all the values in the truth table
	public void buildTable(){
		table = new boolean[model.variables.size()+model.relations.size()][1 << model.variables.size()];
		int numVars = model.variables.size();
		
		//Populate the variables with every possible combination of trues and falses
		for(int i = 0; i < numVars; i++){ //Each variable
			for(int j = 0; j < (1 << (numVars-i))/2; j++){ //Every round of equal trues and falses (if 3 vars: 4, 2, 1)
				for(int k = 0; k < (1 << i); k++){ //Trues (if 3 vars: 1, 2, 4)
					table[i][j*(2 << i)+k] = true;
				}
				for(int k = 0; k < (1 << i); k++){ //Falses (if 3 vars: 1, 2, 4)
					table[i][j*(2 << i)+(1 << i)+k] = false;
				}
			}
		}
		
		//Check values for all relations
		for(int i = model.variables.size(); i < table.length; i++){ //For every relation
			for(int j = 0; j < table[i].length; j++){ //For every true and false
				Relation relation = ((Relation) getModelStatement(i));
				if(!relation.isAtomic()){ //If atomic relation
					boolean v1 = false, v2 = false;
					
					for(int r = 0; r < model.variables.size(); r++){
						if(relation.getS1().equals(model.variables.get(r)))
							v1 = table[r][j];
						if(relation.getS2().equals(model.variables.get(r)))
							v2 = table[r][j];
					}
					for(int r = model.variables.size(); r < model.variables.size()+model.relations.size(); r++){
						//IMPORTANT: For this to work, the simplified versions of the relations MUST come first in the table
						if(relation.getS1().equals(model.relations.get(r-model.variables.size())))
							v1 = table[r][j];
						if(relation.getS2().equals(model.relations.get(r-model.variables.size())))
							v2 = table[r][j];
					}
					
					table[i][j] = relation.calcValue(v1, v2);
				}else{ //If not atomic relation
					boolean v1 = false;

					for(int r = 0; r < model.variables.size(); r++){
						if(relation.getS1().equals(model.variables.get(r)))
							v1 = table[r][j];
					}
					for(int r = model.variables.size(); r < model.variables.size()+model.relations.size(); r++){
						//IMPORTANT: For this to work, the simplified versions of the relations MUST come first in the table
						if(relation.getS1().equals(model.relations.get(r-model.variables.size())))
							v1 = table[r][j];
					}
					
					table[i][j] = ((Relation) getModelStatement(i)).calcValue(v1);
				}
			}
		}
	}
	
	//Returns whether or not Statement s is entailed by the constraints
	public boolean entails(Statement s){
		int sIndex = getStatementIndex(s);
		if(sIndex == -1){
			System.err.println("ERROR: Tried to entail a statement not in the truth table");
			return false;
		}
		
		boolean entails = true;

		for(int j = 0; j < table[0].length; j++){ //For each row
			boolean constraints = true;
			for(int i = 0; i < table.length; i++){ //For each column
				if(getModelStatement(i).isKnown() && table[i][j] != getModelStatement(i).getValue()) //If var is a constraint and is false
					constraints = false; //Then this is not important in determining entailment
			}
			if(constraints && table[sIndex][j] == false) //If the constraints are met, but the entailed statement is false 
				entails = false; //Then the statement is not entailed
		}
		return entails;
	}
	
	//Returns the list of rows that follow the constraints followed; if a row is not true the row number will be returned as negative
	private ArrayList<Integer> entailsStatements(Statement s){
		int sIndex = getStatementIndex(s);
		if(sIndex == -1){
			System.err.println("ERROR: Tried to entail a statement not in the truth table");
			return null;
		}
		
		ArrayList<Integer> rows = new ArrayList<Integer>();

		for(int j = 0; j < table[0].length; j++){ //For each row
			boolean constraints = true;
			for(int i = 0; i < table.length; i++){ //For each column
				if(getModelStatement(i).isKnown() && table[i][j] != getModelStatement(i).getValue()) //If var is a constraint and is false
					constraints = false; //Then this is not important in determining entailment
			}
			if(constraints && table[sIndex][j] == false) //If the constraints are met, but the entailed statement is false 
				rows.add(-j);
			else if(constraints && table[sIndex][j] == true)
				rows.add(j);
		}
		return rows;
	}
	
	public void printEntails(Statement s){
		ArrayList<Integer> entailment = entailsStatements(s);
		
		if(entailment == null) return;

		System.out.print("\n\n");
		
		for(int i = 0; i < model.variables.size(); i++)
			System.out.print(model.variables.get(i).getLabel() + "    ");
		for(int i = 0; i < model.relations.size(); i++)
			System.out.print(model.relations.get(i).getLabel() + "    ");
		
		System.out.println();
		System.out.println();
		
		boolean entailed = true;
		for(Integer j : entailment){
			for(int i = 0; i < table.length; i++){
				System.out.print((table[i][Math.abs(j)] == true ? "T" : "F"));
				
				//Print right amount of spacing between columns
				if(i < model.variables.size())
					for(int l = 0; l < model.variables.get(i).getLabel().length() + 3; l++)
						System.out.print(" ");
				else
					for(int l = 0; l < model.relations.get(i-model.variables.size()).getLabel().length() + 3; l++)
						System.out.print(" ");
			}
			
			System.out.print(j < 0 ? "Fails" : "Passes");
			System.out.println();
			
			if(j < 0)
				entailed = false;
		}
		
		System.out.println();
		
		if(entailed)
			System.out.println("Statement, \"" + s.getLabel() + "\" is entailed by the constraints, as for every row in the truth\n"
				+ "table where the constraints are met (as shown above), the statement is true \n(\"Passes\" means that the "
				+ "statement is true and the constraints are met).");
		else
			System.out.println("Statement, \"" + s.getLabel() + "\" is NOT entailed by the constraints, as the statement "
				+ "is not\ntrue in every row where the constraints are met (as shown above, \"Fails\"\nmeans that even "
				+ "though the constraints are met, the statement is false).");
	}
	
	//Print the truth table
	public void print(){
		for(int i = 0; i < model.variables.size(); i++)
			System.out.print(model.variables.get(i).getLabel() + "    ");
		for(int i = 0; i < model.relations.size(); i++)
			System.out.print(model.relations.get(i).getLabel() + "    ");
		
		System.out.println();
		System.out.println();
		
		for(int i = 0; i < table[0].length; i++){
			for(int j = 0; j < table.length; j++){
				System.out.print(table[j][i] == true ? "T" : "F");
				
				//Print right amount of spacing between columns
				if(j < model.variables.size())
					for(int s = 0; s < model.variables.get(j).getLabel().length() + 3; s++)
						System.out.print(" ");
				else
					for(int s = 0; s < model.relations.get(j-model.variables.size()).getLabel().length() + 3; s++)
						System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	//Get the Statement corresponding to its index in the table array
	public Statement getModelStatement(int index){
		if(index < model.variables.size())
			return model.variables.get(index);
		else
			return model.relations.get(index - model.variables.size());
	}
	
	//Get the index corresponding to its Statement in the table array
	public int getStatementIndex(Statement s){
		for(int i = 0; i < model.variables.size(); i++){
			if(s.equals(model.variables.get(i)))
				return i;
		}
		for(int i = 0; i < model.relations.size(); i++){
			if(s.equals(model.relations.get(i)))
				return i + model.variables.size();
		}
		return -1;
	}
}
