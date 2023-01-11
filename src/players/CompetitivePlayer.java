package players;

import interfaces.IModel;
import interfaces.IPlayer;

/**
 * Implementing this player is an advanced task.
 * See assignment instructions for what to do.
 * If not attempting it, just upload the file as it is.
 *
 * @author <s1950427>
 */
public class CompetitivePlayer implements IPlayer // DO NOT EDIT THIS LINE
{
	// A reference to the model, which you can use to get information about
	// the state of the game. Do not use this model to make any moves!
	private IModel model;

	private int move;

	private byte StateOfBoard[][];

	// The constructor is called when the player is selected from the game menu.
	public CompetitivePlayer()
	{


		// You may (or may not) need to perform some initialisation here.
	}

	// This method is called when a new game is started or loaded.
	// You can use it to perform any setup that may be required before
	// the player is asked to make a move. The second argument tells
	// you if you are playing as player 1 or player 2.
	public void prepareForGameStart(IModel model, byte playerId)
	{
		this.model = model;
		this.move = Math.round(model.getGameSettings().nrCols/2) + 1;
	}
	// This method is called to ask the player to take their turn.
	// The move they choose should be returned from this method.
	public int chooseMove()
	{
		if (canWin() != 100){
			int winning_move = canWin();
			return winning_move;
		}

		if (canLoose() != 100){

			if (canLoose() == -1){
				return IModel.CONCEDE_MOVE;
			} else {


				int loosing_move = canLoose();
				return loosing_move;
			}
		}

		int count = 0;
		this.StateOfBoard = new byte[model.getGameSettings().nrRows][model.getGameSettings().nrCols];
		for (int i = 0; i < model.getGameSettings().nrRows; i++) {
			for (int j = 0; j < model.getGameSettings().nrCols; j++) {
				this.StateOfBoard[i][j] = model.getPieceIn(i, j);
				count += model.getPieceIn(i, j);
			}
		}
		if (count <= 13 + model.getGameSettings().minStreakLength * 3){

			if (count <= 1){
				move -= 1;
			} else if (count <= 4) {
				int m = 1;
				while(m < model.getGameSettings().nrCols - 1){
					if (model.getPieceIn(model.getGameSettings().nrRows - 1, m) != 0){
						move = m - 1;
						break;
					}
					m += 1;
				}
			}
			else if (count <= 7) {
				int m = model.getGameSettings().nrCols - 2;
				while(m > 0){
					if (model.getPieceIn(model.getGameSettings().nrRows - 1, m) != 0){
						move = m + 1;
						break;
					}
					m -= 1;
				}
			} else {
				if (move == Math.round(model.getGameSettings().nrCols/2) - 2){
					move = Math.round(model.getGameSettings().nrCols/2) - 1;
				} else {
					move = Math.round(model.getGameSettings().nrCols/2) - 2;
				}

			}
		} else{
			int sumRow = 0;
			int row =  model.getGameSettings().nrRows - 1;
			while (sumRow < model.getGameSettings().minStreakLength - 2 && row >= 1){
				sumRow = 0;
				row -= 1;
				for(int col = 0; col < model.getGameSettings().nrCols - 1; col++){
					sumRow += model.getPieceIn(row,col);
				}
			}

			int col = 1;
			while(col < model.getGameSettings().nrCols - 3){
				if (model.getPieceIn(row,col) > 0 &&
						model.getPieceIn(row,col+1) > 0 &&
						model.getPieceIn(row,col+2) == 0){
					if (model.getPieceIn(row+1,col+2) > 0){
						move = col - 1;
						col += 10;
					}
				}
				col += 1;
			}

		}


		if (model.getPieceIn(Math.round((model.getGameSettings().nrRows/2)+1), move) != 0){
			if (move <= model.getGameSettings().minStreakLength){
				move += model.getGameSettings().minStreakLength - 1;
			} else if (move > model.getGameSettings().minStreakLength){
				move -= model.getGameSettings().minStreakLength - 1;
			}
		}
		if (move == model.getGameSettings().nrCols-1){
			if(model.getPieceIn(model.getGameSettings().nrRows-1, move) != 0){
				move -= 1;
			}
		}

		if (move == 0){
			if(model.getPieceIn(model.getGameSettings().nrRows-1, move) != 0){
				move += 1;
			}
		}
		if (move > model.getGameSettings().nrCols-1){
			move = 0;
		}

		int counter = 0;
		while(!model.isMoveValid(move) || canLooseNextRound(move)) {
			move -= 1;
			if (move < 0) {
				move = model.getGameSettings().nrCols-1;
			}
			counter += 1;
			if (counter > model.getGameSettings().nrCols + 1){
				return IModel.CONCEDE_MOVE;
			}
		}
		return move;
	}
	// Helper functions

