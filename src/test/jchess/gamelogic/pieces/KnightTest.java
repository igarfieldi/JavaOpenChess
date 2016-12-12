package jchess.gamelogic.pieces;

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

public class KnightTest
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
		Piece whiteKnight = factory.buildPiece(white, null, PieceType.KNIGHT);
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
		Piece whiteKnight = factory.buildPiece(white, null, PieceType.KNIGHT);
		Piece blackPawn = factory.buildPiece(black, new Direction(0, 1), PieceType.PAWN);
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
