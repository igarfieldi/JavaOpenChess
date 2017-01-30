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
	
	/**
	 * Checks if that cat can move.
	 * 
	 * @return true if it can move
	 */
	
	public boolean canMove();
	
	/**
	 * Checks if it's possible for the cat to capture any piece.
	 * 
	 * @return true if can capture piece
	 */
	
	public boolean canCapture();
	
	/**
	 * Finds a random move that can capture an enemy piece.
	 * 
	 * @return field of the capturing piece
	 */
	
	public Field getRandomThreateningMove();
	
	/**
	 * Finds a random move.
	 * 
	 * @return field of the target move
	 */
	
	public Field getRandomNormalMove();
	
	/**
	 * Puts cat to sleep; changes the behaviour from Cat to SleepingCat.
	 */
	
	public void sleepCat();
	
}