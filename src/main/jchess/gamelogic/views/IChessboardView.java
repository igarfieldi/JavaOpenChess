package jchess.gamelogic.views;

import jchess.gamelogic.controllers.IBoardActionHandler;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;

public interface IChessboardView extends IRenderable, IMessageDisplay
{
	public void initialize(IChessboardController chessboard, IBoardActionHandler handler);
	public Field getActiveSquare();
	public void select(Field field);
	public void unselect();
	public void resizeChessboard(int sideLength);
}