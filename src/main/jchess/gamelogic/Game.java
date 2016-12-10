/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.gamelogic;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import jchess.gamelogic.controllers.GameClockController;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.IllegalMoveException;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.views.IChessboardView;
import jchess.gamelogic.views.IGameView;
import jchess.gamelogic.views.gameviews.SwingGameView;
import jchess.util.GameStateParser;

/**
 * Class responsible for the starts of new games, loading games, saving it, and
 * for ending it. This class is also responsible for appoing player with have a
 * move at the moment
 */
public class Game implements IGame
{
	private static Logger log = Logger.getLogger(Game.class.getName());
	
	private IGameView gameView;
	private Settings settings;
	private boolean blockedChessboard;
	private IChessboardController chessboard;
	private GameClockController gameClock;
	
	public Game(Settings settings, IChessboardController chessboard, IChessboardView view,
			GameClockController gameClock)
	{
		this.settings = settings;
		this.chessboard = chessboard;
		view.initialize(chessboard, this);
		this.gameClock = gameClock;
		this.gameClock.setStateHandler(this);
		
		this.blockedChessboard = false;
		
		gameView = new SwingGameView(gameClock.getView(), chessboard.getHistory().getScrollPane());
		gameView.setChessboardView(view);
	}
	
	@Override
	public void onCheckmate() {
		this.endGame("Checkmate! " + this.chessboard.getActivePlayer().getColor()
		        + " player loses!");
	}
	
	@Override
	public void onStalemate() {
		this.endGame("Stalemate! Draw!");
	}
	
	@Override
	public void onTimeOver() {
		this.endGame("Time over! " + chessboard.getActivePlayer().getColor() + " player loses!");
		// TODO: game over
	}

	@Override
	public IGameView getView()
	{
		return gameView;
	}

	@Override
	public Settings getSettings()
	{
		return settings;
	}

	@Override
	public GameClockController getGameClock()
	{
		return gameClock;
	}
	
	@Override
	public void onFieldSelection(Field selectedField)
	{
		log.log(Level.FINE, "Selected field: " + selectedField);
		
		if(!blockedChessboard)
		{
			Field activeField = gameView.getChessboardView().getActiveSquare();
			try
			{
				// TODO: clean up. Hand-full of scenarios:
				// 1. Nothing selected yet and we select either no field, empty
				// field or field with piece not ours
				// 2. Field selected and we select field again -> unselect
				// 3. Field selected and we select different piece belonging to
				// us -> select new field
				// 4. Field selected and we select possible move -> carry out
				// move etc.
				if(activeField == null)
				{
					if(selectedField == null || (chessboard.getBoard().getPiece(selectedField) != null) && (chessboard
					        .getBoard().getPiece(selectedField).getPlayer() != this.chessboard.getActivePlayer()))
					{
						return;
					}
				}
				
				if(chessboard.getBoard().getPiece(selectedField) != null && chessboard.getBoard()
				        .getPiece(selectedField).getPlayer() == this.chessboard.getActivePlayer()
				        && selectedField != activeField)
				{
					gameView.getChessboardView().unselect();
					gameView.getChessboardView().select(selectedField);
				} else if(activeField == selectedField) // unselect
				{
					gameView.getChessboardView().unselect();
				} else if(activeField != null
				        && chessboard.getBoard().getPiece(activeField) != null
				        && chessboard.getPossibleMoves(
				                chessboard.getBoard().getPiece(activeField), true)
				                .contains(selectedField))
				{
					try {
						if(chessboard.move(activeField, selectedField)) {
							// Only switch players etc. when the move was
							// actually executed
	    					// switch player
	    					this.nextMove();
	    					
	    					// Checkmate or stalemate
	    					if(chessboard.isCheckmated(chessboard.getActivePlayer()))
	    					{
	    						this.onCheckmate();
	    					} else if(chessboard.isStalemate())
	    					{
	    						this.onStalemate();
	    					}
						}
						
					} catch(IllegalMoveException exc) {
						log.log(Level.SEVERE, "Tried to execute illegal move!", exc);
					}

					gameView.getChessboardView().unselect();
				}
				
			} catch(NullPointerException exc)
			{
				// TODO: how could there be a NullPointerException here?
				log.log(Level.SEVERE, "Encountered exception while determining click position!", exc);
				gameView.render();
				return;
			}
		} else
		{
			log.info("Chessboard is blocked");
		}
	}
	
	/**
	 * Method to save actual state of game
	 * 
	 * @param path
	 *            address of place where game will be saved
	 */
	@Override
	public void saveGame(GameStateParser parser)
	{
		parser.setProperty("Event", "Game");	// TODO: different game types!
		
		Calendar cal = Calendar.getInstance();
		parser.setProperty("Date", cal.get(Calendar.YEAR) + "."
		        + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH));
		this.chessboard.save(parser);
	}

	@Override
	public void loadGame(String moves) {
		log.info("Loading saved local game");
		
		this.blockedChessboard = true;
		chessboard.loadFromString(moves);
		this.blockedChessboard = false;
		
		this.getView().render();
	}
	
	/**
	 * Method to Start new game
	 *
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
	 * Method to end game
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
	 * Method to swich active players after move
	 */
	private void switchActive()
	{
		chessboard.switchToNextPlayer();
		this.gameClock.switchClocks();
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
			// TODO: implement AI^^
		}
	}
}