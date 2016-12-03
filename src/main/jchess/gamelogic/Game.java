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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.clock.GameClock;
import jchess.gamelogic.field.ChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.IBoardActionHandler;
import jchess.gamelogic.field.Moves;
import jchess.gamelogic.views.ChessboardView;
import jchess.gamelogic.views.GameView;
import jchess.gamelogic.views.IGameView;

/**
 * Class responsible for the starts of new games, loading games, saving it, and
 * for ending it. This class is also responsible for appoing player with have a
 * move at the moment
 */
public class Game implements IBoardActionHandler
{
	private static Logger log = Logger.getLogger(Game.class.getName());
	
	private IGameView gameView;
	private Settings settings;
	private boolean blockedChessboard;
	private ChessboardController chessboard;
	private GameClock gameClock;
	private Moves moves;
	
	public Game()
	{
		this.moves = new Moves(this);
		settings = new Settings();
		chessboard = new ChessboardController(this.settings, this.moves);
		ChessboardView view = new ChessboardView(settings, chessboard);
		view.initialize(chessboard.getBoard(), this);
		chessboard.setView(view);
		gameClock = new GameClock(this);
		
		this.blockedChessboard = false;
		
		gameView = new GameView(view, gameClock.getView(), this.moves.getScrollPane());
	}
	
	public IGameView getView()
	{
		return gameView;
	}
	
	public Settings getSettings()
	{
		return settings;
	}
	
	public ChessboardController getChessboard()
	{
		return chessboard;
	}
	
	public GameClock getGameClock()
	{
		return gameClock;
	}
	
	public Moves getMoves()
	{
		return moves;
	}
	
	public void setSettings(Settings settings)
	{
		this.settings = settings;
	}
	
	@Override
	public void onUndoRequested() {
		this.undo();
	}
	
	@Override
	public void onRedoRequested() {
		this.redo();
	};
	
