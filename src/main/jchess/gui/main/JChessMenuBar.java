package jchess.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

import jchess.JChessApp;
import jchess.gui.secondary.about.JChessAboutWindow;
import jchess.gui.secondary.setup.NewGameWindow;
import jchess.util.TypedResourceBundle;

public class JChessMenuBar extends JMenuBar implements ActionListener
{
	private static final long serialVersionUID = -7586077317133311767L;
	private static final TypedResourceBundle VIEW_PROPERTIES = new TypedResourceBundle("jchess.resources.JChessView");
	
	private JMenuItem loadGameItem;
	private JMenuItem newGameItem;
	private JMenuItem saveGameItem;
	private JMenuItem themeSettingsMenu;
	
	public JChessMenuBar()
	{
		super();
		initializeMenuBarElements();
		createMenuBar();
	}
	
	private void initializeMenuBarElements()
	{
		newGameItem = new JMenuItem();
		loadGameItem = new JMenuItem();
		saveGameItem = new JMenuItem();
		themeSettingsMenu = new JMenuItem();
	}
	
	private void createMenuBar()
	{
		addFileMenu();
		
		JMenu optionsMenu = createMenu("optionsMenu");
		addMenuItem(optionsMenu, themeSettingsMenu, "themeSettingsMenu");
		
		JMenu helpMenu = createMenu("helpMenu");
		addMenuItem(helpMenu, "showAboutBox");
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
	
	private void addMenuItem(JMenu menuParent, String action)
	{
		ActionMap actionMap = Application.getInstance(JChessApp.class).getContext()
		        .getActionMap(JChessView.class, this);
		
		JMenuItem aboutMenuItem = new JMenuItem();
		aboutMenuItem.setAction(actionMap.get(action));
		menuParent.add(aboutMenuItem);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object target = event.getSource();
		if(target == this.newGameItem)
		{
			JFrame mainFrame = JChessApp.getApplication().getMainFrame();
			NewGameWindow newGameWindow = new NewGameWindow(mainFrame);
			JChessApp.getApplication().show(newGameWindow);
		}
		else if(target == this.saveGameItem)
		{
			//saveGame();
		}
		else if(target == this.loadGameItem)
		{
			//loadGame();
		}
		else if(target == this.themeSettingsMenu)
		{
			//showThemeChooseWindow();
		}
	}
	
	@Action
	public void showAboutBox()
	{
		JFrame mainFrame = JChessApp.getApplication().getMainFrame();
		JChessAboutWindow aboutWindow = new JChessAboutWindow(mainFrame);
		
		aboutWindow.setLocationRelativeTo(mainFrame);
		JChessApp.getApplication().show(aboutWindow);
	}
}
