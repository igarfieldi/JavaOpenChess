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

package jchess;

import org.jdesktop.application.*;
import org.jdesktop.application.Action;

import jchess.gamelogic.Game;
import jchess.gui.main.JChessTabbedPane;
import jchess.gui.GUI;
import jchess.gui.secondary.JChessAboutBox;
import jchess.gui.secondary.PawnPromotionWindow;
import jchess.gui.secondary.ThemeChooseWindow;
import jchess.gui.setup.NewGameWindow;

import java.awt.*;
import java.beans.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The application's main frame.
 */
public class JChessView extends FrameView implements ActionListener, ComponentListener
{
	private static Logger log = Logger.getLogger(JChessView.class.getName());
	
	private static GUI gui = null;
	
	private JMenu gameMenu;
	private JTabbedPane gamesPane;
	private JMenuItem loadGameItem;
	private JPanel mainPanel;
	private JMenuBar menuBar;
	private JMenuItem moveBackItem;
	private JMenuItem moveForwardItem;
	private JMenuItem newGameItem;
	private JMenu optionsMenu;
	private JProgressBar progressBar;
	private JMenuItem rewindToBeginItem;
	private JMenuItem rewindToEndItem;
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
		ResourceMap resourceMap = getResourceMap();
		initializeTimers(resourceMap);
		initializeIcons(resourceMap);
		
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
		moveBackItem = new JMenuItem();
		moveForwardItem = new JMenuItem();
		rewindToBeginItem = new JMenuItem();
		rewindToEndItem = new JMenuItem();
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
		ResourceMap resourceMap = Application.getInstance(JChessApp.class).getContext()
		        .getResourceMap(JChessView.class);
		ActionMap actionMap = Application.getInstance(JChessApp.class).getContext()
		        .getActionMap(JChessView.class, this);
		
		menuBar.setName("menuBar"); // NOI18N
		
		addFileMenu(resourceMap, actionMap);
		addGameMenu(resourceMap);
		addOptionsMenu(resourceMap);
		addHelpMenu(resourceMap, actionMap);
		
