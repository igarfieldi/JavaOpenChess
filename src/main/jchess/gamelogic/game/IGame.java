package jchess.gamelogic.game;

import jchess.gamelogic.controllers.IBoardActionHandler;
import jchess.gamelogic.controllers.IGameStateHandler;
import jchess.gamelogic.views.IGameView;
import jchess.util.LoadSaveable;

public interface IGame extends IBoardActionHandler, IGameStateHandler, LoadSaveable
{
	public IGameView getView();
	public void newGame();
}
