package jchess.gamelogic.pieces;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;
import jchess.gamelogic.controllers.ChessboardController;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.chessboardmodels.TwoPlayerChessboardModel;
import jchess.util.Direction;

public class PawnTest
{
	IChessboardModel model;
	IChessboardController board;
	Player white;
	Player black;
	
	@Before
	public void setUp() throws Exception
	{
		// TODO: test in reverse (white on top)!
		Settings settings = new Settings();
		model = new TwoPlayerChessboardModel();
		board = new ChessboardController(settings, null, model);
		white = settings.getWhitePlayer();
		black = settings.getBlackPlayer();
		black.setTopSide(true);
		board.initialize();
		King whiteKing = new King(board, white);
		King blackKing = new King(board, black);
		board.getBoard().setPiece(board.getBoard().getField(4, 7), whiteKing);
		board.getBoard().setPiece(board.getBoard().getField(4, 0), blackKing);
	}
	
	@Test
	public void testRegularMove()
	{
		Pawn whitePawn = new Pawn(board, white, new Direction(0, -1));
		Pawn blackPawn = new Pawn(board, black, new Direction(0, 1));
		whitePawn.markAsMoved();
		blackPawn.markAsMoved();
		board.getBoard().setPiece(board.getBoard().getField(2, 5), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(6, 2), blackPawn);
		assertTrue(canMakeMoves(whitePawn, board.getBoard().getField(2, 4)));
		assertTrue(canMakeMoves(blackPawn, board.getBoard().getField(6, 3)));
	}
	
	@Test
	public void testNormalStrikeToLeft()
	{
		Pawn whitePawn = new Pawn(board, white, new Direction(0, -1));
		Pawn blackPawn = new Pawn(board, black, new Direction(0, 1));
		whitePawn.markAsMoved();
		blackPawn.markAsMoved();
		board.getBoard().setPiece(board.getBoard().getField(4, 4), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(3, 3), blackPawn);
		assertTrue(canMakeMoves(whitePawn, board.getBoard().getField(4, 3), board.getBoard().getField(blackPawn)));
		assertTrue(canMakeMoves(blackPawn, board.getBoard().getField(3, 4), board.getBoard().getField(whitePawn)));
	}
	
	@Test
	public void testNormalStrikeToRight()
	{
		Pawn whitePawn = new Pawn(board, white, new Direction(0, -1));
		Pawn blackPawn = new Pawn(board, black, new Direction(0, 1));
		whitePawn.markAsMoved();
		blackPawn.markAsMoved();
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(4, 3), blackPawn);
		assertTrue(canMakeMoves(whitePawn, board.getBoard().getField(3, 3), board.getBoard().getField(blackPawn)));
		assertTrue(canMakeMoves(blackPawn, board.getBoard().getField(4, 4), board.getBoard().getField(whitePawn)));
	}
	
	@Test
	public void testTwoStepMove()
	{
		Pawn whitePawn = new Pawn(board, white, new Direction(0, -1));
		Pawn blackPawn = new Pawn(board, black, new Direction(0, 1));
		board.getBoard().setPiece(board.getBoard().getField(3, 6), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(3, 1), blackPawn);
		assertTrue(canMakeMoves(whitePawn, board.getBoard().getField(3, 5), board.getBoard().getField(3, 4)));
		assertTrue(canMakeMoves(blackPawn, board.getBoard().getField(3, 2), board.getBoard().getField(3, 3)));
	}
	
	/*
	 * TODO: find a way to test this without direct access to
	 * twoSquaresMovedPawn and without creating an entire game w/ GUI etc.!
	 * 
	 * @Test public void testEnPassantLeft() { Pawn whitePawn = new Pawn(board,
	 * p1); Pawn blackPawn = new Pawn(board, p2); board.getBoard().getField(4,
	 * 3).setPiece(whitePawn); board.getBoard().getField(3,
	 * 1).setPiece(blackPawn); board.move(board.getBoard().getField(3, 1),
	 * board.getBoard().getField(3, 3)); assertTrue(canMakeMoves(whitePawn,
	 * board.getBoard().getField(4, 2), board.getBoard().getField(3, 2))); }
	 */
	
	private boolean canMakeMoves(Piece piece, Field... fields)
	{
		Set<Field> possibleMoves = board.getPossibleMoves(piece, true);
		if(possibleMoves.size() != fields.length)
		{
			return false;
		}
		for(Field field : fields)
		{
			if(!possibleMoves.contains(field))
			{
				return false;
			}
		}
		return true;
	}
}
