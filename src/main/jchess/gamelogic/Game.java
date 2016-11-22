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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.clock.GameClock;
import jchess.gamelogic.field.ChessboardController;
import jchess.gamelogic.field.ChessboardView;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Moves;
import jchess.gamelogic.pieces.King;
import jchess.gui.secondary.Chat;
import jchess.network.Client;

/**
 * Class responsible for the starts of new games, loading games, saving it, and
 * for ending it. This class is also responsible for appoing player with have a
 * move at the moment
 */
public class Game extends JPanel implements MouseListener, ComponentListener
{
	private static final long serialVersionUID = -1534339244756885176L;
	private static Logger log = Logger.getLogger(Game.class.getName());
	
	private Settings settings;
	private boolean blockedChessboard;
	private ChessboardController chessboard;
	private Player activePlayer;
	private GameClock gameClock;
	private Client client;
	private Moves moves;
	private Chat chat;
	
	public Game()
	{
		this.setLayout(null);
		this.moves = new Moves(this);
		settings = new Settings();
		chessboard = new ChessboardController(this.settings, this.moves);
		chessboard.getView().setVisible(true);
		chessboard.getView().setSize(ChessboardView.IMG_HEIGHT, ChessboardView.IMG_WIDTH);
		chessboard.getView().addMouseListener(this);
		chessboard.getView().setLocation(new Point(0, 0));
		this.add(chessboard.getView());
		// this.chessboard.
		gameClock = new GameClock(this);
		gameClock.setSize(new Dimension(200, 100));
		gameClock.setLocation(new Point(500, 0));
		this.add(gameClock);
		
		JScrollPane movesHistory = this.moves.getScrollPane();
		movesHistory.setSize(new Dimension(180, 350));
		movesHistory.setLocation(new Point(500, 121));
		this.add(movesHistory);
		
		this.chat = new Chat();
		this.chat.setSize(new Dimension(380, 100));
		this.chat.setLocation(new Point(0, 500));
		this.chat.setMinimumSize(new Dimension(400, 100));
		
		this.blockedChessboard = false;
		this.setLayout(null);
		this.addComponentListener(this);
		this.setDoubleBuffered(true);
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
	
	public Chat getChat()
	{
		return chat;
	}
	
	public Moves getMoves()
	{
		return moves;
	}
	
	public void setSettings(Settings settings)
	{
		this.settings = settings;
	}
	
	public void setClient(Client client)
	{
		this.client = client;
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
			JOptionPane.showMessageDialog(this, Localization.getMessage("error_writing_to_file") + ": " + exc);
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
			JOptionPane.showMessageDialog(this, Localization.getMessage("error_writing_to_file") + ": " + exc);
			return;
		}
		JOptionPane.showMessageDialog(this, Localization.getMessage("game_saved_properly"));
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
		newGUI.chessboard.getView().repaint();
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
		chessboard.setPieces("", settings.getWhitePlayer(), settings.getBlackPlayer());
		
		log.info("Starting new game of type " + settings.getGameType().name());
		
		activePlayer = settings.getWhitePlayer();
		if(activePlayer.getType() != Player.Type.LOCAL)
		{
			this.blockedChessboard = true;
		}
		// dirty hacks starts over here :)
		// to fix rendering artefacts on first run
		Game activeGame = JChessApp.view.getActiveTabGame();
        if(activeGame != null && JChessApp.view.getNumberOfOpenedTabs() == 1)
		{
			activeGame.chessboard.getView().resizeChessboard(activeGame.chessboard.getView().get_height(false));
			activeGame.chessboard.getView().repaint();
			activeGame.repaint();
		}
		chessboard.getView().repaint();
		this.repaint();
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
		if(activePlayer == settings.getWhitePlayer())
		{
			activePlayer = settings.getBlackPlayer();
		} else
		{
			activePlayer = settings.getWhitePlayer();
		}
		
		this.gameClock.switchClocks();
	}
	
	/**
	 * Method of getting accualy active player
	 * 
	 * @return player The player which have a move
	 */
	public Player getActivePlayer()
	{
		return this.activePlayer;
	}
	