		setMenuBar(menuBar);
	}

	private void addFileMenu(ResourceMap resourceMap, ActionMap actionMap)
	{
		JMenu fileMenu = new JMenu();
		JMenuItem exitMenuItem = new JMenuItem();
		
		fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
		fileMenu.setName("fileMenu"); // NOI18N
		
		setMenuItem(fileMenu, resourceMap, newGameItem, KeyEvent.VK_N, "newGameItem");
		setMenuItem(fileMenu, resourceMap, loadGameItem, KeyEvent.VK_L, "loadGameItem");
		setMenuItem(fileMenu, resourceMap, saveGameItem, KeyEvent.VK_S, "saveGameItem");
		
		exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
		exitMenuItem.setName("exitMenuItem"); // NOI18N
		fileMenu.add(exitMenuItem);
		
		menuBar.add(fileMenu);
	}

	private void setMenuItem(JMenu fileMenu, ResourceMap resourceMap, JMenuItem menuItem, int hotkey, String menuItemName)
	{
		menuItem.setAccelerator(KeyStroke.getKeyStroke(hotkey, InputEvent.CTRL_MASK));
		menuItem.setText(resourceMap.getString(menuItemName + ".text"));
		menuItem.setName(menuItemName);
		fileMenu.add(menuItem);
		menuItem.addActionListener(this);
	}

	private void addGameMenu(ResourceMap resourceMap)
	{
		gameMenu.setText(resourceMap.getString("gameMenu.text")); // NOI18N
		gameMenu.setName("gameMenu"); // NOI18N
		
		setMoveBackItem(resourceMap);
		setMoveForwardItem(resourceMap);
		setRewindToBeginItem(resourceMap);
		setRewindToEndItem(resourceMap);
		
		menuBar.add(gameMenu);
	}

	private void setMoveBackItem(ResourceMap resourceMap)
	{
		moveBackItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		moveBackItem.setText(resourceMap.getString("moveBackItem.text"));
		moveBackItem.setName("moveBackItem"); 
		moveBackItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				moveBackItemActionPerformed(event);
			}
		});
		gameMenu.add(moveBackItem);
	}

	private void moveBackItemActionPerformed(ActionEvent event)
	{
		if(gui != null && gui.getGame() != null)
			gui.getGame().undo();
		else
			try
			{
				Game activeGame = this.getActiveTabGame();
				if(!activeGame.undo())
					JOptionPane.showMessageDialog(null, "Cannot undo!");
			}
			catch(ArrayIndexOutOfBoundsException exception)
			{
				JOptionPane.showMessageDialog(null, "No active tabs!");
			}
			catch(UnsupportedOperationException exception)
			{
				JOptionPane.showMessageDialog(null, exception.getMessage());
			}
	}

	private void setMoveForwardItem(ResourceMap resourceMap)
	{
		moveForwardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		moveForwardItem.setText(resourceMap.getString("moveForwardItem.text")); // NOI18N
		moveForwardItem.setName("moveForwardItem"); // NOI18N
		moveForwardItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				moveForwardItemActionPerformed(event);
			}
		});
		gameMenu.add(moveForwardItem);
	}

	private void moveForwardItemActionPerformed(ActionEvent event)
	{
		if(gui != null && gui.getGame() != null)
			gui.getGame().redo();
		else
		{
			try
			{
				Game activeGame = this.getActiveTabGame();
				if(!activeGame.redo())
					JOptionPane.showMessageDialog(null, "Cannot redo any further!");
			}
			catch(ArrayIndexOutOfBoundsException exception)
			{
				JOptionPane.showMessageDialog(null, "No active tabs!");
			}
			catch(UnsupportedOperationException exception)
			{
				JOptionPane.showMessageDialog(null, exception.getMessage());
			}
		}
	}

	private void setRewindToBeginItem(ResourceMap resourceMap)
	{
		rewindToBeginItem
		        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
		rewindToBeginItem.setText(resourceMap.getString("rewindToBegin.text")); // NOI18N
		rewindToBeginItem.setName("rewindToBegin"); // NOI18N
		rewindToBeginItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				rewindToBeginActionPerformed(event);
			}
		});
		gameMenu.add(rewindToBeginItem);
	}

	private void rewindToBeginActionPerformed(ActionEvent event)
	{
		try
		{
			Game activeGame = this.getActiveTabGame();
			if(!activeGame.rewindToBegin())
				JOptionPane.showMessageDialog(null, "Cannot rewind to beginning!");
		}
		catch(ArrayIndexOutOfBoundsException exception)
		{
			JOptionPane.showMessageDialog(null, "No active tabs!");
		}
		catch(UnsupportedOperationException exception)
		{
			JOptionPane.showMessageDialog(null, exception.getMessage());
		}
	}

	private void setRewindToEndItem(ResourceMap resourceMap)
	{
		rewindToEndItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
		        InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
		rewindToEndItem.setText(resourceMap.getString("rewindToEnd.text")); // NOI18N
		rewindToEndItem.setName("rewindToEnd"); // NOI18N
		rewindToEndItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				rewindToEndActionPerformed(event);
			}
		});
		gameMenu.add(rewindToEndItem);
	}

	private void rewindToEndActionPerformed(ActionEvent event)
	{
		try
		{
			Game activeGame = this.getActiveTabGame();
			if(!activeGame.rewindToEnd())
				JOptionPane.showMessageDialog(null, "Cannot rewind to end!");
		}
		catch(ArrayIndexOutOfBoundsException exception)
		{
			JOptionPane.showMessageDialog(null, "No active tabs!");
		}
		catch(UnsupportedOperationException exception)
		{
			JOptionPane.showMessageDialog(null, exception.getMessage());
		}
	}

	private void addOptionsMenu(ResourceMap resourceMap)
	{
		optionsMenu.setText(resourceMap.getString("optionsMenu.text")); // NOI18N
		optionsMenu.setName("optionsMenu"); // NOI18N
		
		themeSettingsMenu.setText(resourceMap.getString("themeSettingsMenu.text")); // NOI18N
		themeSettingsMenu.setName("themeSettingsMenu"); // NOI18N
		themeSettingsMenu.addActionListener(this);
		optionsMenu.add(themeSettingsMenu);
		
		menuBar.add(optionsMenu);
	}

	private void addHelpMenu(ResourceMap resourceMap, ActionMap actionMap)
	{
		JMenu helpMenu = new JMenu();
		JMenuItem aboutMenuItem = new JMenuItem();
		
		helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
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

	private void initializeTimers(ResourceMap resourceMap)
	{
		int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
		messageTimer = setMessageTimer(messageTimeout);
		messageTimer.setRepeats(false);
		
		int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
		for(int i = 0; i < busyIcons.length; i++)
			busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
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

	private void initializeIcons(ResourceMap resourceMap)
	{
		idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
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

	public Game addNewTab(String title)
	{
		Game newGameTab = new Game();
		this.gamesPane.addTab(title, newGameTab);
		return newGameTab;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		Object target = event.getSource();
		if(target == newGameItem)
		{
			this.newGameFrame = new NewGameWindow();
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
					Game tempGUI = (Game) this.gamesPane.getComponentAt(this.gamesPane.getSelectedIndex());
					if(!selectedFile.exists())
						createSaveFile(selectedFile);
					else if(selectedFile.exists())
					{
						int opt = JOptionPane.showConfirmDialog(tempGUI, Localization.getMessage("file_exists"),
						        Localization.getMessage("file_exists"), JOptionPane.YES_NO_OPTION);
						if(opt == JOptionPane.NO_OPTION) // if user choose to now overwrite
							continue; // go back to file choose
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
	
	public String showPawnPromotionBox(String color)
	{
		if(pawnPromotionBox == null)
		{
			JFrame mainFrame = JChessApp.getApplication().getMainFrame();
			pawnPromotionBox = new PawnPromotionWindow(mainFrame, color);
			pawnPromotionBox.setLocationRelativeTo(mainFrame);
			pawnPromotionBox.setModal(true);
			
		}
		pawnPromotionBox.setColor(color);
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
		Game activeGame = (Game) this.gamesPane.getComponentAt(this.gamesPane.getSelectedIndex());
		return activeGame;
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
