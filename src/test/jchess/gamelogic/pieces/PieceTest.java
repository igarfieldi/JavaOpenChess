package jchess.gamelogic.pieces;

import java.util.HashSet;
import java.util.Set;

import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;

public class PieceTest
{
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
		return board.getPossibleMoves(piece, true).contains(field);
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
			if(!PieceTest.canMakeMove(board, piece, field))
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
	public static boolean canMakeExactlyTheseMoves(IChessboardController board, Piece piece, Field... fields)
	{
		Set<Field> possibleMoves = new HashSet<Field>();
		for(Move move : board.getPossibleMoves(piece, true)) {
			possibleMoves.add(move.getTo());
		}
		if(possibleMoves.size() != fields.length)
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
}
