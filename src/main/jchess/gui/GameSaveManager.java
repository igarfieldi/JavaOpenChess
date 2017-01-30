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
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.views.IMessageDisplay.Option;
import jchess.gui.secondary.setup.SettingsAdopter;
import jchess.util.FileMapParser;

/**
 * This class reads and writes the save files so that games can be loaded or saved.
 */
public class GameSaveManager
{
	private static Logger log = Logger.getLogger(GameSaveManager.class.getName());
	
	private JTabbedPane gamesPane;
	JFileChooser fileChooser;
	
	public GameSaveManager(JTabbedPane gamesPane)
	{
		this.gamesPane = gamesPane;
		this.fileChooser = new JFileChooser();
	}
	
	/**
	 * Saves the currently active game to a location on the disk which is specified
	 * by the player.
	 */
	public void saveGame()
	{
		if(this.gamesPane.getTabCount() == 0)
		{
			JOptionPane.showMessageDialog(null, Localization.getMessage("save_not_called_for_tab"));
		}
		else
		{
			IGame activeTab = JChessApp.view.getActiveGame();
			FileMapParser parser = new FileMapParser();
			
			if(activeTab.save(parser)) {
    			int chosenOption = fileChooser.showSaveDialog(this.gamesPane);
    			if(chosenOption == JFileChooser.APPROVE_OPTION)
    			{
    				File selectedFile = fileChooser.getSelectedFile();
    				if(!selectedFile.exists() || (activeTab.getView().showConfirmMessage("file_exists", "") == Option.YES))
    				{
    					createSaveFile(selectedFile);
    					writeSaveFile(parser, selectedFile, activeTab);
    					log.info(Boolean.toString(fileChooser.getSelectedFile().isFile()));
    				}
    			}
			}
		}
	}
	
	/**
	 * Creates a new save file.
	 * 
	 * @param selectedFile
	 * 				The file to be created.
	 */
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
	
	/**
	 * Writes the game information to the save file.
	 * 
	 * @param selectedFile
	 * 				The file to be written.
	 * @param activeGameTab
	 * 				The game whose info should be written to the file.
	 */
	private void writeSaveFile(FileMapParser parser, File selectedFile, IGame activeGameTab)
	{
		if(selectedFile.canWrite())
		{
			try
			{
				parser.save(selectedFile);
    			activeGameTab.getView().showMessage("game_saved_properly", "");
			}
			catch(IOException exc)
			{
				log.log(Level.SEVERE, "Failed to save game!", exc);
				// TODO: add message key for individual message
				activeGameTab.getView().showMessage("error_writing_to_file", selectedFile.getName());
			}
		}
		else
			activeGameTab.getView().showMessage("error_writing_to_file", selectedFile.getName());
	}
	
	/**
	 * Loads a save file that the player specifies.
	 */
	public void loadGame()
	{
		int chosenOption = fileChooser.showOpenDialog(this.gamesPane);
		if(chosenOption == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			if(file.exists() && file.canRead())
			{
				try
				{
					readSaveFile(file);
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
	
	/**
	 * Reads the game info of the save file and passes it to the settings adopter
	 * to create a new game
	 * 
	 * @param file
	 * 				The file to be read.
	 * @throws IOException
	 */
	private void readSaveFile(File file) throws IOException
	{
		FileMapParser parser = new FileMapParser();
		parser.load(file);
		String gameType = parser.getProperty("Event");

		SettingsAdopter settingsAdopter = new SettingsAdopter();
		String playerNames[] = null;
		
		// Depending on the game we start a new one
		if(gameType.contains("2p")) {
			playerNames = new String[] {
					parser.getProperty("WHITE"),
					parser.getProperty("BLACK")
			};
		} else if(gameType.contains("4p")) {
			playerNames = new String[] {
					parser.getProperty("WHITE"),
					parser.getProperty("RED"),
					parser.getProperty("BLACK"),
					parser.getProperty("GOLDEN")
			};
		} else {
			JOptionPane.showMessageDialog(JChessApp.getApplication().getMainFrame(),
			        Localization.getMessage("unknown_game_type"));
			log.log(Level.SEVERE, "Unknown game type!");
			return ;
		}
		
		if(gameType.contains("Timed")) {
			JOptionPane.showMessageDialog(JChessApp.getApplication().getMainFrame(),
			        Localization.getMessage("unloadable_game_type"));
		} else if(gameType.contains("Ai")) {
			JOptionPane.showMessageDialog(JChessApp.getApplication().getMainFrame(),
			        Localization.getMessage("unloadable_game_type"));
		} else {
			settingsAdopter.createLoadedGameWindow(parser, playerNames);
		}
	}
}
