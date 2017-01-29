package jchess.gamelogic.pieces;

import java.util.HashSet;
import java.util.Set;

import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.util.Direction;

/**
 * Provides support methods for piece testing classes.
 * @author Florian Bethe
 */
public class PieceTestSupport
{
	/**
	 * Constraint implementation for two player board.
	 */
	public static final BoardConstraints CONSTRAINTS_TWO_PLAYER = new BoardConstraints() {
		private static final int SIZE = 8;
		
		@Override
		public boolean isPartOfBoard(int x, int y)
		{
			return (x >= 0) && (x < SIZE) && (y >= 0) && (y < SIZE);
		}
	};

	/**
	 * Constraint implementation for four player board.
	 */
	public static final BoardConstraints CONSTRAINTS_FOUR_PLAYER = new BoardConstraints() {
		private static final int SIZE = 14;
		private static final int SPARE = 3;
		
		@Override
		public boolean isPartOfBoard(int x, int y)
		{
			if((x < 0) || (x >= SIZE) || (y < 0) || (y >= SIZE)) {
				return false;
			}
			
			// Spare out the corners of the board
			if(((x < SPARE) && (y < SPARE)) ||
					((x < SPARE) && (y >= SIZE - SPARE)) ||
					((x >= SIZE - SPARE) && (y < SPARE)) ||
					((x >= SIZE - SPARE) && (y >= SIZE - SPARE))) {
				return false;
			}
			
			return true;
		}
	};
	
	/**
	 * Checks if a piece can make a move to a given field. This includes both
	 * capture and normal movement.
	 * 
	 * @param board
	 *            Board state to test for
	 * @param piece
	 *            Piece to check move for
	 * @param field
	 *            Target field
	 * @return True if piece can move to field
	 */
	public static boolean canMakeMove(IChessboardController board, Piece piece, Field field)
	{
		Set<Field> possibleMoves = new HashSet<Field>();
		for(Move move : board.getPossibleMoves(piece, true))
		{
			possibleMoves.add(move.getTo());
		}
		return possibleMoves.contains(field);
	}
	
	/**
	 * Checks if a piece can make a move to all the given fields. This includes
	 * both capture and normal movement.
	 * 
	 * @param board
	 *            Board state to test for
	 * @param piece
	 *            Piece to check move for
	 * @param field
	 *            Target fields
	 * @return True if piece can move to all fields
	 */
	public static boolean canMakeMoves(IChessboardController board, Piece piece, Field... fields)
	{
		for(Field field : fields)
		{
			if(!PieceTestSupport.canMakeMove(board, piece, field))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if a piece can make a move to ALL AND ONLY the given fields. This
	 * includes both capture and normal movement.
	 * 
	 * @param board
	 *            Board state to test for
	 * @param piece
	 *            Piece to check move for
	 * @param field
	 *            Target fields
	 * @return True if piece can move to all fields (and only these!)
	 */
	public static boolean canMakeExactlyTheseMoves(IChessboardController board, Piece piece, Set<Field> fields)
	{
		Set<Field> possibleMoves = new HashSet<Field>();
		for(Move move : board.getPossibleMoves(piece, true))
		{
			possibleMoves.add(move.getTo());
		}
		
		if(possibleMoves.size() != fields.size())
		{
			return false;
		}
		for(Field field : fields)
		{
			if(!possibleMoves.contains(field))
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean canMakeExactlyTheseMoves(IChessboardController board, Piece piece, Field... fields)
	{
		return PieceTestSupport.canMakeExactlyTheseMoves(board, piece, PieceTestSupport.toSet(fields));
	}
	
	public interface BoardConstraints
	{
		public boolean isPartOfBoard(int x, int y);
	}
	
	/**
	 * Generates a set of fields along the directions and constraints given. If
	 * a blocking field or unsatisfied contraint is encountered, the path along
	 * the current direction is terminated and the next direction is used. The
	 * start field is not considered part of the final set.
	 * 
	 * @param start
	 *            Field to start from
	 * @param constraints
	 *            Specifies what field is valid and what not
	 * @param directions
	 *            Set of directions to follow along
	 * @param repeat
	 *            False if only a single piece in each direction should be
	 *            considered
	 * @param blocked
	 *            Array of fields which are considered as blocking
	 * @return Set of fields
	 */
	public static Set<Field> getFields(Field start, BoardConstraints constraints, Set<Direction> directions,
	        boolean repeat, Field... blocked)
	{
		Set<Field> fields = new HashSet<Field>();
		// We need the blocking fields as a set for easy lookup
		Set<Field> blockedFields = PieceTestSupport.toSet(blocked);
		
		// Iterate all directions
		for(Direction dir : directions)
		{
			Field currField = start;
			
			// Iterate while we stay in the constraints
			while(constraints.isPartOfBoard(currField.getPosX() + dir.getX(), currField.getPosY() + dir.getY()))
			{
				// Modify the field to travel along 'dir'
				currField = new Field(currField.getPosX() + dir.getX(), currField.getPosY() + dir.getY());
				if(blockedFields.contains(currField))
				{
					break;
				} else
				{
					fields.add(currField);
				}
				
				if(!repeat) {
					break;
				}
			}
		}
		
		return fields;
	}
	
	/**
	 * Converts a given array to a set. Performs shallow copy only; duplicate
	 * elements will only occur once by the nature of sets.
	 * 
	 * @param array
	 *            Array to convert
	 * @return Set with elements of array
	 */
	public static <T> Set<T> toSet(T[] array)
	{
		Set<T> set = new HashSet<>();
		for(T t : array)
		{
			set.add(t);
		}
		return set;
	}
}
