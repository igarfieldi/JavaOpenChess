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
	
	public boolean isAlive();
	
	/**
	 * Checks if the cat piece has to respawn. If it does, calls the method
	 * responsible for that and resets the timer. If not, checks if the cat is
	 * alive and reduces the timer by one.
	 */
	
	public void updateRespawnTimer();
	
	/**
	 * Method responsible for finding the next move randomly for the cat piece.
	 * Moves that can capture pieces get selected first; if there isn't any
	 * piece to capture, a random move will be returned.
	 * 
	 * @return random field to move to
	 */
	
	public Field getNextMove();
	
	/**
	 * Finds the position the cat is on.
	 * 
	 * @return field of the cat
	 */
	public Field getCurrentPosition();
	
	/**
	 * Method which gets a list of moves and chooses a random element from it.
	 * 
	 * @param moves
	 *            List of moves
	 * @return random move
	 */
	
	public Move getRandomMove(List<Move> moves);
	
	/**
	 * Method which gets a list of fields and chooses a random element from it.
	 * 
	 * @param fields
	 *            List of fields
	 * @return random field
	 */
	public Field getRandomField(List<Field> fields);
	
	/**
	 * Gets called after the cat has captured a piece. If it's already sleeping,
	 * reduces the timer by one.
	 */
	
	public void updateSleepTimer();
	
	/**
	 * Checks if the cat is sleeping.
	 * 
	 * @return true if it's sleeping
	 */
	
	public boolean isSleeping();
	
}