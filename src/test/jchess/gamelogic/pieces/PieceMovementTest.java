package jchess.gamelogic.pieces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
import jchess.gamelogic.pieces.PieceTestSupport.BoardConstraints;
import jchess.util.Direction;

/**
 * Test class for general piece behaviour. Pieces will be marked as moved before
 * each test.
 * 
 * @author Florian Bethe
 */
@RunWith(Parameterized.class)
public class PieceMovementTest
{
	private PieceFactory factory;
	
	private IChessboardController controller;
	private IChessboardModel board;
	
	private Player players[];
	private BoardConstraints constraints;
	private PieceType type;
	private Direction forward;
	
	private Set<Field> testPositions;
	
	public PieceMovementTest(IChessboardController controller, BoardConstraints constraints, PieceType type,
	        Direction forward, Player[] players, Set<Field> testPositions)
	{
		this.controller = controller;
		
		this.players = players;
		this.constraints = constraints;
		this.type = type;
		this.forward = forward;
		
		this.testPositions = testPositions;
	}
	
	/**
	 * Contains and creates the parameters for the test. It creates the
	 * controllers and the players etc.
	 * 
	 * @return Collection of parameter sets
	 */
	@Parameterized.Parameters
	public static Collection<Object[]> pieceCombinations()
	{
		Player players2p[] = { new Player("p1", Color.WHITE), new Player("p2", Color.BLACK) };
		Player players4p[] = { new Player("p1", Color.WHITE), new Player("p2", Color.RED),
		        new Player("p3", Color.BLACK), new Player("p4", Color.GOLDEN) };
		IChessboardController controller2p = new TwoPlayerChessboardController(null,
		        TwoPlayerChessboardFactory.getInstance(), players2p[0], players2p[1]);
		IChessboardController controller4p = new FourPlayerChessboardController(null,
		        FourPlayerChessboardFactory.getInstance(), players4p[0], players4p[1], players4p[2], players4p[3]);
		
		// Test positions for both board types
		// These represent centre, corners, edges
		Set<Field> positions2p = PieceTestSupport.toSet(new Field[]{
				new Field(3, 3),
				new Field(0, 0), new Field(0, 7), new Field(7, 0), new Field(7, 7),
				new Field(0, 4), new Field(7, 5), new Field(3, 0), new Field(4, 7)
		});
		// Additionally the 'inner' corners
		Set<Field> positions4p = PieceTestSupport.toSet(new Field[]{
				new Field(7, 7),
				new Field(3, 0), new Field(10, 0), new Field(3, 13), new Field(10, 13),
				new Field(0, 3), new Field(0, 10), new Field(13, 3), new Field(13, 10),
				new Field(0, 6), new Field(13, 8), new Field(7, 0), new Field(6, 13),
				new Field(3, 3), new Field(10, 3), new Field(3, 10), new Field(10, 10)
		});
		
		return Arrays.asList(new Object[][]{
		        { controller2p, PieceTestSupport.CONSTRAINTS_TWO_PLAYER, PieceType.PAWN, new Direction(-1, 0),
		                players2p, positions2p },
		        { controller2p, PieceTestSupport.CONSTRAINTS_TWO_PLAYER, PieceType.PAWN, new Direction(1, 0), players2p,
		                positions2p },
		        { controller2p, PieceTestSupport.CONSTRAINTS_TWO_PLAYER, PieceType.PAWN, new Direction(0, -1),
		                players2p, positions2p },
		        { controller2p, PieceTestSupport.CONSTRAINTS_TWO_PLAYER, PieceType.PAWN, new Direction(0, 1), players2p,
		                positions2p },
		        { controller4p, PieceTestSupport.CONSTRAINTS_FOUR_PLAYER, PieceType.PAWN, new Direction(-1, 0),
		                players4p, positions4p },
		        { controller4p, PieceTestSupport.CONSTRAINTS_FOUR_PLAYER, PieceType.PAWN, new Direction(1, 0),
		                players4p, positions4p },
		        { controller4p, PieceTestSupport.CONSTRAINTS_FOUR_PLAYER, PieceType.PAWN, new Direction(0, -1),
		                players4p, positions4p },
		        { controller4p, PieceTestSupport.CONSTRAINTS_FOUR_PLAYER, PieceType.PAWN, new Direction(0, 1),
		                players4p, positions4p },
		        
		        { controller2p, PieceTestSupport.CONSTRAINTS_TWO_PLAYER, PieceType.ROOK, null, players2p, positions2p },
		        { controller4p, PieceTestSupport.CONSTRAINTS_FOUR_PLAYER, PieceType.ROOK, null, players4p,
		                positions4p },
		        { controller2p, PieceTestSupport.CONSTRAINTS_TWO_PLAYER, PieceType.KNIGHT, null, players2p,
		                positions2p },
		        { controller4p, PieceTestSupport.CONSTRAINTS_FOUR_PLAYER, PieceType.KNIGHT, null, players4p,
		                positions4p },
		        { controller2p, PieceTestSupport.CONSTRAINTS_TWO_PLAYER, PieceType.BISHOP, null, players2p,
		                positions2p },
		        { controller4p, PieceTestSupport.CONSTRAINTS_FOUR_PLAYER, PieceType.BISHOP, null, players4p,
		                positions4p },
		        { controller2p, PieceTestSupport.CONSTRAINTS_TWO_PLAYER, PieceType.QUEEN, null, players2p,
		                positions2p },
		        { controller4p, PieceTestSupport.CONSTRAINTS_FOUR_PLAYER, PieceType.QUEEN, null, players4p,
		                positions4p },
		        { controller2p, PieceTestSupport.CONSTRAINTS_TWO_PLAYER, PieceType.KING, null, players2p,
		                positions2p },
		        { controller4p, PieceTestSupport.CONSTRAINTS_FOUR_PLAYER, PieceType.KING, null, players4p,
		                positions4p } });
	}
	
