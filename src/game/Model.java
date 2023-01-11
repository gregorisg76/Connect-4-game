package game;

import interfaces.IModel;
import util.GameSettings;
import util.InputUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * This class represents the state of the game.
 * It has been partially implemented, but needs to be completed by you.
 *
 * @author <s1950427>
 */
public class Model implements IModel // DO NOT EDIT THIS LINE
{
	// A reference to the game settings from which you can retrieve the number
	// of rows and columns the board has and how long the win streak is.
	private GameSettings settings;
	private byte StateOfBoard[][];
	private byte activePlayer;
	// The default constructor.
	private byte statusOfGame;
	public Model()
	{
		// You probably won't need this.
	}
	
	// A constructor that takes another instance of the same type as its parameter.
	// This is called a copy constructor.
	public Model(IModel model)
	{
		// You may (or may not) find this useful for advanced tasks.
	}
	
	// Called when a new game is started on an empty board.
	public void initNewGame(GameSettings settings)
	{


		this.activePlayer = 1;
		this.settings = settings;

		this.StateOfBoard = new byte[settings.nrRows][settings.nrCols];
		this.statusOfGame = IModel.GAME_STATUS_ONGOING;
		for(int i=0; i<settings.nrRows; i++){
			for(int j=0; j<settings.nrCols; j++){
				this.StateOfBoard[i][j] = 0;
			}
		}

		// This method still needs to be extended.
	}
	
	// Called when a game state should be loaded from the given file.
	public void initSavedGame(String fileName) {
		// This is an advanced feature. If not attempting it, you can ignore this method.
		Scanner save = null;


		while (save==null) {

			try {
				String path = "saves/" + fileName;
				save = new Scanner(new File(path));
			} catch (FileNotFoundException e) {
				System.out.print("Please insert an existing file (e.g. Save.txt): ");
				fileName = InputUtil.readStringFromUser();
				save = null;
			}





		}

		int nrRows = save.nextInt();
		int nrCols = save.nextInt();
		int minStreakLength = save.nextInt();
		this.activePlayer = save.nextByte();

		this.settings = new GameSettings(nrRows, nrCols, minStreakLength);
		this.StateOfBoard = new byte[nrRows][nrCols];
		this.statusOfGame = IModel.GAME_STATUS_ONGOING;
		save.nextLine();
		for (int i = 0; i < nrRows; i++) {
			String row = save.nextLine();
			for (int j = 0; j < nrCols; j++) {
				this.StateOfBoard[i][j] = Byte.parseByte(String.valueOf(row.charAt(j)));
			}
		}
	}
	
	// Returns whether or not the passed in move is valid at this time.
	public boolean isMoveValid(int move)
	{
		// Assuming all moves are valid.

		if (move > settings.nrCols-1 || move < -1) {
			return false;
		}

		if (move == -1){
			return true;
		}
		return !isColumnFull(move);
	}
	
	// Actions the given move if it is valid. Otherwise, does nothing.
	public void makeMove(int move) {

		if (move != -1) {
			int emptyRow = getGameSettings().nrRows - 1;
			while (this.StateOfBoard[emptyRow][move] != 0) {
				emptyRow -= 1;
			}

			this.StateOfBoard[emptyRow][move] = getActivePlayer();

			if (thereIsWin()){
				if (getActivePlayer() == 1){
					this.statusOfGame =  IModel.GAME_STATUS_WIN_1;
				} else {
					this.statusOfGame =  IModel.GAME_STATUS_WIN_2;
				}
			}

			if (getActivePlayer() == 1) {
				this.activePlayer = 2;
			} else {
				this.activePlayer = 1;
			}

			if(isBoardFull()){
				this.statusOfGame = IModel.GAME_STATUS_TIE;
			}

		} else {
			if (getActivePlayer() == 1) {
				this.statusOfGame =  IModel.GAME_STATUS_WIN_2;
			} else {
				this.statusOfGame =  IModel.GAME_STATUS_WIN_1;
			}
		}
	}



	// Returns one of the following codes to indicate the game's current status.
	// IModel.java in the "interfaces" package defines constants you can use for this.
	// 0 = Game in progress
	// 1 = Player 1 has won
	// 2 = Player 2 has won
	// 3 = Tie (board is full and there is no winner)
	public byte getGameStatus()
	{
		// Assuming the game is never ending.

		return this.statusOfGame;
	}
	
	// Returns the number of the player whose turn it is.
	public byte getActivePlayer()
	{
		// Assuming it is always the turn of player 1.

		return this.activePlayer;
	}
	
	// Returns the owner of the piece in the given row and column on the board.
	// Return 1 or 2 for players 1 and 2 respectively or 0 for empty cells.
	public byte getPieceIn(int row, int column)
	{
		// Assuming all cells are empty for now.
		return this.StateOfBoard[row][column];
	}
	
