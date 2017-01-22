package jchess.gamelogic.ai;

import java.util.List;

import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;

public interface ICatAi
{
	
	/**
	 * Checks if the cat piece is on the board.
	 * 
	 * @return true if the cat is on the board
	 */
	
	boolean isAlive();
	
	/**
	 * Checks if the cat piece has to respawn. If it does, calls the method
	 * responsible for that and resets the timer. If not, checks if the cat is
	 * alive and reduces the timer by one.
	 */
	
	void updateRespawnTimer();
	
	/**
	 * Method responsible for finding the next move randomly for the cat piece.
	 * 
	 * @return random field to move to
	 */
	
	Field getNextMove();
	
	/**
	 * Finds the position the cat is on.
	 * 
	 * @return field of the cat
	 */
	Field getCurrentPosition();
	
	/**
	 * Method which gets a list of moves and chooses a random element from it.
	 * 
	 * @param moves
	 *            List of moves
	 * @return random move
	 */
	
	Move getRandomMove(List<Move> moves);
	
	/**
	 * Method which gets a list of fields and chooses a random element from it.
	 * 
	 * @param fields
	 *            List of fields
	 * @return random field
	 */
	Field getRandomField(List<Field> fields);

	public void updateSleepTimer();

	public boolean isSleeping();
	
}