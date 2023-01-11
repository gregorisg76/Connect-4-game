package util;

/**
 * DO NOT CHANGE THIS FILE IN ANY WAY!
 * IF YOU DO, YOU MAY GET ZERO FOR THE ASSIGNMENT!
 *
 * A simple structure containing the size of the board and
 * the minimum sequence/streak length required to win.
 *
 * @author David Symons
 */
public final class GameSettings
{
	public final int nrRows;
	public final int nrCols;
	public final int minStreakLength; // The minimum sequence length required to win.
	
	// Loads the default settings if no arguments are provided.
	public GameSettings()
	{
		this(6, 7, 4);
	}
	
	// Creates an instance with the given settings.
	public GameSettings(int nrRows, int nrCols, int minStreakLength)
	{
		this.nrRows = nrRows;
		this.nrCols = nrCols;
		this.minStreakLength = minStreakLength;
	}
}