	@Before
	public void setUp()
	{
		this.board = controller.getBoard();
		// Remove all pieces from the board to get a clean slate
		for(Field field : board.getFields())
		{
			board.removePiece(field);
		}
		
		this.factory = PieceFactory.getInstance();
	}
	
	/**
	 * Test the regular moves of the piece.
	 */
	@Test
	public void testPlayerPossibleMovesRegular()
	{
		// Test for all positions of the board
		
		for(Player player : players)
		{
			Piece piece = factory.buildPiece(player, forward, type);
			piece.markAsMoved();
			
			for(Field pieceField : testPositions)
			{
				board.setPiece(pieceField, piece);
				
				// Get the fields it is supposed to reach
				Set<Field> shouldReach = PieceTestSupport.getFields(pieceField, constraints,
				        piece.getBehaviour().getNormalMovements(), piece.getBehaviour().canMoveMultipleSteps());
				
				// Test if the piece can actually make these moves
				assertTrue(PieceTestSupport.canMakeExactlyTheseMoves(controller, piece, shouldReach));
				
				board.removePiece(pieceField);
			}
		}
	}
	
	/**
	 * Test the capture moves of the piece.
	 */
	@Test
	public void testPlayerPossibleMovesCapture()
	{
		// Test for all positions of the board
		
		for(Player player : players)
		{
			Piece piece = factory.buildPiece(player, forward, type);
			piece.markAsMoved();
			
			for(Field pieceField : testPositions)
			{
				board.setPiece(pieceField, piece);
				
				// Get the fields it is supposed to reach
				Set<Field> shouldReach = PieceTestSupport.getFields(pieceField, constraints,
				        piece.getBehaviour().getCapturingMovements(), piece.getBehaviour().canMoveMultipleSteps());
				
				// Check if enemy pieces can be captured on these positions
				// Don't care for blocking movement for now
				for(Player enemy : controller.getEnemies(player))
				{
					Piece enemyPiece = factory.buildPiece(enemy, null, PieceType.ROOK);
					
					for(Field field : shouldReach)
					{
						board.setPiece(field, enemyPiece);
						assertTrue(PieceTestSupport.canMakeMove(controller, piece, field));
						board.removePiece(field);
					}
				}
				
				board.removePiece(pieceField);
			}
		}
	}
	