	/**
	 * Method to go to next move (checks if game is local/network etc.)
	 */
	public void nextMove()
	{
		switchActive();
		
		log.log(Level.FINE, "Next move: active player: " + activePlayer.getName() + " | color: "
		        + activePlayer.getColor().name() + " | type: " + activePlayer.getType().name());
		if(activePlayer.getType() == Player.Type.LOCAL)
		{
			this.blockedChessboard = false;
		} else if(activePlayer.getType() == Player.Type.NETWORK)
		{
			this.blockedChessboard = true;
		} else if(activePlayer.getType() == Player.Type.COMPUTER)
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
			if(chessboard.getView().getActiveSquare().getPiece().possibleMoves().indexOf(chessboard.getBoard().getField(endX, endY)) != -1) // move
			{
				chessboard.move(chessboard.getBoard().getField(beginX, beginY), chessboard.getBoard().getField(endX, endY));
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
	
	// MouseListener:
	public void mouseClicked(MouseEvent arg0)
	{
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
				chessboard.getView().repaint();// repaint for sure
			}
		} else if(this.settings.getGameType() == Settings.GameType.NETWORK)
		{
			this.client.sendUndoAsk();
			status = true;
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
				chessboard.getView().repaint();// repaint for sure
			}
		} else
		{
			throw new UnsupportedOperationException(Localization.getMessage("operation_supported_only_in_local_game"));
		}
		return status;
	}
	
	public void mousePressed(MouseEvent event)
	{
		if(event.getButton() == MouseEvent.BUTTON3) // right button
		{
			this.undo();
		} else if(event.getButton() == MouseEvent.BUTTON2 && settings.getGameType() == Settings.GameType.LOCAL)
		{
			this.redo();
		} else if(event.getButton() == MouseEvent.BUTTON1) // left button
		{
			
			if(!blockedChessboard)
			{
				try
				{
					int x = event.getX();// get X position of mouse
					int y = event.getY();// get Y position of mouse
					
					Field sq = chessboard.getView().getSquare(x, y);
					if((sq == null && chessboard.getView().getActiveSquare() == null)
					        || (this.chessboard.getView().getActiveSquare() == null && sq.getPiece() != null
					                && sq.getPiece().getPlayer() != this.activePlayer))
					{
						return;
					}
					
					if(sq.getPiece() != null && sq.getPiece().getPlayer() == this.activePlayer
					        && sq != chessboard.getView().getActiveSquare())
					{
						chessboard.getView().unselect();
						chessboard.getView().select(sq);
					} else if(chessboard.getView().getActiveSquare() == sq) // unselect
					{
						chessboard.getView().unselect();
					} else if(chessboard.getView().getActiveSquare() != null && chessboard.getView().getActiveSquare().getPiece() != null
					        && chessboard.getView().getActiveSquare().getPiece().possibleMoves().indexOf(sq) != -1) // move
					{
						if(settings.getGameType() == Settings.GameType.LOCAL)
						{
							chessboard.move(chessboard.getView().getActiveSquare(), sq);
						} else if(settings.getGameType() == Settings.GameType.NETWORK)
						{
							client.sendMove(chessboard.getView().getActiveSquare().getPosX(),
							        chessboard.getView().getActiveSquare().getPosY(), sq.getPosX(), sq.getPosY());
							chessboard.move(chessboard.getView().getActiveSquare(), sq);
						}
						
						chessboard.getView().unselect();
						
						// switch player
						this.nextMove();
						
						// checkmate or stalemate
						King king;
						if(this.activePlayer == settings.getWhitePlayer())
						{
							king = chessboard.getWhiteKing();
						} else
						{
							king = chessboard.getBlackKing();
						}
						
						switch(king.isCheckmatedOrStalemated())
						{
							case 1:
								this.endGame("Checkmate! " + king.getPlayer().getColor().toString() + " player lose!");
								break;
							case 2:
								this.endGame("Stalemate! Draw!");
								break;
						}
					}
					
				} catch(NullPointerException exc)
				{
					// TODO: how could there be a NullPointerException here?
					log.log(Level.SEVERE, "Encountered exception while determining click position!",
							exc);
					chessboard.getView().repaint();
					return;
				}
			} else if(blockedChessboard)
			{
				log.info("Chessboard is blocked");
			}
		}
		chessboard.getView().repaint();
	}
	
	public void mouseReleased(MouseEvent arg0)
	{
	}
	
	public void mouseEntered(MouseEvent arg0)
	{
	}
	
	public void mouseExited(MouseEvent arg0)
	{
	}
	
	public void componentResized(ComponentEvent e)
	{
		int height = this.getHeight() >= this.getWidth() ? this.getWidth() : this.getHeight();
		int chess_height = (int) Math.round((height * 0.8) / 8) * 8;
		this.chessboard.getView().resizeChessboard((int) chess_height);
		chess_height = this.chessboard.getView().getHeight();
		this.moves.getScrollPane().setLocation(new Point(chess_height + 5, 100));
		this.moves.getScrollPane().setSize(this.moves.getScrollPane().getWidth(), chess_height - 100);
		this.gameClock.setLocation(new Point(chess_height + 5, 0));
		if(this.chat != null)
		{
			this.chat.setLocation(new Point(0, chess_height + 5));
			this.chat.setSize(new Dimension(chess_height, this.getHeight() - (chess_height + 5)));
		}
	}
	
	public void componentMoved(ComponentEvent e)
	{
	}
	
	public void componentShown(ComponentEvent e)
	{
	}
	
	public void componentHidden(ComponentEvent e)
	{
	}
}

class ReadGameError extends Exception
{
	private static final long serialVersionUID = -7273990297881723999L;
}