import java.util.Scanner;

public class Main {
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		
		System.out.println("Type the number corresponding to a problem 1 - 5\n(4 is question 4 part a, and 5 is question 4 part b)");
		int problem = input.nextInt();
		System.out.println("Do you want to view the answer to the problem or the data structure built to solve it?\n"
				+ "(0 for answer, 1 for truth table data structure, 2 for DPLL data structure)");
		int type = input.nextInt();
		System.out.println("\nProblem: " + problem);
		if(problem == 5)
			Examples.lieTruthB(type);
		else if(problem == 4)
			Examples.lieTruthA(type);
		else if(problem == 3)
			Examples.hornClauses(type);
		else if(problem == 2)
			Examples.WumpusWorldS(type);
		else if(problem == 1)
			Examples.modusPonens(type);
	}
}