	// Returns a reference to the game settings, from which you can retrieve the
	// number of rows and columns the board has and how long the win streak is.
	public GameSettings getGameSettings()
	{
		return settings;
	}
	
	// =========================================================================
	// ================================ HELPERS ================================
	// =========================================================================
	
	// You may find it useful to define some helper methods here.

	// Returns if the board is full in boolean type after a move.
	public boolean isBoardFull(){
		for(int i=0; i<settings.nrRows; i++){
			for(int j=0; j<settings.nrCols; j++){
				if(getPieceIn(i,j) == 0){
					return false;
				}
			}
		}
		return true;
	}

	// Returns if the column selected for the move is full in boolean type.
	public boolean isColumnFull(int move){
		for(int i=0; i<settings.nrRows; i++) {
			if (getPieceIn(i, move) == 0) {
				return false;
			}
		}
		return true;
	}

	public boolean thereIsWin() {

		// Check each row if any player won
		for(int i=0; i<settings.nrRows; i++) {
			int totalStreak = 1;
			int j=1;
			while(j<settings.nrCols){
				while(j<settings.nrCols && getPieceIn(i,j) == getPieceIn(i,j-1) && getPieceIn(i,j) != 0){
					totalStreak+=1;
					j+=1;
				}
				if (totalStreak >= settings.minStreakLength){
					return true;
				} else {
					totalStreak = 1;
					j+=1;
				}
			}

		}

		// Check each column if any player won
		for(int j=0; j<settings.nrCols; j++) {
			int totalStreak = 1;
			int i=1;
			while(i<settings.nrRows){
				while(i<settings.nrRows && getPieceIn(i,j) == getPieceIn(i-1,j) && getPieceIn(i,j) != 0){
					totalStreak+=1;
					i+=1;
				}
				if (totalStreak >= settings.minStreakLength){
					return true;
				} else {
					totalStreak = 1;
					i+=1;
				}
			}

		}

		// Check if any player completed the streak diagonally from bottom left to top right
		for(int k=1; k<settings.nrCols; k++) {
			int totalStreak = 1;
			int i = settings.nrRows - 2;
			int j = k;
			while (j < settings.nrCols &&
					i >= 0) {
				while ( j < settings.nrCols &&
						i >= 0 &&
						getPieceIn(i, j) == getPieceIn(i + 1, j - 1)
						&& getPieceIn(i, j) != 0) {
					totalStreak += 1;
					j += 1;
					i -= 1;
				}
				if (totalStreak >= settings.minStreakLength) {
					return true;
				} else {
					totalStreak = 1;
					j += 1;
					i -= 1;
				}
			}
		}

		for(int k=1; k<settings.nrRows; k++) {
			int totalStreak = 1;
			int i = k;
			int j = 1;
			while (j < settings.nrCols &&
					i < settings.nrRows -1 &&
					i >= 0) {
				while ( j < settings.nrCols &&
						i < settings.nrRows -1 &&
						i >=0 &&
						getPieceIn(i, j) == getPieceIn(i + 1, j - 1) &&
						getPieceIn(i, j) != 0) {
					totalStreak += 1;
					j += 1;
					i -= 1;
				}
				if (totalStreak >= settings.minStreakLength) {
					return true;
				} else {
					totalStreak = 1;
					j += 1;
					i -= 1;
				}
			}
		}

		// Check if any player completed the streak diagonally from top left to bottom right
		for(int k=1; k<settings.nrCols; k++) {
			int totalStreak = 1;
			int i = 1;
			int j = k;
			while (j < settings.nrCols &&
					i < settings.nrRows) {
				while ( j < settings.nrCols &&
						i < settings.nrRows &&
						getPieceIn(i, j) == getPieceIn(i - 1, j - 1)
						&& getPieceIn(i, j) != 0) {
					totalStreak += 1;
					j += 1;
					i += 1;
				}
				if (totalStreak >= settings.minStreakLength) {
					return true;
				} else {
					totalStreak = 1;
					j += 1;
					i += 1;
				}
			}
		}

		for(int k=1; k<settings.nrRows; k++) {
			int totalStreak = 1;
			int i = k;
			int j = 1;
			while (j < settings.nrCols &&
					i < settings.nrRows) {
				while ( j < settings.nrCols &&
						i < settings.nrRows &&
						getPieceIn(i, j) == getPieceIn(i - 1, j - 1) &&
						getPieceIn(i, j) != 0) {
					totalStreak += 1;
					j += 1;
					i += 1;
				}
				if (totalStreak >= settings.minStreakLength) {
					return true;
				} else {
					totalStreak = 1;
					j += 1;
					i += 1;
				}
			}
		}

		return false;
	}
}
