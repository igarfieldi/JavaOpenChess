package jchess.gamelogic.views;

public interface IGameView extends IRenderable, IMessageDisplay
{
	
	public IChessboardView getChessboardView();
	public void setChessboardView(IChessboardView view);
	
}