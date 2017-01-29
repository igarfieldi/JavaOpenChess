/**
 * 
 */
package jchess.gamelogic.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.chessboardcontrollers.FourPlayerChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.FourPlayerChessboardFactory;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;

/**
 * @author Florian Bethe
 *
 */
public class HistoryTest
{
	private Player players[] = {
			new Player("p1", Color.WHITE),
			new Player("p2", Color.BLACK),
			new Player("p3", Color.RED),
			new Player("p4", Color.GOLDEN),
	};
	private History twoPlayerHistory;
	private History fourPlayerHistory;

	private IChessboardController twoPlayerController;
	private IChessboardController fourPlayerController;
	private IChessboardModel twoPlayerBoard;
	private IChessboardModel fourPlayerBoard;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		twoPlayerController = new TwoPlayerChessboardController(
				null, TwoPlayerChessboardFactory.getInstance(), players[0],
				players[1]);
		fourPlayerController = new FourPlayerChessboardController(
				null, FourPlayerChessboardFactory.getInstance(), players[0],
				players[2], players[1], players[3]);
		twoPlayerBoard = twoPlayerController.getBoard();
		fourPlayerBoard = fourPlayerController.getBoard();
		twoPlayerHistory = twoPlayerController.getHistory();
		fourPlayerHistory = fourPlayerController.getHistory();
	}
	
	/**
	 * Tests the addMove method (and implicitly getLastMove too).
	 */
	@Test
	public void testAddMove()
	{
		Move initialMove = new Move(new Field(4, 1), new Field(3, 7), twoPlayerBoard.getPiece(new Field(4, 1)));
		Move testMove = new Move(new Field(1, 1), new Field(6, 5), twoPlayerBoard.getPiece(new Field(1, 1)));

		// Initial setup of the history (need at least one entry for lastMove)
		twoPlayerHistory.addMove(initialMove);
		fourPlayerHistory.addMove(initialMove);
		assertTrue(twoPlayerHistory.getLastMove().equals(initialMove));
		assertTrue(fourPlayerHistory.getLastMove().equals(initialMove));
		
		// Test two player history
		twoPlayerHistory.addMove(testMove);
		assertTrue(twoPlayerHistory.getLastMove().equals(testMove));
		try {
			twoPlayerHistory.addMove(null);
			fail("Null check missing!");
		} catch(IllegalArgumentException exc) {
			assertTrue(twoPlayerHistory.getLastMove().equals(testMove));
		}
		
		// Test four player history
		fourPlayerHistory.addMove(testMove);
		assertTrue(fourPlayerHistory.getLastMove().equals(testMove));
		try {
			fourPlayerHistory.addMove(null);
			fail("Null check missing!");
		} catch(IllegalArgumentException exc) {
			assertTrue(fourPlayerHistory.getLastMove().equals(testMove));
		}

	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.History#getMovesAsString()}.
	 */
	@Test
	public void testGetMovesAsString()
	{
		// Testing for two player history only is enough since the possible
		// outputs don't change between the two game types
		try {
    		// Inital pawns from both players
    		assertTrue(twoPlayerController.move(new Field(3, 6), new Field(3, 4)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerController.move(new Field(4, 1), new Field(4, 3)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerHistory.getMovesAsString().trim().equals("1. d7-d5 e2-e4"));
    		
    		// Test pawn capture and bishop
    		assertTrue(twoPlayerController.move(new Field(3, 4), new Field(4, 3)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerController.move(new Field(5, 0), new Field(0, 5)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerHistory.getMovesAsString().trim().equals("1. d7-d5 e2-e4 2. d5xe4 Bf1-a6"));
    		
    		// Test queen move->check and knight move/capture
    		assertTrue(twoPlayerController.move(new Field(3, 7), new Field(3, 1)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerController.move(new Field(1, 0), new Field(3, 1)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerHistory.getMovesAsString().trim().equals("1. d7-d5 e2-e4 2. d5xe4 Bf1-a6 3. Qd8xd2+ Nb1xd2"));
    		
    		// Move bishop and pawn
    		assertTrue(twoPlayerController.move(new Field(2, 7), new Field(6, 3)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerController.move(new Field(5, 1), new Field(5, 3)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerHistory.getMovesAsString().trim().
    				equals("1. d7-d5 e2-e4 2. d5xe4 Bf1-a6 3. Qd8xd2+ Nb1xd2 4. Bc8-g4 f2-f4"));
    		
    		// Test en passant and move knight
    		assertTrue(twoPlayerController.move(new Field(4, 3), new Field(5, 2)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerController.move(new Field(6, 0), new Field(5, 2)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerHistory.getMovesAsString().trim().
    				equals("1. d7-d5 e2-e4 2. d5xe4 Bf1-a6 3. Qd8xd2+ Nb1xd2 "
    						+ "4. Bc8-g4 f2-f4 5. e4xf3(e.p) Ng1xf3"));
    		
    		// Move night and short-castle black
    		assertTrue(twoPlayerController.move(new Field(1, 7), new Field(0, 5)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerController.move(new Field(4, 0), new Field(6, 0)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerHistory.getMovesAsString().trim().
    				equals("1. d7-d5 e2-e4 2. d5xe4 Bf1-a6 3. Qd8xd2+ Nb1xd2 "
    						+ "4. Bc8-g4 f2-f4 5. e4xf3(e.p) Ng1xf3 6. Nb8xa6 O-O"));
    		
    		// Long-castle white and move rook
    		assertTrue(twoPlayerController.move(new Field(4, 7), new Field(2, 7)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerController.move(new Field(5, 0), new Field(5, 1)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerHistory.getMovesAsString().trim().
    				equals("1. d7-d5 e2-e4 2. d5xe4 Bf1-a6 3. Qd8xd2+ Nb1xd2 "
    						+ "4. Bc8-g4 f2-f4 5. e4xf3(e.p) Ng1xf3 6. Nb8xa6 O-O "
    						+ "7. O-O-O Rf1-f2"));
    		
    		// Move king
    		assertTrue(twoPlayerController.move(new Field(2, 7), new Field(1, 7)));
			twoPlayerController.switchToNextPlayer();
    		assertTrue(twoPlayerHistory.getMovesAsString().trim().
    				equals("1. d7-d5 e2-e4 2. d5xe4 Bf1-a6 3. Qd8xd2+ Nb1xd2 "
    						+ "4. Bc8-g4 f2-f4 5. e4xf3(e.p) Ng1xf3 6. Nb8xa6 O-O "
    						+ "7. O-O-O Rf1-f2 8. Kc8-b8"));
		} catch(IllegalMoveException exc) {
			fail("Illegal move: " + exc.getMessage());
		}
	}
	
	@Test
	public void testGetMoveAsStringTwoPlayerCheckmate() {
		// Four moves needed for checkmate
		try {
			twoPlayerController.move(new Field(5, 6), new Field(5, 4));
			twoPlayerController.switchToNextPlayer();
			twoPlayerController.move(new Field(4, 1), new Field(4, 2));
			twoPlayerController.switchToNextPlayer();
			twoPlayerController.move(new Field(6, 6), new Field(6, 4));
			twoPlayerController.switchToNextPlayer();
			twoPlayerController.move(new Field(3, 0), new Field(7, 4));
			
			assertTrue(twoPlayerHistory.getMovesAsString().trim().
					equals("1. f7-f5 e2-e3 2. g7-g5 Qd1-h5#"));
		} catch(IllegalMoveException exc) {
			fail("Illegal move: " + exc.getMessage());
		}
	}
	
	@Test
	public void testGetMoveAsStringFourPlayerCheckmate() {
		// It takes 17 moves to create a checkmate
		try {
			// First rotation
			fourPlayerController.move(new Field(5, 12), new Field(5, 10));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(1, 3), new Field(2, 3));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(7, 1), new Field(7, 2));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(12, 7), new Field(11, 7));
			fourPlayerController.switchToNextPlayer();
			// Second rotation
			fourPlayerController.move(new Field(4, 13), new Field(3, 11));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(2, 3), new Field(3, 3));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(7, 2), new Field(7, 3));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(11, 7), new Field(10, 7));
			fourPlayerController.switchToNextPlayer();
			// Third rotation
			fourPlayerController.move(new Field(3, 11), new Field(4, 9));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(3, 3), new Field(4, 3));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(7, 3), new Field(7, 4));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(10, 7), new Field(9, 7));
			fourPlayerController.switchToNextPlayer();
			// 4 rotation
			fourPlayerController.move(new Field(4, 9), new Field(3, 7));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(4, 3), new Field(5, 3));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(7, 4), new Field(7, 5));
			fourPlayerController.switchToNextPlayer();
			fourPlayerController.move(new Field(9, 7), new Field(8, 7));
			fourPlayerController.switchToNextPlayer();
			// Deliver checkmate
			fourPlayerController.move(new Field(6, 13), new Field(1, 8));
			
			assertTrue(fourPlayerHistory.getMovesAsString().trim().
					equals("1. f13-f11 b4-c4 h2-h3 m8-l8 "
							+ "2. Ne14-d12 c4-d4 h3-h4 l8-k8 "
							+ "3. Nd12-e10 d4-e4 h4-h5 k8-j8 "
							+ "4. Ne10-d8 e4-f4 h5-h6 j8-i8 "
							+ "5. Qg14xb9#"));
		} catch(IllegalMoveException exc) {
			fail("Illegal move: " + exc.getMessage());
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.History#setMoves(java.lang.String)}.
	 */
	@Test
	public void testSetMovesTwoPlayer()
	{
		Move moves[] = {
				new Move(new Field(3, 6), new Field(3, 4), twoPlayerBoard.getPiece(new Field(3, 6))),
				new Move(new Field(4, 1), new Field(4, 3), twoPlayerBoard.getPiece(new Field(4, 1))),
				new Move(new Field(3, 4), new Field(4, 3), twoPlayerBoard.getPiece(new Field(3, 6))),
				new Move(new Field(5, 0), new Field(0, 5), twoPlayerBoard.getPiece(new Field(5, 0))),
				new Move(new Field(3, 7), new Field(3, 1), twoPlayerBoard.getPiece(new Field(3, 7))),
				new Move(new Field(1, 0), new Field(3, 1), twoPlayerBoard.getPiece(new Field(1, 0))),
				new Move(new Field(2, 7), new Field(6, 3), twoPlayerBoard.getPiece(new Field(2, 7))),
				new Move(new Field(5, 1), new Field(5, 3), twoPlayerBoard.getPiece(new Field(5, 1))),
				new Move(new Field(4, 3), new Field(5, 2), twoPlayerBoard.getPiece(new Field(3, 6))),
				new Move(new Field(6, 0), new Field(5, 2), twoPlayerBoard.getPiece(new Field(6, 0))),
				new Move(new Field(1, 7), new Field(0, 5), twoPlayerBoard.getPiece(new Field(1, 7))),
				new Move(new Field(4, 0), new Field(6, 0), twoPlayerBoard.getPiece(new Field(4, 0))),
				new Move(new Field(4, 7), new Field(2, 7), twoPlayerBoard.getPiece(new Field(4, 7))),
				new Move(new Field(5, 0), new Field(5, 1), twoPlayerBoard.getPiece(new Field(7, 0))),
				new Move(new Field(2, 7), new Field(1, 7), twoPlayerBoard.getPiece(new Field(4, 7))),
		};
		
		// Set up test controller to check board state against
		IChessboardController testController = new TwoPlayerChessboardController(
				null, TwoPlayerChessboardFactory.getInstance(), players[0], players[1]);
		try {
    		for(Move move : moves) {
    			testController.move(move.getFrom(), move.getTo());
    		}
		} catch(IllegalMoveException exc) {
			fail("Illegal move: " + exc.getMessage());
		}
		
		// Let the history create the game
		twoPlayerHistory.setMoves("1. d7-d5 e2-e4 2. d5xe4 Bf1-a6 3. Qd8xd2+ Nb1xd2 "
				+ "4. Bc8-g4 f2-f4 5. e4xf3(e.p) Ng1xf3 6. Nb8xa6 O-O "
				+ "7. O-O-O Rf1-f2 8. Kc8-b8");
		
		// Now check if the board states are equal
		// This only checks for name; e.g. two pawns are indistinguishable
		for(Field field : twoPlayerBoard.getFields()) {
			if(twoPlayerBoard.getPiece(field) != null) {
				assertTrue(twoPlayerBoard.getPiece(field).getName().
						equals(testController.getBoard().getPiece(field).getName()));
			}
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.History#setMoves(java.lang.String)}.
	 */
	@Test
	public void testSetMovesFourPlayer()
	{
		Move moves[] = {
				new Move(new Field(5, 12), new Field(5, 10), fourPlayerBoard.getPiece(new Field(5, 12))),
				new Move(new Field(1, 3), new Field(2, 3), fourPlayerBoard.getPiece(new Field(1, 3))),
				new Move(new Field(7, 1), new Field(7, 2), fourPlayerBoard.getPiece(new Field(7, 1))),
				new Move(new Field(12, 7), new Field(11, 7), fourPlayerBoard.getPiece(new Field(12, 7))),
				new Move(new Field(4, 13), new Field(3, 11), fourPlayerBoard.getPiece(new Field(4, 13))),
				new Move(new Field(2, 3), new Field(3, 3), fourPlayerBoard.getPiece(new Field(1, 3))),
				new Move(new Field(7, 2), new Field(7, 3), fourPlayerBoard.getPiece(new Field(7, 1))),
				new Move(new Field(11, 7), new Field(10, 7), fourPlayerBoard.getPiece(new Field(12, 7))),
				new Move(new Field(3, 11), new Field(4, 9), fourPlayerBoard.getPiece(new Field(4, 13))),
				new Move(new Field(3, 3), new Field(4, 3), fourPlayerBoard.getPiece(new Field(1, 3))),
				new Move(new Field(7, 3), new Field(7, 4), fourPlayerBoard.getPiece(new Field(7, 1))),
				new Move(new Field(10, 7), new Field(9, 7), fourPlayerBoard.getPiece(new Field(12, 7))),
				new Move(new Field(4, 9), new Field(3, 7), fourPlayerBoard.getPiece(new Field(4, 13))),
				new Move(new Field(4, 3), new Field(5, 3), fourPlayerBoard.getPiece(new Field(1, 3))),
				new Move(new Field(7, 4), new Field(7, 5), fourPlayerBoard.getPiece(new Field(7, 1))),
				new Move(new Field(9, 7), new Field(8, 7), fourPlayerBoard.getPiece(new Field(12, 7))),
				new Move(new Field(6, 13), new Field(1, 8), fourPlayerBoard.getPiece(new Field(6, 13))),
		};
		
		// Set up test controller to check board state against
		IChessboardController testController = new FourPlayerChessboardController(
				null, FourPlayerChessboardFactory.getInstance(), players[0], players[2], players[1], players[3]);
		try {
    		for(Move move : moves) {
    			testController.move(move.getFrom(), move.getTo());
    		}
		} catch(IllegalMoveException exc) {
			fail("Illegal move: " + exc.getMessage());
		}
		
		// Let the history create the game
		fourPlayerHistory.setMoves("1. f13-f11 b4-c4 h2-h3 m8-l8 "
				+ "2. Ne14-d12 c4-d4 h3-h4 l8-k8 "
				+ "3. Nd12-e10 d4-e4 h4-h5 k8-j8 "
				+ "4. Ne10-d8 e4-f4 h5-h6 j8-i8 "
				+ "5. Qg14xb9#");
		
		// Now check if the board states are equal
		// This only checks for name; e.g. two pawns are indistinguishable
		for(Field field : fourPlayerBoard.getFields()) {
			if(fourPlayerBoard.getPiece(field) != null) {
				assertTrue(fourPlayerBoard.getPiece(field).getName().
						equals(testController.getBoard().getPiece(field).getName()));
			}
		}
	}
}
