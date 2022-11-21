package minesweeper;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {
	
	//stores the number of images required for the game
	private final int IMAGES = 13;
	//stores the size of each cell inside of the Container/Component Board
	private final int SIZE_SQUARE = 15;
	
	//the following final constants define the number that each cell will hold, depending on:
	// 1- how many mines in its vicinity
	// 2- if this cell is exposed or hidden
	// 3- if this cell is flagged
	private final int COVER = 10;
	private final int MARK = 10;
	private final int ZERO_CELL = 0;
	private final int MINE = 9;
	private final int COVERED_MINE = MINE + COVER; // 9 + 10 = 19
	private final int MARKED_COVERED_MINE = COVERED_MINE + MARK; // 19 + 10 = 29
	
	//stores the numbers used as reference to draw once the game comes to an end
	private final int PAINT_MINE = 9;
	private final int PAINT_COVER = 10;
	private final int PAINT_MARK = 11;
	private final int PAINT_WRONG_MARK = 12;
	
	//defines the game parameters: number of mines, board size
	private final int TOTAL_MINES = 300;
	private final int TOTAL_ROWS = 46;
	private final int TOTAL_COLS = 46;
	
	//calculates and stores the size of the Board Component + a small margin.
	private final int WINDOW_WIDTH = TOTAL_COLS * SIZE_SQUARE + 1;
	private final int WINDOW_HEIGHT = TOTAL_ROWS * SIZE_SQUARE + 1;
	
	//variables that defines the Board properties, such as:
	//field to distribute the mines, an array of integers that will dictate if it is a mine or not, if it is covered or marked...
	private int[] field;
	//ongoing game property
	private boolean inGame;
	//numbers of mines still left in the game
	private int minesRemaining;
	//array of images to be used by each cell, depending on its status
	private Image[] img;
	
	//number of cells in the game
	private int totalCells;
	//a JLable Component that will show:
	//1 - how many mines are left in the game
	//2 - "You won" if the player marks all the mines, correctly.
	//3 - "Game over" if the player clicks in a mine.
	private final JLabel message;
	
	
	//Board constructor, it will initialize the game using the method InitBoard inside of it.
	public Board(JLabel status) {
		this.message = status;
		initiateWindow();
	}
	
	//public method called by the constructor that will...
	public void initiateWindow() {
		
		//...set the Board Component dimensions (Component dimension),
		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		//create the array to be filled with the possible icons of a cell
		img = new Image[IMAGES];	
		//filling the img array with the images paths;
		for(int i = 0; i < IMAGES; i++) {
			var path = "src/resources/" + i + ".png";
			img[i] = (new ImageIcon(path)).getImage();
		}
		
		//adding a event listener to the user`s mouse. the eventListener will be defined later on.
		addMouseListener(new MinesAdapter());
		//calls the method NewGame, defined below
		newGame();
	}
	
	//method that sets the game
	private void newGame() {
		//feines the cell variable as an integer. This will be used as current cell over loops.
		int currentCell;
		
		//set a random number generator, to be used while spreading the mines through the field 
		var random = new Random();
		
		//sets the inGame property to true
		inGame = true;
		
		//before distributing the mines into the field, minesLeft has the same value as N_MINES property;
		minesRemaining = TOTAL_MINES;
		
		//number of cells in the game
		totalCells = TOTAL_ROWS * TOTAL_COLS;
		//initialize the field with the appropriate size
		field = new int[totalCells];
		//sets all elements of the field array as 10.
		for(int i = 0; i < totalCells; i++) {
			field[i] = COVER;
		}
		
		//sets the text of the status (a JLable component) to show the number of minesLeft in the game;  
		message.setText(Integer.toString(minesRemaining));
		
		//int variable used to distribute the mines to the fields
		int i = 0;
		
		
		//while i is lower than the number of mines allowed for this game, we will keep populating the field 
		while(i < TOTAL_MINES) {
			
			//randomly chooses a field array position by casting a double as a integer.
			// random.nextDouble() will return a pseudorandom double between 0.0(inclusive) and 1.0(exclusive).
			// when multiplied by allCells, it will return a double between 0.0 and 256.0.
			// when casted to an int, it will return an integer from 0 to 255.
			int position = (int) (totalCells * random.nextDouble());
			
			//this assure that the field array will contain that position && that the field position is not equal to 19 (A covered mine)
			//that way, we wont place a mine twice in the same cell
			if((position < totalCells) && (field[position] != COVERED_MINE)) { 	
				
				//defines the column of the random position
				int current_col = position % TOTAL_COLS;
				//place a covered mine (19) in the random position
				field[position] = COVERED_MINE;
				//increment the counter
				i++;
				
				
				//Now that the mine was planted, we need to let the vicinity know and add 1 to the value of each neighbor:
				
				// if the position`s current column is not the West border of the 2D array...  (this will assure the West boundary)
				if(current_col > 0) {
					// cell is defined as the North-West border`s neighbor position.
					currentCell = position -1 - TOTAL_COLS;
					//If the cell is not outside of boundary...
					if(currentCell >= 0) {
						// ...also, if the position is not a covered mine...
						if(field[currentCell] != COVERED_MINE) {
							//.. add 1 to the neighbor.
							field[currentCell] += 1;
						}
					}
					// the same for the West neighbor
					currentCell = position - 1;
					if(currentCell >= 0) {
						if(field[currentCell] != COVERED_MINE) {
							field[currentCell] += 1;
						}
					}
					//the same for the South-West neighbor. mild change...
					currentCell = position + TOTAL_COLS - 1;
					//now it checks the upper boundary of array (or South boundary of 2D array) 
					if(currentCell < totalCells) {
						if(field[currentCell] != COVERED_MINE) {
							field[currentCell] += 1;
						}
					}				
				}
				
				//sets the cell as the North neighbor in the 2d array
				currentCell = position - TOTAL_COLS;
				//make sure it is inside the boundary
				if(currentCell >= 0) {
					if(field[currentCell] != COVERED_MINE) {
						field[currentCell] += 1;
					}
				}
				
				//same for the South neighbor
				currentCell = position + TOTAL_COLS;
				if(currentCell < totalCells) {
					if(field[currentCell] != COVERED_MINE) {
						field[currentCell] += 1;
					}
				}
				
				// if the position`s current column is not the East border of the 2D array...  (this will assure the East boundary)
				if(current_col < (TOTAL_COLS -1)) {
					//set +1 in the North-East neighbor
					currentCell = position - TOTAL_COLS + 1;
					if(currentCell >= 0) {
						if(field[currentCell] != COVERED_MINE) {
							field[currentCell] += 1;
						}						
					}
					//the same for the South-East neighbor
					currentCell = position + TOTAL_COLS + 1;
					if(currentCell < totalCells) {
						if(field[currentCell] != COVERED_MINE) {
							field[currentCell] += 1;
						}						
					}
					//the same for the East neighbor
					currentCell = position + 1;
					if(currentCell < totalCells) {
						if(field[currentCell] != COVERED_MINE) {
							field[currentCell] += 1;
						}
					}					
				}
			}
		}
	}//exits newGame()
	
	
	// Now that the Board is set, we also need to set the rules for when a cell that contains a value of 0 is exposed if a neighbor is also exposed.
	
	
	// create a void method that will find the empty cells
	private void find_empty_cells(int j) {
		//set the column number 
		int current_col = j % TOTAL_COLS;
		int currentCell;
		
		//if the cell position is outside of the West Boundary...
		if(current_col > 0) {
			
			//define cell as the North-West neighbor
			currentCell = j - TOTAL_COLS - 1;
			//if the neighbor is not out of boundary...
			if(currentCell >= 0) {
				//...and its value is bigger than MINE_CELL(9)...
				if(field[currentCell] > MINE) {
					//...uncover cell (-10)
					field[currentCell] -= COVER;
					// if the cell has a value of 0, then call the method again until all 0 value boundary is exposed.
					if(field[currentCell] == ZERO_CELL) {
						find_empty_cells(currentCell);
					}
				}
			}
			//same but for the West neighbor			
			currentCell = j - 1;
			if(currentCell >= 0) {
				if(field[currentCell] > MINE) {
					field[currentCell] -= COVER;
					if(field[currentCell] == ZERO_CELL) {
						find_empty_cells(currentCell);
					}
				}
			}
			//south-West
			currentCell = j + TOTAL_COLS - 1;
			if(currentCell < totalCells) {
				if(field[currentCell] > MINE) {
					field[currentCell] -= COVER;
					if(field[currentCell] == ZERO_CELL) {
						find_empty_cells(currentCell);
					}
				}
			}
		}
		//North neighbor
		currentCell = j - TOTAL_COLS;
		if(currentCell >= 0) {
			if(field[currentCell] > MINE) {
				field[currentCell] -= COVER;
				if(field[currentCell] == ZERO_CELL) {
					find_empty_cells(currentCell);
				}
			}
		}
		//South neighbor
		currentCell = j + TOTAL_COLS;
		if(currentCell < totalCells) {
			if(field[currentCell] > MINE) {
				field[currentCell] -= COVER;
				if(field[currentCell] == ZERO_CELL) {
					find_empty_cells(currentCell);
				}
			}
		}
		
		if(current_col < (TOTAL_COLS -1)) {
			//same for the North-East neighbor
			currentCell = j - TOTAL_COLS + 1;
			if(currentCell >= 0) {
				if(field[currentCell] > MINE) {
					field[currentCell] -= COVER;
					if(field[currentCell] == ZERO_CELL) {
						find_empty_cells(currentCell);
					}
				}
			}
			//same but for the East neighbor			
			currentCell = j + TOTAL_COLS + 1;
			if(currentCell < totalCells) {
				if(field[currentCell] > MINE) {
					field[currentCell] -= COVER;
					if(field[currentCell] == ZERO_CELL) {
						find_empty_cells(currentCell);
					}
				}
			}
			//East
			currentCell = j + 1;
			if(currentCell < totalCells) {
				if(field[currentCell] > MINE) {
					field[currentCell] -= COVER;
					if(field[currentCell] == ZERO_CELL) {
						find_empty_cells(currentCell);
					}
				}
			}
		}			
	}
	//overrides method paintComponent from JPanel Class
	@Override
	public void paintComponent(Graphics g) {
		//set the uncover variable
		int uncover = 0;
		
		//loop over the number of rows of the game
		for (int i= 0; i < TOTAL_ROWS; i++) {
			//and also over the columns
			for (int j = 0; j < TOTAL_COLS; j++) {
				
				// access each cell
				int currentCell = field[(i * TOTAL_COLS) + j];
				
				//if game is on, and any cell is equal 9...
				if(inGame && currentCell == MINE) {
					//set inGame to false, or in other words, finish the game.
					inGame = false;
				}
				
				//if the game is over...
				if(!inGame) {
					//... change the value of a covered mine cell(19) to (9).
					if(currentCell == COVERED_MINE) {
						currentCell = PAINT_MINE;
					
					//... change the value of a marked mine cell(29) to (11).
					} else if(currentCell == MARKED_COVERED_MINE) {
						currentCell = PAINT_MARK;
						
					//... change the value of any cell that does not contain a cell and was marked to (12) a wrong mark.
					} else if(currentCell > COVERED_MINE) {
						currentCell = PAINT_WRONG_MARK;
						
					//...keep all cells that are only numbers (0-8) covered
					} else if(currentCell > MINE) {
						currentCell = PAINT_COVER;
					} 
				//if game is still on...	
				} else {
					//if the cell was marked, keep it marked.
					if(currentCell > COVERED_MINE) {
						currentCell = PAINT_MARK;
					
					//if the cell is covered, keep covered.
					} else if (currentCell > MINE) {
						currentCell = PAINT_COVER;
						uncover++;
					}
				}
				
				g.drawImage(img[currentCell], (j * SIZE_SQUARE), (i * SIZE_SQUARE), this);
			}//exit columns
		}//exit rows
		
		//if the number of uncover is zero, and inGame is True, set the status.text to "You won!"
		if(uncover == 0 && inGame) {
			
			inGame = false;
			message.setText("You won!");
		
		//if the inGame is false, set the status.text to "Game over"
		} else if(!inGame) {
			message.setText("Game over");
		}
	}//exit pintComponent
	
	private class MinesAdapter extends MouseAdapter {
		
		
		public void mousePressed(MouseEvent e) {
			
			//get the click event coordinates
			int x = e.getX();
			int y = e.getY();
			
			//set the column and row indices clicked
			int cCol = x / SIZE_SQUARE;
			int cRow = y / SIZE_SQUARE;
			
			//initiate a boolean that will say if repaint is required
			boolean doRepaint = false;
			
			//if the game is over, start a new game and repaint the board (in this case, all the borders will be painted as covered)
			if(!inGame) {
				newGame();
				repaint();
			}
			
			//if statement that guarantees that the click was inside of the JPanel Board.
			if((x < TOTAL_COLS * SIZE_SQUARE) && (y < TOTAL_ROWS * SIZE_SQUARE)) {
				
				//if the button3 or secondary button is clicked...
				if(e.getButton() == MouseEvent.BUTTON3) {
					
					// if the field clicked is bigger than 9 set the doRepaint to true
					if(field[(cRow * TOTAL_COLS) + cCol] > MINE) {
						doRepaint = true;
						
						//if the field clicked is bigger equal or less than 19...
						if(field[(cRow * TOTAL_COLS) + cCol] <= COVERED_MINE) {
							
							//if there are mines left increase the value of the right-clicked cell by 10,
							//decrease the number of mines left in the display (status)
							if(minesRemaining > 0) {
								field[(cRow * TOTAL_COLS) + cCol] += MARK;
								minesRemaining--;
								String msg = Integer.toString(minesRemaining);
								message.setText(msg);
							//if the minesleft is 0, than show the message "No marks left" in the display
							} else {
								message.setText("No marks left");
							}
						
						//if the field clicked is bigger than 19 (marked cell)...
						} else {
							//unmark the cell and add one to the minesleft display
							field[(cRow * TOTAL_COLS) + cCol] -= MARK;
							minesRemaining++;
							String msg = Integer.toString(minesRemaining);
							message.setText(msg);
						}
					}
				
				//if the button1/2 primary or middle button is clicked...
				} else {
					
					//if the cell value is bigger than 19, exit the method
					if(field[(cRow * TOTAL_COLS) + cCol] > COVERED_MINE) {
						return;
					}
					
					//if the cell value is between 9 and 29
					if(field[(cRow * TOTAL_COLS) + cCol] > MINE && field[(cRow * TOTAL_COLS) + cCol] < MARKED_COVERED_MINE) {
						
						//uncover cell (by taking 10 out of its value)
						field[(cRow * TOTAL_COLS) + cCol] -= COVER;
						doRepaint = true;
						
						//if the clicked cell is a mine, set the inGame to false, finish the game
						if(field[(cRow * TOTAL_COLS) + cCol] == MINE) {
							inGame = false;
						}
						
						//if the clicked cell is an empty cell, call the find_empty_cells with the position of the clicked cell.
						if(field[(cRow * TOTAL_COLS) + cCol] == ZERO_CELL) {
							find_empty_cells((cRow * TOTAL_COLS) + cCol);
						}
					}
				}
				
				//if any cell, that is not a mine is clicked, than repaint the the board.
				if (doRepaint) {
					repaint();
				}
			}
		}// exit mousePressed class
	}// exit MouseAdapter Class	
}//Class Board
