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
		if(players.size() != 4) {
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
		
		// Regular pieces for all 4 players
		board.setPiece(board.getField(3, 13), new Rook(players.get(0)));
		board.setPiece(board.getField(10, 13), new Rook(players.get(0)));
		board.setPiece(board.getField(4, 13), new Knight(players.get(0)));
		board.setPiece(board.getField(9, 13), new Knight(players.get(0)));
		board.setPiece(board.getField(5, 13), new Bishop(players.get(0)));
		board.setPiece(board.getField(8, 13), new Bishop(players.get(0)));
		board.setPiece(board.getField(6, 13), new Queen(players.get(0)));
		board.setPiece(board.getField(7, 13), new King(players.get(0)));
		
		board.setPiece(board.getField(0, 10), new Rook(players.get(1)));
		board.setPiece(board.getField(0, 3), new Rook(players.get(1)));
		board.setPiece(board.getField(0, 9), new Knight(players.get(1)));
		board.setPiece(board.getField(0, 4), new Knight(players.get(1)));
		board.setPiece(board.getField(0, 8), new Bishop(players.get(1)));
		board.setPiece(board.getField(0, 5), new Bishop(players.get(1)));
		board.setPiece(board.getField(0, 7), new Queen(players.get(1)));
		board.setPiece(board.getField(0, 6), new King(players.get(1)));
		
		board.setPiece(board.getField(3, 0), new Rook(players.get(2)));
		board.setPiece(board.getField(10, 0), new Rook(players.get(2)));
		board.setPiece(board.getField(4, 0), new Knight(players.get(2)));
		board.setPiece(board.getField(9, 0), new Knight(players.get(2)));
		board.setPiece(board.getField(5, 0), new Bishop(players.get(2)));
		board.setPiece(board.getField(8, 0), new Bishop(players.get(2)));
		board.setPiece(board.getField(7, 0), new Queen(players.get(2)));
		board.setPiece(board.getField(6, 0), new King(players.get(2)));

		board.setPiece(board.getField(13, 10), new Rook(players.get(3)));
		board.setPiece(board.getField(13, 3), new Rook(players.get(3)));
		board.setPiece(board.getField(13, 9), new Knight(players.get(3)));
		board.setPiece(board.getField(13, 4), new Knight(players.get(3)));
		board.setPiece(board.getField(13, 8), new Bishop(players.get(3)));
		board.setPiece(board.getField(13, 5), new Bishop(players.get(3)));
		board.setPiece(board.getField(13, 6), new Queen(players.get(3)));
		board.setPiece(board.getField(13, 7), new King(players.get(3)));
		
		// Initialize pawns: no special distinctions necessary
		for(int x = 3; x < 11; x++)
		{
			board.setPiece(board.getField(x, 12), new Pawn(players.get(0), new Direction(0, -1)));
			board.setPiece(board.getField(x, 1), new Pawn(players.get(2), new Direction(0, 1)));
		}
		for(int y = 3; y < 11; y++)
		{
			board.setPiece(board.getField(1, y), new Pawn(players.get(1), new Direction(1, 0)));
			board.setPiece(board.getField(12, y), new Pawn(players.get(3), new Direction(-1, 0)));
		}
		
		return board;
	}
}
