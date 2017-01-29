package jchess.gamelogic.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.Cat;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.gamelogic.pieces.SleepingCat;
import jchess.util.Direction;

/**
 * Class responsible for controlling the Cat movement on the board.
 */

public class CatAi implements ICatAi
{
	private static final int DEFAULT_SLEEP_TIME = 2;
	private static final int DEFAULT_RESPAWN_TIME = 2;
	private IChessboardModel board;
	private Piece cat;
	private IChessboardController chessboard;
	private Player aiPlayer;
	private int turnsToRespawn;
	private int turnsToWakeUp;
	
	public CatAi(IChessboardController chessboard, Player aiPlayer)
	{
		this.chessboard = chessboard;
		board = chessboard.getBoard();
		turnsToWakeUp = DEFAULT_SLEEP_TIME;
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
			resetSleepTimer();
			resetRespawnTimer();
		}
		if(!isAlive())
		{
			turnsToRespawn--;
		}
	}
	
	/**
	 * Places a new cat piece on the board randomly.
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
	
	/**
	 * Spawns a cat piece on a specific field on the board and with the desired
	 * behaviour.
	 * 
	 * @param type
	 *            type of the behaviour. Can be Cat or SleepingCat
	 * @param field
	 *            desired spawn position
	 */
	
	private void spawnCat(PieceType type, Field field)
	{
		PieceFactory factory = PieceFactory.getInstance();
		board.setPiece(field, factory.buildPiece(aiPlayer, new Direction(0, -1), type));
		for(Piece piece : board.getPieces(aiPlayer))
		{
			cat = piece;
		}
	}
	
	/**
	 * Method responsible for changing the behaviour of the cat piece from Cat
	 * to SleepingCat. Does so by removing the piece and spawning a new one on
	 * the same field with the opposite behaviour.
	 */
	
	private void changeBehaviour()
	{
		if(cat != null)
		{
			Field currentPosition = board.getField(cat);
			if(cat.getBehaviour() instanceof Cat)
			{
				board.removePiece(currentPosition);
				spawnCat(PieceType.SLEEPINGCAT, currentPosition);
			} else
			{
				board.removePiece(currentPosition);
				spawnCat(PieceType.CAT, currentPosition);
			}
		}
	}
	
	/**
	 * Resets turnstoRespawn to the default respawn time.
	 */
	
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
		if(turnsToWakeUp == 0 && isAlive())
		{
			changeBehaviour();
			resetSleepTimer();
		}
		if(isSleeping())
		{
			turnsToWakeUp--;
		}
	}
	
	/**
	 * Resets sleep timer to the default sleep time.
	 */
	
	private void resetSleepTimer()
	{
		turnsToWakeUp = DEFAULT_SLEEP_TIME;
		
	}
	
	public boolean isSleeping()
	{
		if(isAlive())
		{
			if(cat.getBehaviour() instanceof SleepingCat)
			{
				return true;
			}
		}
		return false;
	}
	
	public void sleepCat()
	{
		changeBehaviour();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.ai.ICatAi#getNextMove()
	 */
	
	public boolean canMove()
	{
		return isAlive() && !isSleeping();
	}
	
	public boolean canCapture()
	{
		if(canMove())
		{
			return getRandomThreateningMove() != null;
		}
		return false;
	}
	
	public Field getRandomThreateningMove()
	{
		List<Move> moves = new ArrayList<Move>();
		moves.addAll(removeKingCapture(chessboard.getThreateningMoves(cat, aiPlayer)));
		if(!moves.isEmpty())
		{
			return getRandomMove(moves).getTo();
		}
		return null;
	}
	
	public Field getRandomNormalMove()
	{
		List<Move> moves = new ArrayList<Move>();
		moves.addAll(removeKingCapture(chessboard.getPossibleMoves(cat, false)));
		return getRandomMove(moves).getTo();
	}
	
	/**
	 * Method that makes sure that the cat won't try to capture a king.
	 * 
	 * @param moves
	 *            set of possible moves cat can move
	 * @return moves without a king capturing move
	 */
	private Set<Move> removeKingCapture(Set<Move> moves)
	{
		for(Move move : moves)
		{
			if((board.getPiece(move.getTo()) != null))
			{
				if(board.getPiece(move.getTo()).getBehaviour() instanceof King)
				{
					moves.remove(move);
				}
			}
		}
		return moves;
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
