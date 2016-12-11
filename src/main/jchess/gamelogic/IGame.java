package jchess.gamelogic;

import jchess.gamelogic.controllers.GameClockController;
import jchess.gamelogic.controllers.IBoardActionHandler;
import jchess.gamelogic.controllers.IGameStateHandler;
import jchess.gamelogic.views.IGameView;
import jchess.util.LoadSaveable;

public interface IGame extends IBoardActionHandler, IGameStateHandler, LoadSaveable
{
	public IGameView getView();
	public Settings getSettings();
	public GameClockController getGameClock();
	public void newGame();
}