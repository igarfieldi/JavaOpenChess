package jchess.gamelogic.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.gamelogic.Player;
import jchess.gamelogic.ai.ICatAi;
import jchess.gamelogic.controllers.IChessboardController;

public class AiGame extends UntimedGame implements IGame
{
	private static Logger log = Logger.getLogger(AiGame.class.getName());
	private IChessboardController chessboard;
	private ICatAi catAi;
	
	public AiGame(IChessboardController chessboard, ICatAi catAi){
		super (chessboard);
		this.chessboard = chessboard;
		blockedChessboard = false;
		this.catAi = catAi;
	}
	
	@Override
	protected void nextMove()
	{
		switchActive();
		
		log.log(Level.FINE,
		        "Next move: active player: " + chessboard.getActivePlayer().getName() + " | color: "
		                + chessboard.getActivePlayer().getColor().name() + " | type: "
		                + chessboard.getActivePlayer().getType().name());
		if(chessboard.getActivePlayer().getType() == Player.Type.LOCAL)
		{
			blockedChessboard = false;
		} else if(chessboard.getActivePlayer().getType() == Player.Type.COMPUTER)
		{
			blockedChessboard = true;
			//checks if the cat is alive and moves it, otherwise skips the moves and goes to the next player
			if(catAi.isAlive() && !catAi.isSleeping())
			{
				executeMove(catAi.getCurrentPosition(), catAi.getNextMove());
			} else
			{
				switchActive();
			}
			blockedChessboard = false;
			catAi.updateSleepTimer();
			catAi.updateRespawnTimer();
		}
	}
	
}
