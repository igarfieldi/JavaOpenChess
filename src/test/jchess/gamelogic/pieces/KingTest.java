package jchess.gamelogic.pieces;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;
import jchess.gamelogic.field.Chessboard;
import jchess.gamelogic.field.Moves;
import jchess.gamelogic.field.Square;

public class KingTest
{
	Chessboard board;
	
	@Before
	public void setUp() throws Exception
	{
		board = new Chessboard(new Settings(), null);
	}
	
	@Test
	public void testPossibleMovesRegular()
	{
		King whiteKing = new King(board, new Player());
		King blackKing = new King(board, new Player());
		board.squares[2][2].setPiece(board.setWhiteKing(whiteKing));
		board.squares[7][7].setPiece(board.setWhiteKing(blackKing));
		ArrayList<Square> moves = whiteKing.possibleMoves();
		assertTrue(moves.contains(board.squares[1][1]));
		assertTrue(moves.contains(board.squares[2][1]));
		assertTrue(moves.contains(board.squares[3][1]));
		assertTrue(moves.contains(board.squares[1][2]));
		assertTrue(moves.contains(board.squares[3][2]));
		assertTrue(moves.contains(board.squares[1][3]));
		assertTrue(moves.contains(board.squares[2][3]));
		assertTrue(moves.contains(board.squares[3][3]));
	}
	
}
