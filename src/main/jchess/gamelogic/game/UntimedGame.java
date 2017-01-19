package jchess.gamelogic.game;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import jchess.gamelogic.Player;
import jchess.gamelogic.ai.CatAi;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.IllegalMoveException;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.views.IGameView;
import jchess.gamelogic.views.gameviews.SwingGameView;
import jchess.util.FileMapParser;

public class UntimedGame implements IGame
{
	private static Logger log = Logger.getLogger(TimedGame.class.getName());
	
	private IGameView gameView;
	private boolean blockedChessboard;
	private IChessboardController chessboard;
	
	private CatAi catAi;
	
	public UntimedGame(IChessboardController chessboard)
	{
		this.chessboard = chessboard;
		this.chessboard.getView().initialize(chessboard, this);
		
		this.blockedChessboard = false;
		
		gameView = new SwingGameView(this.chessboard.getView());
		gameView.addInfoComponent(chessboard.getHistory().getView());
		
		catAi = new CatAi(chessboard);
	}
	
	@Override
	public void onCheckmate()
	{
		this.endGame("Checkmate! " + this.chessboard.getActivePlayer().getColor() + " player loses!");
	}
	
	@Override
	public void onStalemate()
	{
		this.endGame("Stalemate! Draw!");
	}
	
	@Override
	public void onTimeOver()
	{
		this.endGame("Time over! " + chessboard.getActivePlayer().getColor() + " player loses!");
		// TODO: game over
	}
	
	@Override
	public IGameView getView()
	{
		return gameView;
	}
	
	@Override
	public void onFieldSelection(Field selectedField)
	{
		log.log(Level.FINE, "Selected field: " + selectedField);
		
		if(blockedChessboard)
		{
			log.info("Chessboard is blocked!");
			return;
		}
		
		Field activeField = gameView.getChessboardView().getActiveSquare();
		
		// Check if a field was already selected and then either
		// unselect, select different or execute move
		if(activeField != null && selectedField != null)
		{
			if(selectedField.equals(activeField))
			{
				gameView.getChessboardView().unselect();
			} else
			{
				Piece activePiece = chessboard.getBoard().getPiece(activeField);
				Piece selectedPiece = chessboard.getBoard().getPiece(selectedField);
				
				if(selectedPiece == null || activePiece.getPlayer() != selectedPiece.getPlayer())
				{
					this.executeMove(activeField, selectedField);
				} else
				{
					// Select different piece
					gameView.getChessboardView().unselect();
					gameView.getChessboardView().select(selectedField);
				}
			}
		} else if(selectedField != null)
		{
			Piece selectedPiece = chessboard.getBoard().getPiece(selectedField);
			if(selectedPiece != null && selectedPiece.getPlayer() == chessboard.getActivePlayer())
			{
				// Select new field
				gameView.getChessboardView().select(selectedField);
			}
		}
		
		gameView.render();
	}
	
	/**
	 * Attempts to execute a chess move for the two given fields. If the move is
	 * valid, it should get carried out by the chessboard controller. If not, or
	 * if the move was cancelled (e.g. denied promotion), then no change to
	 * board or players happens.
	 * 
	 * @param origin
	 *            Field on which the piece to move is located
	 * @param target
	 *            Target field for move
	 */
	private void executeMove(Field origin, Field target)
	{
		// Try to execute move
		try
		{
			if(chessboard.move(origin, target))
			{
				gameView.getChessboardView().unselect();
				// Only switch players etc. when the move was
				// actually executed
				this.nextMove();
				
				// Checkmate or stalemate
				if(chessboard.isCheckmated(chessboard.getActivePlayer()))
				{
					this.onCheckmate();
				} // Check for Stalemate used to be here, I swear I will add it
				  // later
			}
		} catch(IllegalMoveException exc)
		{
			log.log(Level.WARNING, "Illegal move!");
		}
	}
	
	/**
	 * Method to save actual state of game
	 * 
	 * @param path
	 *            address of place where game will be saved
	 */
	@Override
	public void save(FileMapParser parser)
	{
		parser.setProperty("Event", "Game"); // TODO: different game types!
		
		Calendar cal = Calendar.getInstance();
		parser.setProperty("Date",
		        cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH));
		this.chessboard.save(parser);
	}
	
	@Override
	public void load(FileMapParser parser)
	{
		log.info("Loading saved local game");
		
		this.blockedChessboard = true;
		chessboard.load(parser);
		this.blockedChessboard = false;
		
		this.getView().render();
	}
	
	/**
	 * Method to Start new game
	 */
	@Override
	public void newGame()
	{
		log.info("Starting new local game");
		
		if(chessboard.getActivePlayer().getType() != Player.Type.LOCAL)
		{
			this.blockedChessboard = true;
		}
		
		this.getView().render();
		
	}
	
	/**
	 * Method to end game.
	 * 
	 * @param message
	 *            what to show player(s) at end of the game (for example "draw",
	 *            "black wins" etc.)
	 */
	private void endGame(String message)
	{
		this.blockedChessboard = true;
		log.info(message);
		JOptionPane.showMessageDialog(null, message);
	}
	
	/**
	 * Method to switch active players after move.
	 */
	private void switchActive()
	{
		chessboard.switchToNextPlayer();
	}
	
	/**
	 * Method to go to next move (checks if game is local/network etc.)
	 */
	private void nextMove()
	{
		switchActive();
		
		log.log(Level.FINE,
		        "Next move: active player: " + chessboard.getActivePlayer().getName() + " | color: "
		                + chessboard.getActivePlayer().getColor().name() + " | type: "
		                + chessboard.getActivePlayer().getType().name());
		if(chessboard.getActivePlayer().getType() == Player.Type.LOCAL)
		{
			this.blockedChessboard = false;
		} else if(chessboard.getActivePlayer().getType() == Player.Type.COMPUTER)
		{
			this.blockedChessboard = true;
			if(catAi.isAlive())
			{
				executeMove(catAi.getCurrentPosition(), catAi.getNextTargetMove());
			} else
			{
				switchActive();
				this.blockedChessboard = false;
			}
			catAi.updateRespawnTimer();
		}
	}
}