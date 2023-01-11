package game;

import interfaces.*;
import players.*;
import util.GameSettings;
import util.InputUtil;

/**
 * This class is used to interact with the user.
 * It has been partially implemented, but needs to be completed by you.
 *
 * @author <s1950427>
 */
public class TextView implements IView // DO NOT EDIT THIS LINE
{
	public void displayWelcomeMessage()
	{
		System.out.println("Welcome to Connect Four!");
	}
	
	public void displayChosenMove(int move)
	{
		System.out.println("Selected move: " + move);
	}
	
	public void displayMoveRejectedMessage(int move)
	{
		System.out.println("The move (" + move + ") was rejected, please try again.");
	}
	
	public void displayActivePlayer(byte playerID)
	{
		System.out.println("\nPlayer " + playerID + " is next!");
	}
	
	public void displayGameStatus(byte gameStatus)
	{
		System.out.print("\nGame status: ");
		
		switch(gameStatus)
		{
			case IModel.GAME_STATUS_ONGOING: System.out.println("The game is in progress."); break;
			case IModel.GAME_STATUS_WIN_1: System.out.println("Player 1 has won!"); break;
			case IModel.GAME_STATUS_WIN_2: System.out.println("Player 2 has won!"); break;
			case IModel.GAME_STATUS_TIE: System.out.println("The game has ended in a tie!"); break;
			default : System.out.println("Error: Invalid/unknown game status"); break;
		}
	}
	
	public void displayBoard(IModel model)
	{
		System.out.println("\n-------- BOARD --------");
		
		int nrRows = model.getGameSettings().nrRows;
		int nrCols = model.getGameSettings().nrCols;


		// Remove this and replace it with an actual representation of the board.
		// System.out.println("The board has " + nrRows + " rows and " + nrCols + " columns.");
		for(int i=0; i<nrRows; i++) {

			for (int j = 0; j < nrCols; j++) {
				if (model.getPieceIn(i, j) == 0) {
					System.out.print("_ ");
				} else if (model.getPieceIn(i, j) == 1) {
					System.out.print("X ");
				} else {
					System.out.print("O ");
				}
			}
			System.out.println();
		}
		// Here is an example of how the output should look:
		//_ _ O O _ _ X
		//_ _ X O _ _ X
		//_ O X X _ _ O
		//_ X X O _ X O
		//X O O X O O O
		//X O X X X O X
	}
	
	public char requestMenuSelection()
	{
		// Display menu options.
		System.out.println("\n-------- MENU --------");
		System.out.println("(1) Start new game");
		System.out.println("(2) Resume saved game");
		System.out.println("(3) Change game settings");
		System.out.println("(4) Change players");
		
		// Request and return user input.
		System.out.print("Select an option and confirm with enter or use any other key to quit: ");
		return InputUtil.readCharFromUser();
	}
	
	public String requestSaveFileName()
	{
		System.out.println("\n-------- LOAD GAME --------");
		System.out.print("File name (e.g. Save.txt): ");
		return InputUtil.readStringFromUser();
	}
	
	public GameSettings requestGameSettings()
	{
		System.out.println("\n-------- GAME SETTINGS --------");

		// Replace these lines with code that asks the user to enter the values.
//		System.out.println("This feature has not yet been implemented, using default settings instead.");
//		int nrRows = 6;
//		int nrCols = 7;
//		int streak = 4;

		System.out.println("Please insert number of rows: ");
		int nrRows = InputUtil.readIntFromUser();
		while (nrRows<IModel.MIN_ROWS || nrRows>IModel.MAX_ROWS){
			System.out.println("The number of rows should be between " + IModel.MIN_ROWS + " and " +
					IModel.MAX_ROWS + ". " + "Please insert number of rows again: ");
			nrRows = InputUtil.readIntFromUser();
		}

		System.out.println("Please insert number of columns: ");
		int nrCols = InputUtil.readIntFromUser();
		while (nrCols<IModel.MIN_COLS || nrCols>IModel.MAX_COLS){
			System.out.println("The number of columns should be between " + IModel.MIN_COLS + " and " +
					IModel.MAX_COLS + ". " + "Please insert number of columns again: ");
			nrCols = InputUtil.readIntFromUser();
		}

		System.out.println("Please insert required streak to win: ");
		int streak = InputUtil.readIntFromUser();
		while (streak<3 || streak>nrRows || streak>nrCols){
			System.out.println("The streak should be 3 and above, less than the number of columns or the number of rows." +
					" Please insert streak again: ");
			streak = InputUtil.readIntFromUser();
		}

		// Wrap the selected settings in a GameSettings instance and return (leave this code here).
		return new GameSettings(nrRows, nrCols, streak);
	}
	
	public IPlayer requestPlayerSelection(byte playerId)
	{
		System.out.println("\n-------- CHOOSE PLAYER " + playerId + " --------");
		System.out.println("(1) HumanPlayer");
		System.out.println("(2) RoundRobinPlayer");
		System.out.println("(3) WinDetectingPlayer");
		System.out.println("(4) CompetitivePlayer");
		
		// Request user input.
		System.out.print("Select an option and confirm with enter (invalid input will select a HumanPlayer): ");
		char selectedPlayer = InputUtil.readCharFromUser();
		
		// Instantiate the selected player class.
		switch(selectedPlayer)
		{
			case '2': return new RoundRobinPlayer();
			case '3': return new WinDetectingPlayer();
			case '4': return new CompetitivePlayer();
			default: return new HumanPlayer();
		}
	}
}
