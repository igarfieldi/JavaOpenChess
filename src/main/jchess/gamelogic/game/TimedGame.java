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
package jchess.gamelogic.game;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import jchess.gamelogic.Player;
import jchess.gamelogic.controllers.GameClockController;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.IllegalMoveException;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.views.IGameView;
import jchess.gamelogic.views.gameviews.SwingGameView;
import jchess.util.FileMapParser;

/**
 * Class responsible for the starts of new games, loading games, saving it, and
 * for ending it. This class is also responsible for appoing player with have a
 * move at the moment
 */
public class TimedGame implements IGame
{
	private static Logger log = Logger.getLogger(TimedGame.class.getName());
	
	private IGameView gameView;
	private boolean blockedChessboard;
	private IChessboardController chessboard;
	private GameClockController gameClock;
	
	public TimedGame(IChessboardController chessboard,
	        GameClockController gameClock)
	{
		this.chessboard = chessboard;
		this.chessboard.getView().initialize(chessboard, this);
		this.gameClock = gameClock;
		this.gameClock.setStateHandler(this);
		
		this.blockedChessboard = false;
		
		gameView = new SwingGameView(this.chessboard.getView());
		gameView.addInfoComponent(gameClock.getView());
		gameView.addInfoComponent(chessboard.getHistory().getView());
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
				} else if(chessboard.isStalemate())
				{
					this.onStalemate();
				}
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
	public boolean save(FileMapParser parser)
	{
		String gameType = "Game" + chessboard.getPlayerCount() + "pTimed";
		parser.setProperty("Event", gameType);
		
		Calendar cal = Calendar.getInstance();
		parser.setProperty("Date",
		        cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH));
		this.chessboard.save(parser);
		
		return true;
	}
	
	@Override
	public boolean load(FileMapParser parser)
	{
		// TODO: if we allow loading load clocks too
		this.getView().showMessage("unloadable_game_type", "Timed game");
		return false;
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
		this.gameClock.start();
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
			this.blockedChessboard = true;
		}
	}
}