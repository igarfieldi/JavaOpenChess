package jchess.gamelogic.game;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.gamelogic.Player;
import jchess.gamelogic.ai.ICatAi;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.util.FileMapParser;

public class AiGame extends UntimedGame implements IGame
{
	private static Logger log = Logger.getLogger(AiGame.class.getName());
	private IChessboardController chessboard;
	private ICatAi catAi;
	
	public AiGame(IChessboardController chessboard, ICatAi catAi)
	{
		super(chessboard);
		this.chessboard = chessboard;
		blockedChessboard = false;
		this.catAi = catAi;
	}
	
	@Override
	public void save(FileMapParser parser)
	{
		this.getView().showMessage("unsaveable_game_type", "Cat AI game");
	}
	
	@Override
	public void load(FileMapParser parser)
	{
		this.getView().showMessage("unloadable_game_type", "Cat AI game");
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
			// checks if the cat is alive and moves it, otherwise skips the
			// moves and goes to the next player
			if(catAi.canMove())
			{
				if(catAi.canCapture())
				{
					executeMove(catAi.getCurrentPosition(), catAi.getRandomThreateningMove());
					catAi.sleepCat();
				} else
				{
					executeMove(catAi.getCurrentPosition(), catAi.getRandomNormalMove());
				}
				
			} else
			{
				switchActive();
			}
			catAi.updateRespawnTimer();
			catAi.updateSleepTimer();
			blockedChessboard = false;
		}
	}
	
}
