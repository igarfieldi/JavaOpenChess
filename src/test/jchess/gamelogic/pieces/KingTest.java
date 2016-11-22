package jchess.gamelogic.pieces;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;
import jchess.gamelogic.field.ChessboardController;
import jchess.gamelogic.field.Field;

public class KingTest
{
	ChessboardController board;
	Player p1;
	Player p2;
	King whiteKing;
	King blackKing;
	
	@Before
	public void setUp() throws Exception
	{
		// TODO: test in reverse (white on top!)
		p1 = new Player("p1", "WHITE");
		p2 = new Player("p2", "BLACK");
		p2.setBoardSide(true);
		board = new ChessboardController(new Settings(), null);
		whiteKing = new King(board, p1);
		blackKing = new King(board, p2);
		board.getBoard().getField(4, 7).setPiece(whiteKing);
		board.getBoard().getField(4, 0).setPiece(blackKing);
		board.setWhiteKing(whiteKing);
		board.setBlackKing(blackKing);
	}
	
	@Test
	public void testPossibleMovesRegular()
	{
		movePiece(board.getBoard().getField(4, 4), whiteKing);
		ArrayList<Field> moves = board.getWhiteKing().possibleMoves();
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
		Pawn pawn = new Pawn(board, p2);
		board.getBoard().getField(3, 6).setPiece(pawn);
		assertTrue(whiteKing.isChecked());
		
		movePiece(board.getBoard().getField(5, 6), pawn);
		assertTrue(whiteKing.isChecked());
		
		movePiece(board.getBoard().getField(4, 6), pawn);
		assertFalse(whiteKing.isChecked());
	}
	
	@Test
	public void testCheckFromBishop() {
		checkForCheck(board.getBoard().getField(4, 4), new Bishop(board, p2));
	}
	
	@Test
	public void testCheckFromRook() {
		checkForCheck(board.getBoard().getField(4, 4), new Rook(board, p2));
	}
	
	@Test
	public void testCheckFromQueen() {
		checkForCheck(board.getBoard().getField(4, 4), new Queen(board, p2));
	}
	
	@Test
	public void testCheckFromKnight() {
		checkForCheck(board.getBoard().getField(4, 4), new Knight(board, p2));
	}
	
	private void movePiece(Field field, Piece piece) {
		piece.square.setPiece(null);
		field.setPiece(piece);
	}
	
	private void checkForCheck(Field field, Piece piece) {
		field.setPiece(piece);
		for(Field currField : piece.possibleMoves()) {
			movePiece(currField, whiteKing);
			assertTrue(whiteKing.isChecked());
		}
	}
}
