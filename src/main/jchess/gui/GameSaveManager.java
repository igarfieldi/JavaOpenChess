package jchess.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.game.IGameBuilder;
import jchess.gamelogic.game.IGameBuilderFactory;
import jchess.gamelogic.views.IMessageDisplay.Option;
import jchess.util.FileMapParser;

public class GameSaveManager
{
	private static Logger log = Logger.getLogger(GameSaveManager.class.getName());
	
	private IGameBuilderFactory gameBuilderFactory;
	private JTabbedPane gamesPane;
	
	public GameSaveManager(JTabbedPane gamesPane, IGameBuilderFactory factory)
	{
		this.gamesPane = gamesPane;
		this.gameBuilderFactory = factory;
	}
	
	public void saveGame()
	{
		if(this.gamesPane.getTabCount() == 0)
		{
			JOptionPane.showMessageDialog(null, Localization.getMessage("save_not_called_for_tab"));
			return;
		}
		while(true)
		{// until
			JFileChooser fileChooser = new JFileChooser();
			int retVal = fileChooser.showSaveDialog(this.gamesPane);
			if(retVal == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = fileChooser.getSelectedFile();
				IGame tempGUI = JChessApp.getApplication().view.getActiveTabGame();
				if(!selectedFile.exists())
					createSaveFile(selectedFile);
				else if(selectedFile.exists())
				{
					if(tempGUI.getView().showConfirmMessage("file_exists", "") != Option.YES)
					{
						continue;
					}
				}
				if(selectedFile.canWrite())
				{
					try
					{
						FileMapParser parser = new FileMapParser();
						tempGUI.save(parser);
						parser.save(selectedFile);
						tempGUI.getView().showMessage("game_saved_properly", "");
						
						// TODO: save to disk
					} catch(IOException exc)
					{
						log.log(Level.SEVERE, "Failed to save game!", exc);
						// TODO: add message key for individual message
						tempGUI.getView().showMessage("error_writing_to_file", selectedFile.getName());
					}
				} else
				{
					tempGUI.getView().showMessage("error_writing_to_file", selectedFile.getName());
				}
				
				log.info(Boolean.toString(fileChooser.getSelectedFile().isFile()));
				break;
			}
			else if(retVal == JFileChooser.CANCEL_OPTION)
			{
				break;
			}
		}
	}
	
	private void createSaveFile(File selectedFile)
	{
		try
		{
			selectedFile.createNewFile();
		}
		catch(IOException exception)
		{
			log.log(Level.SEVERE, "error creating file: ", exception);
		}
	}
	
	public void loadGame()
	{
		JFileChooser fileChooser = new JFileChooser();
		int retVal = fileChooser.showOpenDialog(this.gamesPane);
		if(retVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			if(file.exists() && file.canRead())
			{
				try
				{
					parseSaveFile(file);
				}
				catch(IOException exc)
				{
					log.log(Level.SEVERE, "Failed to load saved game!", exc);
					JOptionPane.showMessageDialog(JChessApp.getApplication().getMainFrame(),
					        Localization.getMessage("error_reading_file"));
				}
			}
		}
	}

	private void parseSaveFile(File file) throws IOException
	{
		FileMapParser parser = new FileMapParser();
		parser.load(file);
		
		String gameType = parser.getProperty("Event");
		switch(gameType) // Depending on the game we start a new one
		{
			case "Game":
				IGame newGame = createGame(parser);
				newGame.load(parser);
				break;
			default:
				log.log(Level.SEVERE, "Unknown game type!");
		}
	}

	private IGame createGame(FileMapParser parser)
	{
		IGameBuilder builder = gameBuilderFactory.getBuilder();
		
		Player white = new Player(parser.getProperty("WHITE"), Color.WHITE);
		Player black = new Player(parser.getProperty("BLACK"), Color.BLACK);
		builder.addPlayer(white);
		builder.addPlayer(black);
		
		IGame newGame = builder.create();
		return newGame;
	}
}
