package jchess.gamelogic.pieces;

import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

public interface IPieceFactory
{
	public interface IPieceType
	{
		public IPieceBehaviour getBehaviour(Direction forward);
		
		public String getSymbol();
	}
	
	public Piece buildPiece(Player player, Direction forward, PieceType type);
}
