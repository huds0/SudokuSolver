package code;

import java.io.File;
import java.util.Scanner;
import java.util.Stack;

public class Board{
	
	/*The Sudoku Board is made of 9x9 cells for a total of 81 cells.
	 * In this program we will be representing the Board using a 2D Array of cells.
	 * 
	 */

	private Cell[][] board = new Cell[9][9];
	
	//The variable "level" records the level of the puzzle being solved.
	private String level = "";

	//This must initialize every cell on the board with a generic cell.  It must also assign all of the boxIDs to the cells
	public Board()
	{
		for(int x = 0; x < 9; x++)
			for(int y = 0 ; y < 9; y++)
			{
				board[x][y] = new Cell();
				board[x][y].setBoxID(3*(x/3) + (y)/3+1);
			}
	}
	

	/*This method will take a single String as a parameter.  The String must be either "easy", "medium" or "hard"
	 * If it is none of these, the method will set the String to "easy".  The method will set each of the 9x9 grid
	 * of cells by accessing either "easyPuzzle.txt", "mediumPuzzle.txt" or "hardPuzzle.txt" and setting the Cell.number to 
	 * the number given in the file.  
	 * 
	 * This must also set the "level" variable
	 * TIP: Remember that setting a cell's number affects the other cells on the board.
	 */
	public void loadPuzzle(String level) throws Exception
	{
		this.level = level;
		String fileName = "easyPuzzle.txt";
		if(level.contentEquals("medium"))
			fileName = "mediumPuzzle.txt";
		else if(level.contentEquals("hard"))
			fileName = "hardPuzzle.txt";
		
		Scanner input = new Scanner (new File(fileName));
		
		for(int x = 0; x < 9; x++)
			for(int y = 0 ; y < 9; y++)
			{
				int number = input.nextInt();
				board[x][y].setNumber(number);
				if(number != 0)
					solve(x, y, number);
			}				
		input.close();
	}

