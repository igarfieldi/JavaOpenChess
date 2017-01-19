package jchess.gamelogic.models.factories;

import java.util.List;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IBoardFactory;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.chessboardmodels.ChessboardModel;
import jchess.gamelogic.pieces.IPieceFactory;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

public class FourPlayerChessboardFactory implements IBoardFactory
{
	private static FourPlayerChessboardFactory instance;
	
	public static FourPlayerChessboardFactory getInstance() {
		if(instance == null) {
			instance = new FourPlayerChessboardFactory();
		}
		
		return instance;
	}

	@Override
	public IChessboardModel createChessboard(List<Player> players)
	{
		if(players.size() != 5) {
			return null;
		}
		
		ChessboardModel board = new ChessboardModel();
		
		// Create field structure (plus-like shape)
		for(int x = 0; x < 14; x++) {
			for(int y = 3; y < 11; y++) {
				board.addField(new Field(x, y));
			}
		}
		for(int y = 0; y < 14; y++) {
			for(int x = 3; x < 11; x++) {
				board.addField(new Field(x, y));
			}
		}
		
		IPieceFactory factory = PieceFactory.getInstance();
		Direction up = new Direction(0, -1);
		Direction down = new Direction(0, 1);
		Direction left = new Direction(-1, 0);
		Direction right = new Direction(1, 0);
		
		// Regular pieces for all 4 players
		board.setPiece(board.getField(3, 13), factory.buildPiece(players.get(0), up, PieceType.ROOK));
		board.setPiece(board.getField(10, 13), factory.buildPiece(players.get(0), up, PieceType.ROOK));
		board.setPiece(board.getField(4, 13), factory.buildPiece(players.get(0), up, PieceType.KNIGHT));
		board.setPiece(board.getField(9, 13), factory.buildPiece(players.get(0), up, PieceType.KNIGHT));
		board.setPiece(board.getField(5, 13), factory.buildPiece(players.get(0), up, PieceType.BISHOP));
		board.setPiece(board.getField(8, 13), factory.buildPiece(players.get(0), up, PieceType.BISHOP));
		board.setPiece(board.getField(6, 13), factory.buildPiece(players.get(0), up, PieceType.QUEEN));
		board.setPiece(board.getField(7, 13), factory.buildPiece(players.get(0), up, PieceType.KING));
		
		board.setPiece(board.getField(0, 10), factory.buildPiece(players.get(1), right, PieceType.ROOK));
		board.setPiece(board.getField(0, 3), factory.buildPiece(players.get(1), right, PieceType.ROOK));
		board.setPiece(board.getField(0, 9), factory.buildPiece(players.get(1), right, PieceType.KNIGHT));
		board.setPiece(board.getField(0, 4), factory.buildPiece(players.get(1), right, PieceType.KNIGHT));
		board.setPiece(board.getField(0, 8), factory.buildPiece(players.get(1), right, PieceType.BISHOP));
		board.setPiece(board.getField(0, 5), factory.buildPiece(players.get(1), right, PieceType.BISHOP));
		board.setPiece(board.getField(0, 7), factory.buildPiece(players.get(1), right, PieceType.QUEEN));
		board.setPiece(board.getField(0, 6), factory.buildPiece(players.get(1), right, PieceType.KING));
		
		board.setPiece(board.getField(3, 0), factory.buildPiece(players.get(2), down, PieceType.ROOK));
		board.setPiece(board.getField(10, 0), factory.buildPiece(players.get(2), down, PieceType.ROOK));
		board.setPiece(board.getField(4, 0), factory.buildPiece(players.get(2), down, PieceType.KNIGHT));
		board.setPiece(board.getField(9, 0), factory.buildPiece(players.get(2), down, PieceType.KNIGHT));
		board.setPiece(board.getField(5, 0), factory.buildPiece(players.get(2), down, PieceType.BISHOP));
		board.setPiece(board.getField(8, 0), factory.buildPiece(players.get(2), down, PieceType.BISHOP));
		board.setPiece(board.getField(7, 0), factory.buildPiece(players.get(2), down, PieceType.QUEEN));
		board.setPiece(board.getField(6, 0), factory.buildPiece(players.get(2), down, PieceType.KING));

		board.setPiece(board.getField(13, 10), factory.buildPiece(players.get(3), left, PieceType.ROOK));
		board.setPiece(board.getField(13, 3), factory.buildPiece(players.get(3), left, PieceType.ROOK));
		board.setPiece(board.getField(13, 9), factory.buildPiece(players.get(3), left, PieceType.KNIGHT));
		board.setPiece(board.getField(13, 4), factory.buildPiece(players.get(3), left, PieceType.KNIGHT));
		board.setPiece(board.getField(13, 8), factory.buildPiece(players.get(3), left, PieceType.BISHOP));
		board.setPiece(board.getField(13, 5), factory.buildPiece(players.get(3), left, PieceType.BISHOP));
		board.setPiece(board.getField(13, 6), factory.buildPiece(players.get(3), left, PieceType.QUEEN));
		board.setPiece(board.getField(13, 7), factory.buildPiece(players.get(3), left, PieceType.KING));
		
		// Initialize pawns: no special distinctions necessary
		for(int x = 3; x < 11; x++)
		{
			board.setPiece(board.getField(x, 12), factory.buildPiece(players.get(0), up, PieceType.PAWN));
			board.setPiece(board.getField(x, 1), factory.buildPiece(players.get(2), down, PieceType.PAWN));
		}
		for(int y = 3; y < 11; y++)
		{
			board.setPiece(board.getField(1, y), factory.buildPiece(players.get(1), right, PieceType.PAWN));
			board.setPiece(board.getField(12, y), factory.buildPiece(players.get(3), left, PieceType.PAWN));
		}
		
		// Initialise cat piece
		
		board.setPiece(board.getField(6, 6), factory.buildPiece(players.get(4), down, PieceType.CAT));
		
		return board;
	}
}
