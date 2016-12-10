package jchess.gamelogic.pieces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.chessboardfactories.TwoPlayerChessboardFactory;
import jchess.util.Direction;

public class QueenTest
{
	IChessboardModel model;
	IChessboardController board;
	Player white;
	Player black;
	
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
		King whiteKing = new King(white);
		King blackKing = new King(black);
		board.getBoard().setPiece(board.getBoard().getField(4, 7), whiteKing);
		board.getBoard().setPiece(board.getBoard().getField(4, 0), blackKing);
	}
	
	@Test
	public void testPossibleMovesRegular()
	{
		Queen whiteQueen = new Queen(white);
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whiteQueen);
		
		// The queen has to be able to move along every directional path
		Field[] verticalMoves = { board.getBoard().getField(3, 0), board.getBoard().getField(3, 1),
		        board.getBoard().getField(3, 2), board.getBoard().getField(3, 3), board.getBoard().getField(3, 5),
		        board.getBoard().getField(3, 6), board.getBoard().getField(3, 7) };
		Field[] horizontalMoves = { board.getBoard().getField(0, 4), board.getBoard().getField(1, 4),
		        board.getBoard().getField(2, 4), board.getBoard().getField(4, 4), board.getBoard().getField(5, 4),
		        board.getBoard().getField(6, 4), board.getBoard().getField(7, 4) };
		Field[] diagonalMoves = { board.getBoard().getField(0, 1), board.getBoard().getField(1, 2),
		        board.getBoard().getField(2, 3), board.getBoard().getField(4, 5), board.getBoard().getField(5, 6),
		        board.getBoard().getField(6, 7), board.getBoard().getField(0, 7), board.getBoard().getField(1, 6),
		        board.getBoard().getField(2, 5), board.getBoard().getField(4, 3), board.getBoard().getField(5, 2),
		        board.getBoard().getField(6, 1), board.getBoard().getField(7, 0) };
		
		assertTrue(PieceTest.canMakeMoves(board, whiteQueen, verticalMoves));
		assertTrue(PieceTest.canMakeMoves(board, whiteQueen, horizontalMoves));
		assertTrue(PieceTest.canMakeMoves(board, whiteQueen, diagonalMoves));
	}
	
	@Test
	public void testPossibleMovesCapture()
	{
		Queen whiteQueen = new Queen(white);
		Pawn blackPawn1 = new Pawn(black, new Direction(1, 0));
		Pawn blackPawn2 = new Pawn(black, new Direction(1, 0));
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whiteQueen);
		
		// Place queens in all different directions (vertical, horizontal,
		// diagonal)
		Field[] testFields = { new Field(1, 4), new Field(6, 4), new Field(3, 2), new Field(3, 6), new Field(1, 2),
		        new Field(4, 5), new Field(1, 6), new Field(4, 3), };
		Field[] fieldsBeyond = { new Field(0, 4), new Field(7, 4), new Field(3, 1), new Field(3, 7), new Field(0, 1),
		        new Field(5, 6), new Field(0, 7), new Field(5, 2), };
		
		for(int i = 0; i < testFields.length; i++)
		{
			board.getBoard().setPiece(testFields[i], blackPawn1);
			board.getBoard().setPiece(fieldsBeyond[i], blackPawn2);
			// Capturable
			assertTrue(PieceTest.canMakeMove(board, whiteQueen, testFields[i]));
			// Not reachable anymore because blocked by piece
			assertFalse(PieceTest.canMakeMove(board, whiteQueen, fieldsBeyond[i]));
			board.getBoard().removePiece(testFields[i]);
			board.getBoard().removePiece(fieldsBeyond[i]);
		}
	}
}
