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
import jchess.gamelogic.models.factories.FourPlayerChessboardFactory;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;
import jchess.util.FileMapParser;

public class FourPlayerChessboardControllerTest
{
	private PieceFactory factory;
	private Player white = new Player("p1", Color.WHITE);
	private Player red = new Player("p2", Color.RED);
	private Player black = new Player("p3", Color.BLACK);
	private Player golden = new Player("p4", Color.GOLDEN);
	private FourPlayerChessboardController controller;
	private IChessboardModel board;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		controller = new FourPlayerChessboardController(
				null, FourPlayerChessboardFactory.getInstance(),
				white, red, black, golden);
		board = controller.getBoard();
		this.factory = PieceFactory.getInstance();
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
		for(Player player : new Player[]{white, red, black, golden}) {
			Piece king = factory.buildPiece(player, null, PieceType.KING);
			Move rookMove;

			board.setPiece(new Field(7, 0), king);
			rookMove = controller.getRookMoveForCastling(king, CastlingType.SHORT_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(10, 0)));
			assertTrue(rookMove.getTo().equals(new Field(8, 0)));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.LONG_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(3, 0)));
			assertTrue(rookMove.getTo().equals(new Field(6, 0)));
			
			board.movePiece(king, new Field(7, 13));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.SHORT_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(10, 13)));
			assertTrue(rookMove.getTo().equals(new Field(8, 13)));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.LONG_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(3, 13)));
			assertTrue(rookMove.getTo().equals(new Field(6, 13)));
			
			board.movePiece(king, new Field(0, 7));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.SHORT_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(0, 10)));
			assertTrue(rookMove.getTo().equals(new Field(0, 8)));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.LONG_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(0, 3)));
			assertTrue(rookMove.getTo().equals(new Field(0, 6)));
			
			board.movePiece(king, new Field(13, 7));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.SHORT_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(13, 10)));
			assertTrue(rookMove.getTo().equals(new Field(13, 8)));
			rookMove = controller.getRookMoveForCastling(king, CastlingType.LONG_CASTLING);
			assertTrue(rookMove.getFrom().equals(new Field(13, 3)));
			assertTrue(rookMove.getTo().equals(new Field(13, 6)));
			
			board.removePiece(new Field(13, 7));
		}
	}
	
	@Test
	public void testCheckForPromotion() {
		for(Player player : new Player[]{white, black}) {
			Piece pawn1 = factory.buildPiece(player, new Direction(1, 0), PieceType.PAWN);
			Piece pawn2 = factory.buildPiece(player, new Direction(-1, 0), PieceType.PAWN);
			Piece pawn3 = factory.buildPiece(player, new Direction(0, 1), PieceType.PAWN);
			Piece pawn4 = factory.buildPiece(player, new Direction(0, -1), PieceType.PAWN);
			Piece nonPawn = factory.buildPiece(player, null, PieceType.QUEEN);

			// First pawn; must only promote at the right border
			assertFalse(controller.checkForPromotion(pawn1, new Field(0, 7)));
			assertTrue(controller.checkForPromotion(pawn1, new Field(13, 7)));
			assertFalse(controller.checkForPromotion(pawn1, new Field(7, 0)));
			assertFalse(controller.checkForPromotion(pawn1, new Field(7, 13)));
			assertFalse(controller.checkForPromotion(pawn1, new Field(12, 7)));

			// Second pawn; must only promote at the left border
			assertTrue(controller.checkForPromotion(pawn2, new Field(0, 7)));
			assertFalse(controller.checkForPromotion(pawn2, new Field(13, 7)));
			assertFalse(controller.checkForPromotion(pawn2, new Field(7, 0)));
			assertFalse(controller.checkForPromotion(pawn2, new Field(7, 13)));
			assertFalse(controller.checkForPromotion(pawn2, new Field(12, 7)));

			// Third pawn; must only promote at the bottom border
			assertFalse(controller.checkForPromotion(pawn3, new Field(0, 7)));
			assertFalse(controller.checkForPromotion(pawn3, new Field(13, 7)));
			assertFalse(controller.checkForPromotion(pawn3, new Field(7, 0)));
			assertTrue(controller.checkForPromotion(pawn3, new Field(7, 13)));
			assertFalse(controller.checkForPromotion(pawn3, new Field(12, 7)));

			// Fourth pawn; must only promote at the top border
			assertFalse(controller.checkForPromotion(pawn4, new Field(0, 7)));
			assertFalse(controller.checkForPromotion(pawn4, new Field(13, 7)));
			assertTrue(controller.checkForPromotion(pawn4, new Field(7, 0)));
			assertFalse(controller.checkForPromotion(pawn4, new Field(7, 13)));
			assertFalse(controller.checkForPromotion(pawn4, new Field(12, 7)));
			
			// Non-pawn; must never promote
			assertFalse(controller.checkForPromotion(nonPawn, new Field(0, 7)));
			assertFalse(controller.checkForPromotion(nonPawn, new Field(13, 7)));
			assertFalse(controller.checkForPromotion(nonPawn, new Field(7, 0)));
			assertFalse(controller.checkForPromotion(nonPawn, new Field(7, 13)));
			assertFalse(controller.checkForPromotion(nonPawn, new Field(12, 7)));
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
			controller.move(new Field(4, 12), new Field(4, 10));
			controller.move(new Field(1, 6), new Field(3, 6));
			controller.move(new Field(4, 0), new Field(5, 2));
			controller.move(new Field(12, 8), new Field(10, 8));
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
		String game = "1. e13-e11 b7-d7 Ne1-f3 m9-l9 2. Bf14-c11 d7-e7 e2-e4 l9-k9";
		
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
		assertTrue(controller.getActivePlayer().equals(red));
		controller.switchToNextPlayer();
		assertTrue(controller.getActivePlayer().equals(black));
		controller.switchToNextPlayer();
		assertTrue(controller.getActivePlayer().equals(golden));
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
		assertTrue(controller.getActivePlayer().equals(golden));
		controller.switchToPreviousPlayer();
		assertTrue(controller.getActivePlayer().equals(black));
		controller.switchToPreviousPlayer();
		assertTrue(controller.getActivePlayer().equals(red));
		controller.switchToPreviousPlayer();
		assertTrue(controller.getActivePlayer().equals(white));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#switchToPreviousPlayer()}.
	 */
	@Test
	public void testGetPlayerIndex()
	{
		assertTrue(controller.getPlayerIndex(white) == 0);
		assertTrue(controller.getPlayerIndex(red) == 1);
		assertTrue(controller.getPlayerIndex(black) == 2);
		assertTrue(controller.getPlayerIndex(golden) == 3);
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
		for(Player player : new Player[]{white, red, black, golden}) {
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
		for(Player player : new Player[]{white, red, black, golden}) {
			
			Piece king = factory.buildPiece(player, null, PieceType.KING);
			board.setPiece(new Field(5, 5), king);
			
			for(Player enemy : controller.getEnemies(player)) {
				// Place an enemy unit with nearby capturing trajectory
				Piece threat = factory.buildPiece(enemy, null, PieceType.ROOK);
				board.setPiece(new Field(6, 0), threat);

				// Switch players until it's our turn
				while(!controller.getActivePlayer().equals(player)) {
					controller.switchToNextPlayer();
				}
				// King must not be able to move 'into' the rook
				
				assertFalse(getPossibleFields(king).contains(new Field(6, 4)));
				assertFalse(getPossibleFields(king).contains(new Field(6, 5)));
				assertFalse(getPossibleFields(king).contains(new Field(6, 6)));
				
				board.movePiece(threat, new Field(5, 0));
				// Check for all players that they can't move
				for(Player mover : new Player[]{white, red, black, golden}) {
					// Switch players until it's our turn
					while(!controller.getActivePlayer().equals(mover)) {
						controller.switchToNextPlayer();
					}
					Piece piece = factory.buildPiece(mover, null, PieceType.BISHOP);
					board.setPiece(new Field(5, 3), piece);
					
					// The unit must not be allowed to move away
					// else the king would be in check
					if(!mover.equals(enemy)) {
						assertTrue(getPossibleFields(piece).isEmpty());
					} else {
						assertFalse(getPossibleFields(piece).isEmpty());
					}
					
					board.removePiece(new Field(5, 3));
				}

				board.removePiece(new Field(5, 6));
			}
			
			board.removePiece(new Field(5, 5));
		}
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
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#getPiecesCheckingPlayer(jchess.gamelogic.Player)}.
	 */
	@Test
	public void testGetPiecesCheckingPlayer()
	{
		this.clearBoard();
		
		// Set up board scenarios
		for(Player player : new Player[]{white, red, black, golden}) {
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
		
		for(Player player : new Player[]{white, red, black, golden}) {
			Piece king = factory.buildPiece(player, null, PieceType.KING);
			
			for(Player enemy : controller.getEnemies(player)) {
				Piece rook1 = factory.buildPiece(enemy, null, PieceType.ROOK);
				Piece rook2 = factory.buildPiece(enemy, null, PieceType.ROOK);
				Piece queen = factory.buildPiece(enemy, null, PieceType.QUEEN);

				board.setPiece(new Field(3, 0), king);
				assertFalse(controller.isCheckmated(player));
				board.setPiece(new Field(4, 6), rook1);
				assertFalse(controller.isCheckmated(player));
				board.setPiece(new Field(6, 1), rook2);
				assertFalse(controller.isCheckmated(player));
				board.setPiece(new Field(3, 5), queen);
				assertTrue(controller.isCheckmated(player));
				
				Piece helpingPiece = factory.buildPiece(player, null, PieceType.BISHOP);
				// Piece blocks queen threat
				board.setPiece(new Field(3, 3), helpingPiece);
				assertFalse(controller.isCheckmated(player));
				// Piece can kill the queen
				board.movePiece(helpingPiece, new Field(4, 6));
				assertFalse(controller.isCheckmated(player));

				board.removePiece(new Field(3, 0));
				board.removePiece(new Field(4, 6));
				board.removePiece(new Field(6, 1));
				board.removePiece(new Field(3, 5));
				board.removePiece(new Field(4, 6));
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
				board.setPiece(new Field(3, 0), king);
				assertFalse(controller.isStalemate());
				board.setPiece(new Field(6, 1), rook1);
				assertFalse(controller.isStalemate());
				board.setPiece(new Field(4, 6), rook2);
				assertTrue(controller.isStalemate());
				
				Piece helpingPiece = factory.buildPiece(player, null, PieceType.BISHOP);
				// Piece can move -> no stalemate
				board.setPiece(new Field(5, 5), helpingPiece);
				assertFalse(controller.isStalemate());
				board.removePiece(new Field(5, 5));

				
				Piece pawn = factory.buildPiece(player, new Direction(0, -1),
						PieceType.PAWN);
				// Piece can't move -> still stalemate
				board.setPiece(new Field(4, 7), pawn);
				assertTrue(controller.isStalemate());
				
				board.removePiece(new Field(3, 0));
				board.removePiece(new Field(6, 1));
				board.removePiece(new Field(4, 6));
				board.removePiece(new Field(6, 5));
			}
		}
	}
}
