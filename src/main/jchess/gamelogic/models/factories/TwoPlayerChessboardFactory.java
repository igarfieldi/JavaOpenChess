package jchess.gamelogic.models.factories;

import java.util.List;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IBoardFactory;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.chessboardmodels.ChessboardModel;
import jchess.gamelogic.pieces.Bishop;
import jchess.gamelogic.pieces.IPieceFactory;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Knight;
import jchess.gamelogic.pieces.Pawn;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.Queen;
import jchess.gamelogic.pieces.Rook;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

public class TwoPlayerChessboardFactory implements IBoardFactory
{
	private static TwoPlayerChessboardFactory instance;
	
	public static TwoPlayerChessboardFactory getInstance() {
		if(instance == null) {
			instance = new TwoPlayerChessboardFactory();
		}
		
		return instance;
	}

	@Override
	public IChessboardModel createChessboard(List<Player> players)
	{
		if(players.size() != 2) {
			return null;
		}
		
		IChessboardModel board = new ChessboardModel();
		
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				board.addField(new Field(x, y));
			}
		}

		IPieceFactory factory = PieceFactory.getInstance();
		Direction up = new Direction(0, -1);
		Direction down = new Direction(0, 1);
		
		// Regular pieces
		board.setPiece(board.getField(0, 7), factory.buildPiece(players.get(0), up, PieceType.ROOK));
		board.setPiece(board.getField(7, 7), factory.buildPiece(players.get(0), up, PieceType.ROOK));
		board.setPiece(board.getField(1, 7), factory.buildPiece(players.get(0), up, PieceType.KNIGHT));
		board.setPiece(board.getField(6, 7), factory.buildPiece(players.get(0), up, PieceType.KNIGHT));
		board.setPiece(board.getField(2, 7), factory.buildPiece(players.get(0), up, PieceType.BISHOP));
		board.setPiece(board.getField(5, 7), factory.buildPiece(players.get(0), up, PieceType.BISHOP));
		board.setPiece(board.getField(0, 0), factory.buildPiece(players.get(1), down, PieceType.ROOK));
		board.setPiece(board.getField(7, 0), factory.buildPiece(players.get(1), down, PieceType.ROOK));
		board.setPiece(board.getField(1, 0), factory.buildPiece(players.get(1), down, PieceType.KNIGHT));
		board.setPiece(board.getField(6, 0), factory.buildPiece(players.get(1), down, PieceType.KNIGHT));
		board.setPiece(board.getField(2, 0), factory.buildPiece(players.get(1), down, PieceType.BISHOP));
		board.setPiece(board.getField(5, 0), factory.buildPiece(players.get(1), down, PieceType.BISHOP));
		
		// The queen is always placed on the field of her own color
		board.setPiece(board.getField(3, 7), factory.buildPiece(players.get(0), up, PieceType.QUEEN));
		board.setPiece(board.getField(4, 7), factory.buildPiece(players.get(0), up, PieceType.KING));
		board.setPiece(board.getField(3, 0), factory.buildPiece(players.get(1), down, PieceType.QUEEN));
		board.setPiece(board.getField(4, 0), factory.buildPiece(players.get(1), down, PieceType.KING));
		
		// Initialize pawns: no special distinctions necessary
		for(int x = 0; x < 8; x++)
		{
			board.setPiece(board.getField(x, 6), factory.buildPiece(players.get(0), up, PieceType.PAWN));
			board.setPiece(board.getField(x, 1), factory.buildPiece(players.get(1), down, PieceType.PAWN));
		}
		
		return board;
	}
}
