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

package jchess.gui.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.TaskMonitor;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Game;
import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.Settings;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.FourPlayerChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.models.chessboardfactories.FourPlayerChessboardFactory;
import jchess.gamelogic.models.chessboardfactories.TwoPlayerChessboardFactory;
import jchess.gamelogic.views.IChessboardView;
import jchess.gamelogic.views.IMessageDisplay.Option;
import jchess.gamelogic.views.chessboardviews.FourPlayerChessboardView;
import jchess.gamelogic.views.chessboardviews.TwoPlayerChessboardView;
import jchess.gamelogic.views.gameviews.SwingGameView;
import jchess.gui.secondary.JChessAboutBox;
import jchess.gui.secondary.PawnPromotionWindow;
import jchess.gui.secondary.ThemeChooseWindow;
import jchess.gui.setup.NewGameWindow;
import jchess.util.TypedResourceBundle;

/**
 * The application's main frame.
 */
public class JChessView extends FrameView implements ActionListener, ComponentListener
{
	private static Logger log = Logger.getLogger(JChessView.class.getName());
	private static final TypedResourceBundle VIEW_PROPERTIES = new TypedResourceBundle("jchess.resources.JChessView");
	
	private List<Game> gameList = new ArrayList<Game>(1);
	
	private JMenu gameMenu;
	private JTabbedPane gamesPane;
	private JMenuItem loadGameItem;
	private JPanel mainPanel;
	private JMenuBar menuBar;
	private JMenuItem newGameItem;
	private JMenu optionsMenu;
	private JProgressBar progressBar;
	private JMenuItem saveGameItem;
	private JLabel statusAnimationLabel;
	private JLabel statusMessageLabel;
	private JPanel statusPanel;
	private JMenuItem themeSettingsMenu;
	
	private Timer messageTimer;
	private Timer busyIconTimer;
	private Icon idleIcon;
	private final Icon[] busyIcons = new Icon[15];
	private int busyIconIndex = 0;
	
	private JDialog aboutBox;
	private PawnPromotionWindow pawnPromotionBox;
	public JDialog newGameFrame;
	
	public JChessView(SingleFrameApplication app)
	{
		super(app);
		
		initializeComponents();
		
		// status bar initialization - message timeout, idle icon and busy
		// animation, etc
		initializeTimers(VIEW_PROPERTIES);
		initializeIcons(VIEW_PROPERTIES);
		
		// connecting action tasks to status bar via TaskMonitor
		initializeTaskMonitor();
	}

	private void initializeComponents()
	{
		initializeGuiElements();
		initializeMainPanel();
		initializeMenuBar();
		initializeStatusPanel();
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
		statusPanel = new JPanel();
		statusMessageLabel = new JLabel();
		statusAnimationLabel = new JLabel();
		progressBar = new JProgressBar();
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

	private void initializeStatusPanel()
	{
		JSeparator statusPanelSeparator = new JSeparator();
		
		setStatusGuiElementNames(statusPanelSeparator);
		
		GroupLayout statusPanelLayout = new GroupLayout(statusPanel);
		statusPanel.setLayout(statusPanelLayout);
		setHorizontalStatusPanelLayout(statusPanelSeparator, statusPanelLayout);
		setVerticalStatusPanelLayout(statusPanelSeparator, statusPanelLayout);
		
		setStatusBar(statusPanel);
	}

	private void setStatusGuiElementNames(JSeparator statusPanelSeparator)
	{
		statusPanel.setName("statusPanel"); // NOI18N
		
		statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N
		
		statusMessageLabel.setName("statusMessageLabel"); // NOI18N
		
		statusAnimationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N
		
		progressBar.setName("progressBar");
	}

	private void setHorizontalStatusPanelLayout(JSeparator statusPanelSeparator, GroupLayout statusPanelLayout)
	{
		statusPanelLayout.setHorizontalGroup(statusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addComponent(statusPanelSeparator, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
		        .addGroup(statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(statusMessageLabel)
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 616, Short.MAX_VALUE)
		                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
		                        GroupLayout.PREFERRED_SIZE)
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(statusAnimationLabel)
		                .addContainerGap()));
	}

