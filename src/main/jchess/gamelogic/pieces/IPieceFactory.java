package jchess.gamelogic.pieces;

import jchess.gamelogic.Player;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

/**
 * Creates pieces with given behaviour.
 * @author Florian Bethe
 */
public interface IPieceFactory
{
	public interface IPieceType
	{
		public IPieceBehaviour getBehaviour(Direction forward);
		
		public String getSymbol();
	}
	
	/**
	 * Create a piece with the given player, direction of movement and type.
	 * Forward may be null if it is not relevant to the piece's behaviour.
	 * @param player Player for piece
	 * @param forward General movement direction of piece
	 * @param type Type of piece
	 * @return newly constructed piece
	 */
	public Piece buildPiece(Player player, Direction forward, PieceType type);
}
