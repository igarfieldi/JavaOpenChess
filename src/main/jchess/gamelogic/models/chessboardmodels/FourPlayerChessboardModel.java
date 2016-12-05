package jchess.gamelogic.models.chessboardmodels;

import jchess.gamelogic.pieces.Piece;

public class FourPlayerChessboardModel extends SquareChessboardModel
{
	private static int WIDTH = 14;
	private static int HEIGHT = 14;
	
	public FourPlayerChessboardModel()
	{
		super(WIDTH, HEIGHT);
	}

	@Override
	public void initialize() {
		
		// Center board; just like 2p chess
		this.createFields(3, 3, 10, 10);
		
		// Edge cases
		this.createFields(0, 3, 2, 10);
		this.createFields(11, 3, 13, 10);
		this.createFields(3, 0, 10, 2);
		this.createFields(3, 11, 10, 13);
	}
	
	private void createFields(int startX, int startY, int endX, int endY) {
		for(int x = startX; x <= endX; x++) {
			for(int y = startY; y <= endY; y++) {
				this.createField(x, y);
			}
		}
	}

	@Override
	public FourPlayerChessboardModel copy() {
		FourPlayerChessboardModel board = new FourPlayerChessboardModel();
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
