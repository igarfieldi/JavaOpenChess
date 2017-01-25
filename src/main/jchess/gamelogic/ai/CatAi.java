package jchess.gamelogic.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jchess.gamelogic.Player;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

/**
 * Class responsible for controlling the Cat movement on the board.
 * 
 * @author Panagiota Thomopoulou
 *
 */

public class CatAi implements ICatAi
{
	private static final int DEFAULT_SLEEP_TIME = 1;
	private static final int DEFAULT_RESPAWN_TIME = 2;
	private IChessboardModel board;
	private Piece cat;
	private IChessboardController chessboard;
	private Player aiPlayer;
	private int turnsToRespawn;
	private int turnsToWakeUp;
	private boolean isSleeping;
	
	public CatAi(IChessboardController chessboard, Player aiPlayer)
	{
		this.chessboard = chessboard;
		board = chessboard.getBoard();
		turnsToWakeUp = DEFAULT_SLEEP_TIME;
		wakeUpCat();
		turnsToRespawn = DEFAULT_RESPAWN_TIME;
		this.aiPlayer = aiPlayer;
		spawnCat();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.ai.ICatAi#isAlive()
	 */
	
	@Override
	public boolean isAlive()
	{
		return !board.getPieces(aiPlayer).isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.ai.ICatAi#updateRespawnTimer()
	 */
	
	@Override
	public void updateRespawnTimer()
	{
		if(turnsToRespawn == 0)
		{
			spawnCat();
			resetRespawnTimer();
		}
		if(!isAlive())
		{
			turnsToRespawn--;
		}
	}
	
	/**
	 * Places a new cat piece on the board.
	 */
	
	private void spawnCat()
	{
		PieceFactory factory = PieceFactory.getInstance();
		board.setPiece(getRandomField(board.getEmptyFields()),
		        factory.buildPiece(aiPlayer, new Direction(0, -1), PieceType.CAT));
		for(Piece piece : board.getPieces(aiPlayer))
		{
			cat = piece;
		}
	}
	
	private void resetRespawnTimer()
	{
		turnsToRespawn = DEFAULT_RESPAWN_TIME;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.ai.ICatAi#updateSleepTimer()
	 */
	
	@Override
	public void updateSleepTimer()
	{
		if(turnsToWakeUp == 0)
		{
			wakeUpCat();
			resetSleepTime();
		}
		if(isSleeping)
		{
			turnsToWakeUp--;
		}
	}
	
	/**
	 * Resets sleep timer to the default sleep time and calls the wakeUpCat
	 * method to wake the cat up.
	 */
	
	private void resetSleepTime()
	{
		turnsToWakeUp = DEFAULT_SLEEP_TIME;
		wakeUpCat();
		
	}
	
	private void wakeUpCat()
	{
		isSleeping = false;
	}
	
	private void sleepCat()
	{
		isSleeping = true;
	}
	
	public boolean isSleeping()
	{
		return isSleeping;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.ai.ICatAi#getNextMove()
	 */
	@Override
	public Field getNextMove()
	{
		if(!chessboard.getThreateningMoves(cat, aiPlayer).isEmpty())
		{
			sleepCat();
			return getRandomCaptureMove().getTo();
			
		} else
		{
			ArrayList<Move> moves = new ArrayList<Move>(chessboard.getPossibleMoves(cat, false));
			return getRandomMove(moves).getTo();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.ai.ICatAi#getCurrentPosition()
	 */
	@Override
	public Field getCurrentPosition()
	{
		return board.getField(cat);
	}
	
	/**
	 * Chooses a random move that can capture a piece from a list of all the
	 * moves that can capture pieces.
	 * 
	 * @return move that captures a piece
	 */
	
	private Move getRandomCaptureMove()
	{
		return getRandomMove(chessboard.getThreateningMoves(cat, aiPlayer));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.ai.ICatAi#getRandomMove(java.util.List)
	 */
	@Override
	public Move getRandomMove(List<Move> moves)
	{
		Random random = new Random();
		Move randomMove = moves.get(random.nextInt(moves.size()));
		return randomMove;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.ai.ICatAi#getRandomField(java.util.List)
	 */
	@Override
	public Field getRandomField(List<Field> fields)
	{
		Random random = new Random();
		Field randomField = fields.get(random.nextInt(fields.size()));
		return randomField;
	}
	
}
