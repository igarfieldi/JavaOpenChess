package jchess.gamelogic.models.chessboardmodels;

import jchess.gamelogic.pieces.Piece;

public class TwoPlayerChessboardModel extends SquareChessboardModel
{
	private static int WIDTH = 8;
	private static int HEIGHT = 8;
	
	public TwoPlayerChessboardModel()
	{
		super(WIDTH, HEIGHT);
	}

	@Override
	public void initialize() {
		
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				this.createField(x, y);
			}
		}
	}

	@Override
	public TwoPlayerChessboardModel copy() {
		TwoPlayerChessboardModel board = new TwoPlayerChessboardModel();
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				if(this.getField(x, y) != null) {
					board.createField(x, y);
					Piece piece = this.getPiece(this.getField(x, y));
					if(piece != null) {
						board.setPiece(board.getField(x, y), piece.copy());
					}
				}
			}
		}
		
		return board;
	}
}