	private boolean thereIsWin() {

		// Check each row if any player won
		for(int i=0; i<model.getGameSettings().nrRows; i++) {
			int totalStreak = 1;
			int j=1;
			while(j<model.getGameSettings().nrCols){
				while(j<model.getGameSettings().nrCols &&
						this.StateOfBoard[i][j] == this.StateOfBoard[i][j-1] &&
						this.StateOfBoard[i][j] != 0){
					totalStreak+=1;
					j+=1;
				}
				if (totalStreak >= model.getGameSettings().minStreakLength){
					return true;
				} else {
					totalStreak = 1;
					j+=1;
				}
			}

		}

		// Check each column if any player won
		for(int j=0; j<model.getGameSettings().nrCols; j++) {
			int totalStreak = 1;
			int i=1;
			while(i<model.getGameSettings().nrRows){
				while(i<model.getGameSettings().nrRows &&
						this.StateOfBoard[i][j] == this.StateOfBoard[i-1][j] &&
						this.StateOfBoard[i][j] != 0){
					totalStreak+=1;
					i+=1;
				}
				if (totalStreak >= model.getGameSettings().minStreakLength){
					return true;
				} else {
					totalStreak = 1;
					i+=1;
				}
			}

		}

		// Check if any player completed the streak diagonally from bottom left to top right
		for(int k=1; k<model.getGameSettings().nrCols; k++) {
			int totalStreak = 1;
			int i = model.getGameSettings().nrRows - 2;
			int j = k;
			while (j < model.getGameSettings().nrCols &&
					i >= 0) {
				while ( j < model.getGameSettings().nrCols &&
						i >= 0 &&
						this.StateOfBoard[i][j] == this.StateOfBoard[i+1][j-1] &&
						this.StateOfBoard[i][j] != 0) {
					totalStreak += 1;
					j += 1;
					i -= 1;
				}
				if (totalStreak >= model.getGameSettings().minStreakLength) {
					return true;
				} else {
					totalStreak = 1;
					j += 1;
					i -= 1;
				}
			}
		}

		for(int k=1; k<model.getGameSettings().nrRows; k++) {
			int totalStreak = 1;
			int i = k;
			int j = 1;
			while (j < model.getGameSettings().nrCols &&
					i < model.getGameSettings().nrRows -1 &&
					i >= 0) {
				while ( j < model.getGameSettings().nrCols &&
						i < model.getGameSettings().nrRows -1 &&
						i >=0 &&
						this.StateOfBoard[i][j] == this.StateOfBoard[i+1][j-1] &&
						this.StateOfBoard[i][j] != 0) {
					totalStreak += 1;
					j += 1;
					i -= 1;
				}
				if (totalStreak >= model.getGameSettings().minStreakLength) {
					return true;
				} else {
					totalStreak = 1;
					j += 1;
					i -= 1;
				}
			}
		}

		// Check if any player completed the streak diagonally from top left to bottom right
		for(int k=1; k<model.getGameSettings().nrCols; k++) {
			int totalStreak = 1;
			int i = 1;
			int j = k;
			while (j < model.getGameSettings().nrCols &&
					i < model.getGameSettings().nrRows) {
				while ( j < model.getGameSettings().nrCols &&
						i < model.getGameSettings().nrRows &&
						this.StateOfBoard[i][j] == this.StateOfBoard[i-1][j-1] &&
						this.StateOfBoard[i][j] != 0) {
					totalStreak += 1;
					j += 1;
					i += 1;
				}
				if (totalStreak >= model.getGameSettings().minStreakLength) {
					return true;
				} else {
					totalStreak = 1;
					j += 1;
					i += 1;
				}
			}
		}

		for(int k=1; k<model.getGameSettings().nrRows; k++) {
			int totalStreak = 1;
			int i = k;
			int j = 1;
			while (j < model.getGameSettings().nrCols &&
					i < model.getGameSettings().nrRows) {
				while ( j < model.getGameSettings().nrCols &&
						i < model.getGameSettings().nrRows &&
						this.StateOfBoard[i][j] == this.StateOfBoard[i-1][j-1] &&
						this.StateOfBoard[i][j] != 0) {
					totalStreak += 1;
					j += 1;
					i += 1;
				}
				if (totalStreak >= model.getGameSettings().minStreakLength) {
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

	private int canWin(){
		this.StateOfBoard = new byte[model.getGameSettings().nrRows][model.getGameSettings().nrCols];
		for (int i = 0; i < model.getGameSettings().nrRows; i++) {
			for (int j = 0; j < model.getGameSettings().nrCols; j++) {
				this.StateOfBoard[i][j] = model.getPieceIn(i, j);
			}
		}

		for (int k = 0; k < model.getGameSettings().nrCols; k++){
			int emptyRow = model.getGameSettings().nrRows - 1;
			while (this.StateOfBoard[emptyRow][k] != 0) {
				if (emptyRow != 0) {
					emptyRow -= 1;
				} else {
					emptyRow -= 1;
					break;
				}

			}
			if (emptyRow != -1){
				this.StateOfBoard[emptyRow][k] = model.getActivePlayer();
				if (thereIsWin()){
					return(k);
				} else {
					this.StateOfBoard[emptyRow][k] = 0;
				}
			}

		}


		return 100;

	}

	private int canLoose(){
		this.StateOfBoard = new byte[model.getGameSettings().nrRows][model.getGameSettings().nrCols];
		for (int i = 0; i < model.getGameSettings().nrRows; i++) {
			for (int j = 0; j < model.getGameSettings().nrCols; j++) {
				this.StateOfBoard[i][j] = model.getPieceIn(i, j);
			}
		}

		int count = 0;
		int correct_move = 0;
		for (int k = 0; k < model.getGameSettings().nrCols; k++) {
			int emptyRow = model.getGameSettings().nrRows - 1;
			while (this.StateOfBoard[emptyRow][k] != 0) {
				if (emptyRow != 0) {
					emptyRow -= 1;
				} else {
					emptyRow -= 1;
					break;
				}
			}

			if (emptyRow != -1){

				if (model.getActivePlayer() == 1) {
					this.StateOfBoard[emptyRow][k] = 2;
				} else {
					this.StateOfBoard[emptyRow][k] = 1;
				}

				if (thereIsWin()) {
					correct_move += k;
					count += 1;
					if (count == 2) {
						return -1;
					}
					this.StateOfBoard[emptyRow][k] = model.getActivePlayer();

					for (int l = 0; l < model.getGameSettings().nrCols; l++) {
						int empty = model.getGameSettings().nrRows - 1;
						while (this.StateOfBoard[empty][l] != 0) {
							if (empty != 0) {
								empty -= 1;
							} else {
								empty -= 1;
								break;

							}
						}

						if (empty != -1){
							if (model.getActivePlayer() == 1) {
								this.StateOfBoard[empty][l] = 2;
							} else{
								this.StateOfBoard[empty][l] = 1;
							}

							if (thereIsWin()) {
								return -1;
							}

							this.StateOfBoard[empty][l] = 0;
						}

					}
				}

				this.StateOfBoard[emptyRow][k] = 0;
			}

		}

		if (count == 0){
			return 100;
		}
		return correct_move;
	}



	private boolean canLooseNextRound(int move) {

		if (!model.isMoveValid(move)){
			return true;
		}
		this.StateOfBoard = new byte[model.getGameSettings().nrRows][model.getGameSettings().nrCols];
		for (int i = 0; i < model.getGameSettings().nrRows; i++) {
			for (int j = 0; j < model.getGameSettings().nrCols; j++) {
				this.StateOfBoard[i][j] = model.getPieceIn(i, j);
			}
		}

		int emptyRow = model.getGameSettings().nrRows - 1;
		while (this.StateOfBoard[emptyRow][move] != 0) {
			if (emptyRow != 0) {
				emptyRow -= 1;
			}
		}

		this.StateOfBoard[emptyRow][move] = model.getActivePlayer();

		for (int l = 0; l < model.getGameSettings().nrCols; l++) {
			int empty = model.getGameSettings().nrRows - 1;
			while (this.StateOfBoard[empty][l] != 0) {
				if (empty != 0) {
					empty -= 1;
				} else {
					empty -= 1;
					break;

				}
			}

			if (empty != -1) {
				if (model.getActivePlayer() == 1) {
					this.StateOfBoard[empty][l] = 2;
				} else {
					this.StateOfBoard[empty][l] = 1;
				}

				if (thereIsWin()) {
					return true;
				}

				this.StateOfBoard[empty][l] = 0;
			}
		}
		return false;
	}
}
