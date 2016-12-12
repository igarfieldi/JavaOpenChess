
package jchess.gamelogic.pieces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.pieces.IPieceFactory.IPieceType;
import jchess.util.Direction;

public class PieceFactory implements IPieceFactory
{
	public enum PieceType implements IPieceType
	{
		PAWN
		{
			@Override
			public IPieceBehaviour getBehaviour(Direction forward)
			{
				return new Pawn(forward);
			}
			
			@Override
			public String getSymbol()
			{
				return "";
			}
		},
		BISHOP
		{
			@Override
			public IPieceBehaviour getBehaviour(Direction forward)
			{
				return new Bishop();
			}
			
			@Override
			public String getSymbol()
			{
				return "B";
			}
		},
		KNIGHT
		{
			@Override
			public IPieceBehaviour getBehaviour(Direction forward)
			{
				return new Knight();
			}
			
			@Override
			public String getSymbol()
			{
				return "N";
			}
		},
		ROOK
		{
			@Override
			public IPieceBehaviour getBehaviour(Direction forward)
			{
				return new Rook();
			}
			
			@Override
			public String getSymbol()
			{
				return "R";
			}
		},
		QUEEN
		{
			@Override
			public IPieceBehaviour getBehaviour(Direction forward)
			{
				return new Queen();
			}
			
			@Override
			public String getSymbol()
			{
				return "Q";
			}
		},
		KING
		{
			@Override
			public IPieceBehaviour getBehaviour(Direction forward)
			{
				return new King();
			}
			
			@Override
			public String getSymbol()
			{
				return "K";
			}
		}
	}
	
	private static PieceFactory instance;
	
	public static PieceFactory getInstance()
	{
		if(instance == null)
		{
			instance = new PieceFactory();
		}
		return instance;
	}
	
	public Piece buildPiece(Player player, Direction forward, PieceType type)
	{
		return new Piece(player, type.getSymbol(), type.getBehaviour(forward));
	}
	
}
