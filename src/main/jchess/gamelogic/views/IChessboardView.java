package jchess.gamelogic.views;

import jchess.gamelogic.controllers.IBoardActionHandler;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;

public interface IChessboardView extends Renderable
{
	public void initialize(IChessboardModel board, IBoardActionHandler handler);
	public Field getActiveSquare();
	public void select(Field field);
	public void unselect();
	public void resizeChessboard(int sideLength);
	
}