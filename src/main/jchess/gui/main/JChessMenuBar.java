package jchess.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

import jchess.JChessApp;
import jchess.gui.GameSaveManager;
import jchess.gui.secondary.about.JChessAboutWindow;
import jchess.gui.secondary.setup.NewGameWindow;
import jchess.gui.secondary.themechooser.ThemeChooseWindow;
import jchess.util.TypedResourceBundle;

/**
 * This class handles the creation and behavior of the menu bar in the JChessView.
 */
public class JChessMenuBar extends JMenuBar implements ActionListener
{
	private static Logger log = Logger.getLogger(JChessMenuBar.class.getName());
	private static final long serialVersionUID = -7586077317133311767L;
	
	private static final TypedResourceBundle VIEW_PROPERTIES = new TypedResourceBundle("jchess.resources.JChessView");
	
	private JMenuItem loadGameItem;
	private JMenuItem newGameItem;
	private JMenuItem saveGameItem;
	
	private JFrame mainFrame;
	private GameSaveManager gameSaveManager;
	
	public JChessMenuBar(JTabbedPane gamesPane)
	{
		super();
		
		this.mainFrame = JChessApp.getApplication().getMainFrame();
		this.gameSaveManager = new GameSaveManager(gamesPane);
		
		initializeMenuItems();
		createMenuBar();
	}
	
	/**
	 * Initializes the menu items for the file menu.
	 */
	private void initializeMenuItems()
	{
		newGameItem = new JMenuItem();
		loadGameItem = new JMenuItem();
		saveGameItem = new JMenuItem();
	}
	
	/**
	 * Adds all menus to the menu bar.
	 */
	private void createMenuBar()
	{
		addFileMenu();
		
		addMenu("optionsMenu", "Theme");
		addMenu("helpMenu", "About");
	}
	
	/**
	 * Adds all the menu items to the file menu.
	 */
	private void addFileMenu()
	{
		JMenu fileMenu = createMenu("fileMenu");
		
		addMenuItem(fileMenu, newGameItem, "newGame");
		addMenuItem(fileMenu, loadGameItem, "loadGame");
		addMenuItem(fileMenu, saveGameItem, "saveGame");
		addMenuItem(fileMenu, "quit");
	}
	
	/**
	 * Instantiates a menu.
	 * 
	 * @param label
	 * 			The label to access the menu name from VIEW_PROPERTIES.
	 * @return new menu.
	 */
	private JMenu createMenu(String label)
	{
		JMenu menu = new JMenu();
		menu.setText(VIEW_PROPERTIES.getString(label + ".text"));
		this.add(menu);
		
		return menu;
	}

	/**
	 * Adds a menu item with an action listener.
	 * 
	 * @param menuParent
	 * 				The menu that this item belongs to.
	 * @param menuItem
	 * 				The menu item to be added.
	 * @param menuItemLabel
	 * 				The label to access the menu item name from VIEW_PROPERTIES.
	 */
	private void addMenuItem(JMenu menuParent, JMenuItem menuItem, String menuItemLabel)
	{
		menuItem.setText(VIEW_PROPERTIES.getString(menuItemLabel + "Item.text"));
		menuItem.addActionListener(this);
		menuParent.add(menuItem);
	}
	
	/**
	 * Adds a new menu to the menu bar
	 * 
	 * @param name
	 * 				The label to access the menu name from VIEW_PROPERTIES.
	 * @param menuItemName
	 * 				The name of the menu item that belongs to this menu.
	 */
	private void addMenu(String name, String menuItemName)
	{
		JMenu menu = createMenu(name);
		addMenuItem(menu, menuItemName);
	}
	
	/**
	 * Adds a menu item whose action is set by an action map.
	 * 
	 * @param menuParent
	 * 				The menu that this menu item belongs to.
	 * @param action
	 * 				The action that the menu item does.
	 */
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
			gameSaveManager.saveGame();
		}
		else if(target == this.loadGameItem)
		{
			gameSaveManager.loadGame();
		}
	}
	
	/**
	 * Shows the JChessAboutWindow.
	 */
	@Action
	public void About()
	{
		JChessApp.getApplication().show(new JChessAboutWindow(mainFrame));
	}
	
	/**
	 * Shows the ThemeChooseWindow.
	 */
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
