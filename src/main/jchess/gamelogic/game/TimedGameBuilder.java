package jchess.gamelogic.game;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.gamelogic.Player;
import jchess.gamelogic.controllers.GameClockController;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.FourPlayerChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.models.factories.FourPlayerChessboardFactory;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.views.factories.FourPlayerChessboardViewFactory;
import jchess.gamelogic.views.factories.TwoPlayerChessboardViewFactory;

/**
 * Builder for regular games.
 * This builder is capable of constructing games with 2 or 4 players. If set,
 * it will also create a game with running clock.
 * @author Florian Bethe
 */
public class TimedGameBuilder implements IGameBuilder
{
	private static Logger log = Logger.getLogger(TimedGameBuilder.class.getName());
	
	private int timeLimit;
	private List<Player> playerList;
	
	public TimedGameBuilder() {
		this.timeLimit = 0;
		this.playerList = new ArrayList<Player>();
	}
	
	@Override
	public void addPlayer(Player player) {
		this.playerList.add(player);
	}
	
	@Override
	public void setProperty(String key, String value) {
		if(key.equals("timeLimit")) {
			try {
				this.timeLimit = Integer.parseInt(value);
			} catch(NumberFormatException exc) {
				log.log(Level.WARNING, "Attempted to set invalid time limit!");
			}
		}
	}

	@Override
	public IGame create() {
		IChessboardController controller;
		switch(playerList.size()) {
			case 2:
				controller = new TwoPlayerChessboardController(
						TwoPlayerChessboardViewFactory.getInstance(),
						TwoPlayerChessboardFactory.getInstance(),
						playerList.get(0),
						playerList.get(1)
					);
				break;
			case 5:
				controller = new FourPlayerChessboardController(
						FourPlayerChessboardViewFactory.getInstance(),
						FourPlayerChessboardFactory.getInstance(),
						playerList.get(0),
						playerList.get(1),
						playerList.get(2),
						playerList.get(3),
						playerList.get(4)
					);
				break;
			default:
				log.log(Level.SEVERE, "Invalid number of players for this game builder!");
				throw new UnsupportedOperationException("Invalid number of players for this game builder!");
		}
		
		if(timeLimit > 0) {
    		GameClockController clock = new GameClockController(timeLimit, 
    				playerList.get(0), playerList.get(1));
    		
    		return new TimedGame(controller, clock);
		} else {
			return new UntimedGame(controller);
		}
	}
}
