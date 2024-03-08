package code;

public class Main {

	public static void main(String[] args) throws Exception {
		
		//Test EASY puzzle
		Board puzzle = new Board();
		puzzle.loadPuzzle("medium");
		System.out.println("Unsolved Sudoku: ");
		puzzle.display();
		puzzle.logicCycles();
		System.out.println("Solved Sudoku: ");
		puzzle.display();
		System.out.println("Error: " + puzzle.errorFound());
		System.out.println("Solved: " + puzzle.isSolved());

		//Test HARD puzzle
		/*
		puzzle.loadPuzzle("hard");
		System.out.println("Unsolved Sudoku: ");
		puzzle.display();
		puzzle.logicCycles();
		System.out.println("Solved Sudoku: ");
		puzzle.display();
		System.out.println("Error: " + puzzle.errorFound());
		System.out.println("Solved: " + puzzle.isSolved());
		*/
	}
}






