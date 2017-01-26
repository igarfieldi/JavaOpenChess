package jchess.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.game.IGameBuilder;
import jchess.gamelogic.game.IGameBuilderFactory;
import jchess.gamelogic.views.IMessageDisplay.Option;
import jchess.gui.secondary.about.JChessAboutWindow;
import jchess.gui.secondary.setup.NewGameWindow;
import jchess.gui.secondary.themechooser.ThemeChooseWindow;
import jchess.util.FileMapParser;
import jchess.util.TypedResourceBundle;

public class JChessMenuBar extends JMenuBar implements ActionListener
{
	private static Logger log = Logger.getLogger(JChessMenuBar.class.getName());
	private static final long serialVersionUID = -7586077317133311767L;
	
	private static final TypedResourceBundle VIEW_PROPERTIES = new TypedResourceBundle("jchess.resources.JChessView");
	
	private JMenuItem loadGameItem;
	private JMenuItem newGameItem;
	private JMenuItem saveGameItem;
	
	private JFrame mainFrame;
	private IGameBuilderFactory gameBuilderFactory;
	private JTabbedPane gamesPane;
	
	public JChessMenuBar(JTabbedPane gamesPane, IGameBuilderFactory factory)
	{
		super();
		
		this.mainFrame = JChessApp.getApplication().getMainFrame();
		this.gamesPane = gamesPane;
		this.gameBuilderFactory = factory;
		
		initializeMenuBarElements();
		createMenuBar();
	}
	
	private void initializeMenuBarElements()
	{
		newGameItem = new JMenuItem();
		loadGameItem = new JMenuItem();
		saveGameItem = new JMenuItem();
	}
	
	private void createMenuBar()
	{
		addFileMenu();
		
		addMenu("optionsMenu", "Theme");
		addMenu("helpMenu", "About");
	}
	
	private void addFileMenu()
	{
		JMenu fileMenu = createMenu("fileMenu");
		
		addMenuItem(fileMenu, newGameItem, "newGame");
		addMenuItem(fileMenu, loadGameItem, "loadGame");
		addMenuItem(fileMenu, saveGameItem, "saveGame");
		addMenuItem(fileMenu, "quit");
	}
	
	private JMenu createMenu(String label)
	{
		JMenu menu = new JMenu();
		menu.setText(VIEW_PROPERTIES.getString(label + ".text"));
		this.add(menu);
		
		return menu;
	}

	private void addMenuItem(JMenu menuParent, JMenuItem menuItem, String menuItemName)
	{
		menuItem.setText(VIEW_PROPERTIES.getString(menuItemName + "Item.text"));
		menuItem.addActionListener(this);
		menuParent.add(menuItem);
	}
	
	private void addMenu(String name, String menuItemName)
	{
		JMenu optionsMenu = createMenu(name);
		addMenuItem(optionsMenu, menuItemName);
	}
	
	private void addMenuItem(JMenu menuParent, String action)
	{
		ActionMap actionMap = Application.getInstance(JChessApp.class).getContext()
		        .getActionMap(JChessMenuBar.class, this);
		
		JMenuItem menuItem = new JMenuItem();
		menuItem.setAction(actionMap.get(action));
		menuParent.add(menuItem);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object target = event.getSource();
		if(target == this.newGameItem)
		{
			JChessApp.getApplication().show(new NewGameWindow(mainFrame));
		}
		else if(target == this.saveGameItem)
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
						if(tempGUI.getView().showConfirmMessage("file_exists", "") != Option.YES) {
							continue;
						}
					}
					if(selectedFile.canWrite()) {
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
					} else {
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
		else if(target == this.loadGameItem)
		{
			loadGame();
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

	private void loadGame()
	{
		JFileChooser fileChooser = new JFileChooser();
		int retVal = fileChooser.showOpenDialog(this.gamesPane);
		if(retVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			if(file.exists() && file.canRead()) {
				FileMapParser parser = new FileMapParser();
				try
				{
					parser.load(file);
					IGame newGame = null;
					String gameType = parser.getProperty("Event");
					
					// Depending on the game we start a new one
					switch(gameType) {
						case "Game":
							IGameBuilder builder = gameBuilderFactory.getBuilder();

							Player white = new Player(parser.getProperty("WHITE"), Color.WHITE);
							Player black = new Player(parser.getProperty("BLACK"), Color.BLACK);
							builder.addPlayer(white);
							builder.addPlayer(black);
							
							newGame = builder.create();
							break;
						default:
							log.log(Level.SEVERE, "Unknown game type!");
							return ;
					}
					
					newGame.load(parser);
					
				} catch(IOException exc)
				{
					log.log(Level.SEVERE, "Failed to load saved game!", exc);
					JOptionPane.showMessageDialog(JChessApp.getApplication().getMainFrame(),
					        Localization.getMessage("error_reading_file"));
				}
			}
		}
	}
	
	@Action
	public void About()
	{
		JChessApp.getApplication().show(new JChessAboutWindow(mainFrame));
	}
	
	@Action
	public void Theme()
	{
		try
		{
			JChessApp.getApplication().show(new ThemeChooseWindow(mainFrame));
		}
		catch(Exception exception)
		{
			JOptionPane.showMessageDialog(JChessApp.getApplication().getMainFrame(), exception.getMessage());
			log.log(Level.SEVERE, "Something wrong creating window - perhaps themeList is null", exception);
		}
	}
}
