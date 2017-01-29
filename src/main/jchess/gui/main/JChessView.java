package jchess.gui.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.game.IGameBuilder;
import jchess.gamelogic.game.IGameBuilderFactory;
import jchess.gamelogic.views.IMessageDisplay.Option;
import jchess.gui.secondary.PawnPromotionWindow;
import jchess.gui.secondary.about.JChessAboutWindow;
import jchess.gui.secondary.setup.NewGameWindow;
import jchess.gui.secondary.themechooser.ThemeChooseWindow;
import jchess.util.FileMapParser;
import jchess.util.TypedResourceBundle;

/**
 * The application's main frame.
 */
public class JChessView extends FrameView implements ActionListener, ComponentListener
{
	private static Logger log = Logger.getLogger(JChessView.class.getName());
	private static final TypedResourceBundle VIEW_PROPERTIES = new TypedResourceBundle("jchess.resources.JChessView");
	
	private List<IGame> gameList = new ArrayList<IGame>(1);
	private IGameBuilderFactory gameBuilderFactory;
	
	private JMenu gameMenu;
	private JTabbedPane gamesPane;
	private JMenuItem loadGameItem;
	private JPanel mainPanel;
	private JMenuBar menuBar;
	private JMenuItem newGameItem;
	private JMenu optionsMenu;
	private JMenuItem saveGameItem;
	private JMenuItem themeSettingsMenu;
	
	private JDialog aboutBox;
	private PawnPromotionWindow pawnPromotionWindow;
	public JDialog newGameFrame;
	
	public JChessView(SingleFrameApplication app, IGameBuilderFactory factory)
	{
		super(app);
		this.gameBuilderFactory = factory;
		
		initializeComponents();
	}

	private void initializeComponents()
	{
		initializeGuiElements();
		initializeMainPanel();
		initializeMenuBar();
	}

	private void initializeGuiElements()
	{
		menuBar = new JMenuBar();
		newGameItem = new JMenuItem();
		loadGameItem = new JMenuItem();
		saveGameItem = new JMenuItem();
		gameMenu = new JMenu();
		optionsMenu = new JMenu();
		themeSettingsMenu = new JMenuItem();
	}

	private void initializeMainPanel()
	{
		mainPanel = new JPanel();
		gamesPane = new JChessTabbedPane();
		
		configureMainPanelSize();
		configureMainPanelLayout();
		setComponent(mainPanel);
	}

	private void configureMainPanelSize()
	{
		mainPanel.setMaximumSize(new Dimension(800, 600));
		mainPanel.setMinimumSize(new Dimension(800, 600));
		mainPanel.setName("mainPanel");
		mainPanel.setPreferredSize(new Dimension(800, 600));
	}

