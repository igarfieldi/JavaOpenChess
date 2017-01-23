package jchess.gamelogic.pieces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.FourPlayerChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.FourPlayerChessboardFactory;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

public class BishopTest
{
	private IChessboardController controller2p;
	private IChessboardController controller4p;
	private IChessboardModel board2p;
	private IChessboardModel board4p;
	
	private Player players2p[] = {
			new Player("p1", Color.WHITE),
			new Player("p2", Color.BLACK)
	};
	
	private Player players4p[] = {
			new Player("p1", Color.WHITE),
			new Player("p2", Color.RED),
			new Player("p3", Color.BLACK),
			new Player("p4", Color.GOLDEN)
	};
	private PieceFactory factory;
	
	@Before
	public void setUp() throws Exception
	{
		factory = PieceFactory.getInstance();
		
		controller2p = new TwoPlayerChessboardController(
				null, TwoPlayerChessboardFactory.getInstance(),
				players2p[0], players2p[1]);
		controller4p = new FourPlayerChessboardController(
				null, FourPlayerChessboardFactory.getInstance(),
				players4p[0], players4p[1], players4p[2], players4p[3]);
		
		board2p = controller2p.getBoard();
		board4p = controller4p.getBoard();
		
		// Remove all pieces from the board to get a clean slate
		for(Field field : board2p.getFields()) {
			board2p.removePiece(field);
		}
		for(Field field : board4p.getFields()) {
			board4p.removePiece(field);
		}
	}
	
	@Test
	public void testPossibleMovesRegular()
	{
		for(Player player : players2p) {
			Piece bishop = factory.buildPiece(player, null, PieceType.BISHOP);
			board2p.setPiece(board2p.getField(3, 4), bishop);
			
			// The bishop can move diagonally
			Field[] diagonalMoves = {
					board2p.getField(0, 1), board2p.getField(1, 2),
					board2p.getField(2, 3), board2p.getField(4, 5), board2p.getField(5, 6),
			        board2p.getField(6, 7), board2p.getField(0, 7), board2p.getField(1, 6),
			        board2p.getField(2, 5), board2p.getField(4, 3), board2p.getField(5, 2),
			        board2p.getField(6, 1), board2p.getField(7, 0)
			};
			
			assertTrue(GeneralPieceTest.canMakeExactlyTheseMoves(controller2p, bishop, diagonalMoves));
			
			// Check if enemy pieces can be captured on these positions
			// Don't care for blocking movement for now
			for(Player enemy : controller2p.getEnemies(player)) {
				Piece enemyPiece = factory.buildPiece(enemy, null, PieceType.ROOK);
				
				for(Field field : diagonalMoves) {
					board2p.setPiece(field, enemyPiece);
					assertTrue(GeneralPieceTest.canMakeMove(controller2p, bishop, field));
					board2p.removePiece(field);
				}
			}
			
			board2p.removePiece(board2p.getField(3, 4));
			// TODO: add 4p controller
		}
		
	}
	
	@Test
	public void testPossibleMovesBlocking()
	{
		Field[] blockingFields = {
				new Field(1, 2), new Field(4, 5), new Field(1, 6), new Field(4, 3)
		};
		
		Field[][] possibleMovesAfterBlock = {
				{
					board2p.getField(2, 3), board2p.getField(4, 5), board2p.getField(5, 6),
			        board2p.getField(6, 7), board2p.getField(0, 7), board2p.getField(1, 6),
			        board2p.getField(2, 5), board2p.getField(4, 3), board2p.getField(5, 2),
			        board2p.getField(6, 1), board2p.getField(7, 0)
				},
				{
					board2p.getField(0, 1), board2p.getField(1, 2),
					board2p.getField(2, 3), board2p.getField(0, 7), board2p.getField(1, 6),
			        board2p.getField(2, 5), board2p.getField(4, 3), board2p.getField(5, 2),
			        board2p.getField(6, 1), board2p.getField(7, 0)
				},
				{
					board2p.getField(0, 1), board2p.getField(1, 2),
					board2p.getField(2, 3), board2p.getField(4, 5), board2p.getField(5, 6),
			        board2p.getField(6, 7), board2p.getField(2, 5), board2p.getField(4, 3),
			        board2p.getField(5, 2), board2p.getField(6, 1), board2p.getField(7, 0)
				},
				{
					board2p.getField(0, 1), board2p.getField(1, 2),
					board2p.getField(2, 3), board2p.getField(4, 5), board2p.getField(5, 6),
			        board2p.getField(6, 7), board2p.getField(0, 7), board2p.getField(1, 6),
			        board2p.getField(2, 5)
				},
				
		};
		
		
		for(Player player : players2p) {
			Piece bishop = factory.buildPiece(player, null, PieceType.BISHOP);
			Piece friendlyPawn = factory.buildPiece(player, new Direction(0, 1), PieceType.PAWN);
			board2p.setPiece(board2p.getField(3, 4), bishop);
				
			for(int i = 0; i < blockingFields.length; i++) {
				// Test if bishop gets blocked by friendly pieces
				for(Player ally : controller2p.getAllies(player)) {
					Piece allyPiece = factory.buildPiece(ally, null, PieceType.ROOK);
					
					board2p.setPiece(blockingFields[i], allyPiece);
					assertTrue(GeneralPieceTest.canMakeExactlyTheseMoves(
							controller2p, bishop, possibleMovesAfterBlock[i]));
					board2p.removePiece(blockingFields[i]);
				}
				
				// Copy the array of possible fields and add the piece's field to it
				// Because it should be capturable since it's an enemy
				Field[] possibleFields = new Field[possibleMovesAfterBlock[i].length + 1];
				System.arraycopy(possibleMovesAfterBlock[i], 0, possibleFields, 0,
						possibleMovesAfterBlock[i].length);
				possibleFields[possibleMovesAfterBlock[i].length] = blockingFields[i];

				// Test if bishop gets blocked by enemy pieces
				for(Player enemy : controller2p.getEnemies(player)) {
					Piece enemyPiece = factory.buildPiece(enemy, null, PieceType.ROOK);
					
					board2p.setPiece(blockingFields[i], enemyPiece);
					assertTrue(GeneralPieceTest.canMakeExactlyTheseMoves(
							controller2p, bishop, possibleFields));
					board2p.removePiece(blockingFields[i]);
				}
			}
		}
	}
}
