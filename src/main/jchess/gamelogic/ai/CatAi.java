package jchess.gamelogic.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jchess.gamelogic.Player;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

public class CatAi
{
	private IChessboardModel board;
	private Piece cat;
	private IChessboardController chessboard;
	private Player activePlayer;
	private int turnsToRespawn;
	private boolean addCatPiece; // I promise to remove that later, it's only
	                             // temporary to add the cat piece to the cat.
	
	public CatAi(IChessboardController chessboard)
	{
		board = chessboard.getBoard();
		this.chessboard = chessboard;
		addCatPiece = true;
		turnsToRespawn = 2;
		activePlayer = chessboard.getActivePlayer();
	}
	
	public boolean isAlive()
	{
		return !board.getPieces(activePlayer).isEmpty();
	}
	
	public void updateRespawnTimer()
	{
		if(turnsToRespawn == 0)
		{
			respawnCat();
			resetRespawnTimer();
		}
		if(!isAlive())
		{
			turnsToRespawn--;
		}
	}
	
	private void respawnCat()
	{
		PieceFactory factory = PieceFactory.getInstance();
		board.setPiece(getRandomField(board.getEmptyFields()),
		        factory.buildPiece(activePlayer, new Direction(0, -1), PieceType.CAT));
		activePlayer = chessboard.getActivePlayer();
		for(Piece piece : board.getPieces(activePlayer))
		{
			cat = piece;
		}
	}
	
	private void resetRespawnTimer()
	{
		turnsToRespawn = 2;
	}
	
	public Field getNextTargetMove()
	{
		
		ArrayList<Field> fields = new ArrayList<Field>(chessboard.getPossibleMoves(cat, false));
		return getRandomField(fields);
	}
	
	public Field getCurrentPosition()
	{
		// I swear I'm gonna remove this
		if(addCatPiece)
		{
			activePlayer = chessboard.getActivePlayer();
			for(Piece piece : board.getPieces(activePlayer))
			{
				cat = piece;
			}
			addCatPiece = false;
		}
		return board.getField(cat);
	}
	
	public Field getRandomField(List<Field> fields)
	{
		Random random = new Random();
		Field randomField = fields.get(random.nextInt(fields.size()));
		return randomField;
	}
}