	/**
	 * Tests the piece's moves when blocking units are in its wake. Uses regular
	 * moves.
	 */
	@Test
	public void testPossibleMovesRegularBlocking()
	{
		// Test for all positions of the board
		
		// Test for all players
		for(Player player : players)
		{
			Piece piece = factory.buildPiece(player, forward, type);
			piece.markAsMoved();
			
			// Iterate over all selected board positions
			for(Field pieceField : testPositions)
			{
				board.setPiece(pieceField, piece);
				
				// Compute the fields it should be able to reach
				Set<Field> shouldReach = PieceTestSupport.getFields(pieceField, constraints,
				        piece.getBehaviour().getNormalMovements(), piece.getBehaviour().canMoveMultipleSteps());
				
				// Iterate over all reachable fields and block them:
				for(Field blocking : shouldReach)
				{
					// By friendly unit
					for(Player ally : controller.getAllies(player))
					{
						Piece allyPiece = factory.buildPiece(ally, null, PieceType.ROOK);
						
						board.setPiece(blocking, allyPiece);
						
						// Compute fields reachable after block
						Set<Field> shouldReachAfterBlock = PieceTestSupport.getFields(pieceField, constraints,
						        piece.getBehaviour().getNormalMovements(), piece.getBehaviour().canMoveMultipleSteps(),
						        blocking);
						assertTrue(PieceTestSupport.canMakeExactlyTheseMoves(controller, piece, shouldReachAfterBlock));
						
						board.removePiece(blocking);
					}
				}
				
				board.removePiece(pieceField);
			}
		}
	}
	
	/**
	 * Tests the piece's moves when blocking units are in its wake. Uses its
	 * capturing moves.
	 */
	@Test
	public void testPossibleMovesCapturingBlocking()
	{
		// Test for all positions of the board
		
		for(Player player : players)
		{
			Piece piece = factory.buildPiece(player, forward, type);
			piece.markAsMoved();
			
			// Iterate over all selected board positions
			for(Field pieceField : testPositions)
			{
				board.setPiece(pieceField, piece);
				
				// Iterate over all capturing directions to block
				// them off individually
				for(Direction dir : piece.getBehaviour().getCapturingMovements())
				{
					Set<Field> capturable = PieceTestSupport.getFields(pieceField, constraints,
					        PieceTestSupport.toSet(new Direction[]{ dir }),
					        piece.getBehaviour().canMoveMultipleSteps());
					
					// Test each pair of blocking/blocked fields
					for(BlockingBlockedPair pair : this.getBlockingBlockedPairs(pieceField, capturable))
					{
						// Place a friendly unit as the blocker
						board.setPiece(pair.getBlocking(), factory.buildPiece(player, forward, PieceType.PAWN));
						
						// Check this for all possible enemies
						for(Player enemy : controller.getEnemies(player))
						{
							board.setPiece(pair.getBlocked(), factory.buildPiece(enemy, null, PieceType.KNIGHT));
							
							// The enemy should not be able to be captured
							assertFalse(PieceTestSupport.canMakeMove(controller, piece, pair.getBlocked()));
							
							board.removePiece(pair.getBlocked());
						}
						
						board.removePiece(pair.getBlocking());
					}
				}
				
				board.removePiece(pieceField);
			}
		}
	}
	
	private class BlockingBlockedPair
	{
		private Field blocking;
		private Field blocked;
		
		public BlockingBlockedPair(Field blocking, Field blocked)
		{
			this.blocking = blocking;
			this.blocked = blocked;
		}
		
		public Field getBlocking()
		{
			return blocking;
		}
		
		public Field getBlocked()
		{
			return blocked;
		}
	}
	
	/**
	 * Returns a list of pairs of a blocking a a blocked fields.
	 * 
	 * @param origin
	 *            From where it should be blocked
	 * @param fields
	 *            Set of fields to consider
	 * @return List of pairs
	 */
	private List<BlockingBlockedPair> getBlockingBlockedPairs(Field origin, Set<Field> fields)
	{
		List<BlockingBlockedPair> pairs = new ArrayList<>();
		
		// Iterate over all fields
		for(Field blocking : fields)
		{
			// Compute the distance of the current field to the origin
			int blockingDistance = (int) (Math.pow(blocking.getPosX() - origin.getPosX(), 2)
			        + Math.pow(blocking.getPosY() - origin.getPosY(), 2));
			
			// Also iterate over all fields
			for(Field blocked : fields)
			{
				int blockedDistance = (int) (Math.pow(blocked.getPosX() - origin.getPosX(), 2)
				        + Math.pow(blocked.getPosY() - origin.getPosY(), 2));
				
				// Fields can only be blocked if they are farther away
				// than the blocking one
				if(blockedDistance > blockingDistance)
				{
					pairs.add(new BlockingBlockedPair(blocking, blocked));
				}
			}
		}
		
		return pairs;
	}
}