	@Override
	public void onFieldSelection(Field selectedField)
	{
		log.log(Level.FINE, "Selected field: " + selectedField);
		
		if(!blockedChessboard)
		{
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
				if(gameView.getChessboardView().getActiveSquare() == null)
				{
					if(selectedField == null || (chessboard.getBoard().getPiece(selectedField) != null) && (chessboard
					        .getBoard().getPiece(selectedField).getPlayer() != this.chessboard.getActivePlayer()))
					{
						return;
					}
				}
				
				if(chessboard.getBoard().getPiece(selectedField) != null && chessboard.getBoard()
				        .getPiece(selectedField).getPlayer() == this.chessboard.getActivePlayer()
				        && selectedField != gameView.getChessboardView().getActiveSquare())
				{
					gameView.getChessboardView().unselect();
					gameView.getChessboardView().select(selectedField);
				} else if(gameView.getChessboardView().getActiveSquare() == selectedField) // unselect
				{
					gameView.getChessboardView().unselect();
				} else if(gameView.getChessboardView().getActiveSquare() != null
				        && chessboard.getBoard().getPiece(gameView.getChessboardView().getActiveSquare()) != null
				        && chessboard.getPossibleMoves(
				                chessboard.getBoard().getPiece(gameView.getChessboardView().getActiveSquare()), true)
				                .contains(selectedField))
				{
					if(settings.getGameType() == Settings.GameType.LOCAL)
					{
						chessboard.move(gameView.getChessboardView().getActiveSquare(), selectedField);
					}
					
					gameView.getChessboardView().unselect();
					
					// switch player
					this.nextMove();
					
					// checkmate or stalemate
					if(chessboard.isCheckmated(chessboard.getActivePlayer()))
					{
						this.endGame("Checkmate! " + this.chessboard.getActivePlayer().getColor().toString()
						        + " player lose!");
					} else if(chessboard.isStalemate())
					{
						this.endGame("Stalemate! Draw!");
					}
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
	public void saveGame(File path)
	{
		File file = path;
		FileWriter fileW = null;
		try
		{
			fileW = new FileWriter(file);
		} catch(java.io.IOException exc)
		{
			log.log(Level.SEVERE, "Error creating FileWriter!", exc);
			gameView.showMessage("error_writing_to_file", exc.toString());
			return;
		}
		Calendar cal = Calendar.getInstance();
		String str = new String("");
		String info = new String("[Event \"Game\"]\n[Date \"" + cal.get(Calendar.YEAR) + "."
		        + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH) + "\"]\n" + "[White \""
		        + this.settings.getWhitePlayer().getName() + "\"]\n" + "[Black \""
		        + this.settings.getBlackPlayer().getName() + "\"]\n\n");
		str += info;
		str += this.moves.getMovesInString();
		try
		{
			fileW.write(str);
			fileW.flush();
			fileW.close();
		} catch(java.io.IOException exc)
		{
			log.log(Level.SEVERE, "Error saving game file!", exc);
			gameView.showMessage("error_writing_to_file", exc.toString());
			return;
		}
		gameView.showMessage("game_saved_properly");
	}
	
	/**
	 * Loading game method(loading game state from the earlier saved file)
	 * 
	 * @param file
	 *            File where is saved game
	 */
	
	/*
	 * @Override public void setSize(int width, int height) { Dimension min =
	 * this.getMinimumSize(); if(min.getHeight() < height && min.getWidth() <
	 * width) { super.setSize(width, height); } else if(min.getHeight() <
	 * height) { super.setSize(width, (int)min.getHeight()); } else
	 * if(min.getWidth() < width) { super.setSize((int)min.getWidth(), height);
	 * } else { super.setSize(width, height); } }
	 */
	static public void loadGame(File file)
	{
		FileReader fileR = null;
		try
		{
			fileR = new FileReader(file);
		} catch(java.io.IOException exc)
		{
			log.log(Level.SEVERE, "Error opening FileReader!", exc);
			return;
		}
		BufferedReader br = new BufferedReader(fileR);
		String tempStr = new String();
		String blackName, whiteName;
		try
		{
			tempStr = getLineWithVar(br, new String("[White"));
			whiteName = getValue(tempStr);
			tempStr = getLineWithVar(br, new String("[Black"));
			blackName = getValue(tempStr);
			tempStr = getLineWithVar(br, new String("1."));
		} catch(ReadGameError | IOException err)
		{
			log.log(Level.SEVERE, "Error reading game file!", err);
			return;
		}
		Game newGUI = JChessApp.view.addNewTab(whiteName + " vs. " + blackName);
		Settings locSetts = newGUI.settings;
		locSetts.getBlackPlayer().setName(blackName);
		locSetts.getWhitePlayer().setName(whiteName);
		locSetts.getBlackPlayer().setType(Player.Type.LOCAL);
		locSetts.getWhitePlayer().setType(Player.Type.LOCAL);
		locSetts.setGameMode(Settings.GameMode.LOAD_GAME);
		locSetts.setGameType(Settings.GameType.LOCAL);
		
		newGUI.newGame();
		newGUI.blockedChessboard = true;
		newGUI.moves.setMoves(tempStr);
		newGUI.blockedChessboard = false;
		newGUI.chessboard.getView().render();
		// newGUI.chessboard.draw();
	}
	
	/**
	 * Method checking in with of line there is an error
	 * 
	 * @param br
	 *            BufferedReader class object to operate on
	 * @param srcStr
	 *            String class object with text which variable you want to get
	 *            in file
	 * @return String with searched variable in file (whole line)
	 * @throws ReadGameError
	 *             class object when something goes wrong when reading file
	 * @throws IOException
	 */
	static public String getLineWithVar(BufferedReader br, String srcStr) throws ReadGameError, IOException
	{
		String str = new String();
		while((str = br.readLine()) != null)
		{
			if(str.startsWith(srcStr))
			{
				return str;
			}
		}
		throw new ReadGameError();
	}
	
	/**
	 * Method to get value from loaded txt line
	 * 
	 * @param line
	 *            Line which is readed
	 * @return result String with loaded value
	 * @throws ReadGameError
	 *             object class when something goes wrong
	 */
	static public String getValue(String line) throws ReadGameError
	{
		int from = line.indexOf("\"");
		int to = line.lastIndexOf("\"");
		int size = line.length() - 1;
		String result = new String();
		if(to < from || from > size || to > size || to < 0 || from < 0)
		{
			throw new ReadGameError();
		}
		try
		{
			result = line.substring(from + 1, to);
		} catch(java.lang.StringIndexOutOfBoundsException exc)
		{
			log.log(Level.WARNING, "Couldn't read line value.", exc);
			return "none";
		}
		return result;
	}
	
	/**
	 * Method to Start new game
	 *
	 */
	public void newGame()
	{
		chessboard.initialize();
		
		log.info("Starting new game of type " + settings.getGameType().name());
		
		if(chessboard.getActivePlayer().getType() != Player.Type.LOCAL)
		{
			this.blockedChessboard = true;
		}
		// dirty hacks starts over here :)
		// to fix rendering artefacts on first run
		Game activeGame = JChessApp.view.getActiveTabGame();
		if(activeGame != null && JChessApp.view.getNumberOfOpenedTabs() == 1)
		{
			// TODO: ugly af
			activeGame.chessboard.getView().resizeChessboard(((ChessboardView)activeGame.chessboard.getView()).getHeight(false));
			
			activeGame.chessboard.getView().render();
			activeGame.getView().render();
		}
		chessboard.getView().render();
		this.getView().render();
		// dirty hacks ends over here :)
	}
	
	/**
	 * Method to end game
	 * 
	 * @param message
	 *            what to show player(s) at end of the game (for example "draw",
	 *            "black wins" etc.)
	 */
	public void endGame(String message)
	{
		this.blockedChessboard = true;
		log.info(message);
		JOptionPane.showMessageDialog(null, message);
	}
	
	/**
	 * Method to swich active players after move
	 */
	public void switchActive()
	{
		chessboard.switchToNextPlayer();
		this.gameClock.switchClocks();
	}
	
	/**
	 * Method of getting accualy active player
	 * 
	 * @return player The player which have a move
	 */
	public Player getActivePlayer()
	{
		return chessboard.getActivePlayer();
	}
	
	/**
	 * Method to go to next move (checks if game is local/network etc.)
	 */
	public void nextMove()
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
		}
	}
	
