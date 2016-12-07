
package jchess.gamelogic.pieces;

import jchess.gamelogic.Player;
import jchess.util.Direction;

public class PieceFactory
{
	private static Player player;

	public static Piece buildPiece(PieceType type){
		Piece piece = null;
		switch (type){
			case BISHOP:
				piece = new Bishop(player);
				break;
			case KNIGHT:
				piece = new Knight(player);
				break;
			case KING:
				piece = new King(player);
				break;
			case ROOK:
				piece = new Rook(player);
				break;
			case PAWN:
				//TODO: find a way to pass the direction; preliminary fix
				piece = new Pawn(player, new Direction(0, 1));
				break;
			case QUEEN:
				piece = new Queen(player);
				break;
			default:
				//TODO: throw exception 
				break;
		}
		return piece;
	}
	
}