	private void configureMainPanelLayout()
	{
		gamesPane.setName("gamesPane");
		
		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(mainPanelLayout.createSequentialGroup().addContainerGap()
		                .addComponent(gamesPane, GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE).addContainerGap()));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addComponent(gamesPane,
		                GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)));
	}

	private void initializeMenuBar()
	{
		ActionMap actionMap = Application.getInstance(JChessApp.class).getContext()
		        .getActionMap(JChessView.class, this);
		
		menuBar.setName("menuBar"); // NOI18N
		
		addFileMenu(VIEW_PROPERTIES, actionMap);
		addGameMenu(VIEW_PROPERTIES);
		addOptionsMenu(VIEW_PROPERTIES);
		addHelpMenu(VIEW_PROPERTIES, actionMap);
		
		setMenuBar(menuBar);
	}

	private void addFileMenu(TypedResourceBundle resourceBundle, ActionMap actionMap)
	{
		JMenu fileMenu = new JMenu();
		JMenuItem exitMenuItem = new JMenuItem();
		
		fileMenu.setText(resourceBundle.getString("fileMenu.text")); // NOI18N
		fileMenu.setName("fileMenu"); // NOI18N
		
		setMenuItem(fileMenu, resourceBundle, newGameItem, KeyEvent.VK_N, "newGameItem");
		setMenuItem(fileMenu, resourceBundle, loadGameItem, KeyEvent.VK_L, "loadGameItem");
		setMenuItem(fileMenu, resourceBundle, saveGameItem, KeyEvent.VK_S, "saveGameItem");
		
		exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
		exitMenuItem.setName("exitMenuItem"); // NOI18N
		fileMenu.add(exitMenuItem);
		
		menuBar.add(fileMenu);
	}

	private void setMenuItem(JMenu fileMenu, TypedResourceBundle resourceBundle, JMenuItem menuItem, int hotkey, String menuItemName)
	{
		menuItem.setAccelerator(KeyStroke.getKeyStroke(hotkey, InputEvent.CTRL_MASK));
		menuItem.setText(resourceBundle.getString(menuItemName + ".text"));
		menuItem.setName(menuItemName);
		fileMenu.add(menuItem);
		menuItem.addActionListener(this);
	}

	private void addGameMenu(TypedResourceBundle resourceBundle)
	{
		gameMenu.setText(resourceBundle.getString("gameMenu.text")); // NOI18N
		gameMenu.setName("gameMenu"); // NOI18N
		
		menuBar.add(gameMenu);
	}

	private void addOptionsMenu(TypedResourceBundle resourceBundle)
	{
		optionsMenu.setText(resourceBundle.getString("optionsMenu.text")); // NOI18N
		optionsMenu.setName("optionsMenu"); // NOI18N
		
		themeSettingsMenu.setText(resourceBundle.getString("themeSettingsMenu.text")); // NOI18N
		themeSettingsMenu.setName("themeSettingsMenu"); // NOI18N
		themeSettingsMenu.addActionListener(this);
		optionsMenu.add(themeSettingsMenu);
		
		menuBar.add(optionsMenu);
	}

	private void addHelpMenu(TypedResourceBundle resourceBundle, ActionMap actionMap)
	{
		JMenu helpMenu = new JMenu();
		JMenuItem aboutMenuItem = new JMenuItem();
		
		helpMenu.setText(resourceBundle.getString("helpMenu.text")); // NOI18N
		helpMenu.setName("helpMenu"); // NOI18N
		
		aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
		aboutMenuItem.setName("aboutMenuItem"); // NOI18N
		helpMenu.add(aboutMenuItem);
		
		menuBar.add(helpMenu);
	}
	
	public void addNewGameTab(String title, IGame game) {
		this.gameList.add(game);
		this.gamesPane.addTab(title, (Component)game.getView());
	}
	
	public void actionPerformed(ActionEvent event)
	{
		Object target = event.getSource();
		if(target == newGameItem)
		{
			this.newGameFrame = new NewGameWindow(this.getFrame());
			JChessApp.getApplication().show(this.newGameFrame);
		}
		else if(target == saveGameItem)
		{ // saveGame
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
					IGame tempGUI = this.getActiveTabGame();
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
		else if(target == loadGameItem)
			loadGame();
		else if(target == this.themeSettingsMenu)
			showThemeChooseWindow();
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
					JOptionPane.showMessageDialog(this.getFrame(), Localization.getMessage("error_reading_file"));
				}
			}
		}
	}

	private void showThemeChooseWindow()
	{
		try
		{
			ThemeChooseWindow themeChooseWindow = new ThemeChooseWindow(this.getFrame());
			JChessApp.getApplication().show(themeChooseWindow);
		}
		catch(Exception exception)
		{
			JOptionPane.showMessageDialog(JChessApp.getApplication().getMainFrame(), exception.getMessage());
			log.log(Level.SEVERE, "Something wrong creating window - perhaps themeList is null", exception);
		}
	}

	@Action
	public void showAboutBox()
	{
		if(aboutBox == null)
		{
			JFrame mainFrame = JChessApp.getApplication().getMainFrame();
			aboutBox = new JChessAboutWindow(mainFrame);
			aboutBox.setLocationRelativeTo(mainFrame);
		}
		JChessApp.getApplication().show(aboutBox);
	}
	
	public String showPawnPromotionBox(Color color)
	{
		
		JFrame mainFrame = JChessApp.getApplication().getMainFrame();
		pawnPromotionWindow = new PawnPromotionWindow(mainFrame, color);
		pawnPromotionWindow.setLocationRelativeTo(mainFrame);
		pawnPromotionWindow.setModal(true);
		
		JChessApp.getApplication().show(pawnPromotionWindow);
		
		return pawnPromotionWindow.getSelectedPromotion();
	}
	
	public String showSaveWindow()
	{
		return "";
	}
	
	public void setGameBuilderFactory(IGameBuilderFactory factory) {
		this.gameBuilderFactory = factory;
	}
	
	public void componentResized(ComponentEvent event)
	{
		log.log(Level.SEVERE, "jchessView resized!!;");
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public IGame getActiveTabGame() throws ArrayIndexOutOfBoundsException
	{
		return this.gameList.get(this.gamesPane.getSelectedIndex());
	}
	
	public int getNumberOfOpenedTabs()
	{
		return this.gamesPane.getTabCount();
	}
	
	public void componentMoved(ComponentEvent event)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void componentShown(ComponentEvent event)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void componentHidden(ComponentEvent event)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
