package jchess.gamelogic.pieces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.chessboardmodels.TwoPlayerChessboardModel;
import jchess.util.Direction;

public class BishopTest
{
	IChessboardModel model;
	IChessboardController board;
	Player white;
	Player black;
	
	@Before
	public void setUp() throws Exception
	{
		Settings settings = new Settings();
		model = new TwoPlayerChessboardModel();
		board = new TwoPlayerChessboardController(null, model, null, null);
		white = settings.getWhitePlayer();
		black = settings.getBlackPlayer();
		board.initialize();
		// Need to remove the pieces we don't want
		for(Field field : board.getBoard().getFields())
		{
			board.getBoard().removePiece(field);
		}
		King whiteKing = new King(board, white);
		King blackKing = new King(board, black);
		board.getBoard().setPiece(board.getBoard().getField(4, 7), whiteKing);
		board.getBoard().setPiece(board.getBoard().getField(4, 0), blackKing);
	}
	
	@Test
	public void testPossibleMovesRegular()
	{
		Bishop whiteBishop = new Bishop(board, white);
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whiteBishop);
		
		// The bishop can move diagonally
		Field[] diagonalMoves = { board.getBoard().getField(0, 1), board.getBoard().getField(1, 2),
		        board.getBoard().getField(2, 3), board.getBoard().getField(4, 5), board.getBoard().getField(5, 6),
		        board.getBoard().getField(6, 7), board.getBoard().getField(0, 7), board.getBoard().getField(1, 6),
		        board.getBoard().getField(2, 5), board.getBoard().getField(4, 3), board.getBoard().getField(5, 2),
		        board.getBoard().getField(6, 1), board.getBoard().getField(7, 0) };
		
		assertTrue(PieceTest.canMakeMoves(board, whiteBishop, diagonalMoves));
	}
	
	@Test
	public void testPossibleMovesCapture()
	{
		Bishop whiteBishop = new Bishop(board, white);
		Pawn blackPawn1 = new Pawn(board, black, new Direction(1, 0));
		Pawn blackPawn2 = new Pawn(board, black, new Direction(1, 0));
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whiteBishop);
		
		// Place bishops in diagonal directions
		Field[] testFields = { new Field(1, 2), new Field(4, 5), new Field(1, 6), new Field(4, 3), };
		Field[] fieldsBeyond = { new Field(0, 1), new Field(5, 6), new Field(0, 7), new Field(5, 2), };
		
		for(int i = 0; i < testFields.length; i++)
		{
			board.getBoard().setPiece(testFields[i], blackPawn1);
			board.getBoard().setPiece(fieldsBeyond[i], blackPawn2);
			// Capturable
			assertTrue(PieceTest.canMakeMove(board, whiteBishop, testFields[i]));
			// Not reachable anymore because blocked by piece
			assertFalse(PieceTest.canMakeMove(board, whiteBishop, fieldsBeyond[i]));
			board.getBoard().removePiece(testFields[i]);
			board.getBoard().removePiece(fieldsBeyond[i]);
		}
	}
}
