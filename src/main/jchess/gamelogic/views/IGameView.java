package jchess.gamelogic.views;

public interface IGameView extends IRenderable, IMessageDisplay
{
	public IChessboardView getChessboardView();
	public void addInfoComponent(IRenderable component);
}