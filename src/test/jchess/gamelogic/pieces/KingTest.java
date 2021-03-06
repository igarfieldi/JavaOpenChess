package jchess.gamelogic.pieces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

public class KingTest
{
	IChessboardModel model;
	IChessboardController board;
	Player white;
	Player black;
	Piece whiteKing;
	Piece blackKing;
	IPieceFactory factory;
	
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
		whiteKing = factory.buildPiece(white, null, PieceType.KING);
		blackKing = factory.buildPiece(black, null, PieceType.KING);
		board.getBoard().setPiece(board.getBoard().getField(4, 7), whiteKing);
		board.getBoard().setPiece(board.getBoard().getField(4, 0), blackKing);
	}
	
	@Test
	public void testPossibleMovesRegular()
	{
		movePiece(board.getBoard().getField(4, 4), whiteKing);
		whiteKing.markAsMoved();
		Set<Field> moves = new HashSet<Field>();
		for(Move move : board.getPossibleMoves(whiteKing, true)) {
			moves.add(move.getTo());
		}
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
	public void testCheckFromPawn()
	{
		Piece pawn = factory.buildPiece(black, new Direction(0, 1), PieceType.PAWN);
		pawn.markAsMoved();
		board.getBoard().setPiece(board.getBoard().getField(3, 6), pawn);
		assertTrue(board.isChecked(white));
		
		movePiece(board.getBoard().getField(5, 6), pawn);
		assertTrue(board.isChecked(white));
		
		movePiece(board.getBoard().getField(4, 6), pawn);
		assertFalse(board.isChecked(white));
	}
	
	@Test
	public void testCheckFromBishop()
	{
		checkForCheck(board.getBoard().getField(4, 4), factory.buildPiece(black, null, PieceType.BISHOP));
	}
	
	@Test
	public void testCheckFromRook()
	{
		checkForCheck(board.getBoard().getField(4, 4), factory.buildPiece(black, null, PieceType.ROOK));
	}
	
	@Test
	public void testCheckFromQueen()
	{
		checkForCheck(board.getBoard().getField(4, 4), factory.buildPiece(black, null, PieceType.QUEEN));
	}
	
	@Test
	public void testCheckFromKnight()
	{
		checkForCheck(board.getBoard().getField(4, 4), factory.buildPiece(black, null, PieceType.KNIGHT));
	}
	
	private void movePiece(Field field, Piece piece)
	{
		board.getBoard().movePiece(piece, field);
	}
	
	private void checkForCheck(Field field, Piece piece)
	{
		board.getBoard().setPiece(field, piece);
		
		for(Move move : board.getPossibleMoves(piece, true))
		{
			movePiece(move.getTo(), whiteKing);
			assertTrue(board.isChecked(white));
		}
	}
}
