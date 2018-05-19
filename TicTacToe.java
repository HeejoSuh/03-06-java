/****************************************************************************
 *
 * Created by: Heejo Suh
 * Created on: May 2018
 * Created for: learning
 * 
 * This program plays the Tic Tac Toe game using recursion
 * 
 
	
 ****************************************************************************/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

//----------------------
public class TicTacToe {
	
	//arrays for holding inserts
	int[][] board = new int[3][3];
	int playerMark= 1, computerMark= 5;
	int marksOnGrid = 0;
	int[] computerNextPos;
	boolean userTurn = true;

	Scanner scanner = new Scanner(System.in);
	
	//----------------------
	private void show() {
		//shows the current grid

		System.out.println("\n ------------"); //top
		
		for (int rows = 0; rows < board.length ; rows++) {
			//print spaces or o's or x's
			//-----
			for (int nth = 0; nth < board[rows].length ; nth++) {
				//check input and print
				System.out.print("| ");
				if (board[rows][nth] == playerMark) {
					//user
					System.out.print("O ");
				} else if (board[rows][nth] == computerMark) {
					//computer
					System.out.print("X ");
				} else {
					//print space
					System.out.print("  ");
				}
			}
			//-----
			// draw new line
			System.out.print("|\n ------------\n");
		}
	}

	//----------------------
	public int getIntInput(String askFor) {
		//gets the input of where the user wants to place the O on the grid and returns it
		
		while (true) {

			//--------------
			System.out.println(askFor);

			//--------------
			//check response
			try {
				//check if response is an integer
				String input = scanner.nextLine();
				int inputNumber = Integer.parseInt(input);
				
				//check if input is valid
				if ( inputNumber > 0 && inputNumber <= board.length) {
					return inputNumber-1;
				} else {
					System.out.println("Error: Invalid input!");
				}
			}catch (IllegalArgumentException x) { 
				System.out.println("Insert an integer!");
			}
			//--------------
		}
	}

	//----------------------
	public void placeUserInput() {
		//gets the input of where the user wants to place the O on the grid and returns it
		
		while (true) {
			int column = getIntInput("which column do you choose?");
			int row = getIntInput("which row do you choose?");

			if ( board[row][column]==0 ) {
				//if available
				board[row][column] = playerMark; //add to array
				marksOnGrid+=1;
				break;
			} else {
				System.out.println("Unavailable!");
			}
		}
	}
		
		
	//----------------------
	public ArrayList<int[]> availablePos() {
		//checks for empty positions and returns the list
		ArrayList<int[]> availablePos = new ArrayList<int[]>();
        for (int row = 0; row < 3; ++row) {
        	//for each row
            for (int column = 0; column < 3; ++column) {
            	//for each column
                if (board[row][column] == 0) {
                	//if position is empty, add the position to the list
                	int[] pos = {row, column};
                	availablePos.add(pos);
                }
            }
        }
        return availablePos;
    }

	//----------------------
    public int minimax(int nth, boolean computerTurn) {
    	
    	/* return a value if a terminal state is found (+10, 0, -10)
		1. Go through available spots on the board and
		call the minimax function on each (recursion)
		
		2. Evaluate returning values from function calls
		and return the best value
		
		"MinMax Algorithm", also known as MiniMax Algorithm, 
		is an recursive algorithm used in two players games 
		such as Tic Tac Toe, Chess etc to find the optimal moves. 
		It is used to minimize the worst case (Maximum Loss) scenario. 
		
	explanation for solution retrieved from
	http://www.letscodepro.com/tic-tac-toe-minmax-alpha-beta-pruning-python/
    	 */
    	
    	//Check if the users have win or lost
        if (hasWon(computerMark)) {
        	return +10; //return positive
        }
        if (hasWon(playerMark)) {
        	return -10; //return negative
        }
        
        //-------------------
        //Get a list of available positions
        List<int[]> availablePositions = availablePos();
        
        
        if (availablePositions.isEmpty()) {
            //check if all the places have been marked
        	return 0;
        }
 
        int min = 9, max = 1; //initially set min to 'max' and max to 'min'

        //-------------------
        for (int index = 0; index < availablePositions.size(); ++index) {
        	//for each available position
            int x = availablePositions.get(index)[0], y = availablePositions.get(index)[1];
            
            //----------------
            if (computerTurn) {
                board[x][y] = computerMark;
                int currentScore = minimax(nth + 1, false);
                //compare and sets the maximum to a larger value
                max = Math.max(currentScore, max);
 
                if(currentScore >= 0){ 
                	if(nth == 0) {
                		computerNextPos = availablePositions.get(index);
                	}
                }
                if(currentScore == 1){
                	board[x][y] = 0; 
                	break;
                }
                if(index == availablePositions.size()-1 && max < 0){
                	if(nth == 0) {
                		computerNextPos = availablePositions.get(index);
            		}
            	}
            //----------------
            } else if (!computerTurn) {
                board[x][y] = playerMark;
                int currentScore = minimax(nth + 1, true);
                //compare and sets the minimum to a lesser value
                min = Math.min(currentScore, min);
                if(min == -1){
                	board[x][y] = 0; 
                	break;
                }
            }
        board[x][y] = 0;  //Reset this point
        }
        //If turn equals 1, return max else return min
        return computerTurn == true?max :min;
    }
	
	
	
	//----------------------
	public boolean hasWon(int markValue) {
		//checks for a winner and returns if player wins or not
		//Diagonally
		if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == markValue) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == markValue)) {
			//System.out.println("X Diagonal Win");
			return true;
		}
		//horizontally and vertically
		for (int nth = 0; nth < 3; ++nth) {
            if (((board[nth][0] == board[nth][1] && board[nth][0] == board[nth][2] && board[nth][0] == markValue)
            		|| (board[0][nth] == board[1][nth] && board[0][nth] == board[2][nth] && board[0][nth] == markValue))) {
            	return true;
            }
        }
		//if reach here, has not won
        return false;
	}

	//----------------------
	public void playGame() {
		//plays the game

		show();
		//play the game
		while (marksOnGrid < 8 && !hasWon(computerMark) && !hasWon(playerMark)) {
			
			//user turn
        	placeUserInput();
        	
        	//computer turn
            minimax(0, true);
            board[computerNextPos[0]][computerNextPos[1]] = computerMark; 
			marksOnGrid+=1;
			
			show();
        }
		
		//shows who lost, won, or tied
		if (hasWon(computerMark) && !hasWon(playerMark)) {
            System.out.println("You lost!");
		} else if (hasWon(playerMark) && !hasWon(computerMark)) {
            System.out.println("You win!"); //never happens
		} else {
            System.out.println("You tied!");
		}
	}


	//--------------------------------------------
	public static void main(String[] args) {
		TicTacToe game = new TicTacToe();

		//play game
        game.playGame();
	}
	
}


