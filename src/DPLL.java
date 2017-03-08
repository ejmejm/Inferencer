import java.util.ArrayList;
import java.util.LinkedList;

public class DPLL {
	public static boolean entails(Model model, Statement statement){
		State state = new State(model);		
		ArrayList<State> leafNodes = state.getLeafNodes(state);
		
		boolean entails = true;
		for(State node : leafNodes){
			if(!node.entails(statement))
				entails = false;
		}
		
		return entails;
	}
	
	public static void printEntails(Model model, Statement statement){
		boolean result = entails(model, statement);
		if(result)
			System.out.println("DPLL: Statement, \"" + statement.getLabel() + "\" is entailed");
		else
			System.out.println("DPLL: Statement, \"" + statement.getLabel() + "\" is NOT entailed");
	}
	
	public static void printTree(Model model){
		State state = new State(model);
		LinkedList<State> q = new LinkedList<State>();
		q.addLast(state);
		
		int cnt = 0;
		
		while(!q.isEmpty()){
			State tmp = q.pop();
			if(tmp.statement != null)
				System.out.println("(" + cnt + ") " + tmp.statement.getLabel() + ": " + tmp.statement.getValue());
			if(tmp.children != null && !tmp.children.isEmpty())
				for(int i = 0; i < tmp.children.size(); i++)
					q.addFirst(tmp.children.get(i));
			cnt++;
		}
	}
	
	private static class State{
		Statement statement;
		ArrayList<Statement> knownStms;
		ArrayList<State> children;
		Model model;

		public State(Model model){
			this.model = model;
			knownStms = new ArrayList<Statement>();
			statement = null;
			children = genAncestors(model);
		}
		
		public State(Model model, ArrayList<Statement> knownStms, Statement statement){
			this.model = model;
			this.knownStms = knownStms;
			this.statement = statement;
			children = genAncestors(model);
		}
		
		public ArrayList<State> genAncestors(Model model){
			if(knownStms.size() == model.variables.size())//Maybe do something here
				return null;
				
			ArrayList<State> ancestors = new ArrayList<State>();
			Statement ancestorStmt = model.getStatementByIndex(knownStms.size());
			if(ancestorStmt.isKnown())
				ancestors.add(copyHist(ancestorStmt));
			else{
				ancestors.add(copyHist(ancestorStmt));
				ancestors.add(copyHist(ancestorStmt.getSwitchedValue()));
			}
			
			if(ancestors.size() == 0)
				return null;
			return ancestors;
		}
		
		private State copyHist(Statement nStatement){
			ArrayList<Statement> nKnownStms = new ArrayList<Statement>();
			for(Statement s : knownStms)
				nKnownStms.add(s);
			nKnownStms.add(statement);
			
			return new State(model, nKnownStms, nStatement);
		}
		
		public ArrayList<State> getLeafNodes(State state){
			ArrayList<State> leafNodes = new ArrayList<State>();
			LinkedList<State> q = new LinkedList<State>();
			q.addLast(state);
			
			while(!q.isEmpty()){
				State tmp = q.pop();
				if(tmp.children != null && !tmp.children.isEmpty())
					for(int i = 0; i < tmp.children.size(); i++)
						q.addLast(tmp.children.get(i));
				else{
					tmp.knownStms.add(tmp.statement);
					leafNodes.add(tmp);
				}
			}
			
			return leafNodes;
		}
		
		private boolean constraintsMet(){
			for(Relation r : model.relations) //Make sure constraints are met
				if(r.isKnown() && calcValue(r, knownStms) != r.value)
					return false; //Return false if one of them is not met
			return true; //Otherwise they are all met
		}
		
		public boolean entails(Statement statement){
			if(constraintsMet() && calcValue(statement, knownStms) != true) //Make sure statement is entailed
				return false;
			return true;
		}
	}
	
	private static boolean calcValue(Statement s, ArrayList<Statement> knownStms){
		//If the statement is a variable
		if(s instanceof Variable){
			for(Statement stm : knownStms)
				if(stm != null && stm.equals(s))
					return stm.value;
			System.err.println("ERROR: The variable \"" + s.getLabel() + "\" was not found in the tree but it was in a relationship");
		}

		//If the statement is a relationship
		
		Relation r = (Relation) s;
		
		/*
		boolean p1 = false, p2 = false;
		boolean p1k = false, p2k = false;
		for(Statement stm : knownStms){
			if(stm.equals(r.getS1())){
				p1 = stm.value;
				p1k = true;
			}
			if(!r.isAtomic() && stm.equals(r.getS2())){
				p2 = stm.value;
				p2k = true;
			}
		}
		
		//If the parts of r1 are not variables then recursively calculate the value of 
		if(p1k == false)
			p1 = calcValue(r.getS1(), knownStms);
		if(!r.isAtomic() && p2k == false)
			p2 = calcValue(r.getS2(), knownStms);
		
		*/

		if(r.isAtomic())
			return r.calcValue(calcValue(r.getS1(), knownStms));
		return r.calcValue(calcValue(r.getS1(), knownStms), calcValue(r.getS2(), knownStms));
	}
}
