package jchess.gamelogic.pieces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.util.Direction;

public class KingTest
{
	IChessboardModel model;
	IChessboardController board;
	Player white;
	Player black;
	King whiteKing;
	King blackKing;
	
	@Before
	public void setUp() throws Exception
	{
		white = new Player("p1", Color.WHITE);
		black = new Player("p2", Color.BLACK);
		board = new TwoPlayerChessboardController(null,
				TwoPlayerChessboardFactory.getInstance(), white, black);
		// Need to remove the pieces we don't want
		for(Field field : board.getBoard().getFields())
		{
			board.getBoard().removePiece(field);
		}
		whiteKing = new King(white);
		blackKing = new King(black);
		board.getBoard().setPiece(board.getBoard().getField(4, 7), whiteKing);
		board.getBoard().setPiece(board.getBoard().getField(4, 0), blackKing);
	}
	
	@Test
	public void testPossibleMovesRegular()
	{
		movePiece(board.getBoard().getField(4, 4), whiteKing);
		whiteKing.markAsMoved();
		Set<Field> moves = board.getPossibleMoves(whiteKing, true);
		assertTrue(moves.contains(board.getBoard().getField(3, 3)));
		assertTrue(moves.contains(board.getBoard().getField(4, 3)));
		assertTrue(moves.contains(board.getBoard().getField(5, 3)));
		assertTrue(moves.contains(board.getBoard().getField(3, 4)));
		assertTrue(moves.contains(board.getBoard().getField(5, 4)));
		assertTrue(moves.contains(board.getBoard().getField(3, 5)));
		assertTrue(moves.contains(board.getBoard().getField(4, 5)));
		assertTrue(moves.contains(board.getBoard().getField(5, 5)));
	}
	
	@Test
	public void testCheckFromPawn()
	{
		Pawn pawn = new Pawn(black, new Direction(0, 1));
		pawn.markAsMoved();
		board.getBoard().setPiece(board.getBoard().getField(3, 6), pawn);
		assertTrue(board.isChecked(white));
		
		movePiece(board.getBoard().getField(5, 6), pawn);
		assertTrue(board.isChecked(white));
		
		movePiece(board.getBoard().getField(4, 6), pawn);
		assertFalse(board.isChecked(white));
	}
	
	@Test
	public void testCheckFromBishop()
	{
		checkForCheck(board.getBoard().getField(4, 4), new Bishop(black));
	}
	
	@Test
	public void testCheckFromRook()
	{
		checkForCheck(board.getBoard().getField(4, 4), new Rook(black));
	}
	
	@Test
	public void testCheckFromQueen()
	{
		checkForCheck(board.getBoard().getField(4, 4), new Queen(black));
	}
	
	@Test
	public void testCheckFromKnight()
	{
		checkForCheck(board.getBoard().getField(4, 4), new Knight(black));
	}
	
	private void movePiece(Field field, Piece piece)
	{
		board.getBoard().movePiece(piece, field);
	}
	
	private void checkForCheck(Field field, Piece piece)
	{
		board.getBoard().setPiece(field, piece);
		
		for(Field currField : board.getPossibleMoves(piece, true))
		{
			movePiece(currField, whiteKing);
			assertTrue(board.isChecked(white));
		}
	}
}
