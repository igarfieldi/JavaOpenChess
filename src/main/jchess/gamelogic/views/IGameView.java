package jchess.gamelogic.views;

public interface IGameView extends Renderable
{
	
	public IChessboardView getChessboardView();
	public void showMessage(String key);
	public void showMessage(String key, String args);
	public boolean showYesNoDialog(String key);
	
}