	/**
	 * Method to simulate Move to check if it's correct etc. (usable for network
	 * game).
	 * 
	 * @param beginX
	 *            from which X (on chessboard) move starts
	 * @param beginY
	 *            from which Y (on chessboard) move starts
	 * @param endX
	 *            to which X (on chessboard) move go
	 * @param endY
	 *            to which Y (on chessboard) move go
	 */
	public boolean simulateMove(int beginX, int beginY, int endX, int endY)
	{
		try
		{
			chessboard.getView().select(chessboard.getBoard().getField(beginX, beginY));
			if(chessboard.getPossibleMoves(chessboard.getBoard().getPiece(chessboard.getView().getActiveSquare()), true)
			        .contains(chessboard.getBoard().getField(endX, endY)))
			{
				chessboard.move(chessboard.getBoard().getField(beginX, beginY),
				        chessboard.getBoard().getField(endX, endY));
			} else
			{
				log.log(Level.WARNING, "Bad move!");
				return false;
			}
			chessboard.getView().unselect();
			nextMove();
			
			return true;
			
		} catch(StringIndexOutOfBoundsException exc)
		{
			return false;
		} catch(ArrayIndexOutOfBoundsException exc)
		{
			return false;
		} catch(NullPointerException exc)
		{
			return false;
		} finally
		{
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, "ERROR");
		}
	}
	
	public boolean undo()
	{
		boolean status = false;
		
		if(this.settings.getGameType() == Settings.GameType.LOCAL)
		{
			status = chessboard.undo();
			if(status)
			{
				this.switchActive();
			} else
			{
				chessboard.getView().render();// repaint for sure
			}
		}
		return status;
	}
	
	public boolean rewindToBegin()
	{
		boolean result = false;
		
		if(this.settings.getGameType() == Settings.GameType.LOCAL)
		{
			while(chessboard.undo())
			{
				result = true;
			}
		} else
		{
			throw new UnsupportedOperationException(Localization.getMessage("operation_supported_only_in_local_game"));
		}
		
		return result;
	}
	
	public boolean rewindToEnd() throws UnsupportedOperationException
	{
		boolean result = false;
		
		if(this.settings.getGameType() == Settings.GameType.LOCAL)
		{
			while(chessboard.redo())
			{
				result = true;
			}
		} else
		{
			throw new UnsupportedOperationException(Localization.getMessage("operation_supported_only_in_local_game"));
		}
		
		return result;
	}
	
	public boolean redo()
	{
		boolean status = chessboard.redo();
		if(this.settings.getGameType() == Settings.GameType.LOCAL)
		{
			if(status)
			{
				this.nextMove();
			} else
			{
				chessboard.getView().render();// repaint for sure
			}
		} else
		{
			throw new UnsupportedOperationException(Localization.getMessage("operation_supported_only_in_local_game"));
		}
		return status;
	}
}

class ReadGameError extends Exception
{
	private static final long serialVersionUID = -7273990297881723999L;
}