package code;

import java.util.List;
//import java.util.ArrayList;


public class Cell {

	/*A Cell represents a single square on the Sudoku Game Board. 
	 * It knows it's number - 0 means it is not solved.
	 * It knows the potential numbers that it could have from 1-9.
	 * The Sudoku game board is sub-divided into 9 smaller 3x3 sections that I will call a box. 
	 * These boxes will be numbered from left to right, top to bottom, from 1 to 9.  Each cell
	 * will know which box it belongs in.
	 */
	
	private int number; // This is the solved value of the cell.	
	private List potentialValues;

	private boolean[] potential = {false, true, true, true, true, true, true, true, true, true};
	
	/*This array represents the potential of the cell to be each of the given index numbers.  Index [0] is not used since
	 * the cell cannot be solved for 0. 
	 */
	private int boxID;//The boxID is the box to which the cell belongs.
	
	//USEFUL METHODS:
	
	public boolean canBe(int number)
	{
		return potential[number];
	}

	//This sets the potential array to be false for a given number
	public void cantBe(int number)
	{
		potential[number] = false;
	}

	//This method returns a count of the number of potential numbers that the cell could be.
	public int numberOfPotentials()
	{
		int count = 0;
		for(int x = 1; x < 10; x++) {
			if(canBe(x)) 
				count++;
		}
		return count;
	}
	
	//This method will return the first number that a cell can possibly be.
	public int getFirstPotential()
	{
		for(int x = 1; x < 10; x++) {
			if(canBe(x))
				 return x;
		}
		return -1;
	}
	
	public int getSecondPotential()
	{
	int count = 0;
	
    for(int x = 1; x < 10; x++) {
        if(canBe(x)) {
            if(count == 1)
                return x;
            count++;
        }
    }

    return -1;

 }
	
	//GETTERS AND SETTERS	    
	public int getNumber() {
		return number;
	}
	// This method sets the number for the cell but also sets all of the potentials for the cell (except for the solved number)
	//	to be false
	public void setNumber(int number) {
		this.number = number;
	}
	public boolean getPotential(int number) {
		return true;
	}
	
	public void setPotential(boolean[] list) {
		this.potential = list;
	}
	public int getBoxID() {
		return boxID;
	}
	public void setBoxID(int boxID) {
		this.boxID = boxID;
	}
	public List getPotentialValues() {
	    return potentialValues;
	}

	public void setPotentialValues(List<Integer> potentialValues) {
	    this.potentialValues = potentialValues;
	}  
}
