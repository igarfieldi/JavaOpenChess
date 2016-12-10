package jchess.gamelogic;

import jchess.gamelogic.controllers.IBoardActionHandler;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.IGameStateHandler;
import jchess.gamelogic.views.IGameView;

public interface IGame extends IBoardActionHandler, IGameStateHandler
{
	public IGameView getView();
	public IChessboardController getChessboard();
	
}