	/*This method scans the board and returns TRUE if every cell has been solved.  Otherwise it returns FALSE
	 * 
	 */
	public boolean isSolved()
	{
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(board[x][y].getNumber() == 0)
					return false;
			}
		}
		return true;
	}

	/*This method displays the board neatly to the screen.  It must have dividing lines to show where the box boundaries are
	 * as well as lines indicating the outer border of the puzzle
	 */
	public void display()
	{
		System.out.println("======================");
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(y%3 == 0)
					System.out.print("|");
				System.out.print(board[x][y].getNumber() + " ");
			}
			System.out.print("|");
			System.out.println();
			if(x%3 == 2)
				System.out.println("======================"); 
		}	
	}
	
	/*This method solves a single cell at x,y for number.  It also must adjust the potentials of the remaining cells in the same row,
	 * column, and box.
	 */
	
	public void solve(int x, int y, int number){

		board[x][y].setNumber(number);
		
        for(int z = 0; z < 9; z++) {
            if(board[z][y] != board[x][y]) {
                board[z][y].cantBe(number);
            }
        }
        
       for(int z = 0; z < 9; z++) {
            if(board[x][z] != board[x][y]) {
                board[x][z].cantBe(number);
            }
        } 
       
        int row = 0;
        boolean found = false;
        int rMarker = 0;
        int cMarker = 0;

        while(row < 9 && found == false) {
        	int column = 0;
            while(column < 9 && found == false) {
                if(board[x][y].getBoxID() == board[row][column].getBoxID()) {
                	rMarker = row;
                	cMarker = column;
                    found = true;
                }
                column += 3;
            }
             row += 3;
        }

        for(int i = rMarker; i < rMarker+3; i++) {
            for(int j = cMarker; j < cMarker+3; j++) {
                if(board[i][j] != board[x][y]) {
                    board[i][j].cantBe(number);
                }
            }
        }
            for(int z = 1; z < 10; z++) {
            if(z != number) {
                board[x][y].cantBe(z);
            }
        }
    }
	
	//logicCycles() continuously cycles through the different logic algorithms until no more changes are being made.
	public void logicCycles()throws Exception 
	{
		while(isSolved() == false)
		{
			int changesMade = 0;
			do
			{
				changesMade = 0;
				changesMade += logic1();
				changesMade += logic2();
				changesMade += logic3();
				changesMade += logic4();
				guessAndCheck();
				display();
				if(errorFound())
					break;
			}while(changesMade != 0);
	
		}				
	}
		
	/*This method searches each row of the puzzle and looks for cells that only have one potential.  If it finds a cell like this, it solves the cell 
	 * for that number. This also tracks the number of cells that it solved as it traversed the board and returns that number.
	 */
	public int logic1()
	{
		int changesMade = 0;
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(board[x][y].numberOfPotentials() == 1 && board[x][y].getNumber() == 0) {	
					solve(x, y, board[x][y].getFirstPotential());
					changesMade++;
				}
			}
		}
		return changesMade;				
	}
	
	/*This method searches each row for a cell that is the only cell that has the potential to be a given number.  If it finds such a cell and it
	 * is not already solved, it solves the cell.  It then does the same thing for the columns.This also tracks the number of cells that 
	 * it solved as it traversed the board and returns that number.
	 */
	
	public int logic2()
	{	
		int changesMade = 0;
        int count = 0;
        int place = 0;
        
        for(int number = 1; number < 10; number++) {
            for(int row = 0; row < 9; row++) {
            	count = 0; //VERY IMPORTANT!! reason why I couldn't get it!
              	place = 0;
                for(int y = 0 ; y < 9; y++) {
                    if(board[row][y].canBe(number)) {
                    	count++;
                        place = y;
                    }
                }
                if(count == 1 && board[row][place].getNumber() == 0) {
                    solve(row, place, number);
                    changesMade++;
                }
            }

            for(int column = 0; column < 9; column++) {
            	count = 0;
            	place = 0;
                for(int x = 0 ; x < 9; x++) {
                    if(board[x][column].canBe(number)) {
                    	count++;
                        place = x;
                    }
                }
                if(count == 1 && board[place][column].getNumber() == 0) {
                    solve(place, column, number);
                    changesMade++;
                }

            }
        }
        return changesMade;
	}

	/*This method searches each box for a cell that is the only cell that has the potential to be a given number.  If it finds such a cell and it
	 * is not already solved, it solves the cell. This also tracks the number of cells that it solved as it traversed the board and returns that number.
	 */

	public int logic3()
	{
		int changesMade = 0;
		int placeX = 0; 
		int placeY = 0;
		int count = 0;
		
		for(int x = 0; x < 9; x+=3) {
			for(int y = 0; y < 9; y+=3) {
				for(int number = 1; number < 10; number++) {
					count = 0;
					for(int row = x; row < x+3; row++) {
						for(int column = y; column < y+3; column++) {
							if(board[row][column].canBe(number)) {
								count++;
								placeX = row;
								placeY = column;
						}
					}
				}
					if(count == 1 && board[placeX][placeY].getNumber() == 0) {
						solve(placeX, placeY, number);
						changesMade++;
						}
					}
			
				}
		}
		return changesMade;	
	}

	///TODO: logic4
		/*This method searches each row for the following conditions:
		 * 1. There are two unsolved cells that only have two potential numbers that they can be
		 * 2. These two cells have the same two potentials (They can't be anything else)
		 * 
		 * Once this occurs, all of the other cells in the row cannot have these two potentials.  Write an algorithm to set these two potentials to be false
		 * for all other cells in the row.
		 * 
		 * Repeat this process for columns and rows.
		 * 
		 * This also tracks the number of cells that it solved as it traversed the board and returns that number.
		 */
	public int logic4()
	{
	  
		int changesMade = 0;
		int firstPotential = 0;
		int secondPotential = 0;

		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(board[x][y].numberOfPotentials() == 2) {
					firstPotential = board[x][y].getFirstPotential();
					secondPotential = board[x][y].getSecondPotential();
					for(int column = y+1; column < 9; column++) {
						if(board[x][column].numberOfPotentials() == 2) {
							if(board[x][column].getFirstPotential() == firstPotential && board[x][column].getSecondPotential() == secondPotential) {
								for(int i = 0; i < 9; i++) {
									if(i == y || i == column) {
										continue;
									}
									if(board[x][column].getPotential(firstPotential) == true || board[x][column].getPotential(secondPotential) == true) {
										board[x][i].cantBe(firstPotential);
										board[x][i].cantBe(secondPotential);
										changesMade++;
									}
								}
							}
						}
					}
				}
			}
		}
		
		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 9; x++) {
				if(board[x][y].numberOfPotentials() == 2) {
					firstPotential = board[x][y].getFirstPotential();
					secondPotential = board[x][y].getSecondPotential();
					for(int row = x+1; row < 9; row++) {
						if(board[row][y].numberOfPotentials() == 2) {
							if(board[row][y].getFirstPotential() == firstPotential && board[row][y].getSecondPotential() == secondPotential) {
								for(int i = 0; i < 9; i++) {
									if(i == x || i == row) {
										continue;
									}
									if(board[i][y].getPotential(firstPotential) == true || board[i][y].getPotential(secondPotential) == true) {
										board[i][y].cantBe(firstPotential);
										board[i][y].cantBe(secondPotential);
										changesMade++;
									}
								}
							}
						}
					}
				}
			}
		}
		return changesMade;
	}

	
	public void guessAndCheck() throws Exception { //had to add throws exception for logic cycle being called?
	    Stack<Board> boardStack = new Stack<>();

	    while (!isSolved()) {
	        int[] cellCoords = chooseUnsolvedCell();
	        int x = cellCoords[0];
	        int y = cellCoords[1];
	        Cell cell = board[x][y];

	        Board boardCopy = new Board();
	        boardCopy.board = copyBoard();
	        boardStack.push(boardCopy);

	        int guess = cell.getFirstPotential();
	        solve(x, y, guess);
	        
	        while (!isSolved() && !errorFound()) {
	            logicCycles();
	        }  
	        if (errorFound()) {
	            board = boardStack.pop().board;
	            cell.cantBe(guess);
	        }
	    }
	}

	public int[] chooseUnsolvedCell() {
	    for (int x = 0; x < 9; x++) {
	        for (int y = 0; y < 9; y++) {
	            if (board[x][y].getNumber() == 0) {
	                return new int[] { x, y };
	            }
	        }
	    }
	    return null;
	}

	public Cell[][] copyBoard() {
	    Cell[][] copy = new Cell[9][9];
	    for (int x = 0; x < 9; x++) {
	        for (int y = 0; y < 9; y++) {
	            copy[x][y] = new Cell();
	            copy[x][y].setNumber(board[x][y].getNumber());
	            copy[x][y].setPotentialValues(board[x][y].getPotentialValues());
	            copy[x][y].setBoxID(board[x][y].getBoxID());
	        }
	    }
	    return copy;
	}
	
	/*This method scans the board to see if any logical errors have been made.  It can detect this by looking for a cell that no longer has the potential to be 
	 * any number.
	 */
	public boolean errorFound()
	{
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(board[x][y].numberOfPotentials() == 0)
					return true;
			}
		}
		return false;
	}
	
	
	
	
}