	private void setVerticalStatusPanelLayout(JSeparator statusPanelSeparator, GroupLayout statusPanelLayout)
	{
		statusPanelLayout.setVerticalGroup(statusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(statusPanelLayout.createSequentialGroup()
		                .addComponent(statusPanelSeparator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
		                        Short.MAX_VALUE)
		                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                        .addComponent(statusMessageLabel).addComponent(statusAnimationLabel).addComponent(
		                                progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
		                                GroupLayout.PREFERRED_SIZE))
		                .addGap(3, 3, 3)));
	}

	private void initializeTimers(TypedResourceBundle resourceBundle)
	{
		int messageTimeout = resourceBundle.getInteger("StatusBar.messageTimeout");
		messageTimer = setMessageTimer(messageTimeout);
		messageTimer.setRepeats(false);
		
		int busyAnimationRate = resourceBundle.getInteger("StatusBar.busyAnimationRate");
		for(int i = 0; i < busyIcons.length; i++)
			busyIcons[i] = resourceBundle.getIcon("StatusBar.busyIcons[" + i + "]");
		busyIconTimer = setBusyIconTimer(busyAnimationRate);
	}

	private Timer setMessageTimer(int messageTimeout)
	{
		return new Timer(messageTimeout, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				statusMessageLabel.setText("");
			}
		});
	}

	private Timer setBusyIconTimer(int busyAnimationRate)
	{
		return new Timer(busyAnimationRate, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
				statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
			}
		});
	}

	private void initializeIcons(TypedResourceBundle resourceBundle)
	{
		idleIcon = resourceBundle.getIcon("StatusBar.idleIcon");
		statusAnimationLabel.setIcon(idleIcon);
		progressBar.setVisible(false);
	}

	private void initializeTaskMonitor()
	{
		TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
		taskMonitor.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent event)
			{
				String propertyName = event.getPropertyName();
				if("started".equals(propertyName))
					startProgressTracking();
				else if("done".equals(propertyName))
					stopProgressTracking();
				else if("message".equals(propertyName))
					receiveMessage(event);
				else if("progress".equals(propertyName))
					updateProgress(event);
			}

			private void startProgressTracking()
			{
				if(!busyIconTimer.isRunning())
				{
					statusAnimationLabel.setIcon(busyIcons[0]);
					busyIconIndex = 0;
					busyIconTimer.start();
				}
				progressBar.setVisible(true);
				progressBar.setIndeterminate(true);
			}

			private void stopProgressTracking()
			{
				busyIconTimer.stop();
				statusAnimationLabel.setIcon(idleIcon);
				progressBar.setVisible(false);
				progressBar.setValue(0);
			}

			private void receiveMessage(PropertyChangeEvent event)
			{
				String text = (String) (event.getNewValue());
				statusMessageLabel.setText((text == null) ? "" : text);
				messageTimer.restart();
			}

			private void updateProgress(PropertyChangeEvent event)
			{
				int value = (Integer) (event.getNewValue());
				progressBar.setVisible(true);
				progressBar.setIndeterminate(false);
				progressBar.setValue(value);
			}
		});
	}

	public Game addNewTwoPlayerTab(String p1, String p2)
	{
		Settings settings = new Settings();
		IChessboardView view = new TwoPlayerChessboardView(true, false);
		IChessboardController chessboard = new TwoPlayerChessboardController(view,
				TwoPlayerChessboardFactory.getInstance(), new Player(p1, Player.Color.WHITE),
				new Player(p2, Player.Color.BLACK));
		Game newGameTab = new Game(settings, chessboard, view);
		this.gameList.add(newGameTab);
		
		String title = p1 + " vs " + p2;
		this.gamesPane.addTab(title, (SwingGameView)newGameTab.getView());
		return newGameTab;
	}

	public Game addNewFourPlayerTab(String p1, String p2, String p3, String p4)
	{
		Settings settings = new Settings();
		IChessboardView view = new FourPlayerChessboardView(true, false);
		IChessboardController chessboard = new FourPlayerChessboardController(view,
				FourPlayerChessboardFactory.getInstance(), new Player(p1, Player.Color.WHITE),
				new Player(p2, Player.Color.RED),
				new Player(p3, Player.Color.BLACK),
				new Player(p4, Player.Color.GOLDEN));
		Game newGameTab = new Game(settings, chessboard, view);
		this.gameList.add(newGameTab);
		
		String title = p1 + " vs " + p2 + " vs " + p3 + " vs " + p4;
		this.gamesPane.addTab(title, (SwingGameView)newGameTab.getView());
		return newGameTab;
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
					Game tempGUI = this.getActiveTabGame();
					if(!selectedFile.exists())
						createSaveFile(selectedFile);
					else if(selectedFile.exists())
					{
						if(tempGUI.getView().showConfirmMessage("file_exists", "") != Option.YES) {
							continue;
						}
					}
					if(selectedFile.canWrite())
						tempGUI.saveGame(selectedFile);
					
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
			if(file.exists() && file.canRead())
				Game.loadGame(file);
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
			aboutBox = new JChessAboutBox(mainFrame);
			aboutBox.setLocationRelativeTo(mainFrame);
		}
		JChessApp.getApplication().show(aboutBox);
	}
	
	public String showPawnPromotionBox(Color color)
	{
		
		JFrame mainFrame = JChessApp.getApplication().getMainFrame();
		pawnPromotionBox = new PawnPromotionWindow(mainFrame, color);
		pawnPromotionBox.setLocationRelativeTo(mainFrame);
		pawnPromotionBox.setModal(true);
		
		JChessApp.getApplication().show(pawnPromotionBox);
		
		return pawnPromotionBox.getSelectedPromotion();
	}
	
	public String showSaveWindow()
	{
		return "";
	}
	
	public void componentResized(ComponentEvent event)
	{
		log.log(Level.SEVERE, "jchessView resized!!;");
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public Game getActiveTabGame() throws ArrayIndexOutOfBoundsException
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
