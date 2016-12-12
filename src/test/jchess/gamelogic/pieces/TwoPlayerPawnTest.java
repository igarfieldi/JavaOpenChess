package jchess.gamelogic.pieces;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.IllegalMoveException;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

public class TwoPlayerPawnTest
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
	public void testRegularMove()
	{
		Piece whitePawn = factory.buildPiece(white, new Direction(0, -1), PieceType.PAWN);
		Piece blackPawn = factory.buildPiece(black, new Direction(0, 1), PieceType.PAWN);
		whitePawn.markAsMoved();
		blackPawn.markAsMoved();
		board.getBoard().setPiece(board.getBoard().getField(2, 5), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(6, 2), blackPawn);
		assertTrue(PieceTest.canMakeMoves(board, whitePawn, board.getBoard().getField(2, 4)));
		assertTrue(PieceTest.canMakeMoves(board, blackPawn, board.getBoard().getField(6, 3)));
	}
	
	@Test
	public void testNormalStrikeToLeft()
	{
		Piece whitePawn = factory.buildPiece(white, new Direction(0, -1), PieceType.PAWN);
		Piece blackPawn = factory.buildPiece(black, new Direction(0, 1), PieceType.PAWN);
		whitePawn.markAsMoved();
		blackPawn.markAsMoved();
		board.getBoard().setPiece(board.getBoard().getField(4, 4), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(3, 3), blackPawn);
		assertTrue(PieceTest.canMakeMoves(board, whitePawn, board.getBoard().getField(4, 3),
		        board.getBoard().getField(blackPawn)));
		assertTrue(PieceTest.canMakeMoves(board, blackPawn, board.getBoard().getField(3, 4),
		        board.getBoard().getField(whitePawn)));
	}
	
	@Test
	public void testNormalStrikeToRight()
	{
		Piece whitePawn = factory.buildPiece(white, new Direction(0, -1), PieceType.PAWN);
		Piece blackPawn = factory.buildPiece(black, new Direction(0, 1), PieceType.PAWN);
		whitePawn.markAsMoved();
		blackPawn.markAsMoved();
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(4, 3), blackPawn);
		assertTrue(PieceTest.canMakeMoves(board, whitePawn, board.getBoard().getField(3, 3),
		        board.getBoard().getField(blackPawn)));
		assertTrue(PieceTest.canMakeMoves(board, blackPawn, board.getBoard().getField(4, 4),
		        board.getBoard().getField(whitePawn)));
	}
	
	@Test
	public void testTwoStepMove()
	{
		Piece whitePawn = factory.buildPiece(white, new Direction(0, -1), PieceType.PAWN);
		Piece blackPawn = factory.buildPiece(black, new Direction(0, 1), PieceType.PAWN);
		board.getBoard().setPiece(board.getBoard().getField(3, 6), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(3, 1), blackPawn);
		assertTrue(PieceTest.canMakeMoves(board, whitePawn, board.getBoard().getField(3, 5),
		        board.getBoard().getField(3, 4)));
		assertTrue(PieceTest.canMakeMoves(board, blackPawn, board.getBoard().getField(3, 2),
		        board.getBoard().getField(3, 3)));
		
		// Test if a piece blocks the two square move
		whitePawn.markAsUnmoved();
		board.getBoard().setPiece(board.getBoard().getField(3, 6), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(3, 5), blackPawn);
		assertTrue(board.getPossibleMoves(whitePawn, false).isEmpty());

		// Same for black
		blackPawn.markAsUnmoved();
		board.getBoard().setPiece(board.getBoard().getField(3, 2), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(3, 1), blackPawn);
		assertTrue(board.getPossibleMoves(blackPawn, false).isEmpty());
	}
	
	@Test
	public void testEnPassant()
	{
		Piece whitePawn = factory.buildPiece(white, new Direction(0, -1), PieceType.PAWN);
		Piece blackPawn = factory.buildPiece(black, new Direction(0, 1), PieceType.PAWN);;
		board.getBoard().setPiece(board.getBoard().getField(3, 4), whitePawn);
		board.getBoard().setPiece(board.getBoard().getField(2, 1), blackPawn);
		try
		{
			board.move(board.getBoard().getField(whitePawn), board.getBoard().getField(3, 3));
			board.move(board.getBoard().getField(blackPawn), board.getBoard().getField(2, 3));
			assertTrue(PieceTest.canMakeMoves(board, whitePawn, board.getBoard().getField(3, 2),
			        board.getBoard().getField(2, 2)));
		} catch(IllegalMoveException e)
		{
			e.printStackTrace();
		}
	}
}
