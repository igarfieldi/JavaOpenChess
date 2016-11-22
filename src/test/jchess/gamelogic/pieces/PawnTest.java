package jchess.gamelogic.pieces;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;
import jchess.gamelogic.field.ChessboardController;
import jchess.gamelogic.field.Field;

public class PawnTest
{
	ChessboardController board;
	Player p1;
	Player p2;
	
	@Before
	public void setUp() throws Exception
	{
		// TODO: test in reverse (white on top)!
		p1 = new Player("p1", "WHITE");
		p2 = new Player("p2", "BLACK");
		p2.setBoardSide(true);
		board = new ChessboardController(new Settings(), null);
		King whiteKing = new King(board, p1);
		King blackKing = new King(board, p2);
		board.getBoard().getField(4, 7).setPiece(whiteKing);
		board.getBoard().getField(4, 0).setPiece(blackKing);
		board.setWhiteKing(whiteKing);
		board.setBlackKing(blackKing);
	}
	
	@Test
	public void testRegularMove()
	{
		Pawn whitePawn = new Pawn(board, p1);
		Pawn blackPawn = new Pawn(board, p2);
		board.getBoard().getField(2, 5).setPiece(whitePawn);
		board.getBoard().getField(6, 2).setPiece(blackPawn);
		assertTrue(canMakeMoves(whitePawn, board.getBoard().getField(2, 4)));
		assertTrue(canMakeMoves(blackPawn, board.getBoard().getField(6, 3)));
	}

	@Test
	public void testNormalStrikeToLeft()
	{
		Pawn whitePawn = new Pawn(board, p1);
		Pawn blackPawn = new Pawn(board, p2);
		board.getBoard().getField(4, 4).setPiece(whitePawn);
		board.getBoard().getField(3, 3).setPiece(blackPawn);
		assertTrue(canMakeMoves(whitePawn, board.getBoard().getField(4, 3), blackPawn.square));
		assertTrue(canMakeMoves(blackPawn, board.getBoard().getField(3, 4), whitePawn.square));
	}

	@Test
	public void testNormalStrikeToRight()
	{
		Pawn whitePawn = new Pawn(board, p1);
		Pawn blackPawn = new Pawn(board, p2);
		board.getBoard().getField(3, 4).setPiece(whitePawn);
		board.getBoard().getField(4, 3).setPiece(blackPawn);
		assertTrue(canMakeMoves(whitePawn, board.getBoard().getField(3, 3), blackPawn.square));
		assertTrue(canMakeMoves(blackPawn, board.getBoard().getField(4, 4), whitePawn.square));
	}

	@Test
	public void testTwoStepMove()
	{
		Pawn whitePawn = new Pawn(board, p1);
		Pawn blackPawn = new Pawn(board, p2);
		board.getBoard().getField(3, 6).setPiece(whitePawn);
		board.getBoard().getField(3, 1).setPiece(blackPawn);
		assertTrue(canMakeMoves(whitePawn,
				board.getBoard().getField(3, 5), board.getBoard().getField(3, 4)));
		assertTrue(canMakeMoves(blackPawn,
				board.getBoard().getField(3, 2), board.getBoard().getField(3, 3)));
	}

	/*
		TODO: find a way to test this without direct access to twoSquaresMovedPawn and without
			creating an entire game w/ GUI etc.!
	@Test
	public void testEnPassantLeft()
	{
		Pawn whitePawn = new Pawn(board, p1);
		Pawn blackPawn = new Pawn(board, p2);
		board.getBoard().getField(4, 3).setPiece(whitePawn);
		board.getBoard().getField(3, 1).setPiece(blackPawn);
		board.move(board.getBoard().getField(3, 1), board.getBoard().getField(3, 3));
		assertTrue(canMakeMoves(whitePawn,
				board.getBoard().getField(4, 2), board.getBoard().getField(3, 2)));
	}*/
	
	private boolean canMakeMoves(Piece piece, Field... fields) {
		ArrayList<Field> possibleMoves = piece.possibleMoves();
		if(possibleMoves.size() != fields.length) {
			return false;
		}
		for(Field field : fields) {
			if(!possibleMoves.contains(field)) {
				return false;
			}
		}
		return true;
	}
}
