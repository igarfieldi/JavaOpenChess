/**
 * 
 */
package jchess.gamelogic.controllers.chessboardcontrollers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IllegalMoveException;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;
import jchess.util.FileMapParser;

/**
 * @author Florian Bethe
 */
public class TwoPlayerChessboardControllerTest
{
	private PieceFactory factory;
	private Player white = new Player("p1", Color.WHITE);
	private Player black = new Player("p2", Color.BLACK);
	private TwoPlayerChessboardController controller;
	private IChessboardModel board;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		controller = new TwoPlayerChessboardController(
				null, TwoPlayerChessboardFactory.getInstance(), white, black);
		board = controller.getBoard();
		this.factory = PieceFactory.getInstance();
	}
	
	/**
	 * Gets the fields a piece can move to.
	 * @param piece Piece to move
	 * @return Set of fields
	 */
	private Set<Field> getPossibleFields(Piece piece) {
		Set<Field> fields = new HashSet<>();
		
		for(Move move : controller.getPossibleMoves(piece, true)) {
			fields.add(move.getTo());
		}
		
		return fields;
	}
	
	/**
	 * Clears the board of all pieces.
	 */
	private void clearBoard() {
		for(Field field : board.getFields()) {
			board.removePiece(field);
		}
	}
	
	@Test
	public void testGetRookMoveForCastling() {
		for(Player player : new Player[]{white, black}) {
			Piece king = factory.buildPiece(player, null, PieceType.KING);
			Move rookMove;

			board.setPiece(new Field(4, 0), king);
			rookMove = controller.getRookMoveForCastling(king, CastlingType.SHORT_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(7, 0)));
			assertTrue(rookMove.getTo().equals(new Field(5, 0)));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.LONG_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(0, 0)));
			assertTrue(rookMove.getTo().equals(new Field(3, 0)));
			
			board.movePiece(king, new Field(4, 7));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.SHORT_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(7, 7)));
			assertTrue(rookMove.getTo().equals(new Field(5, 7)));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.LONG_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(0, 7)));
			assertTrue(rookMove.getTo().equals(new Field(3, 7)));
			
			board.removePiece(new Field(4, 7));
		}
	}
	
	@Test
	public void testCheckForPromotion() {
		for(Player player : new Player[]{white, black}) {
			Piece pawn1 = factory.buildPiece(player, new Direction(0, 1), PieceType.PAWN);
			Piece pawn2 = factory.buildPiece(player, new Direction(0, -1), PieceType.PAWN);
			Piece nonPawn = factory.buildPiece(player, null, PieceType.QUEEN);
			
			// Test entire width
			for(int x = 0; x < 8; x++) {
				// Test entire height
				for(int y = 1; y < 7; y++) {
					assertFalse(controller.checkForPromotion(pawn1, new Field(x, y)));
					assertFalse(controller.checkForPromotion(pawn2, new Field(x, y)));
					assertFalse(controller.checkForPromotion(nonPawn, new Field(x, y)));
				}

				// Check top row
				assertFalse(controller.checkForPromotion(pawn1, new Field(x, 0)));
				assertTrue(controller.checkForPromotion(pawn2, new Field(x, 0)));
				assertFalse(controller.checkForPromotion(nonPawn, new Field(x, 0)));
				
				// Check bottom row
				assertTrue(controller.checkForPromotion(pawn1, new Field(x, 7)));
				assertFalse(controller.checkForPromotion(pawn2, new Field(x, 7)));
				assertFalse(controller.checkForPromotion(nonPawn, new Field(x, 7)));
			}
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#save(jchess.util.FileMapParser)}.
	 */
	@Test
	public void testSave()
	{
		try
		{
			controller.move(new Field(3, 6), new Field(3, 5));
			controller.move(new Field(6, 1), new Field(6, 2));
			controller.move(new Field(2, 7), new Field(4, 5));
			controller.move(new Field(6, 0), new Field(5, 2));
		} catch(IllegalMoveException e)
		{
			fail("Unexpected illegal move!");
		}
		FileMapParser saveFile = new FileMapParser();
		controller.save(saveFile);

		assertTrue(saveFile.getProperty("Moves").trim().equals(controller.getHistory().getMovesAsString().trim()));
		assertTrue(saveFile.getProperty("WHITE").trim().equals(white.getName()));
		assertTrue(saveFile.getProperty("BLACK").trim().equals(black.getName()));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#load(jchess.util.FileMapParser)}.
	 */
	@Test
	public void testLoad()
	{
		FileMapParser gameLoader = new FileMapParser();
		String game = "1. a7-a5 b2-b4 2. Nb8-c6 g2-g3";
		
		gameLoader.setProperty("Moves", game);
		controller.load(gameLoader);
		
		// This just tests the basic capability of loading a game; testing
		// correctness is done in the history test class
		assertTrue(controller.getHistory().getMovesAsString().trim().equals(game));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#switchToNextPlayer()}.
	 */
	@Test
	public void testSwitchToNextPlayer()
	{
		assertTrue(controller.getActivePlayer().equals(white));
		controller.switchToNextPlayer();
		assertTrue(controller.getActivePlayer().equals(black));
		controller.switchToNextPlayer();
		assertTrue(controller.getActivePlayer().equals(white));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#switchToPreviousPlayer()}.
	 */
	@Test
	public void testSwitchToPreviousPlayer()
	{
		assertTrue(controller.getActivePlayer().equals(white));
		controller.switchToPreviousPlayer();
		assertTrue(controller.getActivePlayer().equals(black));
		controller.switchToPreviousPlayer();
		assertTrue(controller.getActivePlayer().equals(white));
	}
	
	@Test
	public void testGetPlayerCount() {
		assertTrue(controller.getPlayerCount() == 2);
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#switchToPreviousPlayer()}.
	 */
	@Test
	public void testGetPlayerIndex()
	{
		assertTrue(controller.getPlayerIndex(white) == 0);
		assertTrue(controller.getPlayerIndex(black) == 1);
		try {
			controller.getPlayerIndex(new Player("", Color.WHITE));
			fail("Shouldn't find player");
		} catch(NoSuchElementException exc) {
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#getPlayerIndex(jchess.gamelogic.Player)}.
	 */
	@Test
	public void testIsChecked()
	{
		this.clearBoard();
		
		// Set up board scenarios
		for(Player player : new Player[]{black, white}) {
			Piece king = factory.buildPiece(player, null, PieceType.KING);
			board.setPiece(new Field(3, 3), king);
			
			// Place a threatening piece; testing multiple pieces is not
			// necessary; their capability to capture is tested separately
			for(Player enemy : controller.getEnemies(player)) {
				Piece threat = factory.buildPiece(enemy, null, PieceType.QUEEN);
				
				board.setPiece(new Field(6, 3), threat);
				assertTrue(controller.isChecked(player));
				
				board.movePiece(threat, new Field(4, 3));
				assertTrue(controller.isChecked(player));
				
				board.movePiece(threat, new Field(6, 4));
				assertFalse(controller.isChecked(player));
				
				board.removePiece(new Field(6, 4));
			}
			
			// Make sure that allies cannot check the king
			for(Player ally : controller.getAllies(player)) {
				Piece threat = factory.buildPiece(ally, null, PieceType.QUEEN);
				
				board.setPiece(new Field(6, 3), threat);
				assertFalse(controller.isChecked(player));
				
				board.removePiece(new Field(6, 3));
			}
		}
	}
	
	/**
	 * Tests that a king cannot be exposed to check by itself or other
	 * pieces moving.
	 */
	@Test
	public void testCannotMoveIntoCheck() {
		this.clearBoard();
		
		// Set up board scenarios
		for(Player player : new Player[]{black, white}) {
			// Switch players until it's our turn
			while(!controller.getActivePlayer().equals(player)) {
				controller.switchToNextPlayer();
			}
			
			Piece king = factory.buildPiece(player, null, PieceType.KING);
			board.setPiece(new Field(3, 3), king);
			
			for(Player enemy : controller.getEnemies(player)) {
				// Place an enemy unit with nearby capturing trajectory
				Piece threat = factory.buildPiece(enemy, null, PieceType.ROOK);
				board.setPiece(new Field(4, 6), threat);
				
				// King must not be able to move 'into' the rook
				assertFalse(getPossibleFields(king).contains(new Field(4, 3)));
				assertFalse(getPossibleFields(king).contains(new Field(4, 2)));
				assertFalse(getPossibleFields(king).contains(new Field(4, 4)));
				
				board.movePiece(threat, new Field(0, 3));
				Piece friendly = factory.buildPiece(player, null, PieceType.BISHOP);
				board.setPiece(new Field(2, 3), friendly);
				
				// The friendly unit must not be allowed to move away
				// else the king would be in check
				assertTrue(getPossibleFields(friendly).isEmpty());
				
				board.removePiece(new Field(2, 3));
			}
			
			board.removePiece(new Field(3, 3));
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#getPiecesCheckingPlayer(jchess.gamelogic.Player)}.
	 */
	@Test
	public void testGetPiecesCheckingPlayer()
	{
		this.clearBoard();
		
		// Set up board scenarios
		for(Player player : new Player[]{black, white}) {
			Piece king = factory.buildPiece(player, null, PieceType.KING);
			board.setPiece(new Field(3, 3), king);
			
			// Place a threatening piece; testing multiple pieces is not
			// necessary; their capability to capture is tested separately
			for(Player enemy : controller.getEnemies(player)) {
				Piece queen = factory.buildPiece(enemy, null, PieceType.QUEEN);
				board.setPiece(new Field(6, 3), queen);
				assertTrue(controller.getPiecesCheckingPlayer(player).size() == 1);
				assertTrue(controller.getPiecesCheckingPlayer(player).contains(queen));
				
				Piece bishop = factory.buildPiece(enemy, null, PieceType.BISHOP);
				board.setPiece(new Field(5, 5), bishop);
				assertTrue(controller.getPiecesCheckingPlayer(player).size() == 2);
				assertTrue(controller.getPiecesCheckingPlayer(player).contains(queen));
				assertTrue(controller.getPiecesCheckingPlayer(player).contains(bishop));
				
				Piece noThreat = factory.buildPiece(enemy, null, PieceType.QUEEN);
				board.setPiece(new Field(4, 5), noThreat);
				assertFalse(controller.getPiecesCheckingPlayer(player).contains(noThreat));

				board.removePiece(new Field(6, 3));
				board.removePiece(new Field(5, 5));
				board.removePiece(new Field(4, 5));
			}
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#isCheckmated(jchess.gamelogic.Player)}.
	 */
	@Test
	public void testIsCheckmated()
	{
		this.clearBoard();
		
		for(Player player : new Player[]{white, black}) {
			Piece king = factory.buildPiece(player, null, PieceType.KING);
			
			for(Player enemy : controller.getEnemies(player)) {
				Piece rook1 = factory.buildPiece(enemy, null, PieceType.ROOK);
				Piece rook2 = factory.buildPiece(enemy, null, PieceType.ROOK);
				Piece queen = factory.buildPiece(enemy, null, PieceType.QUEEN);

				board.setPiece(new Field(0, 0), king);
				assertFalse(controller.isCheckmated(player));
				board.setPiece(new Field(1, 4), rook1);
				assertFalse(controller.isCheckmated(player));
				board.setPiece(new Field(4, 1), rook2);
				assertFalse(controller.isCheckmated(player));
				board.setPiece(new Field(0, 5), queen);
				assertTrue(controller.isCheckmated(player));
				
				Piece helpingPiece = factory.buildPiece(player, null, PieceType.BISHOP);
				// Piece blocks queen threat
				board.setPiece(new Field(0, 3), helpingPiece);
				assertFalse(controller.isCheckmated(player));
				// Piece can kill the queen
				board.movePiece(helpingPiece, new Field(1, 6));
				assertFalse(controller.isCheckmated(player));

				board.removePiece(new Field(0, 0));
				board.removePiece(new Field(1, 4));
				board.removePiece(new Field(4, 1));
				board.removePiece(new Field(0, 5));
				board.removePiece(new Field(1, 6));
			}
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#isStalemate()}.
	 */
	@Test
	public void testIsStalemate()
	{
		this.clearBoard();
		
		// Check for all players
		for(Player player : new Player[]{white, black}) {
			Piece king = factory.buildPiece(player, null, PieceType.KING);
			
			for(Player enemy : controller.getEnemies(player)) {
				Piece rook1 = factory.buildPiece(enemy, null, PieceType.ROOK);
				Piece rook2 = factory.buildPiece(enemy, null, PieceType.ROOK);
				
				// Make the king player the active player
				while(!controller.getActivePlayer().equals(player)) {
					controller.switchToNextPlayer();
				}
				
				// Place king into stalemate
				board.setPiece(new Field(0, 0), king);
				assertFalse(controller.isStalemate());
				board.setPiece(new Field(1, 4), rook1);
				assertFalse(controller.isStalemate());
				board.setPiece(new Field(4, 1), rook2);
				assertTrue(controller.isStalemate());
				
				Piece helpingPiece = factory.buildPiece(player, null, PieceType.BISHOP);
				// Piece can move -> no stalemate
				board.setPiece(new Field(5, 5), helpingPiece);
				assertFalse(controller.isStalemate());
				board.removePiece(new Field(5, 5));

				
				Piece pawn = factory.buildPiece(player, new Direction(0, -1),
						PieceType.PAWN);
				// Piece can't move -> still stalemate
				board.setPiece(new Field(1, 5), pawn);
				assertTrue(controller.isStalemate());
				

				board.removePiece(new Field(0, 0));
				board.removePiece(new Field(1, 4));
				board.removePiece(new Field(4, 1));
				board.removePiece(new Field(1, 5));
			}
		}
	}
	
}
