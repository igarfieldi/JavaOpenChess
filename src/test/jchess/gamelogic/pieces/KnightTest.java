package jchess.gamelogic.pieces;

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

public class KnightTest
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
		Knight whiteKnight = new Knight(white);
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whiteKnight);
		
		// The rook can move vertical and horizontal
		Field[] knightMoves = { board.getBoard().getField(1, 3), board.getBoard().getField(1, 5),
		        board.getBoard().getField(2, 2), board.getBoard().getField(4, 2), board.getBoard().getField(2, 6),
		        board.getBoard().getField(4, 6), board.getBoard().getField(5, 3), board.getBoard().getField(5, 5) };
		
		assertTrue(PieceTest.canMakeMoves(board, whiteKnight, knightMoves));
	}
	
	@Test
	public void testPossibleMovesCapture()
	{
		Knight whiteKnight = new Knight(white);
		Pawn blackPawn = new Pawn(black, new Direction(1, 0));
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whiteKnight);
		
		// Knights also have to be able to capture on their moves
		Field[] testFields = { board.getBoard().getField(1, 3), board.getBoard().getField(1, 5),
		        board.getBoard().getField(2, 2), board.getBoard().getField(4, 2), board.getBoard().getField(2, 6),
		        board.getBoard().getField(4, 6), board.getBoard().getField(5, 3), board.getBoard().getField(5, 5) };
		
		for(int i = 0; i < testFields.length; i++)
		{
			board.getBoard().setPiece(testFields[i], blackPawn);
			// Capturable
			assertTrue(PieceTest.canMakeMove(board, whiteKnight, testFields[i]));
			// Not reachable anymore because blocked by piece
			board.getBoard().removePiece(testFields[i]);
		}
	}
}
