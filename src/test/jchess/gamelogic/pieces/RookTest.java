package jchess.gamelogic.pieces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

public class RookTest
{
	IChessboardModel model;
	IChessboardController board;
	Player white;
	Player black;
	PieceFactory factory;
	
	@Before
	public void setUp() throws Exception
	{
		factory = PieceFactory.getInstance();
		white = new Player("p1", Color.WHITE);
		black = new Player("p2", Color.BLACK);
		board = new TwoPlayerChessboardController(null,
				TwoPlayerChessboardFactory.getInstance(), white, black);
		// Need to remove the pieces we don't want
		for(Field field : board.getBoard().getFields())
		{
			board.getBoard().removePiece(field);
		}
	}
	
	@Test
	public void testPossibleMovesRegular()
	{
		Piece whiteRook = factory.buildPiece(white, null, PieceType.ROOK);
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whiteRook);
		
		// The rook can move vertical and horizontal
		Field[] verticalMoves = { board.getBoard().getField(3, 0), board.getBoard().getField(3, 1),
		        board.getBoard().getField(3, 2), board.getBoard().getField(3, 3), board.getBoard().getField(3, 5),
		        board.getBoard().getField(3, 6), board.getBoard().getField(3, 7) };
		Field[] horizontalMoves = { board.getBoard().getField(0, 4), board.getBoard().getField(1, 4),
		        board.getBoard().getField(2, 4), board.getBoard().getField(4, 4), board.getBoard().getField(5, 4),
		        board.getBoard().getField(6, 4), board.getBoard().getField(7, 4) };
		
		assertTrue(GeneralPieceTest.canMakeMoves(board, whiteRook, verticalMoves));
		assertTrue(GeneralPieceTest.canMakeMoves(board, whiteRook, horizontalMoves));
	}
	
	@Test
	public void testPossibleMovesCapture()
	{
		Piece whiteRook = factory.buildPiece(white, null, PieceType.ROOK);
		Piece blackPawn1 = factory.buildPiece(black, new Direction(0, 1), PieceType.PAWN);
		Piece blackPawn2 = factory.buildPiece(black, new Direction(0, 1), PieceType.PAWN);
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whiteRook);
		
		// Place rooks vertically and horizontally
		Field[] testFields = { new Field(1, 4), new Field(6, 4), new Field(3, 2), new Field(3, 6), };
		Field[] fieldsBeyond = { new Field(0, 4), new Field(7, 4), new Field(3, 1), new Field(3, 7), };
		
		for(int i = 0; i < testFields.length; i++)
		{
			board.getBoard().setPiece(testFields[i], blackPawn1);
			board.getBoard().setPiece(fieldsBeyond[i], blackPawn2);
			// Capturable
			assertTrue(GeneralPieceTest.canMakeMove(board, whiteRook, testFields[i]));
			// Not reachable anymore because blocked by piece
			assertFalse(GeneralPieceTest.canMakeMove(board, whiteRook, fieldsBeyond[i]));
			board.getBoard().removePiece(testFields[i]);
			board.getBoard().removePiece(fieldsBeyond[i]);
		}
	}
}
