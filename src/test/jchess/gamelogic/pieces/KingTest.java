package jchess.gamelogic.pieces;

import static org.junit.Assert.assertFalse;
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
		// TODO: test in reverse (white on top!)
		Settings settings = new Settings();
		model = new TwoPlayerChessboardModel();
		board = new ChessboardController(settings, null, model);
		white = settings.getWhitePlayer();
		black = settings.getBlackPlayer();
		black.setTopSide(true);
		board.initialize();
		// Need to remove the pieces we don't want
		for(Field field : board.getBoard().getFields())
		{
			board.getBoard().removePiece(field);
		}
		whiteKing = new King(board, white);
		blackKing = new King(board, black);
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
		Pawn pawn = new Pawn(board, black, new Direction(0, 1));
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
		checkForCheck(board.getBoard().getField(4, 4), new Bishop(board, black));
	}
	
	@Test
	public void testCheckFromRook()
	{
		checkForCheck(board.getBoard().getField(4, 4), new Rook(board, black));
	}
	
	@Test
	public void testCheckFromQueen()
	{
		checkForCheck(board.getBoard().getField(4, 4), new Queen(board, black));
	}
	
	@Test
	public void testCheckFromKnight()
	{
		checkForCheck(board.getBoard().getField(4, 4), new Knight(board, black));
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
