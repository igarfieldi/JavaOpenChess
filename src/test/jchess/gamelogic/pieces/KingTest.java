package jchess.gamelogic.pieces;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;
import jchess.gamelogic.field.ChessboardController;
import jchess.gamelogic.field.Field;

public class KingTest
{
	ChessboardController board;
	Player white;
	Player black;
	King whiteKing;
	King blackKing;
	
	@Before
	public void setUp() throws Exception
	{
		// TODO: test in reverse (white on top!)
		Settings settings = new Settings();
		board = new ChessboardController(settings, null, null);
		white = settings.getWhitePlayer();
		black = settings.getBlackPlayer();
		black.setTopSide(true);
		board.initialize();
		// Need to remove the pieces we don't want
		for(Field field : board.getBoard().getFields()) {
			board.getBoard().removePiece(field);
		}
		whiteKing = new King(board, white);
		blackKing = new King(board, black);
		board.getBoard().setPiece(board.getBoard().getField(4, 7), whiteKing);
		board.getBoard().setPiece(board.getBoard().getField(4, 0), blackKing);
		board.setWhiteKing(whiteKing);
		board.setBlackKing(blackKing);
	}
	
	@Test
	public void testPossibleMovesRegular()
	{
		movePiece(board.getBoard().getField(4, 4), whiteKing);
		whiteKing.markAsMoved();
		Set<Field> moves = board.getPossibleMoves(whiteKing);
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
	public void testCheckFromPawn() {
		Pawn pawn = new Pawn(board, black);
		pawn.markAsMoved();
		board.getBoard().setPiece(board.getBoard().getField(3, 6), pawn);
		assertTrue(board.isChecked(white));
		
		movePiece(board.getBoard().getField(5, 6), pawn);
		assertTrue(board.isChecked(white));
		
		movePiece(board.getBoard().getField(4, 6), pawn);
		assertFalse(board.isChecked(white));
	}
	
	@Test
	public void testCheckFromBishop() {
		checkForCheck(board.getBoard().getField(4, 4), new Bishop(board, black));
	}
	
	@Test
	public void testCheckFromRook() {
		checkForCheck(board.getBoard().getField(4, 4), new Rook(board, black));
	}
	
	@Test
	public void testCheckFromQueen() {
		checkForCheck(board.getBoard().getField(4, 4), new Queen(board, black));
	}
	
	@Test
	public void testCheckFromKnight() {
		checkForCheck(board.getBoard().getField(4, 4), new Knight(board, black));
	}
	
	private void movePiece(Field field, Piece piece) {
		board.getBoard().movePiece(piece, field);
	}
	
	private void checkForCheck(Field field, Piece piece) {
		board.getBoard().setPiece(field, piece);
		for(Field currField : board.getPossibleMoves(piece)) {
			movePiece(currField, whiteKing);
			assertTrue(board.isChecked(white));
		}
	}
}
