package jchess.gui.main;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
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
	
	private PawnPromotionWindow pawnPromotionWindow;
	
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
		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(mainPanelLayout.createSequentialGroup().addContainerGap()
		                .addComponent(gamesPane, GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE).addContainerGap()));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addComponent(gamesPane,
		                GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)));
	}
	
	public void addNewGameTab(String title, IGame game) {
		this.gameList.add(game);
		this.gamesPane.addTab(title, (Component)game.getView());
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
	
	public IGame getActiveTabGame() throws ArrayIndexOutOfBoundsException
	{
		return this.gameList.get(this.gamesPane.getSelectedIndex());
	}
	
	public int getNumberOfOpenedTabs()
	{
		return this.gamesPane.getTabCount();
	}
}
