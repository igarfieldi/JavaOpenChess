package jchess.gui.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

import jchess.JChessApp;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.game.IGameBuilderFactory;
import jchess.gui.secondary.PawnPromotionWindow;

/**
 * The application's main frame.
 */
public class JChessView extends FrameView
{
	private List<IGame> gameList = new ArrayList<IGame>(1);
	
	private JPanel mainPanel;
	private JTabbedPane gamesPane;
	
	private static final Dimension PANEL_SIZE = new Dimension(800, 600);
	
	public JChessView(SingleFrameApplication app, IGameBuilderFactory factory)
	{
		super(app);
		
		this.gamesPane = new JChessTabbedPane();
		JChessMenuBar menuBar = new JChessMenuBar(gamesPane, factory);
		
		initializeMainPanel();
		setMenuBar(menuBar);
	}
	
	private void initializeMainPanel()
	{
		mainPanel = new JPanel();
		
		configureMainPanelSize();
		configureMainPanelLayout();
		setComponent(mainPanel);
	}
	
	private void configureMainPanelSize()
	{
		mainPanel.setMaximumSize(PANEL_SIZE);
		mainPanel.setMinimumSize(PANEL_SIZE);
		mainPanel.setPreferredSize(PANEL_SIZE);
	}
	
	private void configureMainPanelLayout()
	{
		this.mainPanel.setLayout(new BorderLayout());
		mainPanel.add(this.gamesPane, BorderLayout.CENTER);
	}
	
	public void addNewGameTab(String title, IGame game)
	{
		this.gameList.add(game);
		this.gamesPane.addTab(title, (Component) game.getView());
	}
	
	public String showPawnPromotionBox(Color color)
	{
		JFrame mainFrame = JChessApp.getApplication().getMainFrame();
		PawnPromotionWindow pawnPromotionWindow = new PawnPromotionWindow(mainFrame, color);
		pawnPromotionWindow.setLocationRelativeTo(mainFrame);
		pawnPromotionWindow.setModal(true);
		
		JChessApp.getApplication().show(pawnPromotionWindow);
		
		return pawnPromotionWindow.getSelectedPromotion();
	}
	
	public String showSaveWindow()
	{
		return "";
	}
	
	public IGame getActiveTabGame() throws ArrayIndexOutOfBoundsException
	{
		return this.gameList.get(this.gamesPane.getSelectedIndex());
	}
	
	public int getNumberOfOpenedTabs()
	{
		return this.gamesPane.getTabCount();
	}
}
