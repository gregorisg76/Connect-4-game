package interfaces;

import util.GameSettings;

/**
 * DO NOT CHANGE THIS FILE IN ANY WAY!
 * IF YOU DO, YOU MAY GET ZERO FOR THE ASSIGNMENT!
 *
 * This interface defines which methods a view must implement.
 *
 * @author David Symons
 */
public interface IView
{
	// Methods to display a variety of information.
	public void displayWelcomeMessage();
	public void displayChosenMove(int move);
	public void displayMoveRejectedMessage(int move);
	public void displayActivePlayer(byte playerID);
	public void displayGameStatus(byte gameStatus);
	public void displayBoard(IModel model);
	
	// Methods to request user input.
	public char requestMenuSelection();
	public GameSettings requestGameSettings();
	public IPlayer requestPlayerSelection(byte playerId);
	public String requestSaveFileName();
}
