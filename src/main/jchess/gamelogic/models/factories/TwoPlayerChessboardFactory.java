package jchess.gamelogic.models.factories;

import java.util.List;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IBoardFactory;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.chessboardmodels.ChessboardModel;
import jchess.gamelogic.pieces.Bishop;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Knight;
import jchess.gamelogic.pieces.Pawn;
import jchess.gamelogic.pieces.Queen;
import jchess.gamelogic.pieces.Rook;
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
		
		// TODO: extract to PiecePositionFactory or smth
		board.setPiece(board.getField(0, 7), new Rook(players.get(0)));
		board.setPiece(board.getField(7, 7), new Rook( players.get(0)));
		board.setPiece(board.getField(1, 7), new Knight(players.get(0)));
		board.setPiece(board.getField(6, 7), new Knight(players.get(0)));
		board.setPiece(board.getField(2, 7), new Bishop(players.get(0)));
		board.setPiece(board.getField(5, 7), new Bishop(players.get(0)));
		board.setPiece(board.getField(0, 0), new Rook(players.get(1)));
		board.setPiece(board.getField(7, 0), new Rook(players.get(1)));
		board.setPiece(board.getField(1, 0), new Knight(players.get(1)));
		board.setPiece(board.getField(6, 0), new Knight(players.get(1)));
		board.setPiece(board.getField(2, 0), new Bishop(players.get(1)));
		board.setPiece(board.getField(5, 0), new Bishop(players.get(1)));
		
		// The queen is always placed on the field of her own color
		board.setPiece(board.getField(3, 7), new Queen(players.get(0)));
		board.setPiece(board.getField(4, 7), new King(players.get(0)));
		board.setPiece(board.getField(3, 0), new Queen(players.get(1)));
		board.setPiece(board.getField(4, 0), new King(players.get(1)));
		
		// Initialize pawns: no special distinctions necessary
		for(int x = 0; x < 8; x++)
		{
			board.setPiece(board.getField(x, 6), new Pawn(players.get(0), new Direction(0, -1)));
			board.setPiece(board.getField(x, 1), new Pawn(players.get(1), new Direction(0, 1)));
		}
		
		return board;
	}
}
